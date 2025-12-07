package com.crm.crmSoporteTecnico.services;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.services.models.dtos.ClientDashboardDTO;
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

    /**
     * Actualiza un usuario existente.
     * @param userId
     * @param request
     * @return
     */
    AppUser updateUser(Long userId, UserCreationRequest request);

    /**
     * Eliminar un usuario.
     * @param userId
     */
    void deleteUser(Long userId);

    /**
     * Buscar un usuario por ID para el formulario de edición.
     * @param userId
     * @return
     */
    UserCreationRequest findUserById(Long userId);

    /**
     * Obtiene una lisat de clientes para la tabla del admin.
     * @param activeFilter
     * @param packageFilter
     * @return
     */
    List<ClientDashboardDTO> getAllClients(Boolean activeFilter, String packageFilter);

}
