# Solución de Problemas - Hire Tree

## El proyecto no compila después de configurar Firebase

### Problema
Las dependencias de Firebase no se reconocen y aparecen errores como:
- `Unresolved reference 'FirebaseAuth'`
- `Unresolved reference 'FirebaseFirestore'`
- `Cannot infer type for this parameter`

### Solución

#### Opción 1: Sync desde Android Studio (RECOMENDADO)

1. Abre Android Studio
2. Ve a **File → Invalidate Caches / Restart...**
3. Selecciona **"Invalidate and Restart"**
4. Espera a que Android Studio reinicie
5. Una vez reiniciado, ve a **File → Sync Project with Gradle Files**
6. Espera a que termine la sincronización (puede tardar varios minutos)

#### Opción 2: Clean y Rebuild

1. Ve a **Build → Clean Project**
2. Espera a que termine
3. Ve a **Build → Rebuild Project**
4. Espera a que termine (puede tardar varios minutos)

#### Opción 3: Desde Terminal

Abre un terminal en la raíz del proyecto y ejecuta:

```powershell
# En PowerShell
.\gradlew clean
.\gradlew build --refresh-dependencies
```

O ejecuta el archivo batch:
```powershell
.\sync-project.bat
```

### Verificación de configuración

#### 1. Verifica google-services.json

Asegúrate de que el archivo `app/google-services.json` contenga:
- Tu `project_id` real de Firebase
- Tu `mobilesdk_app_id` real
- El `package_name` debe ser: `com.calyrsoft.ucbp1`

#### 2. Verifica local.properties

El archivo debe contener:
```properties
GEMINI_API_KEY=TU_API_KEY_REAL
```

#### 3. Verifica la conexión a Internet

Las dependencias de Firebase y Gemini se descargan desde Internet. Asegúrate de:
- Tener conexión a Internet activa
- No estar detrás de un proxy corporativo que bloquee Maven Central o Google Maven

### Si los errores persisten

#### Eliminar cache de Gradle:

1. Cierra Android Studio
2. Navega a: `C:\Users\ASUS\.gradle\caches`
3. Elimina la carpeta `caches`
4. Abre Android Studio nuevamente
5. Espera a que sincronice (descargará todo nuevamente)

#### Verificar versión de Android Studio

Asegúrate de tener Android Studio Hedgehog (2023.1.1) o superior.

#### Verificar Java/JDK

El proyecto requiere Java 11. Verifica en Android Studio:
- **File → Project Structure → SDK Location**
- Debe mostrar JDK 11 o superior

### Errores comunes y soluciones

#### Error: "Plugin [id: 'com.google.gms.google-services'] was not found"

**Solución:** Verifica que el plugin esté en el archivo raíz `build.gradle.kts`:
```kotlin
plugins {
    alias(libs.plugins.google.gms.google.services) apply false
}
```

#### Error: "google-services.json is missing"

**Solución:** Asegúrate de que el archivo esté en: `app/google-services.json` (no en la raíz del proyecto)

#### Error: "GEMINI_API_KEY not found"

**Solución:** Verifica que en `local.properties` la línea sea exactamente:
```properties
GEMINI_API_KEY=tu_api_key_sin_comillas
```

### Compilación exitosa

Cuando todo esté bien, deberías ver:
```
BUILD SUCCESSFUL in Xs
```

Y deberías poder ejecutar la app en un emulador o dispositivo.

### Próximos pasos después de compilar

1. Ejecuta la app
2. Prueba el registro de usuario
3. Prueba el login
4. Verifica que llegues a la pantalla Home

Si todo funciona, ¡estás listo para continuar con la Fase 4! 🚀

