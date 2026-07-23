## 🌶️ La Chefa - Sistema Central y E-commerce de Salsas & Encurtidos
¡Bienvenido al motor digital de La Chefa! Este proyecto es una plataforma robusta diseñada para la gestión, logística y venta de productos gourmet picantes, desarrollada por Felipe Rojas F. como parte de su portafolio profesional.

## Lógica del Negocio
La plataforma está diseñada bajo un modelo de Separación de Responsabilidades para garantizar la integridad de los datos de la pyme y agilizar la operación logística:

- **Gestión de Inventario Dinámico:** Control total sobre el stock de salsas y encurtidos en la Central (Bodega) mediante JPA y PostgreSQL 18.
- **Sistema Logístico y Puntos de Venta (POS):** Gestión de inventario distribuido, control de mermas y devoluciones. Permite trasladar stock a diferentes locaciones, stands o ferias.
- **Sala de Control (Dashboard v2.0):** Inteligencia de negocios en tiempo real con monitoreo de KPIs de ventas por jornada, rendimiento de Crew (vendedores), rankings de locaciones y control geolocalizado mediante mapas.

- **Sistema de Roles (RBAC):**
  - **Clientes:** Pueden registrarse, ver el catálogo en modo Dark Premium y explorar el recetario.
  - **Vendedores (Crew):** Personal de terreno asignado a los Puntos de Venta responsables de las transacciones in-situ.
  - **Administradores:** Acceso exclusivo al Panel Admin y Sala de Control POS para crear, editar, asignar inventarios o monitorear métricas.

- **Transaccionalidad Segura:** El uso de `@Transactional` en la capa de servicio asegura que si una venta, movimiento de stock o actualización falla, el sistema realice un Rollback automático, protegiendo el inventario.

- **Experiencia de Usuario (UX):** Interfaz responsiva con un diseño oscuro sólido (`#0a0a0a`) para la vista de clientes y contrastes corporativos (Café Tierra, Marfil, Oro) para las vistas operativas e intranet.

## Stack Tecnológico
- **Lenguaje:** Java 21 (LTS).
- **Framework:** Spring Boot 4.0.2.
- **Base de Datos:** PostgreSQL 18.
- **Seguridad:** Spring Security (Cifrado BCrypt y protección contra CSRF/SQL Injection).
- **Motor de Plantillas:** Thymeleaf para renderizado dinámico del lado del servidor.
- **Frontend:** Bootstrap 5.3 + CSS Personalizado (Custom Properties y Flexbox/Grid).
- **Gestor de Dependencias:** Maven.

## Arquitectura del Sistema
El proyecto se basa en una arquitectura de N-Capas para facilitar el mantenimiento:

- **Capa de Presentación (Controller):** Maneja las peticiones HTTP, la navegación y orquesta los Dashboards.
- **Capa de Negocio (Service):** Contiene la lógica transaccional, validaciones de inventario y lógicas de jornada.
- **Capa de Datos (Repository):** Interactúa con la base de datos PostgreSQL, maneja queries personalizadas e inyecciones de reportes.
- **Seguridad (Security):** Filtros de autenticación y autorización estrictos por roles.

## Cómo Desplegar el Proyecto
**Requisitos Previos**
- JDK 21 instalado y configurado.
- PostgreSQL 18 corriendo localmente con una base de datos llamada `lachefa_db`.
- Maven integrado en el IDE (IntelliJ IDEA, Eclipse, etc.) o instalado en el sistema.

**Pasos para la Instalación**
1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/FeloRF/portafoliobootcam.git
   cd portafoliobootcam
   ```
2. **Limpiar y Construir:**
   Utiliza la herramienta Maven de tu IDE o la terminal para limpiar archivos temporales y compilar:
   ```bash
   mvn clean install
   ```
3. **Configurar Variables:**
   Edita el archivo `src/main/resources/application.properties` con tus credenciales de base de datos.
4. **Ejecutar:**
   Inicia la aplicación desde tu IDE o mediante el comando:
   ```bash
   mvn spring-boot:run
   ```
5. **Acceso:**
   Abre tu navegador en `http://localhost:8081`.

## Filosofía de Ingeniería
Este proyecto sigue estándares de arquitectura de software profesional:

- **Inyección por Constructor:** Se evita el uso de `@Autowired` en atributos para favorecer la inmutabilidad y facilitar las pruebas unitarias.
- **Seguridad por Capas:** Las rutas administrativas y de logística de inventarios están blindadas por roles específicos.
- **Control de Versiones:** Manejo estricto de Git para recuperación de desastres y mantenimiento de hitos estables.

🌶🌶🌶 Desarrollado con pasión por Felipe Rojas F. 🌶🌶🌶