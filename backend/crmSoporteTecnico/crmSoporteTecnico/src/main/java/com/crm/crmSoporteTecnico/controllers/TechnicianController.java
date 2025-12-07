package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.entities.Incidence;
import com.crm.crmSoporteTecnico.persistence.enums.IncidenceStatus;
import com.crm.crmSoporteTecnico.persistence.repositories.UserRepository;
import com.crm.crmSoporteTecnico.services.IIncidenceService;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceDashboardDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.TechnicianIncidenceDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.TechnicianPersonalStatsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/technician")
public class TechnicianController {
    private final UserRepository userRepository;
    private final IIncidenceService incidenceService;

    public TechnicianController(IIncidenceService incidenceService, UserRepository userRepository) {
        this.incidenceService = incidenceService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAuthority('TECHNICIAN')")
    @GetMapping("/incidences")
    public ResponseEntity<List<TechnicianIncidenceDTO>> getMyIncidences(Authentication authentication) {

        // 1. Obtener username del JWT
        String username = authentication.getName();

        // 2. Recuperar al técnico real
        AppUser technician = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 3. Buscar incidencias del técnico
        List<Incidence> incidences = incidenceService.findIncidencesForTechnicianEntity(technician.getId());

        List<TechnicianIncidenceDTO> result =
                incidences.stream()
                        .map(TechnicianIncidenceDTO::fromIncidence)
                        .toList();

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('TECHNICIAN')")
    @PatchMapping("/incidences/{incidenceId}/status")
    public ResponseEntity<TechnicianIncidenceDTO> updateStatus(
            @PathVariable Long incidenceId,
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        // 1. Leer el estado desde el body
        String newStatusStr = body.get("status");
        IncidenceStatus newStatus = IncidenceStatus.valueOf(newStatusStr.toUpperCase());

        // 2. Obtener username del JWT
        String username = authentication.getName();

        // 3. Obtener AppUser real
        AppUser technician = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 4. Llamar al servicio con seguridad
        TechnicianIncidenceDTO updated =
                incidenceService.updateIncidenceStatusForTechnician(incidenceId, newStatus, technician.getId());

        // 5. Devolver respuesta
        return ResponseEntity.ok(updated);
    }


    @PreAuthorize("hasAuthority('TECHNICIAN')")
    @GetMapping("/stats")
    public ResponseEntity<TechnicianPersonalStatsDTO> getTechnicianStats(Authentication authentication) {

        String username = authentication.getName();

        AppUser technician = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        TechnicianPersonalStatsDTO stats =
                incidenceService.getTechnicianPersonalStats(technician.getId());

        return ResponseEntity.ok(stats);
    }

}
