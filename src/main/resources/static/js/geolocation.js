const locationButton = document.querySelector('[data-location-button]');
const locationStatus = document.querySelector('[data-location-status]');

locationButton?.addEventListener('click', () => {
    locationStatus.textContent = 'Solicitando tu ubicación…';
    navigator.geolocation.getCurrentPosition(position => {
        const { latitude, longitude } = position.coords;
        locationStatus.textContent = `Ubicación: ${latitude}, ${longitude}`;
    });
});
