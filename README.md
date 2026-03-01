## 🌶️ La Chefa - E-commerce de Salsas & Encurtidos
¡Bienvenido al motor digital de La Chefa! Este proyecto es una plataforma robusta diseñada para la gestión y venta de productos gourmet picantes, desarrollada por Felipe Rojas F. como parte de su portafolio profesional.

## Lógica del Negocio
La plataforma está diseñada bajo un modelo de Separación de Responsabilidades para garantizar la integridad de los datos de la pyme:

Gestión de Inventario Dinámico: Control total sobre el stock de salsas y encurtidos mediante JPA y PostgreSQL 18.

Sistema de Roles (RBAC):

Clientes: Pueden registrarse, ver el catálogo en modo Dark Premium y explorar el recetario.

Administradores: Acceso exclusivo al Panel Admin para crear, editar o eliminar recetas y productos.

Transaccionalidad Segura: El uso de @Transactional en la capa de servicio asegura que si una venta o actualización falla, el sistema realice un Rollback automático, protegiendo el inventario.

Experiencia de Usuario (UX): Interfaz responsiva con un diseño oscuro sólido (#0a0a0a) y contrastes en amarillo y plata para una estética gourmet.

## Stack Tecnológico
Lenguaje: Java 21 (LTS).

Framework: Spring Boot 4.0.2.

Base de Datos: PostgreSQL 18.

Seguridad: Spring Security (Cifrado BCrypt y protección contra CSRF/SQL Injection).

Motor de Plantillas: Thymeleaf para renderizado dinámico del lado del servidor.

Frontend: Bootstrap 5.3 + CSS Personalizado.

Gestor de Dependencias: Maven.

## Arquitectura del Sistema
El proyecto se basa en una arquitectura de N-Capas para facilitar el mantenimiento:

Capa de Presentación (Controller): Maneja las peticiones HTTP y la navegación.

Capa de Negocio (Service): Contiene la lógica transaccional y validaciones.

Capa de Datos (Repository): Interactúa con la base de datos PostgreSQL.

Seguridad (Security): Filtros de autenticación y autorización por roles.

## Cómo Desplegar el Proyecto
Requisitos Previos
JDK 21 instalado y configurado.

PostgreSQL 18 corriendo localmente con una base de datos llamada lachefa_db.

Maven integrado en el IDE (IntelliJ IDEA) o instalado en el sistema.

Pasos para la Instalación
Clonar el repositorio:

Bash

git clone https://github.com/FeloRF/portafoliobootcam.git
cd portafoliobootcam
Limpiar y Construir:
Utiliza la herramienta Maven de tu IDE o la terminal para limpiar archivos temporales y compilar:

Bash

mvn clean install
Configurar Variables:
Edita el archivo src/main/resources/application.properties con tus credenciales de base de datos.

Ejecutar:
Inicia la aplicación desde tu IDE o mediante el comando:

Bash

mvn spring-boot:run
Acceso:
Abre tu navegador en http://localhost:8081.

## Filosofía de Ingeniería
Este proyecto sigue estándares de arquitectura basica de software:

Inyección por Constructor: Se evita el uso de @Autowired en atributos para favorecer la inmutabilidad y facilitar las pruebas unitarias.

Seguridad por Capas: Las rutas administrativas están blindadas por roles específicos.

Control de Versiones: Manejo estricto de Git para recuperación de desastres y mantenimiento de hitos estables.

🌶🌶🌶 Desarrollado con pasión Felipe Rojas F. 🌶🌶🌶️