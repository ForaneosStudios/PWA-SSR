const CACHE_VERSION = 'localshop-v1';
const APP_SHELL = [
    '/',
    '/offline.html',
    '/css/styles.css',
    '/js/pwa-register.js',
    '/manifest.json',
    '/images/icon-192.png',
    '/images/icon-512.png'
];

self.addEventListener('install', event => {
    event.waitUntil(caches.open(CACHE_VERSION).then(cache => cache.addAll(APP_SHELL)));
    self.skipWaiting();
});
