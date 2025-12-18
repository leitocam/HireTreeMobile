# 🎯 PLAN DE IMPLEMENTACIÓN - RÚBRICA DE EVALUACIÓN

## 📊 Estado Actual de Puntos

| Requisito | Puntos | Estado | Prioridad |
|-----------|--------|--------|-----------|
| Clean Architecture | 20 | ✅ Implementado | - |
| MVVM | 25 | ✅ Implementado | - |
| Inicio de Sesión | 5 | ✅ Firebase Auth | - |
| Conectividad/Almacenamiento | 5 | ✅ Firestore | - |
| **Asistencia a clases** | **5** | ⚠️ Depende del estudiante | - |
| **Firebase Remote Config** | **5** | ❌ Faltante | 🟢 ALTA |
| **Notificaciones Push** | **5** | ❌ Faltante | 🟢 ALTA |
| **Pruebas (Unit/Integration/UI)** | **15** | ❌ Faltante | 🔴 CRÍTICA |
| **Mockups Figma** | **2** | ❌ Faltante | 🟡 MEDIA |
| **Descripción Play Store** | **3** | ❌ Faltante | 🟡 MEDIA |
| Publicación Play Store | 5 | ⏳ Posterior | - |
| Descarga Play Store | 5 | ⏳ Posterior | - |
| **TOTAL ALCANZABLE SIN PLAY STORE** | **90** | **55/90** | - |

---

## 🚀 FASE 1: LIMPIEZA DEL PROYECTO (30 min)

### 1.1 Cambiar Package Name ✅
```
De: com.calyrsoft.ucbp1
A:  com.hiretree.mobile
```

### 1.2 Eliminar Features No Relacionadas ✅
**Features a ELIMINAR:**
- ❌ `cardexample/` - Ejemplo de tarjetas
- ❌ `dollar/` - Conversión de moneda
- ❌ `github/` - Integración GitHub
- ❌ `movie/` - Películas
- ❌ `webview/` - WebView genérico
- ❌ `vectorucb/` - UCB específico
- ❌ `__VectorUcb.kt` - UCB específico

**Features a MANTENER:**
- ✅ `auth/` - Autenticación
- ✅ `login/` - Login/Registro
- ✅ `home/` - Pantalla principal
- ✅ `interview/` - Entrevista con IA (CORE)
- ✅ `profile/` - Perfil de usuario
- ✅ `notification/` - Notificaciones (para implementar)
- ✅ `logs/` - Solo si es para debugging

### 1.3 Actualizar Archivos de Configuración ✅
- `build.gradle.kts` - Cambiar applicationId
- `AndroidManifest.xml` - Cambiar namespace
- `strings.xml` - Cambiar nombre de app
- `settings.gradle.kts` - Cambiar rootProject.name

### 1.4 Limpiar Navegación ✅
- Eliminar rutas a features eliminadas
- Actualizar `navigation/` solo con HireTree screens

---

## 🧪 FASE 2: IMPLEMENTAR TESTING (4 horas) - CRÍTICO

### 2.1 Dependencias de Testing (15 min)

**Agregar a `build.gradle.kts`:**
```kotlin
dependencies {
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    
    // Android Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.0")
    androidTestImplementation("io.mockk:mockk-android:1.13.9")
    
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.0")
}
```

### 2.2 Pruebas Unitarias (1.5 horas) - 5 puntos

**Archivo 1: `EvaluateResponseUseCaseTest.kt`**
```
Ubicación: app/src/test/java/com/hiretree/mobile/domain/usecase/
Pruebas:
- ✅ Evaluar respuesta con palabras clave de comunicación → score alto
- ✅ Evaluar respuesta con palabras clave de liderazgo → score alto
- ✅ Evaluar respuesta con palabras clave de trabajo en equipo → score alto
- ✅ Evaluar respuesta con palabras clave de resolución de problemas → score alto
- ✅ Evaluar respuesta con palabras clave de adaptabilidad → score alto
- ✅ Evaluar múltiples respuestas → promedio correcto
- ✅ Evaluar respuesta vacía → scores mínimos
```

**Archivo 2: `InterviewViewModelTest.kt`**
```
Ubicación: app/src/test/java/com/hiretree/mobile/presentation/interview/
Pruebas:
- ✅ Iniciar entrevista → estado loading → success
- ✅ Enviar mensaje → agrega mensaje del usuario
- ✅ Recibir respuesta IA → agrega mensaje de IA
- ✅ Completar entrevista → navega a resultados
- ✅ Error en IA → muestra error
```

### 2.3 Pruebas de Integración (1.5 horas) - 5 puntos

**Archivo 1: `AuthRepositoryTest.kt`**
```
Ubicación: app/src/test/java/com/hiretree/mobile/data/repository/
Pruebas:
- ✅ Login exitoso → retorna usuario
- ✅ Login fallido → retorna error
- ✅ Registro exitoso → crea usuario en Firestore
- ✅ Logout → limpia sesión
```

