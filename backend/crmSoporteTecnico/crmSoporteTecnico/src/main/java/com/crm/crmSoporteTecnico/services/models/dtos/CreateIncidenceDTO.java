package com.crm.crmSoporteTecnico.services.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateIncidenceDTO(
        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotBlank(message = "La prioridad es obligatoria")
        String priority,

        // CORRECCIÓN AQUÍ:
        // Usamos Long porque recibimos el ID (ej: 10), no el objeto entero.
        // Y lo llamamos 'clientUserId' para que coincida con el Servicio.
        @NotNull(message = "El ID del usuario es obligatorio")
        Long clientUserId
) {}