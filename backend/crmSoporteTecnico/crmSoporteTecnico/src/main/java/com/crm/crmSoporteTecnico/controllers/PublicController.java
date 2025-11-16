package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.services.INotificationService;
import com.crm.crmSoporteTecnico.services.models.dtos.ServiceRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final INotificationService notificationService;

    public PublicController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/service-request")
    public ResponseEntity<String> registryServiceRequest(@Valid @RequestBody ServiceRequest request){
        notificationService.notifyManagerNewServiceRequest(request);
        return ResponseEntity.ok("Solicitud recibida. Un manager se pondrá en contacto pronto.");
    }

}
