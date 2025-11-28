package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.services.IStatsService;
import com.crm.crmSoporteTecnico.services.models.dtos.TechnicianStatsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final IStatsService statsService;

    public StatsController(IStatsService statsService) {
        this.statsService = statsService;
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER')")
    @GetMapping("/incidences-by-tech")
    public ResponseEntity<List<TechnicianStatsDTO>> getIncidenceCountsByTechnician() {
        List<TechnicianStatsDTO> stats = statsService.getIncidenceCountsByTechnician();
        return ResponseEntity.ok(stats);
    }

}
