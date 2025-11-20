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
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del contacto es obligatorio.")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "position", length = 50)
    private String burden;

    @Email(message = "El formato del correo electrónico no es válido.")
    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Size(max = 15, message = "El teléfono no debe exceder los 15 caracteres.")
    @Column(name = "telephone", length = 15)
    private String telephone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

}
