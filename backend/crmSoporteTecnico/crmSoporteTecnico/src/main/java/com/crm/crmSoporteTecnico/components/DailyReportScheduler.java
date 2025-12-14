package com.crm.crmSoporteTecnico.components;

import com.crm.crmSoporteTecnico.services.IReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyReportScheduler {

    private final IReportService reportService;

    public DailyReportScheduler(IReportService reportService) {
        this.reportService = reportService;
    }

    @Scheduled(cron = "0 35 18,23 * * *")
    public void generatedReport() {
        System.out.println("Iniciando generación de reporte diario...");
        reportService.generateAndSendDailyValue();
    }

}
