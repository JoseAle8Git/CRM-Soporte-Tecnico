package com.crm.crmSoporteTecnico.services.models.dtos;

import com.crm.crmSoporteTecnico.persistence.entities.ReportLog;

import java.time.LocalDateTime;

public record ReportLogDTO(
        Long id,
        LocalDateTime generationDate,
        String reportType,
        Boolean emailSent
) {
    public static ReportLogDTO fromEntity(ReportLog entity) {
        return new ReportLogDTO(
                entity.getId(),
                entity.getGenerationDate(),
                entity.getReportType(),
                entity.getEmailSent()
        );
    }
}
