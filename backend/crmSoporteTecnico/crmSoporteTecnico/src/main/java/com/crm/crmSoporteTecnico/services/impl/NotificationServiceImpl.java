package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.services.INotificationService;
import com.crm.crmSoporteTecnico.services.models.dtos.ServiceRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

public class NotificationServiceImpl implements INotificationService {

    private final JavaMailSender mailSender;

    private static final String MANAGER_EMAIL = "managersoportetecnico@gmail.com";

    private static final String SENDER_EMAIL = "crmserviciotecnico52@gmail.com";

    public NotificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void notifyManagerNewServiceRequest(ServiceRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(SENDER_EMAIL);
        message.setTo(MANAGER_EMAIL);
        message.setSubject("NUEVA SOLICITUD DE SERVICIO - " + request.packegeName());

        String body = String.format(
                "El cliente %s ha solicitado el Plan %s!\n\n" +
                        "Detalles de la solicitud:\n" +
                        " - Empresa: %s\n" +
                        " - Contacto: %s\n" +
                        ""
        );
    }
}
