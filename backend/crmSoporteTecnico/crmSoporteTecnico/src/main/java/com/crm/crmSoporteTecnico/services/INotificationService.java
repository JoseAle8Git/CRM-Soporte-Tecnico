package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.services.models.dtos.ServiceRequest;

public interface INotificationService {

    /**
     * El método de envío debe de ser asíncrono.
     * @param request
     */
    void notifyManagerNewServiceRequest(ServiceRequest request);

}
