package com.crm.crmSoporteTecnico.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Esta clase representa una clase entidad (rol)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rol")
public class Rol {

    /**
     * PK autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Usa la secuencia SERIAL de PostgreSQL.
    private Long id;

    /**
     * Nombre del rol. Obligatorio, único, maximo 50 caracteres.
     */
    @NotBlank(message = "El nombre del rol es obligatorio.")
    @Size(max = 50, message = "El nombre del rol no puede exceder los 50 caracteres.")
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Breve descripción del rol.
     */
    @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres.")
    @Column(name = "description", length = 255)
    private String description;

}
