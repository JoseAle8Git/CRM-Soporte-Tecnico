package com.crm.crmSoporteTecnico.services.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreationRequest(

        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(max = 50, message = "El username no debe exceder los 50 caracteres.")
        String username,

        @NotBlank(message = "El nombre completo es obligatorio.")
        @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres.")
        String name,

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "Formato de correo inválido.")
        String email,

        @Size(max = 15, message = "El teléfono no debe exceder los 15 caracteres.")
        String telephone,

        @NotBlank(message = "La contraseña temporal es obligatorio.")
        @Size(min = 8, max = 12, message = "La contraseña debe tener entre 8 y 12 caracteres.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$",
                message = "La contraseña debe contener al menos una mayúscula, minúscula, número y carácter especial."
        )
        String rawPassword,

        @NotBlank(message = "El rol es obligatorio.")
        String roleName,

        Long clientId,

        String companyName,

        String cif,

        String packageName

) {
}
