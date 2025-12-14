package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.entities.Client;
import com.crm.crmSoporteTecnico.persistence.entities.Incidence;
import com.crm.crmSoporteTecnico.persistence.entities.ReportLog;
import com.crm.crmSoporteTecnico.persistence.enums.IncidenceStatus;
import com.crm.crmSoporteTecnico.persistence.repositories.ClientRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.IncidenceRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.ReportLogRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.UserRepository;
import com.crm.crmSoporteTecnico.services.INotificationService;
import com.crm.crmSoporteTecnico.services.IReportService;
import com.crm.crmSoporteTecnico.services.models.dtos.ReportLogDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para generar reportes diarios del estado del CRM.
 */
@Service
public class ReportServiceImpl implements IReportService {

    private final ClientRepository clientRepository;
    private final IncidenceRepository incidenceRepository;
    private final ReportLogRepository reportLogRepository;
    private final INotificationService notificationService;

    private static final Map<String, Double> PACKAGES_PRICE = Map.of(
            "Plan Esencial", 99.0,
            "Plan Profesional", 299.0,
            "Plan Corporativo", 499.0
    );

    private static final String DESTINATION_EMAIL ="crmdmndmn@gmail.com";
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ReportServiceImpl(ClientRepository clientRepository, IncidenceRepository incidenceRepository, ReportLogRepository reportLogRepository, INotificationService notificationService, UserRepository userRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.clientRepository = clientRepository;
        this.incidenceRepository = incidenceRepository;
        this.reportLogRepository = reportLogRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public void generateAndSendDailyValue() {
        System.out.println("Generando reporte de estado del CRM...");
        try {
            ByteArrayOutputStream pdfStream = generateReportPdf();
            byte[] pdfData = pdfStream.toByteArray();

            ReportLog log = new ReportLog(
                    LocalDateTime.now(),
                    "DAILY_CRM_STATUS",
                    pdfData,
                    false
            );
            ReportLog savedLog = reportLogRepository.save(log);

            notificationService.sendPdfEmail(DESTINATION_EMAIL, pdfStream, savedLog.getId());
        } catch (Exception ex) {
            System.out.println("Error al generar/enviar reporte:" + ex.getMessage());
        }
    }

    /**
     * Genera el documento PDF con todas las métricas.
     * @return
     */
    @Override
    @Transactional
    public ByteArrayOutputStream generateReportPdf() {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<Client> allClients = clientRepository.findAll();
        List<Long> activeClients = allClients.stream().filter(Client::getActive).map(Client::getId).toList();
        List<AppUser> allTechnicians = userRepository.findAll().stream().filter(u -> "TECH".equalsIgnoreCase(u.getRol().getName())).toList();
        List<Incidence> allIncidences = incidenceRepository.findAll();

        double proyectedRevenue = allClients.stream()
                .filter(Client::getActive)
                .mapToDouble(c -> PACKAGES_PRICE.getOrDefault(c.getServicePackage(), 0.0))
                .sum();
        Map<IncidenceStatus, Long> incidenceStatusCount = allIncidences.stream()
                .collect(Collectors.groupingBy(Incidence::getStatus, Collectors.counting()));

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLUE);
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            document.add(new Paragraph("CRM - Reporte de estado Diario", titleFont));
            document.add(new Paragraph("Generado el: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("1. Resumen de Facturación", sectionFont));
            document.add(new Paragraph("Clientes de Alta: " + activeClients.size()));
            document.add(new Paragraph("Facturación Mensual Proyectada: €" + String.format("%.2f", proyectedRevenue)));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("2. Estado de Incidencias por Fase", sectionFont));
            PdfPTable statusTable = new PdfPTable(2);
            statusTable.setWidthPercentage(50);
            statusTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            statusTable.addCell(new PdfPCell(new Paragraph("Estado", sectionFont)));
            statusTable.addCell(new PdfPCell(new Paragraph("Count", sectionFont)));

            for(Map.Entry<IncidenceStatus, Long> entry : incidenceStatusCount.entrySet()) {
                statusTable.addCell(entry.getKey().name());
                statusTable.addCell(String.valueOf(entry.getValue()));
            }
            document.add(statusTable);
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("3. Detalles de Técnicos", sectionFont));

            for(AppUser tech : allTechnicians) {
                document.add(new Paragraph("Técnico: " + tech.getUsername() + " ( " + tech.getName() + ")", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
                List<Incidence> assignedIncidences = allIncidences.stream()
                        .filter(i -> i.getTechnician() != null && i.getTechnician().getId().equals(tech.getId()))
                        .toList();

                PdfPTable techTable = new PdfPTable(3);
                techTable.setWidthPercentage(80);
                techTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                techTable.addCell("Asignadas:");
                techTable.addCell("Estado:");
                techTable.addCell("Prioridad:");

                for(Incidence i : assignedIncidences) {
                    techTable.addCell(i.getTitle());
                    techTable.addCell(i.getStatus().name());
                    techTable.addCell(i.getPriority().name());
                }
                document.add(techTable);
                document.add(Chunk.NEWLINE);
            }
            document.close();
            return baos;
        } catch (DocumentException ex) {
            throw new RuntimeException("Error al generar el PDF: DocumentException", ex);
        }
    }

    /**
     * Obtiene una lista de logs de reportes.
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReportLogDTO> getReportLogList() {
        return reportLogRepository.findAllByOrderByGenerationDateDesc().stream()
                .map(ReportLogDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene en binario un PDF.
     * @param reportId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] getReportPdfData(Long reportId) {
        ReportLog log = reportLogRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Reporte no encontrado con ID: " + reportId));
        return log.getReportData();
    }

}
