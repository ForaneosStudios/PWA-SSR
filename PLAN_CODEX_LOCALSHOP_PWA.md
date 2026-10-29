# Plan maestro para Codex — LocalShop PWA (Spring Boot SSR)

**Proyecto:** Práctica académica PWA con SSR, persistencia, soporte offline y geolocalización.  
**Repositorio confirmado:** `https://github.com/ForaneosStudios/PWA-SSR`  
**Estado inicial verificado:** repositorio vacío; GitHub indica `main` como rama predeterminada, pero aún no existen commits ni ramas.  
**Fecha objetivo:** antes del cierre de la plataforma (confirmar fecha/hora exacta en la tarea; el contador compartido apuntaba a las 17:00 del 29 de octubre de 2026).  
**Responsable de integración:** Ernesto (`JessErfi`).

## 0. Instrucciones generales obligatorias para Codex

Actúa como desarrollador Java/Spring Boot y asistente de integración Git. **Lee este plan completo antes de editar archivos** y respeta exactamente las etapas, nombres de ramas, cambios esperados, verificaciones y mensajes de commits. El plan es una secuencia de trabajo, no una autorización para ejecutar las siete fases sin supervisión.

1. **Una etapa y un commit lógico a la vez.** Después de cada etapa/PR, informar: archivos cambiados, comandos probados, resultado, SHA de commits y asuntos pendientes. Continuar con la etapa siguiente solo cuando el usuario indique que avance.
2. Mantener `main` estable; trabajar en ramas nacidas de `develop`; **todos los PR de módulos tienen base `develop`**. El último PR será `develop` → `main`.
3. **No ejecutar `git push`, fusionar PR, cambiar autoría Git, reescribir historia ni eliminar ramas sin autorización explícita del usuario en ese momento.** Se pueden preparar comandos y commits, previa aprobación.
4. Los commits deben reflejar **cambios reales y probados**; no crear commits vacíos, reescribir timestamps, fabricar pruebas ni modificar nombre/correo de autor para atribuir artificialmente trabajo de otra persona. La atribución se basará en contribuciones verificadas. El propietario del PR y quien revisó se documentan por separado.
5. Antes de cada cambio, verificar `git status`, rama actual y `git diff`. No sobrescribir trabajo existente, no mezclar módulos distintos en el mismo commit, no commitear secretos, artefactos `target/` ni archivos de base de datos locales.
6. No agregar tecnologías o funcionalidades fuera de la rúbrica: **sin React, sin Vite, sin API REST obligatoria, sin login, sin carrito y sin pagos**.
7. No declarar una funcionalidad terminada sin compilar y ejecutar la prueba adecuada; si una validación requiere navegador humano, indicarlo expresamente como **pendiente de prueba manual**, no como éxito automático.
8. Si el entorno tiene Windows y PowerShell, usar `./mvnw.cmd` / `.\mvnw.cmd`; en Linux/macOS, `./mvnw`. No suponer que `gh` está instalado: verificar `gh --version` y, si falta, abrir PR desde interfaz web.

## 1. Arquitectura y criterios técnicos fijos

| Aspecto | Decisión |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot **3.x**, seleccionar una versión compatible de la serie 3.5.x; no saltar a Boot 4 |
| Build | Maven + Maven Wrapper (`mvnw`, `mvnw.cmd`, `.mvn/wrapper/`) |
| Dependencias | Spring Web, Thymeleaf, Spring Data JPA, H2, Lombok, Spring Boot Starter Test |
| Base de datos | H2 modo archivo local, URL tipo `jdbc:h2:file:./data/localshop`; ignorar `data/` en Git |
| MVC | `com.foraneos.localshop` con `model`, `repository`, `controller`, `config` |
| Entidad | `Producto`: `Long id`, `String nombre`, `String descripcion`, `Double precio`, `String imagenUrl` |
| Vista SSR | Thymeleaf `templates/index.html`, servida por `GET /` desde controlador anotado `@Controller` |
| App Shell | CSS/JS locales, navbar, contenedor, estado de conexión, tarjetas del catálogo; sin depender de CDN |
| Manifest | `static/manifest.json` con `name`, `short_name`, `start_url`, `display: standalone`, `theme_color`, iconos PNG 192/512 |
| Service Worker | `static/sw.js`: precaché del shell, versiones de caché y limpieza, network-first para HTML, cache-first para archivos estáticos, respuesta offline definida |
| Geolocalización | API `navigator.geolocation` activada por botón y con manejo de permisos/errores; `localhost`/HTTPS |
| Puerto | `8080`, salvo conflicto local documentado |
| Documentación | `README.md` en español, autores, fases, ejecución, estructura, capturas reales y pruebas |
| CI | GitHub Actions para `./mvnw -B clean verify` en PR a `develop` y `main`, si el tiempo lo permite |

