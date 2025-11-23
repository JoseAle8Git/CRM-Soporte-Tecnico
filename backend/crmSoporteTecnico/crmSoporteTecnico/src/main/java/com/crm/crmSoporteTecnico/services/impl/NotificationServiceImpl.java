package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.entities.Incidence;
import com.crm.crmSoporteTecnico.services.INotificationService;
import com.crm.crmSoporteTecnico.services.models.dtos.ServiceRequest;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    /**
     * Envía las credenciales de forma asíncrona, ejecutando un hilo separado.
     * @param user
     * @param rawPassword
     */
    @Override
    @Async
    public void notifyNewUserCredentials(AppUser user, String rawPassword) {

        System.out.println("Iniciando envío asíncrono de credenciales a: " + user.getEmail());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SENDER_EMAIL);
        message.setTo(user.getEmail());
        message.setSubject("Bienvenido al CRM - Credenciales de Acceso");

        String body = String.format(
                "¡Hola %s!\n" +
                        "Has sido registrado en nuestro CRM de Soporte Técnico como un usuario %s.\n" +
                        "Utiliza las siguientes credenciales para acceder:\n" +
                        " - URL de Acceso: http://localhost:4200\n" +
                        " - Usuario (Username): %s\n" +
                        " - Contraseña temporal: %s\n" +
                        "Por favor, cambia tu contraseña al iniciar sesión por primera vez.",
                user.getName(),
                user.getRol().getName(),
                user.getName(),
                user.getUsername(),
                rawPassword
        );

        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("Notificación de credenciales enviada correctamente.");
        } catch (Exception ex) {
            System.out.println("Error al enviar la notificación: " + ex.getMessage());
        }

    }

    /**
     * Notifica al técnico de una nueva incidencia asignada.
     * @param incidence
     */
    @Override
    @Async
    public void notifyTechnicianAssignment(Incidence incidence) {

        System.out.println("Iniciando notificación de a asignación a técnico: " + incidence.getTechnician().getEmail());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setTo(incidence.getTechnician().getEmail());
            helper.setSubject("¡Nueva Incidencia Asignada! - # " + incidence.getId());

            String htmlContent = String.format(
                    "<html><body>" +
                            "<h2>Hola %s,</h2>" +
                            "<p>Se te ha asignado una nueva incidencia en el CRM.</p>" +
                            "<ul>" +
                            "<li><b>Incidencia ID:</b> #%d</li>" +
                            "<li><b>Título:</b> %s</li>" +
                            "<li><b>Cliente:</b> %s</li>" +
                            "<li><b>Prioridad:</b> %s</li>" +
                            "</ul>" +
                            "<p>Por favor, revisa el ticket y comienza la gestión.</p>" +
                            "</body></html>",
                    incidence.getTechnician().getName(),
                    incidence.getId(),
                    incidence.getTitle(),
                    incidence.getClient().getCompanyName(),
                    incidence.getPriority().name()
            );

            helper.setText(htmlContent, true);
            helper.setFrom(SENDER_EMAIL);

            mailSender.send(message);
            System.out.println("Correo de asignación enviado a técnico: " + incidence.getTechnician().getEmail());
        } catch(Exception ex) {
            System.out.println("Error al enviar la notificación de asignación: " + ex.getMessage());
        }

    }

}
