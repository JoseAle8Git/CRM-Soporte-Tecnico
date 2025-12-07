package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceAssignmentRequest;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceDashboardDTO;

import java.util.List;

public interface IIncidenceService {

    /**
     * Listar todas las incidencias para el Dashboard del manager.
     *
     * @return
     */
    List<IncidenceDashboardDTO> findAllIncidencesForDashboard();

    /**
     * Asignar una incidencia a un técnico y cambiar su estado.
     *
     * @param request
     * @return
     */
    IncidenceDashboardDTO assignTechnician(IncidenceAssignmentRequest request);


    /**
     * Crea una nueva incidencia asignada al usuario que la solicita.
     *
     * @param request     Datos del formulario (Título, Descripción, Prioridad)
     * @param creatorUser El usuario que está logueado (El que crea el ticket)
     */
    IncidenceDashboardDTO createIncidence(com.crm.crmSoporteTecnico.services.models.dtos.CreateIncidenceDTO request, com.crm.crmSoporteTecnico.persistence.entities.AppUser creatorUser);
}
