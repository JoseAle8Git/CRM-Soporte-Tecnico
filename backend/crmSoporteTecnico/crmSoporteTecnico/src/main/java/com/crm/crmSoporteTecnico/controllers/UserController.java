package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.services.IUserService;
import com.crm.crmSoporteTecnico.services.models.dtos.ClientDashboardDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.UserBasicDTO;
import com.crm.crmSoporteTecnico.services.models.dtos.UserCreationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para crear un nuevo usuario, solo accesible por Manager y Admin.
     * @param request
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<UserBasicDTO> createUser(@Valid @RequestBody UserCreationRequest request) {
        try{
            AppUser newUser = userService.createNewUser(request);
            UserBasicDTO response = UserBasicDTO.fromUser(newUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch(IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Endpoint para obtener yodos los usuarios.
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping("/basic-list")
    public ResponseEntity<List<UserBasicDTO>> getAllBasicUsers() {
        List<UserBasicDTO> users = userService.findAllBasicUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Enpoint para obtener un usuario por Id.
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserCreationRequest> getUserById(@PathVariable Long id){
        UserCreationRequest user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Endpoint para actualizar un usuario existente.
     * @param id
     * @param request
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserBasicDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreationRequest request) {
        try {
            AppUser updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(UserBasicDTO.fromUser(updatedUser));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Endpoint para eliminar un usuario.
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/clients-list")
    public ResponseEntity<List<ClientDashboardDTO>> getAllClients(
            @RequestParam(required = false) Boolean activeFilter,
            @RequestParam(required = false) String packageFilter
    ){
        List<ClientDashboardDTO> clients = userService.getAllClients(activeFilter, packageFilter);
        return ResponseEntity.ok(clients);
    }

}
