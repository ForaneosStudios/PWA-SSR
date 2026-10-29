const locationButton = document.querySelector('[data-location-button]');
const locationStatus = document.querySelector('[data-location-status]');

locationButton?.addEventListener('click', () => {
    if (!('geolocation' in navigator)) {
        locationStatus.textContent = 'Este dispositivo no admite geolocalización.';
        return;
    }
    locationStatus.textContent = 'Solicitando tu ubicación…';
    navigator.geolocation.getCurrentPosition(position => {
        const { latitude, longitude } = position.coords;
        locationStatus.textContent = `Ubicación: ${latitude}, ${longitude}`;
    }, error => {
        const messages = {
            [error.PERMISSION_DENIED]: 'Permiso denegado. Puedes habilitarlo desde la configuración del navegador.',
            [error.POSITION_UNAVAILABLE]: 'La ubicación no está disponible en este momento.',
            [error.TIMEOUT]: 'La solicitud tardó demasiado. Inténtalo de nuevo.'
        };
        locationStatus.textContent = messages[error.code] || 'No fue posible obtener tu ubicación.';
    }, { enableHighAccuracy: false, timeout: 10000, maximumAge: 60000 });
});
