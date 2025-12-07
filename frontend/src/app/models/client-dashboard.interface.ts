/**
 * Interfaz para la tabla de clientes del Admin.
 */
export interface ClientInfo {
    id: number;
    companyName: string;
    cif: string;
    servicePackage: string;
    active: boolean;
    deirection: string;
}

/**
 * Interfaz para la gráfica circular de paquetes.
 */
export interface PackageCount {
    packageName: string;
    clientCount: number;
}