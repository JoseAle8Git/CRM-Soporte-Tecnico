package com.crm.crmSoporteTecnico.services.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) para la solicitud de login.
 * Se utiliza record para la inmutabilidad y concisión.
 */
public record LoginRequest(
        @NotBlank(message = "El nombre de usuario no puede estar vacío.")
        String username,

        @NotBlank(message = "La contraseña no puede estar vacía.")
        @Size(min = 8, max = 12, message = "La contraseña debe tener entre 8 y 12 caracteres.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$",
                message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un caratér especial (@$!%*?&)."
        )
        String password
) {
}
