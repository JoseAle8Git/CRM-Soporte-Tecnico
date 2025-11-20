package com.crm.crmSoporteTecnico.persistence.enums;

/**
 * Estados de una incidencia para el seguimiento.
 */
public enum IncidenceStatus {

    OPEN, // Abierta, sin asignar.
    IN_PROGRESS, // Asignada y en proceso.
    PENDING, // En espera de respuesta del cliente.
    RESOLVED, // Resuelta, pendiente de cierre.
    CLOSED // Cerrada permanentemente.

}
