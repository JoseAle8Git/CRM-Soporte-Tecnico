# CRM - Soporte Técnico (Arquitectura Cloud End-to-End)
- Descripción del Proyecto
Sistema integral de gestión de soporte técnico (CRM) diseñado para agilizar la resolución de incidencias en entornos corporativos. El proyecto destaca por su enfoque en la automatización de procesos y la reproducibilidad de entornos mediante contenedores.

- Stack Tecnológico
Frontend: Angular con gestión de estados eficiente.

Backend: Spring Boot (Java) bajo arquitectura RESTful.

Base de Datos: PostgreSQL.

Infraestructura: Orquestación con Docker y Docker-compose.

- Características Principales e Infraestructura
Dockerización Total: Entorno de desarrollo idéntico a producción mediante docker-compose.

Automatización de Reportes: Motor de generación de métricas empresariales y facturación exportables a PDF de forma periódica.

Sistema de Notificaciones: Integración de Java Mail Sender para alertas automáticas entre usuarios y técnicos.

- Instalación y Ejecución Local
Para levantar el proyecto completo (Frontend, Backend y Base de Datos) con un solo comando, asegúrate de tener instalado Docker y ejecuta:

Bash
# Clonar el repositorio
git clone https://github.com/JoseAle8Git/CRM-Soporte-Tecnico.git

# Levantar la infraestructura
docker-compose up --build

Accede a la aplicación en http://localhost:4200 y a la API en http://localhost:8080.

