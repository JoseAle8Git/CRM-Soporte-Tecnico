package com.crm.crmSoporteTecnico.services.models.dtos;

/**
 * DTO para la gráfica de rendimiento de los técnicos.
 * @param technicianName
 * @param incidenceCount
 */
public record TechnicianStatsDTO(
        String technicianName,
        Long incidenceCount
) {
}
