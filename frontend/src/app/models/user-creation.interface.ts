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
    clientId: number | null;
}