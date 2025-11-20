package com.crm.crmSoporteTecnico.services.models.dtos;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;

/**
 * DTO que excluye la contraseña cifrada.
 * @param id
 * @param username
 * @param name
 * @param email
 * @param role
 */
public record UserBasicDTO(
        Long id,
        String username,
        String name,
        String email,
        String role
) {
    /**
     * Método estático para mapear la entidad a este DTO.
     * @param user
     * @return
     */
    public static UserBasicDTO fromUser(AppUser user) {
        return new UserBasicDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRol().getName()
        );
    }
}
