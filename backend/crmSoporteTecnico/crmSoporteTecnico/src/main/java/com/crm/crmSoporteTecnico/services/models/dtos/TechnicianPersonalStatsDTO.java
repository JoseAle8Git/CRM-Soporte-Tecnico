package com.crm.crmSoporteTecnico.services.models.dtos;

public record TechnicianPersonalStatsDTO(
        long open,
        long pending,
        long inProgress,
        long resolved,
        long closed
) {}
