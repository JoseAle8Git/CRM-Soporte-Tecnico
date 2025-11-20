package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.services.IIncidenceService;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceAssignmentRequest;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceDashboardDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidences")
public class IncidenceController {

    private final IIncidenceService incidenceService;

    public IncidenceController(IIncidenceService incidenceService) {
        this.incidenceService = incidenceService;
    }

    /**
     * Obtener todas las incidencias. Solo accesible para managers y admins.
     * @return
     */
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER')")
    @GetMapping("/dashboard-list")
    public ResponseEntity<List<IncidenceDashboardDTO>> getDashboardIncidence() {
        List<IncidenceDashboardDTO> incidences = incidenceService.findAllIncidencesForDashboard();
        return ResponseEntity.ok(incidences);
    }

    /**
     * Asignar un técnico a una incidencia. Solo accesible a managers y admins.
     * @param request
     * @return
     */
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER')")
    @PostMapping("/assign")
    public ResponseEntity<IncidenceDashboardDTO> assignTechnician(@Valid @RequestBody IncidenceAssignmentRequest request) {
        try {
            IncidenceDashboardDTO updatedIncidence = incidenceService.assignTechnician(request);
            return ResponseEntity.ok(updatedIncidence);
        } catch(IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
