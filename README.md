# Laboratorio 10 — sesión persistente

Aplicación Android en Jetpack Compose basada en la entrega del laboratorio 9.

## Funcionalidades

- El login solicita el nombre y no permite continuar con un valor vacío.
- El nombre se guarda bajo la clave `name` mediante DataStore Preferences.
- Una pantalla intermedia consulta DataStore al iniciar y decide entre Login y Characters.
- Profile muestra el nombre persistido.
- Cerrar sesión elimina `name`, limpia el flujo autenticado y vuelve al login.
- Back desde Login o desde la lista principal cierra la aplicación.

## Verificación

```text
gradlew testDebugUnitTest assembleDebug assembleDebugAndroidTest lintDebug
```

El APK de entrega se genera en `app/build/outputs/apk/debug/app-debug.apk`.
