export interface TechnicianIncidence {
    id: number;
    title: string;
    status: string;
    priority: string;
    creationDate: string;
    technicianId: number;   // <-- necesario para updateStatus
    clientName?: string; // <-- opcional
}
