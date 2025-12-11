package com.crm.crmSoporteTecnico.services.impl;

import com.crm.crmSoporteTecnico.persistence.entities.AppUser;
import com.crm.crmSoporteTecnico.persistence.repositories.UserRepository;
import com.crm.crmSoporteTecnico.services.IAuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * Servicio de autenticación de usuario.
 */
@Service
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Implementación de la lógica de autenticación.
     *
     * @param username
     * @param rawPassword
     * @return
     */
    @Override
    public AppUser authenticate(String username, String rawPassword) {
        Optional<AppUser> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user; // --> Autenticación exitosa
            }
        }
        throw new BadCredentialsException("Credenciales inválidas.");
    }

    /**
     * Implementación de la generación de JWT.
     *
     * @param username
     * @return
     */
    @Override
    public String generateToken(String username) {
        Instant now = Instant.now();
        long expiry = 3600L; // --> 1 hora de expiración.

// 1. Recuperamos el usuario COMPLETO primero
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

        // 2. Preparamos el ID del cliente (con seguridad por si es null)
        Long clientId = (user.getClient() != null) ? user.getClient().getId() : null;

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(username)
                .claim("role", user.getRol().getName())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * Metodo auxiliar para obtener el rol.
     *
     * @param username
     * @return
     */
    private String getUserRole(String username) {
        return userRepository.findByUsername(username)
                .map(u -> u.getRol().getName())
                .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado al intentar obtener el rol."));
    }

}
