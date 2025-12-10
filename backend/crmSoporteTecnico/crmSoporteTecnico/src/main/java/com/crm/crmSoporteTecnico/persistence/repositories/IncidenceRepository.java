package com.crm.crmSoporteTecnico.persistence.repositories;

import com.crm.crmSoporteTecnico.persistence.entities.Incidence;
import com.crm.crmSoporteTecnico.persistence.enums.IncidenceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IncidenceRepository extends JpaRepository<Incidence, Long> {

    List<Incidence> findByTechnicianIsNull();

    List<Incidence> findByStatus(IncidenceStatus status);

    @Query("SELECT i.technician.name, COUNT(i) FROM Incidence i WHERE i.technician IS NOT NULL GROUP BY i.technician.name ORDER BY COUNT(i) DESC")
    List<Object[]> countIncidenceByTechnicianName();

    // BUSCAR POR ID DE CLIENTE
    // Esto hace: SELECT * FROM incidence WHERE client_id = ?
    List<Incidence> findByClientId(Long clientId);
    List<Incidence> findByClient_Id(Long clientId);

    /**
     * Verifica si existe alguna incidencia asignada a ese técnico.
     * @param id
     * @return
     */
    Boolean existsByTechnicianId(Long id);

}

