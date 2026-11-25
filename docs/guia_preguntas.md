# AulaNota – Guía para el cuestionamiento del código (Revisión 4)

Respuestas cortas y con palabras sencillas. Cada una dice **dónde está en tu código**.
Ruta base: `app/src/main/java/com/aulanota/app/`

## 1. ¿Qué es Android?
Es un sistema operativo móvil de Google, basado en Linux, y de código abierto. Sobre él se instalan y ejecutan las apps (como AulaNota).

## 2. ¿Qué es Jetpack Compose?
Es el kit moderno de Android para crear la interfaz **escribiendo código Kotlin** (declarativo) en lugar de archivos XML. Describes *cómo se ve* según el estado y Compose se redibuja cuando el estado cambia. Todas las pantallas están en `ui/screens/`.

## 3. ¿Estás implementando un framework? ¿Cuál(es)?
Sí. Uso **Jetpack Compose** (interfaz), **Room** (base de datos), **Hilt** (inyección de dependencias) y **Navigation Compose** (navegación). Un *framework* te impone una estructura y tú rellenas tu código dentro de ella.

## 4. ¿Qué librerías usas y dónde se declaran?
Se declaran en dos archivos:
- `gradle/libs.versions.toml` → nombre y versión de cada librería.
- `app/build.gradle.kts` → bloque `dependencies { }` donde se activan con `implementation(...)` y `ksp(...)`.

Librerías: Compose + Material3, Room, Hilt, Navigation Compose, Lifecycle (ViewModel y `collectAsStateWithLifecycle`), Coroutines (vienen con las anteriores).

## 5. ¿Por qué el patrón Singleton no puede generar más de una instancia?
Porque la clase (o el contenedor) crea el objeto **una sola vez** y siempre entrega esa misma referencia. En un Singleton clásico el constructor es `private`. En mi app lo hace **Hilt** con `@Singleton`: guarda la primera instancia y la reutiliza.
Dónde: `di/DatabaseModule.kt` (`@Provides @Singleton provideDatabase`), `data/SessionManager.kt` y los 4 repositorios en `data/repository/`.

## 6 y 10. Patrón UDF (flujo de datos unidireccional)
El **estado baja** (ViewModel → pantalla) y los **eventos suben** (pantalla → ViewModel). La pantalla nunca cambia el estado directamente.
Ejemplo: `LoginScreen` recibe `estado: AuthUiState` y llama `onIniciarSesion(...)`; `AuthViewModel` (`ui/viewmodel/AuthViewModel.kt`) actualiza `_uiState` y la pantalla se redibuja. Igual en `MateriasViewModel` y `DetalleMateriaViewModel`. Todo se conecta en `ui/navigation/AulaNotaNavHost.kt`.

## 7. Patrón Observer
Un objeto **observa** a otro y se entera cuando cambia.
- Los DAO devuelven `Flow<List<...>>` (`data/local/dao/MateriaDao.kt`): cuando insertas/editas/borras, Room emite la lista nueva automáticamente.
- El ViewModel los expone como `StateFlow` y la pantalla los observa con `collectAsStateWithLifecycle()` (en `AulaNotaNavHost.kt`).
Por eso al agregar una materia la lista se actualiza sola.

## 8. Patrón Inyección de dependencias
En vez de que una clase **cree** lo que necesita, se lo **entregan** por el constructor. Lo hace Hilt.
- `@HiltAndroidApp` en `AulaNotaApplication.kt` y `@AndroidEntryPoint` en `MainActivity.kt`.
- `di/DatabaseModule.kt` enseña a Hilt a crear la base de datos y los DAO.
- `@Inject constructor(...)` en repositorios y `@HiltViewModel` en los ViewModels. Ej.: `MateriasViewModel` recibe `MateriaRepository` sin crearlo.

## 9. Patrón Repository
Es una clase intermedia entre los ViewModels y la fuente de datos (DAO/Room). El ViewModel no sabe de dónde vienen los datos.
Dónde: `data/repository/` (`UsuarioRepository`, `MateriaRepository`, `ApunteRepository`, `HorarioRepository`).

## 11. ¿Qué es una función `suspend`?
Es una función que puede **pausarse sin bloquear** la pantalla; solo se llama desde una corrutina u otra `suspend`. Se usa para tareas lentas como la base de datos.
Dónde: todas las funciones de escritura de los DAO (`suspend fun insertar(...)`) y repositorios; los ViewModels las llaman dentro de `viewModelScope.launch { }`.

## 12. ¿Qué anotaciones se usan para Room?
`@Entity` (tabla), `@PrimaryKey`, `@ColumnInfo`, `@ForeignKey`/`@Index`, `@Dao`, `@Insert`, `@Update`, `@Delete`, `@Query` y `@Database`.
Dónde: `data/local/entity/`, `data/local/dao/` y `data/local/AulaNotaDatabase.kt`.

## 13. ¿Qué es un ViewModel y cómo lo implementas?
Es una clase que guarda el **estado de la pantalla** y la lógica, y **sobrevive a rotaciones** de pantalla. Mis 3 ViewModels (`AuthViewModel`, `MateriasViewModel`, `DetalleMateriaViewModel`) están en `ui/viewmodel/` (extienden `ViewModel`, anotados `@HiltViewModel`) y se obtienen en la pantalla con `hiltViewModel()` en `AulaNotaNavHost.kt`.

