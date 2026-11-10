# Laboratorio 12 — Offline First

Aplicación Android en Jetpack Compose que consume la API de Rick & Morty una sola vez durante el login y guarda toda la información en Room. Después de sincronizar, las listas, detalles y cambios locales funcionan sin conexión.

## Funcionalidades

- Descarga paginada de todos los personajes y locations desde `rickandmortyapi.com` al iniciar sesión.
- Persistencia offline con Room y sesión con DataStore.
- Creación de personajes desde la lista; edición y eliminación desde el detalle.
- Detalles reactivos: cualquier cambio local se refleja inmediatamente.
- Locations favoritas con filtros de todas, favoritas y no favoritas.
- El cierre de sesión elimina la sesión y toda la información local; el siguiente login comienza desde cero.
- Arquitectura por capas con API remota, DAOs, repositorios, ViewModels y UI en Compose.

## Verificación

```text
gradlew testDebugUnitTest assembleDebug lintDebug
```

El APK de entrega está incluido como `laboratorio12.apk`.
