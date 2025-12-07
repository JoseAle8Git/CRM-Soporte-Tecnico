package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.persistence.entities.ReportLog;
import com.crm.crmSoporteTecnico.services.models.dtos.ReportLogDTO;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface IReportService {

    /**
     * Envía a un correo electrónico un pdf.
     */
    void generateAndSendDailyValue();

    /**
     * Genera un pdf con información del CRM.
     * @return
     */
    ByteArrayOutputStream generateReportPdf();

    /**
     * Obtiene una lista de logs para el Dashboard del Admin.
     * @return
     */
    List<ReportLogDTO> getReportLogList();

    /**
     * Obtiene el archivo en binario del PDF.
     * @param reportId
     * @return
     */
    byte[] getReportPdfData(Long reportId);

}
