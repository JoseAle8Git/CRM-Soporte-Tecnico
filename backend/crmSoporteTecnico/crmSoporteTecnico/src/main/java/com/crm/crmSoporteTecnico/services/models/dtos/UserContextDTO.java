package com.crm.crmSoporteTecnico.services.models.dtos;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;

public record UserContextDTO(
        Long userId,
        String username,
        String role,
        Long clientId,
        String companyName
) {
    public static UserContextDTO fromEntity(AppUser user) {
        return new UserContextDTO(
                user.getId(),
                user.getUsername(),
                user.getRol().getName(),
                (user.getClient() != null) ? user.getClient().getId() : null,
                (user.getClient() != null) ? user.getClient().getCompanyName() : null
        );
    }
}