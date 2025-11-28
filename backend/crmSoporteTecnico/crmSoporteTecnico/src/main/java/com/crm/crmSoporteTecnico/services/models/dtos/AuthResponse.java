package com.crm.crmSoporteTecnico.services.models.dtos;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;

/**
 * DTO para enviar la información del usuario al Frontend tras el login.
 * ¡El token no envía aquí, va en la Cookie HttpOnly!
 */
public record AuthResponse(
        Long userId,
        String username,
        String role
) {
    /**
     * Metodo estático para crear el DTO desde la entidad user.
     * @param user
     * @return
     */
    public static AuthResponse fromUser(AppUser user) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getRol().getName()
        );
    }
}
