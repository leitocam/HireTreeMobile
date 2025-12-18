# 🔥 CONFIGURACIÓN FIREBASE REMOTE CONFIG - PASO A PASO

## Guía Completa para Producción

---

## 📋 PASOS EN FIREBASE CONSOLE

### PASO 1: Acceder a Firebase Console

1. Ve a: https://console.firebase.google.com/
2. Selecciona tu proyecto: **hiretree-248d4**
3. En el menú lateral, busca **"Remote Config"**
4. Click en **"Remote Config"**

---

### PASO 2: Crear Parámetros de Configuración

Click en **"Agregar parámetro"** para cada uno de los siguientes:

#### Parámetro 1: gemini_api_key
```
Nombre: gemini_api_key
Tipo: String
Valor por defecto: (vacío o tu API key de Gemini)
Descripción: API Key de Google Gemini para IA real
```

**Cómo obtener tu Gemini API Key:**
1. Ve a: https://makersuite.google.com/app/apikey
2. Click en "Create API Key"
3. Copia la key
4. Pégala en el valor del parámetro

#### Parámetro 2: gemini_model
```
Nombre: gemini_model  
Tipo: String
Valor por defecto: gemini-1.5-flash
Descripción: Modelo de Gemini a utilizar
```

Opciones de modelos:
- `gemini-1.5-flash` (Rápido y económico) ⭐ Recomendado
- `gemini-1.5-pro` (Más potente, más lento)
- `gemini-pro` (Versión anterior)

#### Parámetro 3: use_real_ai
```
Nombre: use_real_ai
Tipo: Boolean
Valor por defecto: false
Descripción: Activar IA real (true) o simulador (false)
```

**Importante:**
- `false` → Usa simulador (NO consume API, gratis)
- `true` → Usa Gemini real (consume API, requiere billing)

#### Parámetro 4: min_messages_to_complete
```
Nombre: min_messages_to_complete
Tipo: Number
Valor por defecto: 5
Descripción: Mínimo de mensajes para completar entrevista
```

#### Parámetro 5: max_questions
```
Nombre: max_questions
Tipo: Number
Valor por defecto: 7
Descripción: Máximo de preguntas en la entrevista
```

---

### PASO 3: Configurar Condiciones (Opcional)

Puedes crear diferentes configuraciones por:
- **Versión de app**
- **País**
- **Idioma**
- **Porcentaje de usuarios**

Ejemplo - Activar IA solo para el 10% de usuarios:
```
Condición: Porcentaje aleatorio de usuarios
Nombre: test_ai_10_percent
Porcentaje: 10%
Valor personalizado:
  use_real_ai: true
```

---

### PASO 4: Publicar Cambios

1. Click en **"Publicar cambios"**
2. Confirmar en el diálogo
3. ✅ Los cambios estarán disponibles en ~1 minuto

---

## 📱 CONFIGURACIÓN RECOMENDADA PARA INICIO

### Para Testing/Desarrollo:
```json
{
  "gemini_api_key": "",
  "gemini_model": "gemini-1.5-flash",
  "use_real_ai": false,
  "min_messages_to_complete": 3,
  "max_questions": 5
}
```

### Para Producción (Sin IA Real):
```json
{
  "gemini_api_key": "",
  "gemini_model": "gemini-1.5-flash",
  "use_real_ai": false,
  "min_messages_to_complete": 5,
  "max_questions": 7
}
```

### Para Producción (Con IA Real):
```json
{
  "gemini_api_key": "TU_API_KEY_AQUI",
  "gemini_model": "gemini-1.5-flash",
  "use_real_ai": true,
  "min_messages_to_complete": 5,
  "max_questions": 10
}
```

---

## 🔍 VERIFICAR CONFIGURACIÓN

### En Logcat (Android Studio):

```
Filtro: "HireTree"

✅ Logs esperados:
I/HireTree: ✅ Remote Config inicializado correctamente
D/RemoteConfigService: ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
D/RemoteConfigService: 📋 CONFIGURACIÓN ACTUAL:
D/RemoteConfigService:    Gemini Model: gemini-1.5-flash
D/RemoteConfigService:    Use Real AI: false
D/RemoteConfigService:    API Key: ❌ No configurada
D/RemoteConfigService:    Min Messages: 5
D/RemoteConfigService:    Max Questions: 7
D/RemoteConfigService: ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

### Al Iniciar Entrevista:

```
Filtro: "GeminiService"

✅ Logs esperados:
D/GeminiService: ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
D/GeminiService: 🚀 Iniciando nueva entrevista
D/GeminiService:    Modo: SIMULADOR
D/GeminiService:    Modelo: gemini-1.5-flash
D/GeminiService:    API Key: ❌ No disponible
D/GeminiService: ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
D/GeminiService: 📝 Usando SIMULADOR de entrevista
```

---

## 🚀 CÓMO ACTIVAR IA REAL

### 1. Obtener API Key de Gemini:

```
1. Ve a https://makersuite.google.com/app/apikey
2. Inicia sesión con tu cuenta Google
3. Click "Create API Key"
4. Copia la key (empieza con AIza...)
```

### 2. Actualizar en Firebase Console:

```
1. Firebase Console > Remote Config
2. Editar parámetro "gemini_api_key"
3. Pegar tu API Key
4. Editar parámetro "use_real_ai"
5. Cambiar a "true"
6. Click "Publicar cambios"
```

### 3. Verificar en la App:

```
1. Cerrar app completamente
2. Abrir de nuevo
3. Verificar Logcat:
   
