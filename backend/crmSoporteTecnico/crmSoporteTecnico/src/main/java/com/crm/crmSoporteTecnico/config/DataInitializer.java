package com.crm.crmSoporteTecnico.config;

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
                "admin@crm.com",
                adminRole,
                null
        );

        userRepository.save(adminUser);

        System.out.println("Roles y usuario iniciales creados exitosamente.");
    }

}
