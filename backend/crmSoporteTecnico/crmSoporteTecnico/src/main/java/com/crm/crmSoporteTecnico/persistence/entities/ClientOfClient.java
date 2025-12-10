package com.crm.crmSoporteTecnico.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "clients_of_clients") // El nombre exacto de la tabla en SQL
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientOfClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Los campos de datos
    private String name;
    private Boolean active;
    private BigDecimal billing; // Para el dinero

    // LA RELACIÓN (Foreign Key)
    // Esto conecta con la empresa padre
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false) // Debe coincidir con la columna del SQL
    private Client client;
}