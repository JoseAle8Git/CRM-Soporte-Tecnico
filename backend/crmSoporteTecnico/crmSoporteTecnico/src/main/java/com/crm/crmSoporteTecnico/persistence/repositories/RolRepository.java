package com.crm.crmSoporteTecnico.persistence.repositories;

import com.crm.crmSoporteTecnico.persistence.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interfaz que representa el repositorio de rol.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    /**
     * Encuentra un rol por su nombre.
     * @param name
     * @return
     */
    Optional<Rol> findByName(String name);

}
