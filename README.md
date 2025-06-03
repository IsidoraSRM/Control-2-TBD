# 🗂️ Sistema de Gestión de Tareas Geoespaciales

Aplicación full-stack para gestión de tareas con capacidades de georreferenciación (PostGIS). Permite a los usuarios registrar, filtrar y analizar tareas en un contexto espacial. Desarrollado con **Vue.js 3 (frontend)** y **Spring Boot 3 (backend)**.

## 🌟 Características Clave
- **🔐 Autenticación JWT**: Registro e inicio de sesión seguro.
- **🗺️ Georreferenciación**: 
  - Ubicación de usuarios y tareas con PostGIS.
  - Consultas espaciales (distancias, proximidad, agrupación).
- **📝 Gestión de Tareas**:
  - CRUD completo (Crear, Leer, Actualizar, Eliminar).
  - Filtros por estado, fecha y palabras clave.
- **🔔 Notificaciones**: Alertas para tareas próximas a vencer.
- **📊 Dashboard Analítico**:
  - Tareas más cercanas al usuario.
  - Sectores con mayor actividad.
  - Métricas de distancia promedio.

## 🛠 Stack Tecnológico
| **Área**          | **Tecnologías**                                                                 |
|-------------------|---------------------------------------------------------------------------------|
| **Frontend**      | Vue.js 3, Vue Router, Axios, Leaflet/MapLibre (mapas), Pinia (gestión de estado)|
| **Backend**       | Spring Boot 3, Spring Security, JWT, Java 17+, Maven                            |
| **Base de Datos** | PostgreSQL 15+ con PostGIS (consultas ST_Distance, ST_Within)               |
| **Herramientas**  | Docker, Git, IntelliJ IDEA, Postman                                             |

## ⚙️ Requisitos Previos
- **Node.js 18+** (para frontend)
- **Java JDK 17+** (para backend)
- **PostgreSQL 15+** con extensión PostGIS
- **Docker** (opcional, para despliegue en contenedores)

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

git clone https://github.com/tu-usuario/Control-2-TBD.git
cd Control-2-TBD


### 2. Configuración de la Base de Datos

  - Crear una base de datos PostgreSQL llamada: control2 
  - Ejecutar el script de inserción (inserts.sql) y poder tener usuarios de distinto tipo en la base de datos

  

### 3. Configuración del Backend (Spring Boot)
    Abrir el proyecto en IntelliJ IDEA (recomendado).

    Configurar credenciales en backend-c2/src/main/resources/application.properties:

    properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/nombre_base_datos
    spring.datasource.username=postgres
    spring.datasource.password=tu_contraseña
    spring.jpa.hibernate.ddl-auto=update
    Ejecutar la aplicación (clic derecho → Run Application).

### 4. Configuración del Frontend (Vue.js)
    - Ubicarse en la carpeta frontend-c2
    - Luego en la terminal ejecutar: `npm install`       # Instalar dependencias
    - Despues para ejecutar el frontend: `npm run dev`      # Iniciar servidor de desarrollo
    
   La aplicación estará disponible en lacalhost
### 5. Inicio de sesión por frontend
   - Opción 1: registrar un usuario de tipo cliente mediante el formulario de registro de la interfaz del frontend.
   - Opción 2: Iniciar sesion con uno de los clientes de la insersión de inserts.sql
   - Opción 3: Iniciar sesion como Administrador con las siguientes credenciales
     - email: admin@example.com
     - contraseña: admin1
     
###   VERSION MEDIANTE DOCKER

🚢 Instalación y Ejecución con Docker Compose
Para desplegar la aplicación con Docker Compose, sigue estos pasos:

- Asegurarse de tener Docker Desktop instalado
- Descarga e instala Docker Desktop 
- Inicia Docker Desktop antes de ejecutar los contenedores.
- Ejecutar Docker Compose desde la carpeta raíz del repositorio

Abre una terminal en la carpeta donde clonaste el repositorio y ejecuta:
docker compose up -d
- Esto iniciará todos los servicios en segundo plano.
- Verificar que los contenedores están corriendo

Para ver el estado de los contenedores, usa:
docker ps

- Apagar los servicios cuando sea necesario
Para detener y eliminar los contenedores, usa:
docker compose down


Con estos pasos, la aplicación estará corriendo en un entorno Dockerizado.

🛠 Instalación Manual del Backend (Alternativa)
Si el despliegue con Docker falla, puedes ejecutar el backend manualmente desde IntelliJ IDEA:
- Abre el proyecto en IntelliJ IDEA.
- Asegúrate de haber configurado las credenciales en el archivo:
backend-c2/src/main/resources/application.properties

.....
