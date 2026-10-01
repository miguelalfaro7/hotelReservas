# HotelSpringBoot

API REST de reservas de hotel construida con Spring Boot 3.3.4, Java 17, Spring Data JPA, PostgreSQL y MapStruct 1.5.5. Administra clientes, habitaciones estándar y suites presidenciales, y reservas.

## Requisitos

- Java 17
- PostgreSQL con una base de datos llamada `hotel_reservas_db`
- Maven (o el wrapper incluido)

Configura usuario y contraseña de PostgreSQL en `src/main/resources/application.properties` antes de iniciar la aplicación.

## Ejecutar

Desde la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

La API queda disponible por defecto en `http://localhost:8080`.

## Endpoints

| Verbo | Ruta | Respuesta correcta | Errores relevantes |
|---|---|---:|---|
| POST | `/api/clientes` | 201 Created | 400 validación/JSON, 409 email duplicado |
| GET | `/api/clientes` | 200 OK | — |
| GET | `/api/clientes/{id}` | 200 OK | 404 no encontrado |
| GET | `/api/clientes/{id}/resumen` | 200 OK | 404 no encontrado |
| PATCH | `/api/clientes/{id}` | 200 OK | 400 validación/JSON, 404 no encontrado, 409 email duplicado |
| POST | `/api/habitaciones/estandar` | 201 Created | 400 validación/JSON |
| POST | `/api/habitaciones/suites` | 201 Created | 400 validación/JSON |
| GET | `/api/habitaciones` | 200 OK | — |
| GET | `/api/habitaciones/{id}` | 200 OK | 404 no encontrado |
| POST | `/api/reservas` | 201 Created | 400 fechas/datos inválidos, 404 cliente o habitación |
| GET | `/api/reservas` | 200 OK | — |
| GET | `/api/reservas/{id}` | 200 OK | 404 no encontrado |

Los errores se entregan como JSON con `timestamp`, `status`, `error` y `mensaje`. JSON mal formado y validaciones del dominio producen 400; recursos inexistentes producen 404; el email duplicado produce 409.

## Solución del taller

- **Contratos polimórficos:** `HabitacionResponse` es una interfaz sellada con los subtipos estándar y suite. `HabitacionMapper` usa `@SubclassMapping` para generar las proyecciones concretas; Jackson incluye `tipo` como `ESTANDAR` o `SUITE`.
- **Resumen del cliente:** `ClienteMapper` calcula cantidad y suma de reservas con Streams y proyecta reservas a `ReservaItemResponse`, un DTO plano sin una referencia de vuelta al cliente. Las reservas recientes se ordenan por fecha de inicio descendente.
- **PATCH seguro:** el mapper usa `NullValuePropertyMappingStrategy.IGNORE`; los valores omitidos o nulos se conservan. La entidad Cliente no ofrece setters para `id`, `activo`, `penalizaciones` ni `reservas`, evitando asignación masiva y preservando sus reglas de dominio.
- **Semántica HTTP:** `GlobalExceptionHandler` convierte excepciones de dominio en respuestas 400, 404 y 409 con un contrato de error común.

## Solicitudes de demostración

El archivo [`solicitudes-avanzadas.http`](solicitudes-avanzadas.http) contiene solicitudes REST Client de VS Code que crean cliente, habitaciones y reserva, consultan el listado y resumen, verifican el PATCH y muestran un 404. Las respuestas de creación guardan automáticamente los UUID para las solicitudes siguientes.

## Pruebas automatizadas

Ejecuta la suite completa con:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

Las pruebas de los mappers no necesitan PostgreSQL ni inician el contexto de Spring.