**Archivo 2: `InterviewRepositoryTest.kt`**
```
Pruebas:
- ✅ Guardar sesión → llama a Firestore
- ✅ Obtener sesión por ID → retorna sesión correcta
- ✅ Actualizar sesión → actualiza en Firestore
- ✅ Listar sesiones de usuario → retorna lista filtrada
```

### 2.4 Pruebas de UI (1 hora) - 5 puntos

**Archivo 1: `InterviewScreenTest.kt`**
```
Ubicación: app/src/androidTest/java/com/hiretree/mobile/presentation/interview/
Pruebas:
- ✅ Pantalla muestra mensajes correctamente
- ✅ Input de texto funciona
- ✅ Botón enviar habilitado cuando hay texto
- ✅ Botón enviar deshabilitado cuando está vacío
- ✅ Scroll automático al agregar mensaje
```

**Archivo 2: `LoginScreenTest.kt`**
```
Pruebas:
- ✅ Campos de email y password visibles
- ✅ Botón login deshabilitado con campos vacíos
- ✅ Validación de email inválido
- ✅ Mensaje de error visible en login fallido
```

---

## 🔥 FASE 3: FIREBASE REMOTE CONFIG (1 hora) - 5 puntos

### 3.1 Configuración (20 min)

**Paso 1: Agregar dependencia**
```kotlin
implementation("com.google.firebase:firebase-config-ktx:21.6.3")
```

**Paso 2: Crear `RemoteConfigManager.kt`**
```
Ubicación: app/src/main/java/com/hiretree/mobile/data/remote/
```

**Paso 3: Configurar valores por defecto**
```kotlin
- min_interview_questions: 8
- max_interview_questions: 12
- enable_certificates: true
- gemini_model_name: "gemini-2.0-flash-exp"
- welcome_message: "¡Bienvenido a HireTree!"
```

### 3.2 Integración (20 min)

**Modificar `InterviewViewModel.kt`:**
- Usar `remoteConfig.getMinQuestions()` en lugar de hardcoded
- Usar `remoteConfig.getMaxQuestions()` en lugar de hardcoded
- Usar `remoteConfig.getGeminiModel()` para el modelo

**Modificar `HomeScreen.kt`:**
- Mostrar `welcome_message` desde Remote Config

### 3.3 Configurar en Firebase Console (20 min)

**Firebase Console → Remote Config:**
```json
{
  "min_interview_questions": 8,
  "max_interview_questions": 12,
  "enable_certificates": true,
  "gemini_model_name": "gemini-2.0-flash-exp",
  "welcome_message": "¡Bienvenido a HireTree Mobile!",
  "app_version_required": "1.0"
}
```

---

## 🔔 FASE 4: NOTIFICACIONES PUSH (1 hora) - 5 puntos

### 4.1 Configuración (15 min)

**Paso 1: Agregar dependencia**
```kotlin
implementation("com.google.firebase:firebase-messaging-ktx:23.4.1")
```

**Paso 2: Ya existe `FirebaseService.kt`**
```
Ubicación: app/src/main/java/com/hiretree/mobile/features/notification/
✅ Ya implementado
```

**Paso 3: Verificar `AndroidManifest.xml`**
```xml
✅ Ya existe el service configurado
```

### 4.2 Implementar Notificaciones Locales (20 min)

**Crear `NotificationHelper.kt`:**
```kotlin
Ubicación: app/src/main/java/com/hiretree/mobile/data/notification/

Funciones:
- showInterviewReminderNotification()
- showInterviewCompletedNotification()
- showCertificateReadyNotification()
```

### 4.3 Integrar en la App (25 min)

**Eventos que disparan notificaciones:**

1. **Al completar entrevista:**
```
Título: "¡Entrevista Completada!"
Mensaje: "Tu evaluación está lista. Toca para ver resultados."
```

2. **Al generar certificado:**
```
Título: "Certificado Generado"
Mensaje: "Tu certificado de soft skills está listo para descargar."
```

3. **Recordatorio diario (opcional):**
```
Título: "¿Listo para practicar?"
Mensaje: "Realiza una entrevista hoy y mejora tus habilidades."
```

---

## 🎨 FASE 5: MEJORAS VISUALES (2 horas)

### 5.1 Actualizar Tema y Colores (30 min)

**Archivo: `ui/theme/Color.kt`**
```kotlin
// Paleta HireTree
val HireTreePrimary = Color(0xFF1E88E5)      // Azul profesional
val HireTreeSecondary = Color(0xFF43A047)    // Verde éxito
val HireTreeAccent = Color(0xFFFFA726)       // Naranja energía
val HireTreeBackground = Color(0xFFF5F5F5)   // Gris claro
val HireTreeSurface = Color(0xFFFFFFFF)      // Blanco
val HireTreeError = Color(0xFFE53935)        // Rojo error
```

### 5.2 Mejorar UI de Pantallas (1.5 horas)

**HomeScreen.kt - Mejoras:**
- ✅ Card con gradiente para "Iniciar Entrevista"
- ✅ Estadísticas de entrevistas completadas
- ✅ Últimas evaluaciones en cards
- ✅ Animaciones al entrar

