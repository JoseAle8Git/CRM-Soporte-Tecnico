package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.services.models.dtos.PackageCountDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.TechnicianStatsDTO;

import java.util.List;

public interface IStatsService {

    List<TechnicianStatsDTO> getIncidenceCountsByTechnician();

    /**
     * Calcula el total de facturación proyectada.
     * @return
     */
    Double getProjectMonthlyRevenue();

    /**
     * Obtiene el conteo de clientes activos por paquete.
     * @return
     */
    List<PackageCountDTO> getActiveClientCountsByPackage();

}