### Decisiones específicas

- La base de datos H2 puede mantener registros entre ejecuciones. Usar `spring.jpa.hibernate.ddl-auto=update` solo para la práctica y dejarlo indicado en README.
- Sembrar **6 productos de prueba** en el arranque **solo si** el repositorio está vacío (`count()==0`); impedir duplicados entre reinicios. No usar URL de imágenes externas si se exige offline; para producto sin imagen mostrar placeholder visual local.
- Mantener precios con `Double` para respetar literalmente la entidad solicitada en la consigna. No introducir operaciones de pago.
- Thymeleaf renderiza el catálogo en el servidor. **Offline se verá la última página HTML cacheada**, no habrá acceso a H2 cuando el navegador esté desconectado del servidor.
- El Service Worker debe interceptar solo `GET` del mismo origen y respuestas que puedan almacenarse. No cachear POST, datos privados ni páginas de error.
- Para instalación, incorporar iconos PNG verdaderos de 192×192 y 512×512. No basta renombrar SVG o texto como `.png`.
- El sitio debe funcionar visualmente sin red luego de una primera visita conectada. No usar fuentes, scripts, imágenes ni CSS críticos desde CDN.
- La primera visita instala el SW; recargar o abrir de nuevo después de quedar controlada por el SW antes de probar offline.

## 2. Modelo de ramas y PR

```
main  [inicial: scaffold probado]
  └── develop  [integración continua de la práctica]
       ├── feat/aceves-productos     ── PR 1 ──► develop
       ├── feat/judith-catalogo-ssr  ── PR 2 ──► develop
       ├── feat/ivan-app-shell       ── PR 3 ──► develop
       ├── feat/rochin-service-worker ─ PR 4 ──► develop
       ├── feat/ernesto-geolocalizacion ─ PR 5 ─► develop
       └── docs/ernesto-calidad-entrega ─ PR 6 ─► develop

       develop ── PR 7: release LocalShop PWA ──► main
```

**Orden recomendado de integración:** bootstrap inicial → PR 1 → PR 2 → PR 3 → PR 4 → PR 5 → PR 6 → PR 7. Es secuencial a propósito: minimiza conflictos y asegura que cada rama parta de una `develop` funcional. Se puede preparar contenido con antelación, pero no ramificar sobre una versión desactualizada.

**Conservación del historial:** escoger **Create a merge commit**, no **Squash and merge**, al fusionar cada PR. Dejar `develop` existente después del PR final. Los números de PR aquí son identificadores de planificación; GitHub asignará los números reales.

### Personas y responsabilidades propuestas

| Integrante/cuenta | Módulo principal | Rama | PR |
|---|---|---|---|
| Jesús Aceves — `aceves08` | Entidad Producto, repositorio y H2 | `feat/aceves-productos` | PR 1 |
| Judith — `JudithHP` | Controlador SSR, Thymeleaf y diseño | `feat/judith-catalogo-ssr` | PR 2 |
| Iván — `Kilver15` | Manifest, iconos, registro SW, App Shell | `feat/ivan-app-shell` | PR 3 |
| Emmanuel Rochín — `Emmanuel-Rochin` | SW, caché y soporte sin conexión | `feat/rochin-service-worker` | PR 4 |
| Ernesto — `JessErfi` | Geolocalización, integración, tests, README | `feat/ernesto-geolocalizacion` y `docs/ernesto-calidad-entrega` | PR 5 y PR 6 |
| Integrador — `JessErfi` | Revisión de release y entrega | `develop` → `main` | PR 7 |

