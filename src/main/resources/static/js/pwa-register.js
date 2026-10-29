const connectionStatus = document.querySelector('[data-connection-status]');

function updateConnectionStatus() {
    if (!connectionStatus) return;
    const online = navigator.onLine;
    connectionStatus.textContent = online ? 'En línea' : 'Sin conexión';
    connectionStatus.dataset.online = String(online);
}

window.addEventListener('online', updateConnectionStatus);
window.addEventListener('offline', updateConnectionStatus);
updateConnectionStatus();

if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('/sw.js')
            .catch(() => console.info('El soporte offline estará disponible pronto.'));
    });
}
