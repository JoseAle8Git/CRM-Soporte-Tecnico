package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.services.IReportService;
import com.crm.crmSoporteTecnico.services.models.dtos.ReportLogDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final IReportService reportService;

    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Obtiene un listado de logs para la tabla del Admin.
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/logs")
    public ResponseEntity<List<ReportLogDTO>> getReportLogs() {
        List<ReportLogDTO> logs = reportService.getReportLogList();
        return ResponseEntity.ok(logs);
    }

    /**
     * Descarga el PDF en binario.
     * @param reportId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/{reportId}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long reportId) {
        try {
            byte[] pdfData = reportService.getReportPdfData(reportId);
            String filename = "CRM_REPORTE_" + LocalDateTime.now().toString() + ".pdf";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfData);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
