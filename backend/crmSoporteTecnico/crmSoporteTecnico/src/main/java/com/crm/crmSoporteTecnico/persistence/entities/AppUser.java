package com.crm.crmSoporteTecnico.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {

    /**
     * PK autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del usuario para el login. Obligatorio y único.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Contraseña cifrada. Obligatoria.
     */
    @NotBlank(message = "La contraseña es obligatoria.")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    /**
     * Nombre completo. Obligatorio.
     */
    @NotBlank(message = "El nombre completo es obligatorio.")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Teléfono.
     */
    @Size(max = 15, message = "El teléfono no debe exceder los 15 caracteres.")
    @Column(name = "telephone", length = 15)
    private String telephone;

    /**
     * Correo electrónico. Válida el formato de email.
     */
    @Email(message = "El formato del correo electrónico no es válido.")
    @Size(max = 100, message = "El correo no debe exceder los 100 caracteres.")
    @Column(name = "email", unique = true, length = 100)
    private String email;

    /**
     * Relación 1:N con rol.
     * Se optimiza con lazy para rendimiento (evita N+1).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    /**
     * Relacion N:1 con client.
     * Se mantiene el lazy por rendimiento.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

}
