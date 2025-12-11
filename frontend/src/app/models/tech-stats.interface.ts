/**
 * Interfaz que coincide con TechIncidenceStatsDTO.java.
 */
export interface TechIncidenceStats {
    technicianName: string;
    incidenceCount: number;
}
/**
 * Interfaz para las estadísticas personales del técnico (TechnicianPersonalStatsDTO.java).
 */
export interface TechnicianPersonalStats {
    open: number;
    pending: number;
    inProgress: number;
    resolved: number;
    closed: number;
}