# Pruebas manuales de geolocalización

Realizar en `http://localhost:8080` o en un origen HTTPS:

- Permitir acceso: deben mostrarse latitud y longitud con cinco decimales.
- Bloquear acceso: debe aparecer un mensaje de permiso denegado sin romper el catálogo.
- Simular timeout y posición no disponible desde DevTools: debe mostrarse el mensaje correspondiente.
- Navegador sin soporte: debe informarse que el dispositivo no admite geolocalización.

Las coordenadas solo se muestran en el navegador; no se envían ni se almacenan.
