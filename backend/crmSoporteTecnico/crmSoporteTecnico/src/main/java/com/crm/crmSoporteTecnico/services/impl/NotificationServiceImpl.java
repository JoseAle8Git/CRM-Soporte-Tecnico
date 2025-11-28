package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.entities.Incidence;
import com.crm.crmSoporteTecnico.services.INotificationService;
import com.crm.crmSoporteTecnico.services.models.dtos.CreateIncidenceDTO;
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
     *
     * @param user
     * @param rawPassword
     */
    @Override
    @Async
    public void notifyNewUserCredentials(AppUser user, String rawPassword) {

        System.out.println("Iniciando envío asíncrono de credenciales a: " + user.getEmail());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setTo(user.getEmail());
            helper.setSubject("Bienvenido al CRM - Credenciales de Acceso");
            String htmlContent = String.format(
                    "<html><body>" +
                            "<h2>¡Hola %s!</h2>" +
                            "<p>Has sido registrado en nuestro CRM de Soporte Técnico con el rol de <b>%s</b>.</p>" +
                            "<p>Utiliza las siguientes credenciales para acceder:</p>" +
                            "<ul>" +
                            "<li><b>URL de Acceso:</b> http://localhost:4200</li>" +
                            "<li><b>Usuario (Username):</b> %s</li>" +
                            "<li><b>Contraseña Temporal:</b> %s</li>" +
                            "</ul>" +
                            "<p>Por favor, cambia tu contraseña al iniciar sesión por primera vez.</p>" +
                            "</body></html>",
                    user.getName(), user.getRol().getName(), user.getUsername(), rawPassword
            );
        } catch (Exception ex) {
            System.err.println("Error al enviar correo de credenciales: " + ex.getMessage());
        }

    }

    /**
     * Notifica al técnico de una nueva incidencia asignada.
     *
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
        } catch (Exception ex) {
            System.out.println("Error al enviar la notificación de asignación: " + ex.getMessage());
        }

    }

    /**
     * Implementación: Envía correo al Manager sobre nueva incidencia.
     */
    @Override
    @Async
    public void notifyManagerNewIncidence(Incidence incidence, CreateIncidenceDTO request) {

        System.out.println("Enviando notificación de NUEVA INCIDENCIA al Manager...");

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setFrom(SENDER_EMAIL);
            helper.setTo(MANAGER_EMAIL);
            helper.setSubject("🚨 NUEVA INCIDENCIA CREADA - ID #" + incidence.getId());

            String htmlContent = String.format(
                    "<html><body>" +
                            "<h2>Nueva Incidencia Reportada</h2>" +
                            "<p>Un cliente ha registrado un ticket en el sistema que requiere atención.</p>" +
                            "<hr/>" +
                            "<ul>" +
                            "<li><b>ID Ticket:</b> #%s</li>" +
                            "<li><b>Cliente:</b> %s</li>" +
                            "<li><b>Asunto:</b> %s</li>" +
                            "<li><b>Prioridad:</b> <span style='color:red;'>%s</span></li>" +
                            "</ul>" +
                            "<h3>Descripción:</h3>" +
                            "<p><i>%s</i></p>" +
                            "<hr/>" +
                            "<p>Por favor, accede al CRM para asignar un técnico.</p>" +
                            "</body></html>",
                    incidence.getId(),
                    incidence.getClient().getCompanyName(),
                    incidence.getTitle(),
                    incidence.getPriority(),
                    incidence.getDescription()
            );

            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("Correo de nueva incidencia enviado al Manager.");

        } catch (Exception ex) {
            // ESTO EVITA QUE EL ERROR 500 ROMPA LA CREACIÓN DEL TICKET
            System.err.println("ERROR enviando correo (Pero el ticket se creó): " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
