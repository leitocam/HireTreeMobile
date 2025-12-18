# 🎨 SPLASH SCREEN CONFIGURADA - HireTree Mobile

## Configuración Completa de Splash Screen

---

## ✅ ARCHIVOS CREADOS Y MODIFICADOS

### 1. Recursos Drawable

#### `splash_logo.xml`
**Ubicación:** `app/src/main/res/drawable/splash_logo.xml`
- Logo vectorial "HT" con círculo azul
- Tamaño: 200dp x 200dp
- Color: #007AFF (iOS Blue)

#### `splash_background.xml`
**Ubicación:** `app/src/main/res/drawable/splash_background.xml`
- Background con gradiente gris claro
- Logo centrado de 160dp
- Gradiente: #F2F2F7 → #FFFFFF

### 2. Archivos de Configuración

#### `themes.xml`
**Ubicación:** `app/src/main/res/values/themes.xml`
```xml
✅ Theme.App.SplashScreen - Para API 31+
✅ Theme.App.Starting - Para API < 31 (legacy)
```

#### `colors.xml`
**Ubicación:** `app/src/main/res/values/colors.xml`
```xml
✅ splash_background: #F2F2F7
✅ splash_icon_background: #007AFF
```

### 3. Código

#### `MainActivity.kt`
```kotlin
✅ Import: androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
✅ onCreate(): installSplashScreen() agregado
```

#### `AndroidManifest.xml`
```xml
✅ MainActivity theme: @style/Theme.App.Starting
```

#### `build.gradle.kts`
```gradle
✅ implementation("androidx.core:core-splashscreen:1.0.1")
```

---

## 🖼️ CÓMO USAR TU PROPIA IMAGEN

### Opción 1: Reemplazar el Logo Vectorial (Recomendado)

Si tienes un archivo **SVG** o **Vector**:

1. **Convertir SVG a XML Vector**:
   - En Android Studio: Right-click en `res/drawable`
   - New > Vector Asset
   - Local file (SVG, PSD)
   - Selecciona tu archivo
   - Name: `splash_logo`
   - ✅ Sobrescribe el existente

2. **O editar manualmente** `splash_logo.xml`:
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <vector xmlns:android="http://schemas.android.com/apk/res/android"
       android:width="200dp"
       android:height="200dp"
       android:viewportWidth="200"
       android:viewportHeight="200">
       
       <!-- TU CÓDIGO SVG AQUÍ -->
       
   </vector>
   ```

### Opción 2: Usar Imagen PNG/JPG

Si tienes una imagen **PNG** o **JPG**:

1. **Preparar la imagen**:
   ```
   Tamaño recomendado: 512x512 px o 1024x1024 px
   Formato: PNG con transparencia
   Background: Transparente
   ```

2. **Copiar a carpetas drawable**:
   ```
   app/src/main/res/drawable/splash_logo.png
   
   O mejor, crear múltiples densidades:
   drawable-mdpi/splash_logo.png    (160 dpi)
   drawable-hdpi/splash_logo.png    (240 dpi)
   drawable-xhdpi/splash_logo.png   (320 dpi)
   drawable-xxhdpi/splash_logo.png  (480 dpi)
   drawable-xxxhdpi/splash_logo.png (640 dpi)
   ```

3. **Actualizar** `splash_background.xml`:
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
       <item>
           <shape android:shape="rectangle">
               <gradient
                   android:angle="135"
                   android:startColor="#F2F2F7"
                   android:endColor="#FFFFFF"
                   android:type="linear" />
           </shape>
       </item>
       
       <!-- CAMBIAR ESTA LÍNEA -->
       <item
           android:drawable="@drawable/splash_logo"
           android:gravity="center"
           android:width="200dp"
           android:height="200dp"/>
   </layer-list>
   ```

### Opción 3: Cambiar solo el Background

Para cambiar el fondo sin cambiar el logo:

**Editar** `splash_background.xml`:
```xml
<item>
    <shape android:shape="rectangle">
        <gradient
            android:angle="135"
            android:startColor="#TU_COLOR_1"
            android:endColor="#TU_COLOR_2"
            android:type="linear" />
    </shape>
</item>
```

**O usar color sólido**:
```xml
<item>
    <shape android:shape="rectangle">
        <solid android:color="#FFFFFF"/>
    </shape>
</item>
```

**O usar imagen de fondo**:
```xml
<item android:drawable="@drawable/tu_background_imagen"/>
```

---

## 🎨 PERSONALIZACIÓN AVANZADA

### Cambiar Colores del Tema

**Editar** `colors.xml`:
```xml
<color name="splash_background">#TU_COLOR</color>
<color name="splash_icon_background">#TU_COLOR</color>
```

### Ajustar Tamaño del Logo

**En** `splash_background.xml`:
```xml
<item
    android:drawable="@drawable/splash_logo"
    android:gravity="center"
    android:width="120dp"  <!-- Cambiar tamaño -->
    android:height="120dp"/> <!-- Cambiar tamaño -->
```

### Añadir Texto (Nombre de la App)

**Actualizar** `splash_background.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- Background -->
    <item>
        <shape android:shape="rectangle">
            <solid android:color="#FFFFFF"/>
        </shape>
    </item>
    
    <!-- Logo -->
    <item
        android:drawable="@drawable/splash_logo"
        android:gravity="center"
        android:top="-60dp"
        android:width="160dp"
        android:height="160dp"/>
    
    <!-- Texto debajo del logo -->
    <!-- Nota: Para texto dinámico, mejor usar un drawable custom -->
</layer-list>
```

