package com.crm.crmSoporteTecnico.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Esta clase representa una clase entidad (client).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client {

    /**
     * PK autoincrmental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la empresa. Obligatorio, maximo 100 caracteres.
     */
    @NotBlank(message = "El nombre de la empresa es obligatorio.")
    @Size(max = 100, message = "El nombre debe exceder los 100 caracteres.")
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    /**
     * Identificador fiscal de la empresa (NIF). Obligatorio y único.
     */
    @NotBlank(message = "El CIF/NIF es obligatorio.")
    @Size(min = 9, max = 20, message = "El CIF debe tener entre 9 y 20 caracteres.")
    @Column(name = "cif", nullable = false, unique = true, length = 20)
    private String cif;

    /**
     * Dirección física.
     */
    @Column(name = "direction", length = 255)
    private String direction;

    /**
     * Estado de actividad. Obligatorio.
     */
    @NotNull(message = "El estado de actividad es obligatorio.")
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    /**
     * Paquete de servicio seleccionado.
     */
    @NotBlank(message = "El paquete de servicio es obligatorio.")
    @Column(name = "service_package", nullable = false, length = 50)
    private String servicePackage;

    /**
     * ID para posible integración de API externa.
     */
    @Column(name = "api_extern_id", length = 50)
    private String apiExternId;

}
