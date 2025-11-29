# Hire Tree - Setup Instructions

## Configuración Inicial

### 1. Configurar Firebase

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Crea un nuevo proyecto o usa uno existente
3. Agrega una aplicación Android con el package name: `com.calyrsoft.ucbp1`
4. Descarga el archivo `google-services.json`
5. Reemplaza el archivo `app/google-services.json` con el que descargaste

#### Habilitar servicios en Firebase:

- **Authentication**: Habilita "Email/Password" en la sección de Authentication
- **Firestore Database**: Crea una base de datos en modo test
- **Storage**: Habilita Firebase Storage

#### Reglas de Firestore (Security Rules):

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    match /certificates/{certificateId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == resource.data.userId;
    }
  }
}
```

### 2. Configurar API Key de Gemini

1. Ve a [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Crea una API key gratuita para Gemini
3. Abre el archivo `local.properties` en la raíz del proyecto
4. Reemplaza `YOUR_API_KEY_HERE` con tu API key:

```properties
GEMINI_API_KEY=tu_api_key_aqui
```

### 3. Sincronizar el proyecto

1. Abre el proyecto en Android Studio
2. Espera a que se sincronice automáticamente, o ejecuta: **File → Sync Project with Gradle Files**
3. Si hay errores, ejecuta: **Build → Clean Project** y luego **Build → Rebuild Project**

### 4. Ejecutar la aplicación

1. Conecta un dispositivo Android o inicia un emulador
2. Presiona el botón **Run** en Android Studio
3. La aplicación debe compilar e instalarse correctamente

## Estructura del Proyecto

```
features/
├── auth/               # Autenticación (Login/Registro)
│   ├── data/
│   ├── domain/
│   └── presentation/
├── home/               # Pantalla principal
│   └── presentation/
├── interview/          # Chat de entrevista con IA (próximamente)
├── certificate/        # Generación de certificados (próximamente)
└── history/            # Historial de certificados (próximamente)
```

## Próximos pasos

1. ✅ Configuración de Firebase
2. ✅ Sistema de autenticación (Login/Registro)
3. ✅ Pantalla Home
4. 🔄 Integración con Gemini API para entrevistas
5. ⏳ Sistema de evaluación de soft skills
6. ⏳ Generación de certificados en PDF
7. ⏳ Historial de certificados

## Dependencias Principales

- **Jetpack Compose**: UI moderna para Android
- **Firebase Auth**: Autenticación de usuarios
- **Firebase Firestore**: Base de datos NoSQL
- **Firebase Storage**: Almacenamiento de archivos
- **Google Gemini AI**: API de IA conversacional
- **Koin**: Inyección de dependencias
- **Retrofit**: Cliente HTTP
- **Room**: Base de datos local

## Soporte

Si tienes problemas durante la configuración:

1. Verifica que el archivo `google-services.json` esté correctamente configurado
2. Asegúrate de tener conexión a internet
3. Verifica que la API key de Gemini sea válida
4. Limpia y recompila el proyecto

