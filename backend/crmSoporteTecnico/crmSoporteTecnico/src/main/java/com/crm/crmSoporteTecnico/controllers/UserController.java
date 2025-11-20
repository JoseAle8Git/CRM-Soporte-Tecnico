package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.services.IUserService;
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
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER')")
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

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_MANAGER')")
    @GetMapping("/basic-list")
    public ResponseEntity<List<UserBasicDTO>> getAllBasicUsers() {
        List<UserBasicDTO> users = userService.findAllBasicUsers();
        return ResponseEntity.ok(users);
    }

}
