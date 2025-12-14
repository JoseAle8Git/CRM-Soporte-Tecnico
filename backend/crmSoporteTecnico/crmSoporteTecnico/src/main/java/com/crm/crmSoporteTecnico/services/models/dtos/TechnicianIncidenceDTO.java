package com.crm.crmSoporteTecnico.services.models.dtos;

import com.crm.crmSoporteTecnico.persistence.entities.Incidence;

import java.time.LocalDateTime;

public record TechnicianIncidenceDTO(
        Long id,
        String title,
        String status,
        String priority,
        LocalDateTime creationDate,
        Long technicianId
) {

    public static TechnicianIncidenceDTO fromIncidence(Incidence incidence) {
        return new TechnicianIncidenceDTO(
                incidence.getId(),
                incidence.getTitle(),
                incidence.getStatus().name(),
                incidence.getPriority().name(),
                incidence.getCreationDate(),
                incidence.getTechnician() != null ? incidence.getTechnician().getId() : null
        );
    }
}
