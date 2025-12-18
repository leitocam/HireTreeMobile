# 🧹 INSTRUCCIONES DE LIMPIEZA MANUAL - HIRETREE

## ⚠️ IMPORTANTE: Ejecutar estas acciones ANTES de continuar

---

## 📂 PASO 1: ELIMINAR FEATURES NO RELACIONADAS (5 min)

### En Android Studio:

1. **Abre el Project Explorer** (Vista: Project)

2. **Navega a:** `app/src/main/java/com/calyrsoft/ucbp1/features/`

3. **ELIMINA las siguientes carpetas** (Click derecho → Delete):
   - ❌ `cardexample/` - Ejemplo de tarjetas
   - ❌ `dollar/` - Conversión de moneda  
   - ❌ `github/` - Integración GitHub
   - ❌ `movie/` - Películas
   - ❌ `webview/` - WebView genérico

4. **Navega a:** `app/src/main/java/com/calyrsoft/ucbp1/`

5. **ELIMINA:**
   - ❌ `vectorucb/` - UCB específico
   - ❌ `__VectorUcb.kt` - UCB específico

6. **MANTÉN estas carpetas:**
   - ✅ `auth/` - Autenticación
   - ✅ `login/` - Login/Registro
   - ✅ `home/` - Pantalla principal
   - ✅ `interview/` - **CORE** - Entrevista con IA
   - ✅ `profile/` - Perfil de usuario
   - ✅ `notification/` - Notificaciones
   - ✅ `logs/` - Solo si es necesario

---

## 📦 PASO 2: REFACTORIZAR PACKAGE NAME (10 min)

### Método Automático (Recomendado):

1. **En Android Studio:** View → Tool Windows → Project

2. **Cambia vista a "Project"** (no Android)

3. **Navega a:** `app/src/main/java/com/calyrsoft/ucbp1/`

4. **Click derecho en `ucbp1`** → Refactor → Rename Package

5. **Nuevo nombre:** `com.hiretree.mobile`

6. **Marca:** "Search in comments and strings"

7. **Click:** Refactor

8. **Android Studio preguntará:** "Do Refactor?" → **SÍ**

9. **Espera** a que termine (puede tardar 1-2 minutos)

### Verificar:

10. **Busca en todo el proyecto** (Ctrl+Shift+F):
    - Busca: `com.calyrsoft.ucbp1`
    - Debe aparecer: 0 resultados (o muy pocos)

11. **Busca:**
    - Busca: `com.hiretree.mobile`
    - Debe aparecer: Múltiples resultados ✅

---

## 🔍 PASO 3: LIMPIAR NAVEGACIÓN (5 min)

### Archivos a revisar:

1. **Abre:** `app/src/main/java/com/hiretree/mobile/navigation/`

2. **Busca referencias a features eliminadas:**
   - `cardexample`
   - `dollar`
   - `github`
   - `movie`
   - `webview`
   - `vectorucb`

3. **Elimina:**
   - Rutas de navegación
   - Imports
   - Composables

### Ejemplo de código a eliminar:

```kotlin
// ELIMINAR líneas como estas:
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarScreen
import com.calyrsoft.ucbp1.features.movie.presentation.MovieScreen
import com.calyrsoft.ucbp1.features.github.presentation.GithubScreen

// ELIMINAR composables en NavHost:
composable("dollar") { DollarScreen() }
composable("movie") { MovieScreen() }
composable("github") { GithubScreen() }
```

---

## 📋 PASO 4: ACTUALIZAR DI (Dependency Injection) (5 min)

1. **Abre:** `app/src/main/java/com/hiretree/mobile/di/AppModule.kt`

2. **Busca y elimina** ViewModels/Repositories de features eliminadas:

```kotlin
// ELIMINAR:
viewModel { DollarViewModel() }
viewModel { MovieViewModel() }
viewModel { GithubViewModel() }

single { DollarRepository() }
single { MovieRepository() }
```

3. **MANTÉN solo:**

```kotlin
// Auth
viewModel { AuthViewModel(get()) }
single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

// Interview (CORE)
viewModel { InterviewViewModel(get(), get()) }
single<InterviewRepository> { InterviewRepositoryImpl(get(), get()) }

// Profile
viewModel { ProfileViewModel(get()) }
single<ProfileRepository> { ProfileRepositoryImpl(get()) }

// Notification
viewModel { NotificationViewModel() }
```

---

## ✅ PASO 5: VERIFICACIÓN (5 min)

### Build y Sync:

1. **File** → **Invalidate Caches / Restart** → **Invalidate and Restart**

2. Espera a que reinicie Android Studio

3. **Build** → **Clean Project**

4. **Build** → **Rebuild Project**

5. **Revisa errores** en Build Output

### Checklist de Verificación:

- [ ] Features eliminadas (cardexample, dollar, github, movie, webview, vectorucb)
- [ ] Package renombrado a `com.hiretree.mobile`
- [ ] No hay imports rojos en el código
- [ ] Navegación limpia (sin rutas a features eliminadas)
- [ ] DI module limpio
- [ ] Build exitoso sin errores
- [ ] App ejecuta correctamente

---

## 🚨 ERRORES COMUNES Y SOLUCIONES

### Error: "Unresolved reference"

**Causa:** Quedan imports de features eliminadas

**Solución:**
1. Ctrl+Shift+F → Buscar el nombre de la feature
2. Eliminar todas las referencias
3. Build → Rebuild

### Error: "Cannot find symbol"

**Causa:** Referencias en navegación

**Solución:**
1. Revisa archivos de navegación
2. Elimina composables de features eliminadas
3. Sync Project

### Error: "Package does not exist"

**Causa:** Package no se refactorizó correctamente

**Solución:**
1. View → Tool Windows → Project
2. Verifica que la estructura sea: `com/hiretree/mobile/`
3. Si no, refactoriza manualmente

---

## 📊 ESTRUCTURA FINAL ESPERADA

```
app/src/main/java/com/hiretree/mobile/
├── App.kt
├── MainActivity.kt
├── di/
│   └── AppModule.kt
├── data/
│   ├── remote/
│   ├── repository/
│   └── notification/
├── domain/
│   ├── model/
│   ├── usecase/
│   └── repository/
├── presentation/
│   ├── auth/
│   ├── login/
│   ├── home/
│   ├── interview/     ← CORE
│   ├── profile/
│   └── notification/
├── navigation/
│   └── NavHost.kt
└── ui/
    └── theme/
```

---

## ⏭️ SIGUIENTE PASO

Una vez completada la limpieza y verificación:

✅ **Continuar con:** Implementación de Testing (Fase 2)

---

## ⏱️ TIEMPO TOTAL: ~30 minutos

**¡No continúes hasta completar esta limpieza!**

El resto de la implementación depende de que estos cambios estén hechos correctamente.

