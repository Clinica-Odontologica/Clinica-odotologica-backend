# Clínica Odontológica – Backend

API REST robusta para la gestión integral de una clínica dental, administración de turnos, pacientes y seguridad basada en roles.

## 📌 Descripción

**Clínica Odontológica App** es una plataforma diseñada para digitalizar el flujo de trabajo de un centro de salud dental. Permite a recepcionistas agendar turnos, a los doctores gestionar historias clínicas y a los administradores controlar el inventario de servicios y usuarios.

El backend está desarrollado con **Java 17 y Spring Boot 3**, diseñado bajo una arquitectura en capas, seguro mediante **Spring Security (JWT)** y fácilmente desplegable con **Docker**, utilizando **MySQL** como base de datos relacional.

## 🛠️ Stack Tecnológico

* **Java 17 (JDK)**
* **Spring Boot 3** (Web, Data JPA, Security, Validation)
* **MySQL 8.0** (Persistencia de datos)
* **JWT (JSON Web Tokens)** (Autenticación Stateless)
* **BCrypt** (Encriptación de contraseñas)
* **Swagger** (Documentación viva)
* **Docker & Docker Compose** (Containerización)
* **Maven** (Gestión de dependencias)

## 🚀 Funcionalidades Principales

* **Seguridad Avanzada:** Autenticación JWT y autorización basada en Roles (`ADMIN`, `RECEPTIONIST`, `DOCTOR`).
* **Gestión de Turnos:** Agenda centralizada con validación de disponibilidad y cálculo automático de costos.
* **Historias Clínicas:** Registro de diagnósticos y evoluciones (Exclusivo para Doctores).
* **Gestión de Pacientes:** Búsqueda rápida por DNI para recepción.
* **Catálogo de Servicios:** ABM de tratamientos y precios.
* **Borrado Lógico:** Protección de datos históricos mediante desactivación (`isActive`) en lugar de borrado físico.
* **Dashboard Administrativo:** Endpoints paginados para gestión de usuarios y métricas.
* **Documentación API:** Interfaz interactiva con Swagger UI.

## 📚 Documentación de la API

La documentación interactiva Swagger está disponible en:
👉 **http://localhost:8080/swagger-ui.html**

## 🐳 Ejecución Local con Docker

Esta aplicación está completamente containerizada para facilitar su ejecución local sin necesidad de instalar Java o MySQL manualmente.

### Requisitos

* **Docker Engine** 20.10 o superior
* **Docker Compose** 1.29 o superior

### 📥 Clonar el Repositorio

```bash
git clone [https://github.com/tu-usuario/clinica-odontologica-backend.git](https://github.com/tu-usuario/clinica-odontologica-backend.git)
cd clinica-odontologica-backend
```

### Ejecutar la Aplicación
 Antes de iniciar, asegúrate de que Docker Desktop (o el servicio de Docker) esté en ejecución.

```bash
docker-compose up --build -d
```

Este comando:

* Compila la aplicación Spring Boot (Maven Build).

* Inicia MySQL en un contenedor dedicado.

* Conecta la API a la base de datos automáticamente.

### Probar la API
 Swagger UI: Accede a http://localhost:8080/swagger-ui.html


### Detener la Aplicación
```bash
docker-compose down
```
### Eliminar todos los recursos (Limpieza total)
```bash
docker-compose down --volumes --rmi all
```
### 🔐 Variables de Entorno
* El proyecto ya viene preconfigurado en el docker-compose.yml, pero para entornos de producción o ejecución manual, estas son las variables clave:

### Base de Datos
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/clinica_db
SPRING_DATASOURCE_USERNAME=clinica_user
SPRING_DATASOURCE_PASSWORD=clinica_password
```

### Seguridad (JWT)

```bash
# Debe ser una clave segura de al menos 256 bits
JWT_SECRET=TuClaveSecretaSuperSegura_DebeSerLargaYCompleja12345
JWT_EXPIRATION=86400000
```

Desarrollado con ❤️ por Joao Dev.