/**
 * Interfaz para el ReportLogDTO del Backend.
 *  */
export interface ReportLog {
    id: number;
    generationDate: string;
    reportType: string;
    emailSent: boolean;
}