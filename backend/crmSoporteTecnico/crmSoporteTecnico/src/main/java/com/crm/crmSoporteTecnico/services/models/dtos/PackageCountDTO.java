package com.crm.crmSoporteTecnico.services.models.dtos;

/**
 * DTO para la gráfica circular de distribución de paquetes.
 * @param packageName
 * @param clientCount
 */
public record PackageCountDTO(
        String packageName,
        Long clientCount
) {
}
