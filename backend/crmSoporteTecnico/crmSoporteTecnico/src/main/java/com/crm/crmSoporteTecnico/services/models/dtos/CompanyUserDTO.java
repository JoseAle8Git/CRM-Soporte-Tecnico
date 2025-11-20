package com.crm.crmSoporteTecnico.services.models.dtos;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;

public record CompanyUserDTO(
        String username,
        String name,
        String email,
        String role
) {
    // Método para convertir de Entidad a DTO
    public static CompanyUserDTO fromEntity(AppUser user) {
        return new CompanyUserDTO(
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRol().getName() // Obtenemos el nombre del rol limpio
        );
    }
}