# LocalShop PWA

LocalShop es una práctica académica de comercio local construida con Spring Boot 3.5, Java 17 y renderizado SSR con Thymeleaf. El catálogo persiste en H2, puede instalarse como PWA, conserva la última vista para uso sin conexión y solicita geolocalización solo al pulsar el botón.

## Funcionalidades

- Catálogo SSR de seis productos desde JPA/H2.
- Diseño responsive sin dependencias CDN.
- Manifest e iconos PNG 192/512 para instalación.
- Service Worker: network-first para navegación y cache-first para estáticos.
- Fallback offline y limpieza de cachés antiguas.
- Geolocalización con manejo de permiso, timeout y posición no disponible.
- Pruebas unitarias/de integración y verificación continua.

## Requisitos y ejecución

- JDK 17 o posterior.
- Puerto 8080 disponible.

Windows:

```powershell
.\mvnw.cmd clean verify
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw clean verify
./mvnw spring-boot:run
```

Abrir `http://localhost:8080/`. La consola H2 está en `/h2-console`, con URL `jdbc:h2:file:./data/localshop`, usuario `sa` y contraseña vacía.

## Arquitectura

```text
controller/ProductoController -> repository/ProductoRepository -> H2
              |                              |
              +------ templates/index.html <-+

static/manifest.json + sw.js + css/ + js/ + images/
```

La página se renderiza en el servidor. Sin red, el navegador muestra el último HTML guardado; no consulta H2. `ddl-auto=update` se usa solo para esta práctica. El inicializador inserta seis productos únicamente si la tabla está vacía.

## Pruebas manuales

- [Flujo offline](docs/pruebas-offline.md)
- [Geolocalización](docs/pruebas-geolocalizacion.md)

### Evidencias reales

![Catálogo LocalShop en escritorio](docs/evidencias/catalogo-desktop.png)

![Catálogo LocalShop en vista móvil](docs/evidencias/catalogo-movil.png)

#### Manifest y recursos PWA

![Manifest servido por LocalShop](docs/evidencias/manifest.png)

![Página de respaldo sin conexión](docs/evidencias/pagina-offline.png)

![Consola H2 disponible](docs/evidencias/consola-h2.png)

#### Evidencias manuales pendientes

Estas capturas deben realizarse en Chrome porque requieren DevTools o decisiones de permisos. Guarda los archivos con los nombres indicados dentro de `docs/evidencias/`; al hacerlo aparecerán directamente en estos espacios.

**Service Worker activo y controlando la página**  
Archivo esperado: `docs/evidencias/service-worker-activo.png`

<!-- Sustituir este comentario por: ![Service Worker activo](docs/evidencias/service-worker-activo.png) -->

**Catálogo funcionando con Network > Offline**  
Archivo esperado: `docs/evidencias/catalogo-offline.png`

<!-- Sustituir este comentario por: ![Catálogo offline](docs/evidencias/catalogo-offline.png) -->

**Manifest reconocido e instalación disponible**  
Archivo esperado: `docs/evidencias/instalacion-pwa.png`

<!-- Sustituir este comentario por: ![Instalación PWA](docs/evidencias/instalacion-pwa.png) -->

**Geolocalización permitida mostrando coordenadas**  
Archivo esperado: `docs/evidencias/geolocalizacion-permitida.png`

<!-- Sustituir este comentario por: ![Geolocalización permitida](docs/evidencias/geolocalizacion-permitida.png) -->

**Geolocalización denegada con mensaje controlado**  
Archivo esperado: `docs/evidencias/geolocalizacion-denegada.png`

<!-- Sustituir este comentario por: ![Geolocalización denegada](docs/evidencias/geolocalizacion-denegada.png) -->

Para probar offline: visitar la página, recargar tras activar el Service Worker, seleccionar Offline en DevTools y volver a cargar. La geolocalización funciona en `localhost` o HTTPS y nunca se persiste ni se envía al servidor.

## Desarrollo por fases

| Fase | Cuenta | Aporte |
|---|---|---|
| Backend | [aceves08](https://github.com/aceves08) | Producto, JPA, H2 y datos iniciales |
| SSR | [JudithHP](https://github.com/JudithHP) | Controlador, Thymeleaf y diseño |
| App Shell | [Kilver15](https://github.com/Kilver15) | Manifest, iconos y registro PWA |
| Offline | [Emmanuel-Rochin](https://github.com/Emmanuel-Rochin) | Service Worker y caché |
| Integración | [JessErfi](https://github.com/JessErfi) | Geolocalización, QA, CI y documentación |

### Autores

- Jesús Aceves — [aceves08](https://github.com/aceves08)
- Judith — [JudithHP](https://github.com/JudithHP)
- Iván — [Kilver15](https://github.com/Kilver15)
- Emmanuel Rochín — [Emmanuel-Rochin](https://github.com/Emmanuel-Rochin)
- Ernesto Fierro — [JessErfi](https://github.com/JessErfi)

Las ramas se integran en `develop` mediante merge commit; la entrega se promueve de `develop` a `main`.

## Limitaciones

- Permisos, instalación y modo offline requieren validación en navegador real.
- Offline presenta el HTML SSR cacheado; no ofrece operaciones de base de datos desconectadas.
- No incluye autenticación, carrito, pagos ni API REST.
