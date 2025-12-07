package com.crm.crmSoporteTecnico.persistence.repositories;

import com.crm.crmSoporteTecnico.persistence.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfaz que representa el repositorio de client.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Encuentra la empresa por su CIF.
     * @param cif
     * @return
     */
    Client findByCif(String cif);

    /**
     * Metodo para verificar si un NIF/CIF ya existe.
     * @param cif
     * @return
     */
    Boolean existsByCif(String cif);

    /**
     * Obtener clientes ordenados por estado.
     */
    List<Client> findAllByOrderByActiveDesc();

    /**
     * Filtra por estado.
     */
    List<Client> findByActive(Boolean active);

    /**
     * Filtra por paquete de servicio.
     */
    List<Client> findByServicePackage(String servicePackage);

}
