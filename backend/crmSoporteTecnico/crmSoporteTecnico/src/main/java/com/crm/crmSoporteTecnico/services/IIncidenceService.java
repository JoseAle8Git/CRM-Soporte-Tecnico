package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.persistence.entities.Incidence;
import com.crm.crmSoporteTecnico.persistence.enums.IncidenceStatus;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceAssignmentRequest;
import com.crm.crmSoporteTecnico.services.models.dtos.IncidenceDashboardDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.TechnicianIncidenceDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.TechnicianPersonalStatsDTO;

import java.util.List;
import java.util.Map;

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

    /**
     * Obtiene todas las incidencias asignadas a un técnico.
     */
    List<IncidenceDashboardDTO> findIncidencesByTechnician(Long technicianId);

    /**
     * Cambia el estado de una incidencia (solo el técnico asignado).
     */
    IncidenceDashboardDTO updateIncidenceStatus(Long incidenceId, IncidenceStatus newStatus, Long technicianId);

    /**
     * Estadísticas para dashboard del técnico.
     */
    TechnicianPersonalStatsDTO getTechnicianPersonalStats(Long technicianId);
    List<Incidence> findIncidencesForTechnicianEntity(Long technicianId);
    TechnicianIncidenceDTO updateIncidenceStatusForTechnician(Long incidenceId, IncidenceStatus newStatus, Long technicianId);

}
