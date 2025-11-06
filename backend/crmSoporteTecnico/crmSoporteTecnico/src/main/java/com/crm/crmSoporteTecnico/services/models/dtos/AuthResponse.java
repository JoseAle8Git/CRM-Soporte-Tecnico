package com.crm.crmSoporteTecnico.services.models.dtos;

import com.crm.crmSoporteTecnico.persistence.entities.User;

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
     * Método estático para crear el DTO desde la entidad user.
     * @param user
     * @return
     */
    public static AuthResponse fromUser(User user) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getRol().getName()
        );
    }
}
