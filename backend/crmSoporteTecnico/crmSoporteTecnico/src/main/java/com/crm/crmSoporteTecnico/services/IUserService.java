package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.services.models.dtos.UserBasicDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.UserCreationRequest;

import java.util.List;

public interface IUserService {

    /**
     * Función principal para crear un usuario desde el manager.
     * @param request
     * @return
     */
    AppUser createNewUser(UserCreationRequest request);

    /**
     * Listar todos los usuarios para la tabla del Dashboard.
     * @return
     */
    List<UserBasicDTO> findAllBasicUsers();

}
