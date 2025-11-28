/**
 * Interfaz que coincide con UserCreationRequest.java .
 */
export interface UserCreationRequest {
    username: string;
    name: string;
    email: string;
    telephone: string | null;
    rawPassword: string;
    roleName: string;
    companyName: string;
    cif: string;
    packageName: string;
    clientId: number | null;
}