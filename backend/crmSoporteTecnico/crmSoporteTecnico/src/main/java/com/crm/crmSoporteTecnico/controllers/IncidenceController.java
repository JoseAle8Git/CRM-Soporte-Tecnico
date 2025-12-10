package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.persistence.entities.Incidence;
import com.crm.crmSoporteTecnico.persistence.repositories.UserRepository;
import com.crm.crmSoporteTecnico.services.IIncidenceService;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceAssignmentRequest;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceDashboardDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.crm.crmSoporteTecnico.persistence.repositories.IncidenceRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/incidences")
public class IncidenceController {

    @Autowired
    private IncidenceRepository incidenceRepository;

    @Autowired
    private UserRepository userRepository;

    private final IIncidenceService incidenceService;

    public IncidenceController(IIncidenceService incidenceService) {
        this.incidenceService = incidenceService;
    }

    /**
     * Obtener todas las incidencias. Solo accesible para managers y admins.
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping("/dashboard-list")
    public ResponseEntity<List<IncidenceDashboardDTO>> getDashboardIncidence() {
        List<IncidenceDashboardDTO> incidences = incidenceService.findAllIncidencesForDashboard();
        return ResponseEntity.ok(incidences);
    }

    /**
     * Asignar un técnico a una incidencia. Solo accesible a managers y admins.
     *
     * @param request
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/assign")
    public ResponseEntity<IncidenceDashboardDTO> assignTechnician(@Valid @RequestBody IncidenceAssignmentRequest request) {
        try {
            IncidenceDashboardDTO updatedIncidence = incidenceService.assignTechnician(request);
            return ResponseEntity.ok(updatedIncidence);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Crear una nueva incidencia (Accesible para CLIENTES, Managers y Admins).
     *
     * @param request Datos del formulario
     * @return La incidencia creada
     */
    // @PreAuthorize("hasAuthority('SCOPE_CLIENT')") // Descomenta si quieres restringirlo solo a clientes
    @PostMapping("/create")
    public ResponseEntity<IncidenceDashboardDTO> createIncidence(@Valid @RequestBody com.crm.crmSoporteTecnico.services.models.dtos.CreateIncidenceDTO request) {
        try {
            // OBTENER EL USUARIO LOGUEADO AUTOMÁTICAMENTE
            // 1. Sacamos el nombre del usuario del token de seguridad
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();

            // 2. Buscamos su ficha completa en la base de datos
            com.crm.crmSoporteTecnico.persistence.entities.AppUser currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario de seguridad no encontrado en DB"));

            // LLAMAR AL SERVICIO PASÁNDOLE EL USUARIO REAL
            // Ya no importa qué ID mande el front, usamos el real (currentUser)
            IncidenceDashboardDTO createdIncidence = incidenceService.createIncidence(request, currentUser);

            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(createdIncidence);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint: Obtener las incidencias de un cliente concreto
    // URL: GET /sistema/api/v1/incidences/client/{id}

    @GetMapping("/client/{id}")
    public ResponseEntity<List<IncidenceDashboardDTO>> getIncidencesByClient(@PathVariable Long id) {

        //  USAMOS EL METODO NUEVO CON EL GUIÓN BAJO
        List<Incidence> incidences = incidenceRepository.findByClient_Id(id);

        // Convertimos a DTO
        List<IncidenceDashboardDTO> dtos = incidences.stream()
                .map(IncidenceDashboardDTO::fromIncidence)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

}
