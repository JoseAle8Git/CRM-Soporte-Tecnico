package com.crm.crmSoporteTecnico.services.models.dtos;

import com.crm.crmSoporteTecnico.persistence.entities.Client;

/**
 * DTO para la tabla de clientes del Dashboard del Administrador.
 * @param id
 * @param companyName
 * @param cif
 * @param servicePackage
 * @param active
 * @param direction
 */
public record ClientDashboardDTO(
        Long id,
        String companyName,
        String cif,
        String servicePackage,
        Boolean active,
        String direction
) {
    public static ClientDashboardDTO fromClient(Client client) {
        return new ClientDashboardDTO(
                client.getId(),
                client.getCompanyName(),
                client.getCif(),
                client.getServicePackage(),
                client.getActive(),
                client.getDirection()
        );
    }
}
