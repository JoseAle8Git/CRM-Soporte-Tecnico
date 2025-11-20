package com.crm.crmSoporteTecnico.persistence.repositories;

import com.crm.crmSoporteTecnico.persistence.entities.ClientOfClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClientOfClientRepository extends JpaRepository<ClientOfClient, Long> {

    // Esta función busca todos los registros donde la columna client_id sea X
    List<ClientOfClient> findByClientId(Long clientId);
}