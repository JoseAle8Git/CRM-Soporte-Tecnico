package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.entities.Client;
import com.crm.crmSoporteTecnico.persistence.entities.Rol;
import com.crm.crmSoporteTecnico.persistence.repositories.ClientRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.IncidenceRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.RolRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.UserRepository;
import com.crm.crmSoporteTecnico.services.INotificationService;
import com.crm.crmSoporteTecnico.services.IUserService;
import com.crm.crmSoporteTecnico.services.models.dtos.ClientDashboardDTO;
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
    private final IncidenceRepository incidenceRepository;

    public UserServiceImpl(UserRepository userRepository, RolRepository rolRepository, ClientRepository clientRepository, PasswordEncoder passwordEncoder, INotificationService notificationService, IncidenceRepository incidenceRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
        this.incidenceRepository = incidenceRepository;
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
        Client newClient = null;
        if(request.roleName().equalsIgnoreCase("CLIENT")) {
            if(clientRepository.existsByCif(request.cif())) {
                throw new IllegalArgumentException("El cif de la empresa ya existe.");
            }

            newClient = new Client(
                    null,
                    request.companyName(),
                    request.cif(),
                    null,
                    true,
                    request.packageName(),
                    null
            );
            newClient = clientRepository.save(newClient);
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
                newClient
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

    /**
     * Actualizar un usuario.
     * @param userId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public AppUser updateUser(Long userId, UserCreationRequest request) {
        AppUser existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado para edición."));
        existingUser.setName(request.name());
        existingUser.setEmail(request.email());
        existingUser.setTelephone(request.telephone());
        if(request.rawPassword() != null && !request.rawPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(request.rawPassword());
            existingUser.setPassword(encodedPassword);
        }
        return userRepository.save(existingUser);
    }

    /**
     * Borrar un usuario por Id.
     * @param userId
     */
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        boolean hasAssignedIncidence = incidenceRepository.existsByTechnicianId(userId);
        AppUser userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        if(hasAssignedIncidence) {
            throw new IllegalArgumentException("No se puede eliminar el usuario. Aún tiene incidencias asignadas.");
        }
        if(userToDelete.getClient() != null) {
            Client client = userToDelete.getClient();
            client.setActive(false);
            clientRepository.save(client);
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserCreationRequest findUserById(Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        return new UserCreationRequest(
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getTelephone(),
                null,
                user.getRol().getName(),
                null,
                null,
                null,
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDashboardDTO> getAllClients(Boolean activeFilter, String packageFilter) {
        List<Client> clients;

        if(activeFilter != null) {
            clients = clientRepository.findByActive(activeFilter);
        } else if(packageFilter != null && !packageFilter.isBlank()) {
            clients = clientRepository.findByServicePackage(packageFilter);
        } else {
            clients = clientRepository.findAllByOrderByActiveDesc();
        }

        return clients.stream()
                .map(ClientDashboardDTO::fromClient)
                .collect(Collectors.toList());
    }

}
