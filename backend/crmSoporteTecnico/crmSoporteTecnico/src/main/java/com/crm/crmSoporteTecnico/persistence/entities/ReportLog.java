package com.crm.crmSoporteTecnico.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.jdbc.BinaryJdbcType;

import java.time.LocalDateTime;

/**
 * Clase POJO de la entidad ReportLog.
 */
@Entity
@Table(name = "report_log")
public class ReportLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "generation_date", nullable = false)
    private LocalDateTime generationDate;

    @Column(name = "report_type", nullable = false, length = 50)
    private String reportType;

    @Lob
    @JdbcType(BinaryJdbcType.class)
    @Column(name = "report_data", nullable = false)
    private byte[] reportData;

    @Column(name = "email_sent", nullable = false)
    private Boolean emailSent;

    public ReportLog() {}

    public ReportLog(LocalDateTime generationDate, String reportType, byte[] reportData, Boolean emailSent) {
        this.generationDate = generationDate;
        this.reportType = reportType;
        this.reportData = reportData;
        this.emailSent = emailSent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDateTime generationDate) {
        this.generationDate = generationDate;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public byte[] getReportData() {
        return reportData;
    }

    public void setReportData(byte[] reportData) {
        this.reportData = reportData;
    }

    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }
}