D/RemoteConfigService:    Use Real AI: true
D/RemoteConfigService:    API Key: ✅ Configurada
D/GeminiService:    Modo: IA REAL (Gemini)
```

---

## 🌍 FUNCIONAMIENTO EN DIFERENTES DISPOSITIVOS

### ✅ Con Remote Config:

```
Dispositivo A (Tu PC):
- Abre app → Descarga config de Firebase
- usa_real_ai = false → SIMULADOR
- ✅ Funciona sin API Key

Dispositivo B (Otro celular):
- Abre app → Descarga MISMA config de Firebase
- usa_real_ai = false → SIMULADOR
- ✅ Funciona igual que dispositivo A

Dispositivo C (Producción):
- Abre app → Descarga config de Firebase
- usa_real_ai = true → IA REAL
- ✅ Usa Gemini API si hay key configurada
```

### ❌ Sin Remote Config (Antes):

```
Dispositivo A (Tu PC):
- API Key hardcodeada en el código
- ✅ Funciona

Dispositivo B (Otro celular):
- NO tiene API Key en el código
- ❌ No funciona

Dispositivo C (Producción):
- Necesita recompilar con nueva API Key
- ❌ No es escalable
```

---

## 💡 VENTAJAS DE REMOTE CONFIG

### 1. **Sin Recompilación**
```
Cambiar de simulador a IA real:
❌ Antes: Editar código → Recompilar → Reinstalar
✅ Ahora: Firebase Console → Publicar → Listo
```

### 2. **Configuración por Usuarios**
```
10% de usuarios → IA Real (testing)
90% de usuarios → Simulador (estable)
```

### 3. **Rollback Instantáneo**
```
Si IA real falla:
Firebase Console → Cambiar use_real_ai a false → Publicar
Todos los dispositivos usan simulador en ~1 minuto
```

### 4. **A/B Testing**
```
Grupo A: max_questions = 5
Grupo B: max_questions = 10
Medir cuál tiene mejor conversión
```

### 5. **Actualización Sin Play Store**
```
Cambiar parámetros → Efecto inmediato
No necesita nueva versión en Play Store
```

---

## 🔐 SEGURIDAD

### API Keys:

⚠️ **NUNCA** pongas API Keys sensibles en Remote Config si es pública

✅ **Mejor práctica:**
```
1. API Keys del servidor → Backend
2. Remote Config → Solo flags y parámetros no sensibles
3. App → Solicita al backend cuando necesita IA
```

Para este proyecto educativo:
- ✅ OK poner Gemini API Key (tiene límites gratuitos)
- ❌ NO poner keys de pago o datos sensibles

---

## 📊 MONITOREO

### Ver Actividad en Firebase:

```
Firebase Console > Remote Config > Pestaña "Actividad"

Verás:
- Cuándo se publicaron cambios
- Quién los publicó
- Qué parámetros cambiaron
- Rollback si es necesario
```

### Analytics de Uso:

```
Firebase Console > Analytics

Eventos personalizados:
- interview_started
- interview_completed
- ai_mode_used (simulator | real)
```

---

## 🐛 TROUBLESHOOTING

### Problema: Config no se actualiza

```
Solución:
1. Verifica que publicaste los cambios en Firebase
2. Espera 1-2 minutos
3. Fuerza cierre de la app
4. Abre de nuevo
5. Verifica Logcat

Si persiste:
- Firebase Console > Remote Config > Ver historial
- Confirmar que cambios están publicados
```

### Problema: App sigue usando valores viejos

```
Causa: Cache local de Remote Config

Solución:
App.kt tiene minimumFetchIntervalInSeconds = 3600 (1 hora)

Para testing inmediato:
Cambiar a: setMinimumFetchIntervalInSeconds(0)
```

### Problema: IA real no funciona

```
Verificar:
1. ✅ use_real_ai = true en Firebase
2. ✅ gemini_api_key tiene valor válido
3. ✅ API Key tiene billing habilitado (si aplica)
4. ✅ Logcat muestra "Modo: IA REAL"

Si todo está bien pero falla:
- Implementación de Gemini API aún no está completa
- Fallback a simulador automáticamente
```

---

## ✅ CHECKLIST FINAL

Antes de considerar Remote Config completo:

- [ ] Parámetros creados en Firebase Console
- [ ] Valores por defecto configurados
- [ ] Cambios publicados
- [ ] App descarga config correctamente (Logcat)
- [ ] Simulador funciona en todos los dispositivos
- [ ] (Opcional) IA real funciona con API Key válida
- [ ] (Opcional) Condiciones configuradas para testing
- [ ] Documentación actualizada

---

## 🎯 ESTADO ACTUAL

```
╔════════════════════════════════════╗
║  REMOTE CONFIG: ✅ IMPLEMENTADO   ║
║                                    ║
║  ✅ Servicio creado                ║
║  ✅ Integrado en App               ║
║  ✅ GeminiService actualizado      ║
║  ✅ Logs de debugging              ║
║  ⏳ Pendiente: Config en Firebase  ║
║                                    ║
║  Próximo: Configurar en Console   ║
╚════════════════════════════════════╝
```

---

**Fecha:** 18 de Diciembre, 2024
**Estado:** ✅ **CÓDIGO LISTO - CONFIGURAR FIREBASE**
**Siguiente:** Configurar parámetros en Firebase Console

