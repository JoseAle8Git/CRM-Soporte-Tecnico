-- V1__create_tables.sql

-- -------------------------------------------------------------------------------------------------------
-- 1. Tabla: rol (Roles y permisos)
-- Define los roles (puestos) para la gestión de acceso.
-- -------------------------------------------------------------------------------------------------------

CREATE TABLE rol (
    -- PK: Identificador único del rol. SERIAL es autoincremental en PostgreSQL.
    id SERIAL PRIMARY KEY,
    -- Nombre del rol (ADMIN, MANAGER, TECH, CLIENT). Debe ser único.
    name VARCHAR(50) NOT NULL UNIQUE,
    -- Descripción del rol.
    description VARCHAR(255)
);

-- -------------------------------------------------------------------------------------------------------
-- 2. Tabla: client (Empresas de soporte)
-- Entidad principal para las empresas que reciben el servicio.
-- -------------------------------------------------------------------------------------------------------

CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    cif VARCHAR(20) UNIQUE,
    direction VARCHAR(255),
    -- Booleano para filtros de métricas (clientes activos/inactivos).
    active BOOLEAN NOT NULL DEFAULT TRUE,
    service_package VARCHAR(50) NOT NULL,
    -- Hueco para ID de API externa.
    api_extern_id VARCHAR(50)
);

-- -------------------------------------------------------------------------------------------------------
-- 3. Tabla: user (Personal interno y cliente con acceso)
-- Almacena las credenciales y la relación con el rol y la empresa cliente.
-- -------------------------------------------------------------------------------------------------------

CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    -- Nombre de usuario para autenticación (Spring Security).
    username VARCHAR(50) NOT NULL UNIQUE,
    -- Contraseña (debe ser cifrada antes de guardarse).
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    telephone VARCHAR(15),
    email VARCHAR(100) UNIQUE,
    -- FK 1: Relación N:1 con rol.
    rol_id INT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES rol(id),
    -- FK 2: Relación N:1 con client.
    -- Solo es obligatorio si el rol es 'CLIENTE'.
    client_id INT,
    FOREIGN KEY (client_id) REFERENCES client(id)
);

-- -------------------------------------------------------------------------------------------------------
-- 4. Tabla: contact (Persona de contacto en el client)
-- Quién reporta y recibe las notificaciones de soporte.
-- -------------------------------------------------------------------------------------------------------

CREATE TABLE contact (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(50) NOT NULL,
    -- Correo clave para el sistema de correos automáticos (concurrencia).
    email VARCHAR(100) unique,
    -- FK: Relación N:1 con client.
    client_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id)
);

-- -------------------------------------------------------------------------------------------------------
-- 5. Tabla: incidence (Ticket de soporte)
-- -------------------------------------------------------------------------------------------------------

CREATE TABLE incidence (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    -- Crucial para soportes diarios y métricas (TMR).
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    close_date TIMESTAMP,
    -- Control de flujo (OPEN, IN PROGRESS, RESOLVED, CLOSE).
    status VARCHAR(20) NOT NULL,
    -- (LOW, MEDIUM, HIGH, CRITICAL).
    priority VARCHAR(10) NOT NULL,
    -- FK 1: Quién es el cliente afectado.
    client_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id),
    -- FK 2: Quién la reportó.
    contact_id INT,
    FOREIGN KEY (contact_id) REFERENCES contact(id),
    -- FK 3: Quién está resolviendo (Técnico asignado).
    tech_asigned_id INT,
    FOREIGN KEY (tech_asigned_id) REFERENCES app_user(id)
);

-- -------------------------------------------------------------------------------------------------------
-- 6. Tabla: task (Tareas derivadas de soporte técnico o comerciales)
-- Control de tareas internas del personal técnico/gerencial.
-- -------------------------------------------------------------------------------------------------------

CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    limit_date DATE,
    status VARCHAR(20) NOT NULL,
    priority INT,
    -- FK 1: A qué incidencia se relaciona (puede ser NULL)
    incidence_id INT,
    FOREIGN KEY (incidence_id) REFERENCES incidence(id),
    -- FK 2: A quién está asignada.
    user_asigned_id INT NOT NULL,
    FOREIGN KEY (user_asigned_id) REFERENCES app_user(id)
);

-- -------------------------------------------------------------------------------------------------------
-- 7. Tabla: log_notification (Auditoría de concurrencia/WebSockets)
-- Necesaria para el requerimiento obligatorio de concurrencia y auditoría.
-- -------------------------------------------------------------------------------------------------------

CREATE TABLE log_notification (
    id BIGSERIAL PRIMARY KEY,
    -- Fecha exacta de cuándo ocurrió el evento o se envió el correo.
    register_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- Tipo de evento (EMAIL_INCIDENCE, WEB_SOCKET_UPDATE, STATUS_CHANGE).
    event_type VARCHAR(50) NOT NULL,
    message TEXT,
    -- FK 1: A qué ticket de soporte se refiere el log.
    incidence_id INT,
    FOREIGN KEY (incidence_id) REFERENCES incidence(id),
    -- FK 2: Quién (o qué proceso programado) generó el evento.
    user_reponsible_id INT,
    FOREIGN KEY (user_reponsible_id) REFERENCES user(id)
);