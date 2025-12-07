package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;

/**
 * Interfaz (Contrato) para el servicio de autenticación.
 */
public interface IAuthService {

    /**
     * Método para autenticar un usuario.
     * @param username
     * @param rawPassword
     * @return
     */
    AppUser authenticate(String username, String rawPassword);

    /**
     * Método para generar un token JWT para un usuario autenticado.
     * @param username
     * @return
     */
    String generateToken(String username);

}
