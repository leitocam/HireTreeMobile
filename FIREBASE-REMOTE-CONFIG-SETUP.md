# 🔥 Configuración de Firebase Remote Config

## 📋 Pasos para Configurar en Firebase Console

### 1. Acceder a Firebase Console
```
URL: https://console.firebase.google.com/
Proyecto: hiretree-248d4
```

### 2. Navegar a Remote Config
```
Menú lateral → Engage → Remote Config
```

### 3. Crear Parámetros

Haz clic en **"Agregar parámetro"** para cada uno:

---

#### ⚙️ Parámetro 1: min_interview_questions
```
Clave: min_interview_questions
Tipo: Number
Valor por defecto: 8
Descripción: Número mínimo de preguntas en una entrevista
```

---

#### ⚙️ Parámetro 2: max_interview_questions
```
Clave: max_interview_questions
Tipo: Number
Valor por defecto: 12
Descripción: Número máximo de preguntas en una entrevista
```

---

#### ⚙️ Parámetro 3: enable_certificates
```
Clave: enable_certificates
Tipo: Boolean
Valor por defecto: true
Descripción: Habilitar o deshabilitar generación de certificados
```

---

#### ⚙️ Parámetro 4: gemini_model_name
```
Clave: gemini_model_name
Tipo: String
Valor por defecto: gemini-2.0-flash-exp
Descripción: Nombre del modelo de Gemini a utilizar
```

**Valores posibles:**
- `gemini-2.0-flash-exp` (recomendado - más reciente)
- `gemini-1.5-flash` (alternativa rápida)
- `gemini-1.5-pro` (más potente pero más lento)

---

#### ⚙️ Parámetro 5: welcome_message
```
Clave: welcome_message
Tipo: String
Valor por defecto: ¡Bienvenido a HireTree Mobile!
Descripción: Mensaje de bienvenida en la pantalla principal
```

---

#### ⚙️ Parámetro 6: app_version_required
```
Clave: app_version_required
Tipo: String
Valor por defecto: 1.0
Descripción: Versión mínima requerida de la app
```

---

### 4. Publicar Cambios

```
Click en "Publicar cambios" (botón azul superior derecho)
Confirmar publicación
```

---

## 🎯 Configuración JSON Completa (para importar)

Si prefieres importar la configuración completa, usa este JSON:

```json
{
  "parameters": {
    "min_interview_questions": {
      "defaultValue": {
        "value": "8"
      },
      "valueType": "NUMBER",
      "description": "Número mínimo de preguntas en una entrevista"
    },
    "max_interview_questions": {
      "defaultValue": {
        "value": "12"
      },
      "valueType": "NUMBER",
      "description": "Número máximo de preguntas en una entrevista"
    },
    "enable_certificates": {
      "defaultValue": {
        "value": "true"
      },
      "valueType": "BOOLEAN",
      "description": "Habilitar generación de certificados"
    },
    "gemini_model_name": {
      "defaultValue": {
        "value": "gemini-2.0-flash-exp"
      },
      "valueType": "STRING",
      "description": "Modelo de Gemini a utilizar"
    },
    "welcome_message": {
      "defaultValue": {
        "value": "¡Bienvenido a HireTree Mobile!"
      },
      "valueType": "STRING",
      "description": "Mensaje de bienvenida en Home"
    },
    "app_version_required": {
      "defaultValue": {
        "value": "1.0"
      },
      "valueType": "STRING",
      "description": "Versión mínima requerida"
    }
  }
}
```

---

## 🧪 Casos de Uso de Remote Config

### 1️⃣ Cambiar Modelo de IA sin actualizar app
```
Problema: gemini-2.0-flash-exp está dando errores
Solución: Cambiar a "gemini-1.5-flash" desde Remote Config
Resultado: Todos los usuarios usan el nuevo modelo en ~1 hora
```

### 2️⃣ Ajustar Dificultad de Entrevistas
```
Feedback: "Las entrevistas son muy largas"
Solución: Cambiar max_interview_questions de 12 a 10
Resultado: Entrevistas más cortas sin actualizar app
```

### 3️⃣ Desactivar Features Temporalmente
```
Problema: El servidor de certificados está caído
Solución: Cambiar enable_certificates a false
Resultado: Los usuarios no ven el botón de generar certificado
```

