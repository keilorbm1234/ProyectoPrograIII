# Sistema de Reserva de Recursos

Proyecto #1 — EIF206 Programación 3 (2026-II)
Universidad Nacional de Costa Rica — Escuela de Informática

## Descripción

Sistema de escritorio desarrollado en **Java** para que una organización gestione las **reservas de recursos** (salas, computadoras, proyectores, etc.) requeridos por sus funcionarios para realizar actividades de trabajo.

Cada funcionario puede describir una actividad (fecha, hora de inicio y fin) y seleccionar las categorías de recursos que necesita. El sistema valida la disponibilidad de al menos una unidad de cada categoría solicitada:

- **Si hay éxito**, la reserva se concreta con el primer recurso disponible de cada categoría.
- **Si no hay éxito**, se indican las categorías no disponibles y el funcionario puede modificar la reserva e intentar de nuevo.

El sistema además permite completar el formulario de reserva mediante **Inteligencia Artificial**: el usuario escribe una frase en lenguaje natural describiendo la actividad y los recursos requeridos, y un LLM extrae los datos y llena el formulario automáticamente (editable antes de confirmar).

## Funcionalidades

| # | Funcionalidad | Rol(es) | Porcentaje |
|---|---------------|---------|------------|
| 1 | Ingreso (login) y cambio de clave | Administrador / Funcionario | 5% |
| 2 | Reservas (crear, ver, cancelar) | Funcionario | 25% |
| 3 | Lista de Funcionarios (CRUD + búsqueda) | Administrador | 10% |
| 4 | Lista de categorías de recursos (CRUD + búsqueda) | Administrador | 10% |
| 5 | Lista de recursos (CRUD + filtrado por categoría) | Administrador | 10% |
| 6 | Visualización de calendarización de recursos | Administrador / Funcionario | 10% |
| 7 | Visualización de programación de actividades | Administrador / Funcionario | 10% |
| 8 | Estadísticas (recursos y actividades, con gráficos) | Administrador / Funcionario | 20% |

Todas las funcionalidades incluyen la opción de generar un **reporte en formato PDF**.

### Detalle de funcionalidades

**1. Ingreso (login)**
Los usuarios (administrador o funcionario) ingresan con su `id` y `clave`, y pueden cambiar su clave en cualquier momento.

**2. Reservas**
Un funcionario puede ver sus reservas, crear nuevas (manualmente o asistido por IA) o cancelar una reserva futura, liberando los recursos asignados.

**3. Lista de Funcionarios**
Búsqueda por id o nombre, inclusión, consulta, modificación y borrado. Cada funcionario tiene nombre y teléfono además de sus datos de usuario. Al crearse, su clave inicial es igual a su id.

**4. Lista de categorías de recursos**
Búsqueda por descripción, inclusión, consulta, modificación y borrado. Cada categoría tiene un id autogenerado y una descripción (ej. *"Sala para 10 personas"*, *"Laptop windows 11"*).

**5. Lista de recursos**
Filtrado por categoría, inclusión, consulta, modificación y borrado. Cada recurso tiene id/número de activo, categoría y descripción (ej. *"Sala 1 primer piso"*, *"Laptop #238715"*).

**6. Calendarización de recursos**
Dada una fecha y una categoría, muestra una matriz (horas × recursos) indicando qué recurso está reservado, con el nombre de la actividad y del funcionario.

**7. Programación de actividades**
Para una semana dada, muestra una matriz (horas × días) con las actividades programadas, indicando actividad y funcionario responsable.

**8. Estadísticas**
Dado un rango de fechas, muestra cantidad de recursos reservados por categoría y actividades programadas por semana, cada una con su gráfico de barras.

## Arquitectura

- **Arquitectura por capas**: `datos` (persistencia) / `lógica` (reglas de negocio) / `presentación` (interfaz de usuario).
- **Patrón MVC** en la capa de presentación.
- **Persistencia** en archivos **XML** mediante **JAXB**.
- **Interfaz gráfica** de escritorio con **Java Swing**.
- Dos tipos de usuario: **administrador** y **funcionario**, cada uno con id, clave y rol.

## Tecnologías utilizadas

- **Java** (Swing / GUI Forms de IntelliJ)
- **JAXB** — persistencia de datos en XML
- **langchain4j** (OpenAI) — extracción de datos de reserva a partir de lenguaje natural
- **iText7** — generación de reportes en PDF
- **JFreeChart** — gráficos de estadísticas
- **JUnit Jupiter** — pruebas unitarias (Surefire) y de integración (Failsafe)
- **Maven** — gestión de dependencias y build
- **Git / GitHub** — control de versiones

## Estructura del proyecto

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── datos/          # Capa de acceso a datos (XML/JAXB)
│   │   │   ├── logica/         # Capa de lógica de negocio
│   │   │   └── presentacion/   # Capa de presentación (MVC, Swing)
│   │   └── resources/
│   └── test/
│       └── java/               # Pruebas unitarias e integración (JUnit Jupiter)
├── pom.xml
└── README.md
```

> Ajustar según la estructura real de paquetes del repositorio.

## Requisitos previos

- JDK 17 o superior
- Maven 3.8+
- Clave de API de OpenAI (para la funcionalidad de extracción por IA) configurada como variable de entorno

## Instalación y ejecución

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/keilorbm1234/ProyectoPrograIII.git
   cd ProyectoPrograIII
   ```
2. Configurar la clave de API necesaria para la integración con IA (ver sección de configuración en el código o `.env.example` si aplica).
3. Compilar el proyecto:
   ```bash
   mvn clean install
   ```
4. Ejecutar la aplicación:
   ```bash
   mvn exec:java
   ```
   o ejecutar la clase principal (`Main`) desde el IDE.

## Ejecución de pruebas

- Pruebas unitarias (Surefire):
  ```bash
  mvn test
  ```
- Pruebas de integración (Failsafe):
  ```bash
  mvn verify
  ```

## Reglas del proyecto

- Arquitectura por capas y patrón MVC obligatorios.
- Validación y reporte adecuado de errores en todos los datos.
- Equipos de tres personas, matriculados en el mismo grupo.
- Repositorio GitHub compartido, actualizado permanentemente (habrá revisiones de avance).
- El proyecto debe incluir Test Cases con JUnit Jupiter (unitarios e integración).
- Entrega y defensa del proyecto según cronograma publicado por el profesor.

## Integrantes

- Keilor Baltodano Martínez
- Samuel Cortés San Lee
- Fernanda Alfaro Ortega

## Curso

**EIF206 — Programación 3 (2026-II)**
Escuela de Informática, Universidad Nacional de Costa Rica
