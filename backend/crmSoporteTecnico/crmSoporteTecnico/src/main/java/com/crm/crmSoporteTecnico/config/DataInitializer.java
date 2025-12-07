package com.crm.crmSoporteTecnico.config;

import com.crm.crmSoporteTecnico.persistence.entities.Client;
import com.crm.crmSoporteTecnico.persistence.entities.Rol;
import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.repositories.ClientRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.RolRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Se le indica a Spring que esta clase es un bean y debe ejecutarse al inicio.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;

    /**
     * Inyección de dependencias por constructor.
     * @param rolRepository
     * @param passwordEncoder
     * @param userRepository
     */
    public DataInitializer(RolRepository rolRepository, ClientRepository clientRepository, UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
    }

    /**
     * El método 'run' se ejecuta inmediatamente después de que Spring carga el contexto.
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // Solo ejecuta la inicialización si la tabla de roles está vacía.
        if(rolRepository.count() == 0) {
            System.out.println("Inicializando roles y usuario administrador...");
            initializeRolesUsers();
        }
    }

    private void initializeRolesUsers() {

        // Definición de roles.
        Rol adminRole = new Rol(null, "ADMIN", "Administrador del sistema con control total.");
        Rol managerRole = new Rol(null, "MANAGER", "Supervisor de soporte y gestión de métricas.");
        Rol techRole = new Rol(null, "TECH", "Usuario externo con acceso limitado a sus propias incidencias.");
        Rol clientRole = new Rol(null, "CLIENT", "Usuario externo con acceso limitado a su propia empresa.");

        // Guardar todos los roles en la BD.
        rolRepository.saveAll(Arrays.asList(adminRole, managerRole, techRole, clientRole));

        // Creación del Usuario Inicial
        // Contraseña: "Admin123@"
        String encodedPassword = passwordEncoder.encode("Admin123@");

        AppUser adminUser = new AppUser(
                null,
                "admin",
                encodedPassword,
                "Super Admin",
                "555-1234",
                "crmdmndmn@gmail.com",
                adminRole,
                null
        );

        userRepository.save(adminUser);

        String pass1 = passwordEncoder.encode("Manager123@");
        String pass2 = passwordEncoder.encode("Tech123@");
        String pass3 = passwordEncoder.encode("Client123@");

        AppUser appUser1 = new AppUser(
                null,
                "manager",
                pass1,
                "Super Manager",
                "444-1234",
                "manager@crm.com",
                managerRole,
                null
        );
        userRepository.save(appUser1);
        AppUser appUser2 = new AppUser(
                null,
                "tech",
                pass2,
                "Super Tech",
                "333-1234",
                "tech@crm.com",
                techRole,
                null
        );
        userRepository.save(appUser2);

        Client client = new Client(
                null,
                "Cliente Prueba",
                "123456789",
                "Dirección Prueba",
                true,
                "CORPORATIVO",
                null
        );
        clientRepository.save(client);
        AppUser appUser3 = new AppUser(
                null,
                "client",
                pass3,
                "Super Client",
                "222-1234",
                "client@crm.com",
                clientRole,
                client
        );
        userRepository.save(appUser3);

        System.out.println("Roles y usuario iniciales creados exitosamente.");
    }

}
