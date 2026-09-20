# Laboratorio 7 — App con navegación

**Sergio Daniel López Barrientos · Carné 25848**

Aplicación Android en Kotlin y Jetpack Compose. Usa una sola Activity, MaterialTheme, imágenes por internet con Coil y navegación tipada mediante destinos anotados con `@Serializable`.

## Abrir y ejecutar

1. Abrir esta carpeta desde Android Studio compatible con Android Gradle Plugin 9.3.2.
2. Usar JDK 17 o superior y tener instalado Android SDK Platform 37 y Build Tools 36.0.0. Android Studio puede solicitar instalar componentes faltantes.
3. Esperar la sincronización de Gradle y ejecutar `app` en un emulador o dispositivo con Android 7.0 / API 24 o superior.
4. Permitir conexión a internet para descargar los retratos. El logo está incluido en la app; la lista y los detalles usan los datos locales del curso. Si una imagen no se puede descargar, se muestra un avatar de respaldo.

En Windows:

```powershell
.\gradlew.bat assembleDebug
```

En macOS o Linux:

```sh
sh gradlew assembleDebug
```

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`. Es un APK de depuración instalable, firmado automáticamente por Gradle.

## Pantallas y requisitos

| Requisito | Implementación |
| --- | --- |
| Login con logo, botón y datos del estudiante | `ui/screens/LoginScreen.kt` y `res/values/strings.xml` |
| Botón Empezar lleva al listado | `navigate(Characters)` |
| Login eliminado del historial | `popUpTo<Login> { inclusive = true }` |
| Atrás desde listado cierra la app | `BackHandler` invoca `finishAndRemoveTask()` |
| Nueva apertura después de salir muestra Login | La nueva tarea empieza con `startDestination = Login` |
| Lista de personajes y barra Characters | `ui/screens/CharactersScreen.kt` |
| Enviar únicamente el ID | `CharacterDetails(val id: Int)` |
| Nombre, especie, estado, imagen y género en detalle | `ui/screens/CharacterDetailsScreen.kt` |
| Barra Characters details con botón de regreso | `TopAppBar` y `popBackStack()` |
| Imágenes cargadas mediante Coil | `ui/screens/CharacterPortrait.kt`, compartido por lista y detalle |
| Una sola Activity | `MainActivity.kt` |
| Colores del tema | `ui/theme/Theme.kt`, con soporte de tema claro y oscuro |
| Destinos anotados con Serializable | `navigation/Destinations.kt` |

Las rutas de código de la tabla son relativas a `app/src/main/java/edu/sergio/lab7`, excepto los recursos XML, que están en `app/src/main/res`.

## Datos del curso

`Character.kt` y `CharacterDb.kt` son los archivos proporcionados por el estudiante. Únicamente se agregó la declaración de paquete `edu.sergio.lab7.data`; sus propiedades, datos y métodos se conservaron. La base local incluye los 20 personajes originales.

El detalle recibe el ID, lo recupera con `toRoute<CharacterDetails>()` y consulta `getCharacterById(id)`. No se envía el personaje completo. Si se recibe un ID inexistente, se muestra un mensaje con opción de regresar.

## Pruebas

```powershell
.\gradlew.bat testDebugUnitTest lintDebug
# Requiere un emulador iniciado o un dispositivo conectado:
.\gradlew.bat connectedDebugAndroidTest
```

Las pruebas unitarias comprueban los 20 IDs únicos, los datos de Morty y que el destino de detalle serialice únicamente el ID. Las pruebas de interfaz comprueban la identificación del estudiante, la eliminación del login del historial, la salida mediante Atrás, los datos del detalle, ambos métodos de regreso y un ID inexistente.

## Entrega

- Adjuntar el APK como archivo en la plataforma del curso.
- Subir este proyecto a un repositorio de GitHub y mantener el código del laboratorio en la rama **laboratorio7**.
- Agregar en el comentario de entrega el enlace a la rama, con este formato: `https://github.com/USUARIO/REPOSITORIO/tree/laboratorio7`.
- Conservar el repositorio para los siguientes laboratorios y crear una nueva rama para cada uno.

La publicación en GitHub y el envío a la plataforma del curso se realizan por separado de la compilación local. No se incluyen contraseñas, tokens ni claves de firma privadas en el repositorio.

## Referencias

- [Navegación tipada con Kotlin Serialization](https://developer.android.com/guide/navigation/design/type-safety).
- [Administración del historial de navegación](https://developer.android.com/guide/navigation/backstack/circular).
- [Coil: carga de imágenes en Compose](https://coil-kt.github.io/coil/compose/).
- [Rick and Morty API: origen de los retratos enlazados en los datos del curso](https://rickandmortyapi.com/documentation).
- [Logo usado en la pantalla inicial](https://commons.wikimedia.org/wiki/File:Rick_and_Morty.svg). Los personajes y la marca pertenecen a sus titulares; se usan como material del ejercicio académico.
