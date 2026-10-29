# Prueba manual de funcionamiento offline

1. Ejecutar LocalShop y abrir `http://localhost:8080/` en Chrome.
2. Esperar el registro del Service Worker y recargar una vez.
3. Confirmar en DevTools > Application que `/sw.js` controla la página.
4. En DevTools > Network seleccionar **Offline** y recargar.
5. Confirmar que aparecen el catálogo SSR previamente guardado y sus estilos.
6. Volver a **Online** y recargar para actualizar los datos.

La prueba de navegador requiere ejecución manual. Sin servidor, H2 no se consulta: se muestra el HTML guardado.