Las ramas representan **áreas de responsabilidad propuestas**, no prueba de autoría. Si Codex desarrolla una parte, registrar como autor a quien realmente realice la contribución, y permitir a los demás agregar sus revisiones, pruebas o cambios mediante sus propias cuentas.

## 3. Arranque desde un repositorio vacío (bootstrap, 1 commit)

**Trabajar en `main` solo para crear la primera versión funcional mínima.** El repositorio está vacío; `git clone` puede funcionar mostrando aviso de repositorio vacío. No inventar una rama `main` ya existente.

Pasos:
1. Clonar `https://github.com/ForaneosStudios/PWA-SSR.git`.
2. Generar proyecto Maven Spring Boot 3.x Java 17 con grupo `com.foraneos`, artefacto `localshop`, paquete `com.foraneos.localshop`, dependencias mencionadas y Maven Wrapper. Si hay archivos preexistentes, inspeccionar antes.
3. Crear `.gitignore` con `.idea/`, `.vscode/` opcionalmente salvo configuraciones compartidas, `target/`, `.classpath`, `.project`, `data/`, `*.mv.db`, `*.trace.db`, `.env`, archivos temporales. No ignorar `mvnw`, `mvnw.cmd`, `.mvn/wrapper/` ni el código.
4. Crear aplicación `LocalshopApplication.java`, `application.properties` mínimo y README inicial con la forma de arrancar. Incluir `PLAN_CODEX_LOCALSHOP_PWA.md` en repositorio si el usuario lo copia allí.
5. Comprobar `java -version`, `./mvnw.cmd -v`, `./mvnw.cmd clean verify` y `./mvnw.cmd spring-boot:run` en Windows.
6. Con autorización, crear commit único en `main`:
   - **C00:** `chore: initialize Spring Boot 3 MVC project`
7. Hacer `push` de `main`; crear `develop` desde ese commit y subirla a `origin`; mantener `main` como predeterminada.

**Gate:** Maven compila, el servidor arranca, no hay secretos ni archivos compilados dentro de Git. En este punto `GET /` puede no tener catálogo hasta PR 2.

## 4. Plan exacto de commits por módulo

### PR 1 — Backend JPA y H2 — `aceves08`

**Rama:** `feat/aceves-productos` (crear desde `origin/develop`).  
**Base PR:** `develop`.  
**Título:** `feat: implementar persistencia de productos con JPA y H2`.

| Orden | Commit exacto | Cambios esperados |
|---|---|---|
| C01 | `feat(product): add Producto entity and JpaRepository` | `model/Producto.java`, `repository/ProductoRepository.java`, anotaciones JPA y Lombok |
| C02 | `chore(db): configure persistent H2 database` | `application.properties`, URL local H2, JPA ddl-auto para desarrollo, `.gitignore` revisado |
| C03 | `feat(seed): initialize sample product catalog` | `config/DataInitializer.java`, seis productos únicos cuando BD vacía |
| C04 | `test(product): verify persistence and seed behavior` | Tests de JPA, lectura de productos y no duplicación de datos |

**Aceptación:** los tests pasan; productos se obtienen con `findAll()`; un segundo arranque no duplica los datos; la BD persistente no se versiona.

**PR review:** verificar campos exactos de la consigna, SQL/JPA y resultados de prueba; fusionar a `develop` con merge commit.

### PR 2 — Renderizado SSR — `JudithHP`

**Rama:** `feat/judith-catalogo-ssr` (nace de `develop` **después** del PR 1).  
**Base PR:** `develop`.  
**Título:** `feat: crear catálogo SSR con Thymeleaf y diseño adaptable`.

