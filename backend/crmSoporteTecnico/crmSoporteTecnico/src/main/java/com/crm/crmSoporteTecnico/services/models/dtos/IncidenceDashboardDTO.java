package com.crm.crmSoporteTecnico.services.models.dtos;

import com.crm.crmSoporteTecnico.persistence.entities.Incidence;

import java.time.LocalDateTime;

/**
 * DTO para la lista de incidencias en el Dashboard del Manager.
 *
 * @param id
 * @param title
 * @param status
 * @param priority
 * @param clientName
 * @param technicianName
 * @param creationDate
 */
public record IncidenceDashboardDTO(

        Long id,
        String title,
        String status,
        String priority,
        String clientName,
        String technicianName,
        LocalDateTime creationDate

) {

    /**
     * Método estático para mapear la entidad a este DTO.
     *
     * @param incidence
     * @return
     */
    public static IncidenceDashboardDTO fromIncidence(Incidence incidence) {
        String techName = incidence.getTechnician() != null ? incidence.getTechnician().getName() : "No asignado";

        return new IncidenceDashboardDTO(
                incidence.getId(),
                incidence.getTitle(),
                incidence.getStatus().name(),
                incidence.getPriority().name(),
                incidence.getClient().getCompanyName(),
                techName,
                incidence.getCreationDate()
        );
    }

}
