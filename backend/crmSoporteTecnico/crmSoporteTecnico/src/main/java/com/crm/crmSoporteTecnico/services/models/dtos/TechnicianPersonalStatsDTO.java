package com.crm.crmSoporteTecnico.services.models.dtos;

public record TechnicianPersonalStatsDTO(
        long pending,
        long inProgress,
        long resolved,
        long closed
) {}