| Orden | Commit exacto | Cambios esperados |
|---|---|---|
| C05 | `feat(ssr): render products with Thymeleaf controller` | `controller/ProductoController.java`, `templates/index.html`, atributo `${productos}` |
| C06 | `feat(ui): build responsive product cards` | Tarjetas dinámicas con nombre, descripción, precio, y placeholder de imagen local |
| C07 | `style(ui): add local styles and empty catalog state` | `static/css/styles.css`, barra de navegación, diseño móvil, estado de catálogo vacío |
| C08 | `test(ssr): verify catalog page rendering` | Test MVC `GET /` 200, plantilla, contenido y productos de prueba |

**Aceptación:** `http://localhost:8080/` muestra productos de H2; HTML devuelto por Spring incluye productos sin necesitar JavaScript; la página se ve en móvil; ningún CDN crítico.

**Regla anti-conflictos:** Judith es la propietaria inicial de `index.html`. Otros módulos solo la editarán después de fusionar este PR.

### PR 3 — Manifest, App Shell e instalación — `Kilver15`

**Rama:** `feat/ivan-app-shell` (desde `develop` después del PR 2).  
**Base PR:** `develop`.  
**Título:** `feat: preparar instalación y App Shell de LocalShop PWA`.

| Orden | Commit exacto | Cambios esperados |
|---|---|---|
| C09 | `feat(pwa): add web app manifest` | `static/manifest.json` con campos y paths correctos; `<link rel="manifest">` y `<meta name="theme-color">` |
| C10 | `feat(pwa): add installable application icons` | `static/images/icon-192.png`, `icon-512.png`, archivos PNG reales |
| C11 | `feat(shell): register service worker and shell assets` | `static/js/pwa-register.js`, vínculos de scripts y estilos locales, indicadores básicos de conexión; registro SW manejando error transitorio hasta PR 4 |
| C12 | `test(pwa): verify manifest and static assets` | Tests de rutas HTTP de manifest y PNG, rutas sin 404, JSON parseable |

**Aceptación:** `manifest.json` accesible y validable; iconos existentes en dimensiones correctas; shell local sin enlaces críticos externos. La instalación completa se valida **después** de integrar SW (PR 4).

### PR 4 — Service Worker y offline — `Emmanuel-Rochin`

**Rama:** `feat/rochin-service-worker` (desde `develop` después del PR 3).  
**Base PR:** `develop`.  
**Título:** `feat: implementar caché offline con Service Worker`.

| Orden | Commit exacto | Cambios esperados |
|---|---|---|
| C13 | `feat(sw): precache LocalShop application shell` | `static/sw.js`, `static/offline.html`, precaché de HTML y assets existentes, evento install |
| C14 | `feat(sw): use network-first strategy for navigation` | Fetch de navegación usa red primero y HTML cacheado si falla |
| C15 | `feat(sw): cache static assets and clean old versions` | Cache-first de CSS/JS/iconos, activación y limpieza de versiones antiguas, filtros seguros solo GET/same-origin |
| C16 | `test(sw): validate service worker assets and offline flow` | Tests HTTP para `/sw.js` y `/offline.html`, guía de prueba manual offline con DevTools |

**Aceptación manual:** abrir `/` con red → esperar activación SW → recargar → DevTools > Network > Offline → recargar → ver catálogo HTML cacheado y CSS. Confirmar que al volver a conectar los datos se actualizan. Ninguna supuesta prueba manual se marca aprobada hasta realizarla.

**Límite declarado:** offline no consulta H2; muestra contenido SSR previamente cacheado.

### PR 5 — Geolocalización — `JessErfi`

**Rama:** `feat/ernesto-geolocalizacion` (desde `develop` después del PR 4).  
**Base PR:** `develop`.  
**Título:** `feat: integrar geolocalización del dispositivo`.

| Orden | Commit exacto | Cambios esperados |
|---|---|---|
| C17 | `feat(geo): request device location on button click` | `static/js/geolocation.js`, botón y área de estado en `index.html` |
| C18 | `feat(geo): handle permissions and location errors` | Mensajes para acceso denegado, geolocalización no soportada, timeout y posición no disponible |
| C19 | `feat(ui): show location and connection feedback` | Formato legible de lat/lon, actualización accesible con `aria-live`, estado online/offline |
| C20 | `test(geo): document secure-context location checks` | Lista manual de casos de éxito, bloqueo y errores; tests HTTP si aplican |