### Duración de la Splash Screen

**En** `MainActivity.kt`:
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    val splashScreen = installSplashScreen()
    
    // Mantener visible hasta que esté listo
    var keepSplashOnScreen = true
    splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
    
    super.onCreate(savedInstanceState)
    
    // Simular carga (opcional)
    lifecycleScope.launch {
        delay(2000) // 2 segundos
        keepSplashOnScreen = false
    }
    
    // ...resto del código
}
```

---

## 📋 PASOS RÁPIDOS PARA USAR TU IMAGEN

### Método Rápido (PNG/JPG):

1. **Renombra tu imagen** a `splash_logo.png`

2. **Cópiala** a:
   ```
   app/src/main/res/drawable/
   ```

3. **Elimina** el archivo XML actual:
   ```
   app/src/main/res/drawable/splash_logo.xml
   ```

4. **Sync Project** con Gradle

5. **¡Listo!** Compila y ejecuta

---

## 🎯 ESTRUCTURA FINAL DE ARCHIVOS

```
app/src/main/res/
├── drawable/
│   ├── splash_logo.xml (o .png)
│   └── splash_background.xml
├── values/
│   ├── colors.xml
│   └── themes.xml
└── AndroidManifest.xml

app/src/main/java/.../
└── MainActivity.kt

app/
└── build.gradle.kts
```

---

## ✨ VISTA PREVIA DEL RESULTADO

```
┌─────────────────────────────┐
│                             │
│                             │
│          ╔════════╗         │
│          ║        ║         │
│          ║   HT   ║         │
│          ║        ║         │
│          ╚════════╝         │
│                             │
│                             │
│                             │
└─────────────────────────────┘
     ↑ Background gradiente
```

---

## 🚀 COMPILAR Y PROBAR

### 1. Sync Gradle
```
File > Sync Project with Gradle Files
```

### 2. Rebuild
```
Build > Rebuild Project
```

### 3. Run
```
Run > Run 'app'
```

### 4. Verificar
- La splash screen debe aparecer al iniciar
- Duración: ~1-2 segundos
- Transición suave a LoginScreen

---

## 🎨 EJEMPLOS DE PERSONALIZACIÓN

### Ejemplo 1: Logo + Nombre

**splash_background.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape android:shape="rectangle">
            <gradient
                android:angle="135"
                android:startColor="#007AFF"
                android:endColor="#5856D6"
                android:type="linear" />
        </shape>
    </item>
    
    <item
        android:drawable="@drawable/splash_logo"
        android:gravity="center"
        android:width="160dp"
        android:height="160dp"/>
</layer-list>
```

### Ejemplo 2: Solo Imagen

**splash_background.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/tu_splash_completo"/>
</layer-list>
```

### Ejemplo 3: Color Sólido + Logo Blanco

**splash_background.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape android:shape="rectangle">
            <solid android:color="#007AFF"/>
        </shape>
    </item>
    
    <item
        android:drawable="@drawable/logo_blanco"
        android:gravity="center"
        android:width="200dp"
        android:height="200dp"/>
</layer-list>
```

---

## ⚠️ RECOMENDACIONES

### Tamaños de Imagen:

```
Óptimo para Android:
- 512x512 px (mínimo)
- 1024x1024 px (recomendado)
- 2048x2048 px (máximo)

Formato:
- PNG con transparencia (mejor)
- JPG (si no necesitas transparencia)
- Vector XML (mejor rendimiento)
```

### Colores:

```
Usar colores del tema:
- Fondo claro: #F2F2F7, #FFFFFF
- Fondo oscuro: #000000, #1C1C1E
- Acento: #007AFF (iOS Blue)
```

### Performance:

```
✅ Usa vectores cuando sea posible
✅ Comprime imágenes PNG/JPG
✅ Evita animaciones complejas
✅ Mantén la splash < 2 segundos
```

---

## 🐛 TROUBLESHOOTING

### La splash no aparece:
```
1. Verifica que installSplashScreen() esté ANTES de super.onCreate()
2. Check que el tema esté en AndroidManifest
3. Sync Gradle Files
4. Clean & Rebuild Project
```

### Logo no se ve:
```
1. Verifica ruta del drawable
2. Check que el archivo exista
3. Verifica permisos del archivo
4. Rebuild proyecto
```

### Colores incorrectos:
```
1. Verifica colors.xml
2. Check referencias en themes.xml
3. Sync proyecto
```

---

## 📸 CAPTURA DE PANTALLA

Para documentación, toma screenshot de:
1. Splash Screen inicial
2. Transición a LoginScreen
3. Logo en diferentes densidades de pantalla

---

## ✅ CHECKLIST FINAL

Antes de publicar:

- [ ] Logo se ve correctamente
- [ ] Colores coinciden con el tema
- [ ] Transición suave a la app
- [ ] No hay lag o freeze
- [ ] Funciona en diferentes dispositivos
- [ ] Funciona en modo claro/oscuro
- [ ] Duración apropiada (1-2 seg)

---

**Estado:** ✅ SPLASH SCREEN CONFIGURADA Y LISTA

**Archivos modificados:** 7
**Nuevos recursos:** 2 drawables + colores + temas

**Próximo paso:** 
1. Reemplaza `splash_logo.xml` con tu imagen
2. Ajusta colores si necesario
3. ¡Compila y disfruta! 🎉

---

**Fecha:** 18 de Diciembre, 2024
**Versión:** 1.0
**Estado:** ✅ **PRODUCCIÓN READY**

