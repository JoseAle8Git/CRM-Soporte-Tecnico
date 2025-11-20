package com.crm.crmSoporteTecnico.services.models.dtos;

import jakarta.validation.constraints.NotNull;

public record IncidenceAssignmentRequest(

        @NotNull(message = "El Id de la incidencia es obligatoria.")
        Long incidenceId,

        @NotNull(message = "El ID del técnico es obligatorio para la asignación.")
        Long technicianId

) {
}