**Aceptación:** botón solicita permiso en `localhost`; aceptar muestra coordenadas; rechazar muestra mensaje sin romper el catálogo. No enviar coordenadas al servidor ni persistirlas.

### PR 6 — QA, CI, README y evidencias — `JessErfi`

**Rama:** `docs/ernesto-calidad-entrega` (desde `develop` después del PR 5).  
**Base PR:** `develop`.  
**Título:** `test/docs: completar validaciones y documentación de LocalShop`.

| Orden | Commit exacto | Cambios esperados |
|---|---|---|
| C21 | `test(integration): verify SSR and PWA resource routes` | Tests de integración de controlador, plantilla y manifest/SW/estáticos; corregir problemas reales |
| C22 | `ci: run Maven verification on pull requests` | `.github/workflows/maven.yml` con checkout, Java 17, cache Maven y `./mvnw -B clean verify`; disparadores en PR a `develop` y `main` |
| C23 | `docs: explain setup architecture and development phases` | `README.md`: objetivo, requisitos, ejecución en Windows/Linux, estructura, MVC/SSR, JPA/H2, manifest/SW/geoloc, fases y autores |
| C24 | `docs: add verified screenshots and delivery checklist` | `docs/evidencias/` con **capturas reales**, imágenes insertadas en README, pruebas realizadas, limitaciones y checklist |

**Aceptación:** `clean verify` verde; README autocontenido; imágenes existentes y correctamente referenciadas; ejecución local reproducible; captura offline, manifest, SW, geolocalización, catálogo y BD. Si falta una captura, registrar pendiente y no inventarla.

### PR 7 — Integración de `develop` a `main`

**Head:** `develop`. **Base:** `main`.  
**Título:** `release: entregar LocalShop PWA SSR v1.0`.

**Descripción sugerida:**
- Catálogo SSR con Thymeleaf conectado a H2/JPA.
- Manifest e iconos para instalación.
- Service Worker con caché y funcionamiento offline.
- Geolocalización con manejo de permisos.
- Tests, README y evidencias.
- Estado de pruebas y limitaciones conocidas.

**Condiciones para fusionar:**
1. `develop` contiene los PR 1–6 fusionados con merge commit.
2. `./mvnw.cmd clean verify` final pasa en Windows (o `./mvnw clean verify` en Linux).
3. `GET /` muestra seis productos; reiniciar no duplica registros.
4. `manifest.json`, `sw.js` y PNG devuelven 200.
5. Offline se comprueba en Chrome; geolocalización se comprueba permitiendo y negando permiso.
6. README con todas las evidencias reales y autores.
7. Revisar en GitHub `Files changed`, `Commits` y `Checks`.

**Merge:** usar **Create a merge commit** y **no borrar `develop`**. Opcional: etiquetar la versión en `main` como `v1.0.0` después de validar.

## 5. Comandos Git reproducibles (Windows PowerShell)

### Preparar repositorio nuevo

```powershell
git clone https://github.com/ForaneosStudios/PWA-SSR.git
cd PWA-SSR
# Codex genera el proyecto Maven en esta carpeta. Validar que compila.
git status
git add .
git commit -m "chore: initialize Spring Boot 3 MVC project"
git branch -M main
git push -u origin main
git switch -c develop
git push -u origin develop
```

> Requiere que Git tenga la identidad **real** de quien está haciendo el commit y permisos de escritura en el repositorio. Requiere autorización del usuario antes de los commits y pushes.

### Crear una rama de funcionalidad (repetir para cada PR)

