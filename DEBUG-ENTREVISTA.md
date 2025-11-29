# 🐛 DEBUG - Entrevista No Funciona

## 🔍 Cambios Aplicados para Debug:

### 1. **Logs Agregados** ✅

He agregado logs detallados en:
- ✅ `InterviewViewModel` - startInterview y sendMessage
- ✅ `InterviewScreen` - LaunchedEffect y botón de enviar
- ✅ `GeminiService` - Respuestas de IA

### 2. **Código Simplificado** ✅

He simplificado `GeminiService` para que:
- ✅ No use `suspend` en `startInterview()`
- ✅ Mejore manejo de errores
- ✅ Retorne mensajes de error más descriptivos

---

## 🧪 Pasos para Debug:

### Paso 1: Sync y Clean
```
1. File → Sync Project with Gradle Files
2. Build → Clean Project
3. Build → Rebuild Project
```

### Paso 2: Ejecutar con Logcat Abierto
```
1. Abre Logcat (parte inferior de Android Studio)
2. Filtra por: "Interview"
3. Run ▶️
4. Observa los logs
```

### Paso 3: Probar la Entrevista

1. **Login**
2. **Home → Iniciar Entrevista**
3. **Observa Logcat**

---

## 📊 Logs Esperados:

### Al cargar InterviewScreen:
```
D/InterviewScreen: LaunchedEffect triggered
D/InterviewScreen: Auth user: [uid del usuario]
D/InterviewScreen: Current sessionId: null
D/InterviewScreen: User ID found: [uid]
D/InterviewScreen: Starting interview...
D/InterviewViewModel: startInterview called with userId: [uid]
D/InterviewViewModel: Interview started successfully. Session: [sessionId], Messages: 1
```

### Al escribir mensaje:
```
D/InterviewScreen: Input changed: Hola
D/InterviewScreen: Input changed: Hola, soy...
```

### Al enviar mensaje:
```
D/InterviewScreen: Send button clicked. Input: Hola, soy Carlos
D/InterviewScreen: Sending message...
D/InterviewViewModel: sendMessage called with message: Hola, soy Carlos
D/InterviewViewModel: User message added. Total messages: 2
D/InterviewViewModel: AI response received: [respuesta de Gemini]
```

---

## ❌ Posibles Errores y Soluciones:

### Error 1: "User ID is null!"
**Causa:** El usuario no está autenticado correctamente

**Solución:**
1. Cierra la app completamente
2. Vuelve a hacer login
3. Intenta de nuevo

---

### Error 2: No aparece "Interview started successfully"
**Causa:** Firebase o Gemini API falló

**Soluciones:**
1. **Verifica Firebase:**
   - Firestore debe estar habilitado
   - Reglas de seguridad correctas

2. **Verifica Gemini API:**
   - API Key correcta en `local.properties`
   - No has excedido el límite de requests

3. **Verifica conexión:**
   ```
   Chequea que tengas Internet activo
   ```

---

### Error 3: "Session already exists"
**Causa:** El ViewModel mantiene estado de una sesión anterior

**Solución:**
```
Cierra la app y abre de nuevo
```

---

### Error 4: Botón no responde
**Causa:** Estado `isAiTyping` o `isLoading` está en true

**Busca en logs:**
```
D/InterviewScreen: Input is blank, not sending
```

**O verifica estado:**
- `enabled = !uiState.isLoading && !uiState.isAiTyping`

---

## 🔧 Verificaciones Adicionales:

### 1. Verifica que Gemini API Key esté configurada
```
Abre: local.properties
Busca: GEMINI_API_KEY=AIzaSy...
```

### 2. Verifica que Firebase esté configurado
```
Archivo: app/google-services.json
Debe contener tu project_id real
```

### 3. Verifica permisos de Internet
```
AndroidManifest.xml debe tener:
<uses-permission android:name="android.permission.INTERNET"/>
```

---

## 📝 Información para Reportar:

Si sigue sin funcionar, copia y envía:

### 1. **Logs Completos de Logcat** (filtrado por "Interview")
```
Copia TODO lo que aparezca con el tag "Interview"
```

### 2. **Logs de Error** (si hay)
```
Busca líneas en rojo (ERROR) en Logcat
```

### 3. **Estado de la UI**
- ¿Aparece pantalla de carga?
- ¿Aparece algún mensaje de error en pantalla?
- ¿El campo de texto está habilitado?
- ¿El botón de enviar está habilitado?

---

## 🎯 Checklist de Verificación:

Antes de reportar, verifica:

- [ ] Proyecto sincronizado (File → Sync)
- [ ] Clean + Rebuild ejecutado
- [ ] Logcat abierto y filtrado
- [ ] Usuario está logueado correctamente
- [ ] Gemini API Key configurada
- [ ] Firebase configurado
- [ ] Internet activo
- [ ] App ejecutándose sin crashes

---

## 🚀 Prueba Rápida:

```kotlin
// Si ves esto en Logcat, todo está bien:
D/InterviewViewModel: Interview started successfully
D/InterviewScreen: Send button clicked
D/InterviewViewModel: AI response received

// Si NO ves los logs, hay un problema de inicialización
```

---

## 📞 Siguiente Paso:

1. **Ejecuta la app con Logcat abierto**
2. **Navega a la entrevista**
3. **Copia TODOS los logs que digan "Interview"**
4. **Envíalos para análisis**

Los logs me dirán exactamente dónde está el problema.

---

**Archivos modificados para debug:**
- ✅ `GeminiService.kt`
- ✅ `InterviewViewModel.kt`
- ✅ `InterviewScreen.kt`

