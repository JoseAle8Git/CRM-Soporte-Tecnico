package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.entities.Client;
import com.crm.crmSoporteTecnico.persistence.entities.ClientOfClient;
import com.crm.crmSoporteTecnico.persistence.repositories.ClientOfClientRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.ClientRepository;
import com.crm.crmSoporteTecnico.persistence.repositories.UserRepository;
import com.crm.crmSoporteTecnico.services.models.dtos.UserContextDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// Importa el DTO
import com.crm.crmSoporteTecnico.services.models.dtos.CompanyUserDTO;

import java.util.stream.Collectors;

import java.util.List;

@RestController
@RequestMapping("/clients") // Ruta protegida
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientOfClientRepository subClientRepo;

    // 1. DATOS DE LA EMPRESA (Para la tarjeta del Dashboard)
// 1. Endpoint: Datos de la empresa
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(client -> ResponseEntity.ok(client))
                .orElse(ResponseEntity.notFound().build());
    }

    // 2. Endpoint: Obtener compañeros (¡AHORA DEVUELVE DTOs!)
    @GetMapping("/{id}/users")
    public ResponseEntity<List<CompanyUserDTO>> getUsersByClient(@PathVariable Long id) {

        // 1. Buscamos los usuarios en la base de datos
        List<AppUser> users = userRepository.findByClientId(id);

        // 2. LOS CONVERTIMOS A DTO (Para quitar contraseñas y bucles)
        List<CompanyUserDTO> usersDTO = users.stream()
                .map(CompanyUserDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(usersDTO);
    }

    // 3. SUB-CLIENTES Y FACTURACIÓN (Para la pestaña "Facturación")
    @GetMapping("/{id}/sub-clients")
    public ResponseEntity<List<ClientOfClient>> getSubClients(@PathVariable Long id) {
        // Necesitas tener 'findByClientId' en ClientOfClientRepository
        List<ClientOfClient> misClientes = subClientRepo.findByClientId(id);
        return ResponseEntity.ok(misClientes);
    }

    // 4. BUSCAR USUARIO LOGUEADO
    // URL Final: /sistema/api/v1/clients/profile
    @GetMapping("/profile")
    public ResponseEntity<UserContextDTO> getMyProfile() {

        // A. Quién está logueado?
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // B. Buscamos sus datos
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // C. Devolvemos el DTO completo
        return ResponseEntity.ok(UserContextDTO.fromEntity(user));
    }
}