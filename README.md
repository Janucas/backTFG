# 🧳 Asistente de Equipaje Inteligente - Backend  
## Proyecto de Desarrollo de Aplicaciones Web (DAW)  
**Alumno:** Juan Antonio Núñez Castaño

---

## 📑 Índice

- [Introducción](#✨-introducción)
- [Funcionalidades y Tecnologías](#⚙️-funcionalidades-y-tecnologías)
- [Guía de Instalación](#🛠️-guía-de-instalación)
- [Guía de Uso](#📚-guía-de-uso)
- [Documentación](#🧾-documentación)
- [Interfaz Figma](#🎨-interfaz-figma)
- [Conclusión](#✅-conclusión)
- [Contribuciones, Agradecimientos y Referencias](#🙌-contribuciones-agradecimientos-y-referencias)
- [Licencia](#📜-licencia)
- [Contacto](#📬-contacto)

---

## ✨ Introducción

Este proyecto forma parte del Trabajo de Fin de Ciclo del Grado Superior en Desarrollo de Aplicaciones Web (DAW). Se trata de un **Asistente de Equipaje Inteligente**, un sistema que ayuda a los usuarios a generar listas de equipaje automáticas en función del destino, fechas de viaje y el pronóstico del clima.

### Justificación
Planificar un viaje puede ser estresante. El objetivo es facilitar este proceso generando una lista personalizada de artículos que se deben llevar, teniendo en cuenta factores climáticos y temporales.

### Objetivos
- Automatizar la creación de listas de equipaje basadas en el clima.
- Proveer una API REST segura con autenticación JWT.
- Permitir a los usuarios guardar su historial de viajes y equipajes.

### Motivación
La idea nace de la necesidad personal y de conocidos de evitar olvidar cosas importantes al viajar, especialmente en viajes internacionales.

---

## ⚙️ Funcionalidades y Tecnologías

### Funcionalidades
- Registro e inicio de sesión con autenticación JWT.
- Creación automática de equipaje según el destino y el clima.
- Consulta y gestión del historial de equipajes.
- Clasificación detallada por categorías de ítems.
- Soporte para clima diario y recomendaciones específicas.

### Tecnologías utilizadas
- **Backend:** Java 17, Spring Boot  
- **Seguridad:** Spring Security + JWT  
- **Persistencia:** JPA + H2/MySQL  
- **Documentación:** Swagger/OpenAPI  
- **Despliegue:** Docker  

---

## 🛠️ Guía de Instalación

1. Clona este repositorio:
   ```bash
   git clone https://github.com/Janucas/backTFG.git
   cd backTFG
   ```

2. Configura el archivo `application.properties` o `application.yml` según tu base de datos.

3. Lanza el proyecto con Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Accede a `http://localhost:8080` para utilizar la API.

---

## 📚 Guía de Uso

### Registro e Inicio de Sesión

- **POST /api/auth/register**  
  Registra un nuevo usuario.

- **POST /api/auth/login**  
  Devuelve un JWT para autenticar las siguientes peticiones.

### Generar equipaje

- **POST /api/equipajes**  
  Envía el destino y fechas, y se generará automáticamente un equipaje personalizado con ítems clasificados por días y clima.

### Consultar historial

- **GET /api/equipajes**  
  Devuelve todos los equipajes del usuario autenticado.

---

## 🧾 Documentación

La documentación de la API está disponible en Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🎨 Interfaz Figma

Diseño visual de la interfaz del sistema:

```
https://www.figma.com/design/9R7G8ICkfYkpuF9rmb8cEM/Untitled?node-id=0-1&t=s6zTLgJMPZbiriny-1
```

---

## ✅ Conclusión

Este proyecto ha permitido aplicar y consolidar conocimientos en desarrollo backend con Java, diseño de APIs RESTful, seguridad con JWT y uso de tecnologías modernas como Docker. La experiencia ha sido enriquecedora y ha demostrado cómo la tecnología puede resolver problemas reales y cotidianos.

---

## 🙌 Contribuciones, Agradecimientos y Referencias

### Contribuciones
- Juan Antonio Núñez Castaño - Desarrollador principal

### Agradecimientos
- Profesores y tutores del ciclo DAW.
- Familiares y amigos que aportaron ideas y feedback.

### Referencias
- https://spring.io/projects/spring-boot  
- https://jwt.io/  
- https://openweathermap.org/api  

---

## 📜 Licencia

Este proyecto está licenciado bajo los términos de la licencia MIT.  
Ver el archivo `LICENSE` para más información.

---