```powershell
git switch develop
git pull --ff-only origin develop
git switch -c feat/aceves-productos
# Codex implementa un cambio lógico, corre pruebas y propone cada commit.
git status
git add src/main/java/com/foraneos/localshop/model/Producto.java src/main/java/com/foraneos/localshop/repository/ProductoRepository.java
git commit -m "feat(product): add Producto entity and JpaRepository"
# Repetir con el resto de los cambios de la rama.
git push -u origin feat/aceves-productos
```

> El ejemplo `git add` por ruta aplica al primer commit del PR 1, después de verificar que dichos archivos existen. Para otros commits seleccionar los archivos apropiados con `git add -p` o rutas explícitas, no arrastrar cambios ajenos.

### Crear un PR con GitHub CLI (si está instalada)

```powershell
gh auth status
gh pr create --base develop --head feat/aceves-productos `
  --title "feat: implementar persistencia de productos con JPA y H2" `
  --body "Agrega Producto, repositorio JPA, H2, datos de prueba y validaciones. Destino: develop."
```

Una vez revisado y aprobada la fusión, usar el botón **Create a merge commit** en GitHub. El siguiente módulo debe partir del `develop` actualizado. Si `gh` no existe, abrir la pestaña Pull requests del repositorio y usar `New pull request` con las mismas ramas.

### PR final

```powershell
git switch develop
git pull --ff-only origin develop
.\mvnw.cmd clean verify
gh pr create --base main --head develop `
  --title "release: entregar LocalShop PWA SSR v1.0" `
  --body "Integra las seis etapas completadas, pruebas y README con evidencias."
```

Comandos de revisión:

```powershell
git log --oneline --graph --decorate --all
git shortlog -sne develop
git status
git diff --stat main..develop
```

## 6. Estructura final esperada

```text
PWA-SSR/
├── .github/workflows/maven.yml
├── .mvn/wrapper/...
├── .gitignore
├── PLAN_CODEX_LOCALSHOP_PWA.md        # si se incorpora esta planeación
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── docs/
│   ├── evidencias/...
│   └── pruebas-offline.md             # puede integrarse en README si se prefiere
└── src/
    ├── main/
    │   ├── java/com/foraneos/localshop/
    │   │   ├── LocalshopApplication.java
    │   │   ├── config/DataInitializer.java
    │   │   ├── controller/ProductoController.java
    │   │   ├── model/Producto.java
    │   │   └── repository/ProductoRepository.java
    │   └── resources/
    │       ├── application.properties
    │       ├── templates/index.html
    │       └── static/
    │           ├── css/styles.css
    │           ├── js/geolocation.js
    │           ├── js/pwa-register.js
    │           ├── images/icon-192.png
    │           ├── images/icon-512.png
    │           ├── manifest.json
    │           ├── offline.html
    │           └── sw.js
    └── test/java/com/foraneos/localshop/...
```

## 7. Pruebas obligatorias y evidencias

| Prueba | Procedimiento | Evidencia esperada |
|---|---|---|
| Compilación | `./mvnw.cmd clean verify` | Terminal sin errores, tests aprobados |
| Arranque | `./mvnw.cmd spring-boot:run` | Servidor escuchando en 8080 |
| SSR + BD | Abrir `http://localhost:8080/`, ver HTML fuente, reiniciar servidor | Productos del repositorio en HTML; sin duplicados |
| H2 | Revisar consulta con consola H2 si está habilitada | Tabla `productos` con registros de prueba |
| Manifest | DevTools > Application > Manifest | Nombre, colores e iconos válidos |
| Service Worker | DevTools > Application > Service Workers | SW registrado y controlando la página |
| Offline | Visita previa online, recarga controlada, activar Network > Offline, recargar | Catálogo y estilos aparecen sin conexión |
| Geoloc | Click botón, permitir; luego negar permiso | Coordenadas o mensaje de error correcto |
| Accesibilidad/responsive | DevTools móvil, teclado, mensajes de estado | Vista usable sin cortes, etiquetas accesibles |
| Documentación | Abrir README en GitHub y seguir instrucciones desde cero | Imágenes sin enlaces rotos y comandos correctos |

Lighthouse puede utilizarse para rendimiento, accesibilidad y buenas prácticas, pero **no depender de una categoría PWA antigua**. Tomar capturas de DevTools para manifest e instalación.

