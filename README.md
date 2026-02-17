🛠️ Documentación Técnica: Ecosistema Jobs_Prueba_1
Este repositorio contiene una solución empresarial desarrollada en Java bajo una arquitectura de módulos desacoplados gestionados por Maven. El sistema está diseñado para el procesamiento de tareas programadas (Jobs) con un enfoque en escalabilidad y mantenibilidad.

🏗️ Arquitectura del Sistema
El proyecto implementa un patrón de diseño Layered Architecture (Arquitectura por Capas) distribuido en módulos independientes:

1. loxc001 (Core / Commons)
Responsabilidad: Capa transversal que contiene utilerías, excepciones personalizadas, constantes globales y objetos de transferencia de datos (DTOs).

Dependencias: Es el módulo base. No depende de otros módulos internos del proyecto.

Uso técnico: Provee las interfaces y configuraciones que garantizan la homologación de datos en todo el sistema.

2. loxbd001 (Persistence Layer)
Responsabilidad: Gestión de la capa de datos. Implementa el acceso a base de datos (repositorios, entidades JPA/Hibernate o mapeos de MyBatis).

Dependencia: loxc001.

Uso técnico: Centraliza las transacciones y la lógica de persistencia para evitar duplicidad de conexiones en los procesos de negocio.

3. loxj001-01-mx (Business Logic - Regional México)
Responsabilidad: Implementación de la lógica de negocio y ejecución de Jobs específicos para la región MX.

Dependencia: loxbd001 y loxc001.

Uso técnico: Contiene los servicios que orquestan las reglas de negocio. Al estar segregado por región (-mx), permite la coexistencia de múltiples lógicas territoriales sin colisiones de código.

🚀 Guía de Instalación y Build
Requisitos Técnicos
Java Development Kit (JDK): v1.8 o superior (verificar pom.xml para versión exacta).

Apache Maven: v3.6.0+.

Lombok: (Opcional) Asegúrese de tener el plugin activo en su IDE si se utiliza para la generación de código.

Compilación Completa
Para generar los artefactos de todos los módulos, ejecute en la raíz:

Bash
mvn clean install
Ejecución de Tests
Bash
mvn test
⚙️ Configuración de Entorno (Environment)
El sistema utiliza perfiles de Maven para gestionar diferentes entornos (Dev, QA, Prod). Los archivos de propiedades se encuentran típicamente en:

loxj001-01-mx/src/main/resources/application.properties (o .yml)

Parámetros Críticos:
Datasource: Configuración de URL, usuario y credenciales de BD.

Cron Expressions: Definición de la periodicidad de los Jobs en el módulo loxj.

📝 Estándares de Desarrollo y Git Flow
Nomenclatura: Se sigue el estándar de nombres de paquetes com.proyecto.modulo.*.

Control de Versiones:

No subir archivos de configuración local (.idea/, .vscode/, *.iml).

Uso estricto de .gitignore.

Gestión de Dependencias: Cualquier nueva librería debe ser declarada en el dependencyManagement del pom.xml raíz para mantener la consistencia de versiones.

🛠️ Troubleshooting (Resolución de Problemas)
Error: Cyclic Dependency: Verificar que loxc001 no intente importar clases de loxj001. La jerarquía es estrictamente descendente.

Error: Artifact Not Found: Asegúrese de ejecutar mvn install en los módulos base antes de compilar el módulo de lógica.

Desarrollador Responsable: Alger125

Última Actualización: Febrero 2026
