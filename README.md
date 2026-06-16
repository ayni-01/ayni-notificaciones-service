# 🔔 Ayni Notificaciones Service

Microservicio de **dominio genérico de soporte** dentro de la plataforma **Somos Ayni**. Centraliza el envío y la gestión de alertas del sistema hacia los usuarios. Existe como dominio separado para que cualquier otro microservicio pueda solicitar el envío de notificaciones sin acoplar lógica de alertas en su propio contexto.

## Responsabilidad del Bounded Context

**Maneja:**
- Creación de notificaciones a solicitud de otros servicios (vía REST)
- Marcado de notificaciones como leídas por el usuario destinatario
- Consulta de notificaciones propias del usuario autenticado, con filtro por estado de lectura

**NO maneja:**
- Lógica de negocio propia: no decide cuándo se debe notificar, solo recibe la instrucción
- Envío de correos electrónicos ni push notifications (canales externos pueden agregarse como adaptadores secundarios)
- Perfiles de usuarios ni datos de empresas (se referencia al destinatario únicamente por `destinatarioId`)

---

## Endpoints REST

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| `POST` | `/api/v1/notificaciones` | Crear una notificación para un usuario | JWT (servicio interno o ADMIN) |
| `PATCH` | `/api/v1/notificaciones/{id}/leida` | Marcar una notificación como leída | JWT (destinatario) |
| `GET` | `/api/v1/notificaciones` | Obtener notificaciones del usuario autenticado | JWT (header `X-User-Id`) |

### Body de `POST /api/v1/notificaciones`

```json
{
  "destinatarioId": "uuid-del-usuario",
  "tipo": "NUEVA_POSTULACION",
  "mensaje": "Tienes una nueva postulación en tu reto 'Landing con React'"
}
```

### Query params de `GET /api/v1/notificaciones`

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `leida` | `Boolean` | `true` para solo leídas, `false` para no leídas. Omitir para todas. |

### Header requerido en `GET /api/v1/notificaciones`

| Header | Descripción |
|--------|-------------|
| `X-User-Id` | UUID del usuario autenticado (propagado desde el gateway o el token JWT) |

---

## Arquitectura (Hexagonal)

```
src/main/java/com/somosayni/notificaciones/
├── domain/
│   ├── model/          # Entidad Notificacion, enums TipoNotificacion y EstadoNotificacion
│   ├── port/
│   │   ├── in/         # Casos de uso (interfaces de entrada)
│   │   └── out/        # Repositorios (interfaces de salida)
│   └── exception/      # Excepciones de dominio (NotificacionNoEncontrada, etc.)
├── application/
│   └── service/        # Implementaciones de los casos de uso
└── infrastructure/
    ├── adapter/
    │   ├── in/rest/    # Controladores REST, DTOs de entrada/salida
    │   └── out/jpa/    # Repositorios JPA, entidades de BD, mappers
    └── config/         # Spring Security, Swagger, beans de configuración
```

- **Domain:** Define qué es una notificación y cuáles son sus posibles estados. Lógica mínima, sin dependencias de frameworks.
- **Application:** Orquesta los casos de uso: crear, marcar como leída y listar notificaciones de un usuario.
- **Infrastructure:** Controladores HTTP con Spring MVC, persistencia con Spring Data JPA y filtros de seguridad JWT.

---

## Modelos de dominio principales

### Notificacion

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `UUID` | Identificador único |
| `destinatarioId` | `UUID` | ID del usuario que debe recibir la notificación |
| `tipo` | `TipoNotificacion` | Categoría semántica de la alerta |
| `mensaje` | `String` | Texto legible por el usuario |
| `leida` | `Boolean` | Indica si el usuario ya la leyó |
| `fechaCreacion` | `LocalDateTime` | Timestamp de creación |
| `fechaLectura` | `LocalDateTime` | Timestamp en que fue marcada como leída (nullable) |

### Enum: `TipoNotificacion`

| Valor | Evento que lo dispara |
|-------|-----------------------|
| `NUEVA_POSTULACION` | Un talento postuló a uno de los retos de la empresa |
| `APROBADO` | El talento fue aprobado en un reto |
| `RECHAZADO` | El talento fue rechazado en un reto |
| `RETO_CERRADO` | Un reto al que postuló el talento fue cerrado |

### Estado de lectura

Las notificaciones son inmutables en su contenido. Solo cambia el campo `leida` (y `fechaLectura`) al invocar el endpoint `PATCH /{id}/leida`.

---

## Cómo ejecutar

### Local

```bash
# Requisitos: Java 21, Maven 3.9+, PostgreSQL 15+
# La base de datos 'somosayni' debe existir antes de iniciar

mvn clean package -DskipTests
java -jar target/*.jar
```

### Docker

```bash
cp .env.example .env
# Editar .env con tus valores reales
docker-compose up --build
```

---

## Variables de entorno

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `SPRING_DATASOURCE_URL` | URL JDBC de la base de datos | `jdbc:postgresql://localhost:5432/somosayni` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de PostgreSQL | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de PostgreSQL | *(obligatorio)* |
| `JWT_SECRET` | Clave secreta compartida para validar JWT | *(obligatorio)* |
| `SERVER_PORT` | Puerto en que levanta el servicio | `8086` |

---

## Swagger / OpenAPI

| | Link |
|---|---|
| **Swagger UI (local)** | [http://localhost:8086/swagger-ui.html](http://localhost:8086/swagger-ui.html) |
| **OpenAPI JSON (local)** | [http://localhost:8086/api-docs](http://localhost:8086/api-docs) |
| **swagger.json (repo)** | [ver en GitHub](https://github.com/ayni-01/ayni-notificaciones-service/blob/main/swagger.json) |
| **Swagger Editor (online)** | [abrir en Swagger Editor](https://editor.swagger.io/?url=https://raw.githubusercontent.com/ayni-01/ayni-notificaciones-service/main/swagger.json) |

> Para probar los endpoints protegidos: copia el JWT del login → clic en **Authorize** → pega `Bearer <tu-token>`.

---

## Dependencias

Este servicio forma parte del ecosistema **Somos Ayni** y comparte las siguientes convenciones:

- **Base de datos compartida:** Usa el esquema PostgreSQL `somosayni`. Cada microservicio opera sobre sus propias tablas dentro de la misma instancia.
- **Autenticación JWT:** Valida tokens firmados por `identidad-service` usando la misma clave `JWT_SECRET`. No genera tokens propios.
- **Comunicación entre servicios:** Los microservicios son independientes entre sí. Se referencian únicamente por ID (p. ej. `destinatarioId`), nunca por llamadas directas entre servicios en el flujo normal. Este servicio es el receptor pasivo: espera que otros servicios le llamen para crear notificaciones.
