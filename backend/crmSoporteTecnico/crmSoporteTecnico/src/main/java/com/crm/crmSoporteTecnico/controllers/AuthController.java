package com.crm.crmSoporteTecnico.controllers;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.services.IAuthService;
import com.crm.crmSoporteTecnico.services.models.dtos.AuthResponse;
import com.crm.crmSoporteTecnico.services.models.dtos.LoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    /**
     * Inyección del servicio por constructor.
     * @param authService
     */
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para el login.
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        System.out.println("Login request: " + request.username());
        try {
            // Autenticar el usuario y obtener el objeto user.
            AppUser user = authService.authenticate(request.username(), request.password());

            // Genera el token JWT.
            String token = authService.generateToken(user.getUsername());

            String cookie = String.format(
                    "jwt=%s; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=3600",
                    token
            );
            response.setHeader("Set-Cookie", cookie);

            // Devolver la información no sensible al Frontend.
            AuthResponse authResponse = AuthResponse.fromUser(user);
            return ResponseEntity.ok(authResponse);
        }catch (BadCredentialsException ex) {
            // Si las credenciales son inválidas, se devuelve un 401 Unauthorized.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos.");
        }
    }

    /**
     * Endpoint para el logout.
     * @param response
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = null;
        if(request.getCookies() != null) {
            for(Cookie c : request.getCookies()) {
                token = c.getValue();
                break;
            }
        }
        if(token != null) {
            authService.invalidateToken(token);
        }

        String cookie = "jwt=; Path=/; HttpOnly; SameSite=None; Max-Age=0";
        response.setHeader("Set-Cookie", cookie);

        return ResponseEntity.ok("Sesión cerrada correctamente");
    }

}
