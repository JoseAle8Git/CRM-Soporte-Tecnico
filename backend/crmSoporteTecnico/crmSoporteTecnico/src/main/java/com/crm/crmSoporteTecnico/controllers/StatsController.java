package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.services.IStatsService;
import com.crm.crmSoporteTecnico.services.models.dtos.PackageCountDTO;
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping("/incidences-by-tech")
    public ResponseEntity<List<TechnicianStatsDTO>> getIncidenceCountsByTechnician() {
        List<TechnicianStatsDTO> stats = statsService.getIncidenceCountsByTechnician();
        return ResponseEntity.ok(stats);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/revenue")
    public ResponseEntity<Double> getProjectedRevenue() {
        Double revenue = statsService.getProjectMonthlyRevenue();
        return ResponseEntity.ok(revenue);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/package-counts")
    public ResponseEntity<List<PackageCountDTO>> getActiveClientCountsByPackage() {
        List<PackageCountDTO> counts = statsService.getActiveClientCountsByPackage();
        return ResponseEntity.ok(counts);
    }
}
