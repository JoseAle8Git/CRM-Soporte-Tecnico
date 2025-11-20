package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceAssignmentRequest;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceDashboardDTO;

import java.util.List;

public interface IIncidenceService {

    /**
     * Listar todas las incidencias para el Dashboard del manager.
     * @return
     */
    List<IncidenceDashboardDTO> findAllIncidencesForDashboard();

    /**
     * Asignar una incidencia a un técnico y cambiar su estado.
     * @param request
     * @return
     */
    IncidenceDashboardDTO assignTechnician(IncidenceAssignmentRequest request);

}
