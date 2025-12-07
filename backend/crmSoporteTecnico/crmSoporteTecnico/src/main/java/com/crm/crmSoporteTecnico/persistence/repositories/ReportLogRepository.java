package com.crm.crmSoporteTecnico.persistence.repositories;

import com.crm.crmSoporteTecnico.persistence.entities.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de la clase ReportLog
 */
public interface ReportLogRepository extends JpaRepository<ReportLog, Long> {

    /**
     * Obtiene todos los logs, ordenados del más reciente al más antiguo.
     * @return
     */
    List<ReportLog> findAllByOrderByGenerationDateDesc();

}
