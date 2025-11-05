package com.crm.crmSoporteTecnico.persistence.repositories;

import com.crm.crmSoporteTecnico.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interfaz que representa el repositorio de user.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    /**
     * Buscar un Usuario por su username para el Login/Autenticación.
     * @param username
     * @return
     */
    Optional<User> findByUsername(String username);

    /**
     * Verificar si un username ya existe antes de registrar uno nuevo.
     * @param username
     * @return
     */
    Boolean existsByUsername(String username);

    /**
     * Verificar su un email ya existe.
     * @param email
     * @return
     */
    Boolean existsByEmail(String email);

}