**InterviewScreen.kt - Mejoras:**
- ✅ Burbujas de chat con sombras
- ✅ Indicador de "IA está escribiendo..."
- ✅ Animación al enviar mensaje
- ✅ Scroll suave automático

**ResultsScreen.kt - Mejoras:**
- ✅ Gráfico circular de soft skills
- ✅ Barras de progreso animadas
- ✅ Colores por nivel (Bajo: Rojo, Medio: Naranja, Alto: Verde)
- ✅ Botón destacado para generar certificado

**ProfileScreen.kt - Mejoras:**
- ✅ Avatar circular con inicial
- ✅ Cards de información
- ✅ Lista de certificados con iconos

---

## 📄 FASE 6: DOCUMENTACIÓN (1 hora)

### 6.1 Descripción para Play Store (30 min)

**Crear: `playstore/DESCRIPCION.md`**

Contenido:
- Título corto (50 caracteres)
- Descripción corta (80 caracteres)
- Descripción completa (4000 caracteres)
- Qué hace la app
- Características principales
- Para quién es
- Capturas de pantalla sugeridas

### 6.2 Mockups en Figma (30 min)

**Crear: `playstore/MOCKUPS.md`**

Lista de pantallas a crear en Figma:
1. Splash Screen
2. Login/Registro
3. Home con botón destacado
4. Chat de entrevista activa
5. Pantalla de resultados con gráfico
6. Certificado PDF preview

**Link Figma:** Incluir en el documento

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Limpieza del Proyecto
- [ ] Cambiar package a `com.hiretree.mobile`
- [ ] Eliminar features no relacionadas
- [ ] Actualizar strings.xml
- [ ] Limpiar navegación
- [ ] Rebuild project exitoso

### Testing (15 puntos)
- [ ] 5 pruebas unitarias funcionando
- [ ] 3 pruebas de integración funcionando
- [ ] 3 pruebas de UI funcionando
- [ ] Cobertura mínima 50%
- [ ] Todas las pruebas pasan

### Firebase Remote Config (5 puntos)
- [ ] Dependencia agregada
- [ ] RemoteConfigManager implementado
- [ ] Configurado en Firebase Console
- [ ] Integrado en InterviewViewModel
- [ ] Valores dinámicos funcionando

### Notificaciones Push (5 puntos)
- [ ] Dependencia agregada
- [ ] FirebaseService configurado
- [ ] NotificationHelper implementado
- [ ] Notificaciones locales funcionando
- [ ] Integrado en flujo de entrevista

### Mejoras Visuales
- [ ] Paleta de colores actualizada
- [ ] HomeScreen mejorado
- [ ] InterviewScreen mejorado
- [ ] ResultsScreen mejorado
- [ ] Animaciones agregadas

### Documentación
- [ ] Descripción Play Store escrita
- [ ] Mockups en Figma creados
- [ ] README.md actualizado

---

## 📊 PUNTAJE FINAL ESPERADO

| Categoría | Puntos | Estado |
|-----------|--------|--------|
| Clean Architecture | 20 | ✅ |
| MVVM | 25 | ✅ |
| Pruebas Completas | 15 | ⏳ Implementar |
| Inicio de Sesión | 5 | ✅ |
| Remote Config | 5 | ⏳ Implementar |
| Conectividad/Almacenamiento | 5 | ✅ |
| Notificaciones | 5 | ⏳ Implementar |
| Asistencia | 5 | ⚠️ Estudiante |
| Mockups Figma | 2 | ⏳ Crear |
| Descripción | 3 | ⏳ Escribir |
| **TOTAL SIN PLAY STORE** | **90** | **55 → 90** |

---

## ⏱️ TIEMPO TOTAL ESTIMADO

| Fase | Tiempo |
|------|--------|
| Limpieza | 30 min |
| Testing | 4 horas |
| Remote Config | 1 hora |
| Notificaciones | 1 hora |
| Mejoras Visuales | 2 horas |
| Documentación | 1 hora |
| **TOTAL** | **9.5 horas** |

---

## 🎯 ORDEN DE EJECUCIÓN RECOMENDADO

1. **DÍA 1 (3 horas):**
   - ✅ Limpieza del proyecto (30 min)
   - ✅ Remote Config (1 hora)
   - ✅ Notificaciones (1 hora)
   - ✅ Rebuild y testing manual (30 min)

2. **DÍA 2 (4 horas):**
   - ✅ Pruebas Unitarias (1.5 horas)
   - ✅ Pruebas de Integración (1.5 horas)
   - ✅ Pruebas de UI (1 hora)

3. **DÍA 3 (2.5 horas):**
   - ✅ Mejoras Visuales (2 horas)
   - ✅ Documentación (30 min)

---

## 🚀 COMENZAR AHORA

**Siguiente paso:** Ejecutar limpieza del proyecto y cambio de package.

¿Listo para comenzar? 🎯

