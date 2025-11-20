/**
 * Interfaz para la lista de incidencias en el Dashboard del manager.
 */
export interface IncidenceDashboard {
    id: number;
    title: string;
    status: string;
    priority: string;
    clientName: string;
    technicianName: string;
    creationDate: string;
}

/**
 * Interfaz para la solicitud de asignación.
 */
export interface AssignmentRequest {
    incidenceId: number;
    technicianId: number;
}