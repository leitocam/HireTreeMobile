# 🔧 Pasos para Ejecutar Hire Tree (GUÍA RÁPIDA)

## ⚠️ Tu problema: "No me deja correr el app"

### Diagnóstico:
Has configurado correctamente:
- ✅ Firebase (`google-services.json`)
- ✅ Gemini API key (`local.properties`)

**Pero las dependencias aún no se han sincronizado en Android Studio.**

---

## 🚀 SOLUCIÓN (sigue estos pasos EXACTAMENTE):

### Paso 1: Cerrar Android Studio
1. Si Android Studio está abierto, **ciérralo completamente**
2. Asegúrate de que no haya procesos de Gradle en ejecución:
   - Abre Task Manager (Ctrl + Shift + Esc)
   - Busca procesos "java.exe" o "gradle"
   - Termínalos si existen

### Paso 2: Ejecutar el script de sincronización
1. Abre el Explorador de Windows
2. Navega a: `C:\Users\ASUS\AndroidStudioProjects\Hire-Tree`
3. Doble clic en: **`sync-project.bat`**
4. **ESPERA** a que termine (puede tardar 5-10 minutos)
5. Debes ver: "SINCRONIZACION COMPLETADA EXITOSAMENTE"

### Paso 3: Abrir Android Studio
1. Abre Android Studio
2. Abre el proyecto `Hire-Tree`
3. **ESPERA** a que termine la indexación (barra de progreso abajo)
4. Ve a: **File → Invalidate Caches / Restart...**
5. Selecciona: **"Invalidate and Restart"**
6. **ESPERA** a que reinicie (2-3 minutos)

### Paso 4: Sincronizar en Android Studio
1. Una vez que Android Studio reinicie
2. Ve a: **File → Sync Project with Gradle Files**
3. **ESPERA** a que termine la sincronización (5-10 minutos)
4. Verás en la parte inferior: "Gradle sync finished" o "BUILD SUCCESSFUL"

### Paso 5: Verificar que no haya errores
1. Ve al panel "Problems" (abajo)
2. Si ves errores de Firebase:
   - Repite el Paso 4
3. Si no hay errores, **¡estás listo!**

### Paso 6: Ejecutar la app
1. Conecta un dispositivo Android O inicia un emulador
2. Haz clic en el botón **Run** (▶️) o presiona `Shift + F10`
3. Selecciona tu dispositivo
4. **ESPERA** a que compile e instale (primera vez puede tardar 5-10 minutos)

---

## ✅ Si todo salió bien:

Deberías ver:
1. La app se instala en tu dispositivo/emulador
2. Se abre la pantalla de **Login**
3. Puedes crear una cuenta nueva
4. Después de registrarte, llegas a la pantalla **Home**

---

## ❌ Si siguen los errores:

### Error: "Unresolved reference FirebaseAuth"

**Causa:** Las dependencias no se descargaron.

**Solución:**
1. Elimina la carpeta: `C:\Users\ASUS\.gradle\caches`
2. Reinicia Android Studio
3. Repite desde el Paso 2

### Error: "google-services.json is missing"

**Causa:** El archivo no está en el lugar correcto.

**Solución:**
1. Verifica que el archivo esté en: `app\google-services.json`
2. NO debe estar en la raíz del proyecto

### Error: "Plugin com.google.gms.google-services was not found"

**Causa:** El plugin no está aplicado correctamente.

**Solución:**
1. Abre: `build.gradle.kts` (raíz del proyecto)
2. Verifica que tenga:
   ```kotlin
   plugins {
       alias(libs.plugins.google.gms.google.services) apply false
   }
   ```

### Error: "Cannot resolve symbol BuildConfig"

**Causa:** El proyecto no se ha compilado aún.

**Solución:**
1. Ve a: **Build → Clean Project**
2. Luego: **Build → Rebuild Project**
3. Espera a que termine

---

## 🎯 Comando rápido para verificar:

Abre PowerShell en la carpeta del proyecto y ejecuta:

```powershell
.\gradlew.bat dependencies --configuration implementation | Select-String "firebase"
```

Deberías ver líneas que incluyan:
- `firebase-auth`
- `firebase-firestore`
- `firebase-storage`

Si NO ves esas líneas, las dependencias no se descargaron.

---

## 📞 Última opción (si nada funciona):

1. Descarga la carpeta `.gradle` limpia:
   - Elimina: `C:\Users\ASUS\.gradle`
   
2. Elimina en el proyecto:
   - Carpeta `.gradle` (en la raíz del proyecto)
   - Carpeta `.idea`
   - Carpeta `build` (en raíz)
   - Carpeta `app/build`

3. Reinicia Android Studio

4. Repite desde el Paso 2

---

## ⏱️ Tiempos normales de espera:

- Primera sincronización: **10-15 minutos**
- Invalidate Caches: **2-3 minutos**
- Primera compilación: **5-10 minutos**
- Compilaciones posteriores: **30 segundos - 2 minutos**

**¡Ten paciencia! La primera vez siempre tarda más.** 🕐

---

## 🆘 Si después de todo esto sigue sin funcionar:

Copia y pega el error EXACTO que aparece en Android Studio y envíalo para ayudarte mejor.

---

**¿Todo funcionó?** ¡Perfecto! Ahora puedes continuar con la Fase 4 para implementar el chat con IA. 🎉

