package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.services.INotificationService;
import com.crm.crmSoporteTecnico.services.models.dtos.ServiceRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
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
        message.setSubject("NUEVA SOLICITUD DE SERVICIO - " + request.packageName());

        String body = String.format(
                "El cliente %s ha solicitado el Plan %s!\n\n" +
                        "Detalles de la solicitud:\n" +
                        " - Empresa: %s\n" +
                        " - CIF: %s\n" +
                        " - Contacto: %s\n" +
                        " - Email: %s\n" +
                        " - Package: %s\n" +
                        " - Mensaje: %s\n\n" +
                        "Acción requerida: El manager debe contactar al cliente y realizar el registro manual en el CRM.",
                request.contactName(), request.packageName(),
                request.companyName(), request.cif(),
                request.contactName(), request.email(),
                request.packageName(), request.message()
        );

        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("Notificación enviada correctamente.");
        } catch (Exception ex) {
            System.out.println("Error al enviar la notificación: " + ex.getMessage());
        }
    }
}
