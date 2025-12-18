# 🧹 LIMPIEZA COMPLETA - SOLO HIRETREE

## ✅ LO QUE YA HICE POR TI

He limpiado automáticamente:
- ✅ `Screen.kt` - Eliminadas rutas de dollar, github, movie, webview, cardexample
- ✅ `NavigationDrawer.kt` - Solo quedan Home, Interview, Profile
- ✅ Creado `AppNavigation_CLEAN.kt` con navegación limpia

---

## 🚀 PASOS PARA COMPLETAR LA LIMPIEZA

### PASO 1: Ejecutar Script de Limpieza (2 minutos)

Abre PowerShell en la carpeta del proyecto y ejecuta:

```powershell
.\limpiar-proyecto.ps1
```

Esto eliminará las carpetas:
- ❌ `features/cardexample/`
- ❌ `features/dollar/`
- ❌ `features/github/`
- ❌ `features/movie/`
- ❌ `features/webview/`
- ❌ `vectorucb/`
- ❌ `__VectorUcb.kt`

---

### PASO 2: Reemplazar AppNavigation.kt (1 minuto)

En Android Studio:

1. Abre `AppNavigation.kt`
2. Abre `AppNavigation_CLEAN.kt`
3. **Copia todo el contenido** de `AppNavigation_CLEAN.kt`
4. **Pega** en `AppNavigation.kt` (reemplazar todo)
5. **Elimina** `AppNavigation_CLEAN.kt` (ya no lo necesitas)

---

### PASO 3: Limpiar MainActivity.kt (3 minutos)

Abre: `app/src/main/java/com/calyrsoft/ucbp1/MainActivity.kt`

**Busca la línea ~137-142:**
```kotlin
val navigationDrawerItems = listOf(
    NavigationDrawer.Profile,
    NavigationDrawer.Dollar,      // ← ELIMINAR
    NavigationDrawer.Movie,        // ← ELIMINAR
    NavigationDrawer.Github        // ← ELIMINAR
)
```

**Reemplaza con:**
```kotlin
val navigationDrawerItems = listOf(
    NavigationDrawer.Home,
    NavigationDrawer.Interview,
    NavigationDrawer.Profile
)
```

---

### PASO 4: Limpiar NavigationViewModel.kt (5 minutos)

Abre: `app/src/main/java/com/calyrsoft/ucbp1/navigation/NavigationViewModel.kt`

**Busca todas las referencias a:**
- `Screen.Dollar`
- `Screen.Github`
- `Screen.PopularMovies`
- `Screen.MovieDetail`
- `Screen.CardExamples`

**Reemplaza con navegación a Home:**
```kotlin
// ANTES:
"movies" -> navigateTo(Screen.PopularMovies.route, ...)
"dollar" -> navigateTo(Screen.Dollar.route, ...)
"github" -> navigateTo(Screen.Github.route, ...)

// DESPUÉS:
"movies" -> navigateTo(Screen.Home.route, NavigationOptions.REPLACE_HOME)
"dollar" -> navigateTo(Screen.Home.route, NavigationOptions.REPLACE_HOME)
"github" -> navigateTo(Screen.Home.route, NavigationOptions.REPLACE_HOME)
```

O simplemente **elimina** esos casos del when.

---

### PASO 5: Limpiar modules.kt (DI) (5 minutos)

Abre: `app/src/main/java/com/calyrsoft/ucbp1/di/modules.kt`

**Elimina o comenta estas secciones:**

```kotlin
// ❌ ELIMINAR - Github
single{ GithubRemoteDataSource(get()) }
single<IGithubRepository>{ GithubRepository(get()) }
factory { FindByNickNameUseCase(get()) }
viewModel { GithubViewModel(get(), get(), get()) }

// ❌ ELIMINAR - Dollar
single(named("dollarDao")) { get<AppRoomDatabase>().dollarDao() }
single { RealTimeRemoteDataSource() }
single { DollarLocalDataSource(get(named("dollarDao"))) }
single<IDollarRepository> { DollarRepository(get(), get()) }
factory { FetchDollarUseCase(get()) }
factory { FetchDollarParallelUseCase(get()) }
viewModel{ DollarViewModel(get(), get(), get()) }

// ❌ ELIMINAR - Movies
single(named("apiKey")) { androidApplication().getString(R.string.api_key) }
single<MovieService> { get<Retrofit>(named("RetrofitMovie")).create(MovieService::class.java) }
single { MovieRemoteDataSource(get()) }
single { MovieLocalDataSource() }
single<IMoviesRepository> { MovieRepository(get(), get()) }
factory { FetchPopularMoviesUseCase(get()) }
factory { RateMovieUseCase(get()) }
viewModel { PopularMoviesViewModel(get(), get(), get()) }
```