### 4️⃣ Mensajes Dinámicos
```
Evento: Navidad
Solución: Cambiar welcome_message a "¡Feliz Navidad! 🎄"
Resultado: Mensaje personalizado sin actualizar app
```

### 5️⃣ Forzar Actualización
```
Problema: Versión 1.0 tiene bug crítico
Solución: Cambiar app_version_required a "1.1"
App: Detecta y pide actualizar
```

---

## 📱 Cómo se Usa en la App

### En `InterviewViewModel.kt`:
```kotlin
class InterviewViewModel(
    private val remoteConfig: RemoteConfigManager,
    ...
) {
    fun startInterview() {
        val minQuestions = remoteConfig.getMinQuestions() // 8
        val maxQuestions = remoteConfig.getMaxQuestions() // 12
        // Usar estos valores en lugar de hardcoded
    }
}
```

### En `GeminiService.kt`:
```kotlin
class GeminiService(
    private val remoteConfig: RemoteConfigManager
) {
    private val generativeModel = GenerativeModel(
        modelName = remoteConfig.getGeminiModel(), // Dinámico!
        apiKey = BuildConfig.GEMINI_API_KEY
    )
}
```

### En `HomeScreen.kt`:
```kotlin
@Composable
fun HomeScreen(
    remoteConfig: RemoteConfigManager
) {
    Text(text = remoteConfig.getWelcomeMessage())
}
```

---

## ⚡ Ventajas de Remote Config

| Sin Remote Config | Con Remote Config |
|-------------------|-------------------|
| ❌ Actualizar app para cambiar valores | ✅ Cambios en tiempo real |
| ❌ Esperar aprobación de Play Store | ✅ Sin aprobación necesaria |
| ❌ Usuarios deben actualizar | ✅ Automático en 1 hora |
| ❌ No se puede A/B testing | ✅ A/B testing fácil |
| ❌ Rollback requiere nueva versión | ✅ Rollback instantáneo |

---

## 🔄 Frecuencia de Actualización

```kotlin
// En RemoteConfigManager.kt
minimumFetchIntervalInSeconds = 3600 // 1 hora
```

**Comportamiento:**
- Primera vez: Usa valores por defecto
- Al abrir app: Intenta obtener nuevos valores
- Si pasan >1 hora: Obtiene valores actualizados
- Si no hay conexión: Usa últimos valores obtenidos

**Para desarrollo (obtener siempre):**
```kotlin
minimumFetchIntervalInSeconds = 0 // Para testing
```

---

## ✅ Verificar que Funciona

### 1. En Android Studio (Logcat):
```
D/RemoteConfig: Fetch succeeded: true
D/RemoteConfig: Min questions: 8
D/RemoteConfig: Max questions: 12
D/RemoteConfig: Gemini model: gemini-2.0-flash-exp
```

### 2. Cambiar valor en Firebase Console:
```
max_interview_questions: 12 → 10
Publicar cambios
```

### 3. En la app:
```kotlin
// Forzar fetch (solo para testing)
remoteConfig.fetchConfig()

// Verificar nuevo valor
println(remoteConfig.getMaxQuestions()) // Debería ser 10
```

### 4. Reiniciar app después de 1 hora:
```
Nuevos valores aplicados automáticamente
```

---

## 🎯 RESUMEN EJECUTIVO

| Aspecto | Detalle |
|---------|---------|
| **Parámetros Totales** | 6 |
| **Tiempo de Setup** | 10 minutos |
| **Intervalo de Fetch** | 1 hora |
| **Punto de Entrada** | `RemoteConfigManager.kt` |
| **Beneficio Clave** | Cambios sin actualizar app |

---

## 📞 Próximos Pasos

1. ✅ Configurar parámetros en Firebase Console
2. ✅ Publicar cambios
3. ✅ Integrar RemoteConfigManager en DI (Koin)
4. ✅ Inyectar en ViewModels
5. ✅ Reemplazar valores hardcoded
6. ✅ Probar cambios en tiempo real

---

**¡Remote Config configurado! 🎉**

Ahora puedes controlar tu app sin publicar actualizaciones.

