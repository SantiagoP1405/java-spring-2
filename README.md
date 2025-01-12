# Proyecto: Screenmatch 2

## Descripción
Práctica para el curso "Java: persistencia de datos y consultas con Spring Data JPA"
Aplicación desarrollada en Java utilizando Spring Framework y JPA (Java Persistence API) para gestionar series y sus episodios. Permite realizar diversas operaciones sobre una base de datos, como la búsqueda, organización y filtrado de información relacionada con series y episodios.

## Características Principales

- **Gestión de Series y Episodios**: Permite crear, leer, actualizar y eliminar series y sus episodios, realizando consultas a la API de OMDB.
  
- **Búsquedas Personalizadas**:
  - Búsqueda por título de serie o episodio.
  - Filtro por género utilizando enums.
  - Recuperación del Top 5 de episodios de una serie basada en calificaciones.
  
- **Soporte para Relaciones Complejas**: Configuración de relaciones “uno a muchos” y “muchos a uno” entre entidades (series y episodios).
  
- **Optimización de Consultas**: Uso de JPQL, consultas nativas y derivadas para mejorar la eficiencia y flexibilidad.

## Tecnologías Utilizadas

- **Lenguaje**: Java
- **Frameworks**: Spring Framework, Spring Data JPA
- **Base de Datos**: Relacional (compatibilidad con MySQL, PostgreSQL, etc.)
  
### Herramientas Complementarias

- Postman e Insomnia para pruebas de API.
- IntelliJ IDEA o Eclipse para el desarrollo.