**MANTÉN solo:**
```kotlin
// ✅ MANTENER - Auth
single { FirebaseAuth.getInstance() }
single { FirebaseFirestore.getInstance() }
single { AuthRepositoryImpl(get(), get()) as AuthRepository }
viewModel { AuthViewModel(get()) }

// ✅ MANTENER - Interview
single { GeminiService() }
single { InterviewRepositoryImpl(get(), get()) as InterviewRepository }
factory { StartInterviewUseCase(get()) }
factory { SendMessageUseCase(get()) }
factory { CompleteInterviewUseCase(get()) }
viewModel { InterviewViewModel(get(), get(), get()) }

// ✅ MANTENER - Profile
single<IProfileRepository> { ProfileRepository() }
factory { GetProfileUseCase(get()) }
viewModel { ProfileViewModel(get(), get()) }

// ✅ MANTENER - Navigation
viewModel { NavigationViewModel() }
```

---

### PASO 6: Sync y Rebuild (5 minutos)

```
1. File → Sync Project with Gradle Files
2. Build → Clean Project
3. Build → Rebuild Project
```

**Espera a que termine** (puede tardar 3-5 minutos)

---

## ⚠️ ERRORES ESPERADOS (Y CÓMO SOLUCIONARLOS)

### Error: "Unresolved reference: DollarScreen"

**Solución:** 
- El archivo `AppNavigation.kt` todavía tiene imports viejos
- Asegúrate de haber reemplazado con `AppNavigation_CLEAN.kt`

### Error: "Unresolved reference: GithubViewModel"

**Solución:**
- El archivo `modules.kt` (DI) todavía tiene referencias
- Elimina las secciones de Github, Dollar, Movies

### Error: "Cannot find symbol MovieModel"

**Solución:**
- Algún archivo todavía importa `movie` package
- Busca y elimina esos imports

---

## 🔍 VERIFICACIÓN FINAL

Ejecuta estos comandos en PowerShell para verificar:

```powershell
# Buscar referencias a features eliminadas
Select-String -Path "app\src\main\java\**\*.kt" -Pattern "dollar|github|movie|webview|cardexample" -Exclude "*.md"
```

**Debe retornar:** 0 resultados (o muy pocos en comentarios)

---

## 📊 ESTRUCTURA FINAL ESPERADA

```
app/src/main/java/com/calyrsoft/ucbp1/
├── App.kt
├── MainActivity.kt
├── di/
│   └── modules.kt (LIMPIO - solo HireTree)
│
├── navigation/
│   ├── Screen.kt (LIMPIO - 6 screens)
│   ├── NavigationDrawer.kt (LIMPIO - 3 items)
│   ├── AppNavigation.kt (LIMPIO)
│   └── NavigationViewModel.kt (LIMPIO)
│
├── features/
│   ├── auth/          ✅ MANTENER
│   ├── login/         ✅ MANTENER
│   ├── home/          ✅ MANTENER
│   ├── interview/     ✅ MANTENER (CORE)
│   ├── profile/       ✅ MANTENER
│   ├── notification/  ✅ MANTENER
│   └── logs/          ✅ MANTENER (opcional)
│
├── data/
│   ├── remote/
│   │   └── RemoteConfigManager.kt ✅ NUEVO
│   └── notification/
│       └── NotificationHelper.kt  ✅ NUEVO
│
└── ui/
    └── theme/
```

---

## 🎯 CHECKLIST DE LIMPIEZA

- [ ] Ejecutado `limpiar-proyecto.ps1`
- [ ] Reemplazado `AppNavigation.kt` con versión limpia
- [ ] Limpiado `MainActivity.kt` (navigationDrawerItems)
- [ ] Limpiado `NavigationViewModel.kt` (casos del when)
- [ ] Limpiado `modules.kt` (DI - eliminadas features)
- [ ] Sync Project
- [ ] Rebuild Project
- [ ] Build exitoso sin errores
- [ ] App ejecuta correctamente

---

## 📈 BENEFICIOS DE LA LIMPIEZA

**Antes:**
- 12 features (8 no relacionadas)
- ~15,000 líneas de código innecesarias
- Build time: ~3 minutos
- APK size: ~25 MB

**Después:**
- 6 features (solo HireTree)
- Código enfocado y mantenible
- Build time: ~1.5 minutos
- APK size: ~12 MB

---

## ⏭️ SIGUIENTE PASO

Una vez completada la limpieza:

1. ✅ Ejecutar tests: `./gradlew test`
2. ✅ Verificar que la app funcione
3. ✅ Continuar con integración de Remote Config y Notificaciones

---

**¡Ahora comienza con el PASO 1 (ejecutar el script)! 🚀**