## 14. Principios SOLID (y cómo los aplico)
- **S** – una sola responsabilidad: cada clase hace una cosa (un DAO por tabla, un repositorio por tabla, un ViewModel por pantalla).
- **O** – abierto/cerrado: puedo agregar una pantalla o tabla nueva sin reescribir las existentes.
- **L** – sustitución de Liskov: cualquier subtipo puede reemplazar a su padre (las rutas `data object` de `Screen` son intercambiables como `Screen`).
- **I** – segregación de interfaces: interfaces pequeñas (`UsuarioDao`, `MateriaDao`... en vez de un DAO gigante).
- **D** – inversión de dependencias: las clases dependen de lo que se les inyecta (Hilt), no de crearlo ellas mismas.

## 15. `@AndroidEntryPoint`
Le dice a Hilt que esa clase de Android (Activity, Fragment...) puede **recibir dependencias inyectadas**. Dónde: `MainActivity.kt`.

## 16. `NavHost` y `composable` de `NavGraphBuilder`
`NavHost` es el contenedor que muestra la pantalla actual. Dentro, cada `composable(ruta) { Pantalla() }` (función de `NavGraphBuilder`) registra **una pantalla con su ruta**. Dónde: `ui/navigation/AulaNotaNavHost.kt`; las rutas están en `Screen.kt`.

## 17. `@Dao` y `@Entity`
`@Entity` convierte una `data class` en una **tabla**. `@Dao` marca la interfaz con las **operaciones** (insertar, consultar, actualizar, borrar) sobre esa tabla.
Dónde: `data/local/entity/Usuario.kt`, etc. y `data/local/dao/UsuarioDao.kt`, etc.

## 18. Función de extensión en Kotlin
Agrega una función a una clase **que ya existe** sin modificarla. Dónde: `util/Extensiones.kt` → `fun String.sha256()`, `fun String.esCorreoValido()`, `fun String.esHoraValida()`. Se usan como `contrasena.sha256()`.

## 19. Punto de entrada de una app Android
La Activity marcada como `MAIN` + `LAUNCHER` en `AndroidManifest.xml`: **`MainActivity`** (su `onCreate` llama a `setContent`). (La clase `Application` se crea antes: `AulaNotaApplication`.)

## 20. ¿Cómo se declara una función en Kotlin?
`fun nombre(parametro: Tipo): TipoRetorno { ... }`
Ejemplo mío: `fun fechaActual(): String` en `util/Extensiones.kt`, o `fun iniciarSesion(correo: String, contrasena: String)` en `AuthViewModel.kt`.

## 21. `@HiltAndroidApp`
Inicia Hilt y crea el contenedor de dependencias de **toda la app**. Va en la clase `Application`: `AulaNotaApplication.kt` (registrada en el Manifest con `android:name`).

## 22. Diferencia entre `@Preview` y `@Composable`
`@Composable` marca una función que **dibuja interfaz** (se usa en la app real). `@Preview` solo permite **verla en Android Studio** sin ejecutar la app; no afecta al usuario. Dónde: cada pantalla tiene su función `@Composable` y abajo una `@Preview` (ej. `LoginPreview` en `LoginScreen.kt`).

## 23. ¿Cómo se declara una interfaz de usuario en Compose?
Con funciones `@Composable` que llaman a otros composables (`Column`, `Text`, `Button`, `OutlinedTextField`...), dentro de `setContent { }` en `MainActivity`. Ej.: `BienvenidaScreen.kt`.

## 24. Lenguaje de programación
**Kotlin**.

## 25. `stringResource()` vs `painterResource()`
- `stringResource(R.string.x)` → obtiene un **texto** de `res/values/strings.xml`.
- `painterResource(R.drawable.x)` → obtiene una **imagen** de `res/drawable`.
Dónde: ambos en `BienvenidaScreen.kt` y `LoginScreen.kt` (el logo usa `painterResource`, los textos `stringResource`).

## 26. ¿Dónde veo la versión de la API?
En `app/build.gradle.kts`: `compileSdk`, `targetSdk` (37) y `minSdk` (24). También en Android Studio: *File → Project Structure*.

---
## Mapa rápido de la rúbrica
| Requisito | Dónde |
|---|---|
| Arquitectura en capas | `data/` (datos) · `ui/` (presentación: screens, viewmodel, navigation) · `di/` · `util/` |
| Room + 4 entidades | `data/local/entity/` (Usuario, Materia, Apunte, HorarioClase) |
| CRUD de las 4 | Usuario: registrar/login/editar nombre/eliminar cuenta (diálogo de perfil en Materias) · Materia, Apunte, HorarioClase: agregar/listar/editar/eliminar |
| Inyección de dependencias | Hilt (`di/`, `@HiltViewModel`, `@Inject`) |
| UDF | ViewModels con `UiState` + eventos |
| Repository | `data/repository/` |
| Singleton | `@Singleton` (BD, repositorios, SessionManager) |
| Observer | `Flow` / `StateFlow` + `collectAsStateWithLifecycle` |
| Las 4 interfaces | Bienvenida · Login/Registro (se alterna con un botón) · Materias (incluye el diálogo de perfil) · Detalle de materia (pestañas Apuntes y Horario) |
| Errores con mensajes personalizados | validaciones en formularios + `error_credenciales`, `error_correo_existe`, `error_generico` |
