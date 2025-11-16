package com.crm.crmSoporteTecnico.services.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para recibir la solicitud de servicio desde el Frontend.
 * @param companyName
 * @param contactName
 * @param email
 * @param packageName
 * @param message
 */
public record ServiceRequest(
        @NotBlank(message = "El nombre de la empresa no puede estar vacío.")
        String companyName,

        @NotBlank(message = "El CIF de la empresa es obligatorio.")
        @Size(min = 9, max = 20, message = "El CIF debe tener entre 9 y 20 caracteres.")
        String cif,

        @NotBlank(message = "El nombre del contacto no puede estar vacío.")
        String contactName,

        @NotBlank(message = "El email no puede estar vacío.")
        @Email(message = "El formato del email es inválido.")
        String email,

        @NotBlank(message = "El paquete de servicio debe estar seleccionado.")
        String packageName,

        @Size(max = 500, message = "El mensaje no puede exceder los 500 caracteres.")
        String message
) {
}
