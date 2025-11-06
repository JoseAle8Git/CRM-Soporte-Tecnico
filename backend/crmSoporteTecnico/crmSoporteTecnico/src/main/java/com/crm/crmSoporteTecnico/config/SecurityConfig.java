package com.crm.crmSoporteTecnico.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Esta clase representa varias configuraciones de la app.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final RsaKeyProperties rsaKeys;

    /**
     * Inyección de las propiedades de las llaves RSA.
     * @param rsaKeys
     */
    public SecurityConfig(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    /**
     * Definición del passwordEncoder.
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private RSAPublicKey loadPublicKey() throws Exception {
        String key = new String(rsaKeys.publicKeyLocation().getEncoded())
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\\s+", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(decoded));
    }

    private RSAPrivateKey loadPrivateKey() throws Exception {
        String key = new String(rsaKeys.privateKeyLocation().getEncoded())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\s+", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    /**
     * definción de la cadena de filtros de seguridad.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Se deshabilita CSRF (típico con REST/JWT).
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests((auth -> auth
                .requestMatchers("/sistema/api/v1").permitAll()
                .anyRequest().authenticated())
        );

        // Configuración clave para JWT: Sesión sin estado (Stateless)
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {})); // Esto habilita el manejo de JWT.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * Bean para decodificar (Verificar) el JWT.
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        return NimbusJwtDecoder.withPublicKey(loadPublicKey()).build();
    }

    /**
     * Bean para codificar (Crear) el JWT.
     * @return
     */
    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        // Se usa la llave privada para firmar los tokens que el servidor genera.
        JWK jwk = new RSAKey.Builder(loadPublicKey()).privateKey(loadPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}