## 8. Gestión de riesgo y prioridad en el plazo corto

**Prioridad 1 (imprescindible):** proyecto compilando, JPA/H2, SSR, manifest, SW offline y geolocalización.  
**Prioridad 2 (entrega):** pruebas del navegador y README con capturas.  
**Prioridad 3 (si sobra tiempo):** CI, refactorización estética y mejoras menores.

Propuesta de bloques de ejecución (estimaciones, no garantía):

| Bloque | Actividad | Duración orientativa |
|---|---|---|
| A | Bootstrap, git, `develop` | 30 min |
| B | PR 1 backend y pruebas | 50 min |
| C | PR 2 SSR y diseño | 55 min |
| D | PR 3 manifest e iconos | 40 min |
| E | PR 4 offline SW | 60 min |
| F | PR 5 geolocalización | 35 min |
| G | PR 6 integración, README, capturas y CI | 70 min |
| H | PR 7 release y revisión de GitHub | 30 min |

Reserva margen hasta la hora de cierre para fallas de Maven, GitHub o caché del navegador. Si la fecha real es 29/10/2026 17:00, objetivo interno: finalizar PR 7 **antes de las 16:00**.

**Fallos y decisión:**
- Si falla un commit o pruebas: detener nueva funcionalidad, corregir en la misma rama con commit lógico de arreglo y repetir verificación.
- Si un PR no integra: actualizar rama desde `origin/develop`, resolver conflictos, probar de nuevo; nunca hacer `push --force` sobre `main` o `develop`.
- Si la instalabilidad no aparece: revisar tamaños/tipos de iconos, manifest, Service Worker activo y origen seguro.
- Si offline falla: comprobar que el SW controle la pestaña, el precaché no tenga rutas 404 y la página se haya visitado online.
- Si H2 falla: revisar ruta `data/`, URL JDBC, permisos y existencia de tablas; nunca borrar datos sin autorización.
- Si geoloc falla: comprobar `localhost`/HTTPS, bloqueo de permisos del navegador y manejo de error.
- No eliminar la documentación ni pruebas para “pasar” un build: arreglar la causa.

## 9. Plantilla de descripción de PR

```markdown
## Objetivo
[Qué aporta este módulo]

## Archivos principales
- [Rutas editadas]

## Commits incluidos
- [Mensajes reales y sus SHA]

## Pruebas realizadas
- [Comando y resultado]
- [Prueba manual, solo si se ejecutó]

## Evidencias
- [Enlaces/capturas existentes]

## Pendientes o limitaciones
- [Puntos pendientes reales]

## Checklist
- [ ] Rama parte de `develop` actualizado
- [ ] Cambios revisados y sin secretos
- [ ] Compilación y tests aprobados
- [ ] PR apunta a `develop`
- [ ] Aprobado para merge commit
```

## 10. Instrucción inicial, lista para pegar en Codex

> Estoy trabajando en el repositorio `ForaneosStudios/PWA-SSR`. Lee primero el documento `PLAN_CODEX_LOCALSHOP_PWA.md`, que está en la raíz del proyecto. Ejecuta únicamente **la etapa 3: bootstrap** de ese plan. No cambies Spring Boot 3.x por 4, no uses React ni APIs que no solicita la práctica. Prepara un proyecto Maven Java 17 con Spring Web, Thymeleaf, JPA, Lombok, H2 y Test, wrapper, `.gitignore`, clase principal y configuración mínima. Verifica la compilación y dime exactamente qué se ha generado y cómo ejecutar el proyecto. Antes de hacer `git commit`, `git push`, crear `develop` o crear PR, solicita autorización. No avances a PR 1 hasta que lo indique.

---

**Nota de alcance:** esta planeación sirve para desarrollo y colaboración verificables. Los commits planeados representan puntos de implementación concretos; pueden necesitar ajustes si Codex detecta dependencias o errores reales. Los PR no están creados todavía y ninguna prueba está marcada como aprobada de antemano.
