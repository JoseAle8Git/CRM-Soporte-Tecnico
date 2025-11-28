package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.entities.Incidence;
import com.crm.crmSoporteTecnico.services.models.dtos.CreateIncidenceDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.ServiceRequest;

public interface INotificationService {

    /**
     * El método de envío debe de ser asíncrono.
     *
     * @param request
     */
    void notifyManagerNewServiceRequest(ServiceRequest request);

    /**
     * Notificación asíncrona de credenciales.
     *
     * @param user
     * @param rawPassword
     */
    void notifyNewUserCredentials(AppUser user, String rawPassword);

    /**
     * Notificación de asignación de incidencia.
     *
     * @param incidence
     */
    void notifyTechnicianAssignment(Incidence incidence);


    /**
     * Notifica al Manager cuando un cliente crea una incidencia.
     *
     * @param incidence La incidencia creada.
     */
    void notifyManagerNewIncidence(Incidence incidence, CreateIncidenceDTO request);

}
