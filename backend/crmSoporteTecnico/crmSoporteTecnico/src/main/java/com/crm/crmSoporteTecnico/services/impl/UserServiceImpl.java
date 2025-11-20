package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.entities.Client;
import com.crm.crmSoporteTecnico.persistence.entities.Rol;
import com.crm.crmSoporteTecnico.persistence.repositories.ClientRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.RolRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.UserRepository;
import com.crm.crmSoporteTecnico.services.INotificationService;
import com.crm.crmSoporteTecnico.services.IUserService;
import com.crm.crmSoporteTecnico.services.models.dtos.UserBasicDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.UserCreationRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que implementa lógica para la creación de usuarios.
 */
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final INotificationService notificationService;

    public UserServiceImpl(UserRepository userRepository, RolRepository rolRepository, ClientRepository clientRepository, PasswordEncoder passwordEncoder, INotificationService notificationService) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }

    /**
     * Lógica para crear un nuevo usuario.
     * Se realiza dentro de una transacción para asegurar la integridad de los datos.
     * @param request
     * @return
     */
    @Override
    @Transactional
    public AppUser createNewUser(UserCreationRequest request) {
        // Validaciones de unicidad.
        if(userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
        if(userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El correo electrónico ya está en uso.");
        }

        // Obtener rol y cliente.
        Rol rol = rolRepository.findByName(request.roleName())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado."));
        Client client = null;
        if(request.roleName().equalsIgnoreCase("CLIENT")) {
            if(request.clientId() == null) {
                throw new IllegalArgumentException("Se requiere el ID de cliente para el rol CLIENT.");
            }
            client = clientRepository.findById(request.clientId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado."));
        }

        // Creación de la entidad.
        String encodedPassword = passwordEncoder.encode(request.rawPassword());

        AppUser newUser = new AppUser(
                null,
                request.username(),
                encodedPassword,
                request.name(),
                request.telephone(),
                request.email(),
                rol,
                client
        );
        AppUser savedUser = userRepository.save(newUser);

        // Notificación de las credenciales al usuario.
        notificationService.notifyNewUserCredentials(savedUser, request.rawPassword());

        return savedUser;
    }

    /**
     * Retorna todos los usuarios mapeados al DTO básico.
     * @return
     */
    @Override
    public List<UserBasicDTO> findAllBasicUsers() {
        return userRepository.findAll().stream().map(UserBasicDTO::fromUser).collect(Collectors.toList());
    }

}
