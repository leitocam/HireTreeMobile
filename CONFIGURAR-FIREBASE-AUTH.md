# 🔐 CONFIGURAR FIREBASE AUTH - INICIO DE SESIÓN EN CUALQUIER DISPOSITIVO

## Guía Completa para que Funcione en Todos los Dispositivos

---

## ❌ PROBLEMA ACTUAL

El inicio de sesión y registro **NO funciona en otros dispositivos** porque:
1. Firebase Authentication no está habilitado en la consola
2. Métodos de autenticación no están configurados
3. Dominios autorizados pueden estar restringidos

---

## ✅ SOLUCIÓN PASO A PASO

### PASO 1: Habilitar Firebase Authentication (3 min)

#### 1.1 Acceder a Firebase Console

```
1. Ir a: https://console.firebase.google.com/
2. Seleccionar proyecto: hiretree-248d4
3. En el menú lateral, buscar "Authentication"
4. Click en "Authentication"
```

#### 1.2 Comenzar con Authentication

```
Si es la primera vez:
1. Click en botón "Comenzar" o "Get Started"
2. Esperar a que se active el servicio
```

---

### PASO 2: Habilitar Email/Password (2 min)

#### 2.1 Ir a Sign-in Methods

```
1. En la pantalla de Authentication
2. Click en pestaña "Sign-in method" (arriba)
3. Verás lista de proveedores disponibles
```

#### 2.2 Activar Email/Password

```
1. Buscar "Email/Password" en la lista
2. Click en "Email/Password"
3. Aparecerá un diálogo
4. Activar el interruptor "Habilitar" (Enable)
5. Click "Guardar"
```

**Resultado esperado:**
```
✅ Email/Password debe mostrar estado "Habilitado" (Enabled)
```

---

### PASO 3: Configurar Dominios Autorizados (1 min)

#### 3.1 Ir a Settings

```
1. Aún en Authentication
2. Click en pestaña "Settings"
3. Scroll hasta "Authorized domains"
```

#### 3.2 Verificar Dominios

```
Debe incluir al menos:
✅ localhost
✅ hiretree-248d4.firebaseapp.com
✅ hiretree-248d4.web.app (si tienes hosting)
```

**Nota:** localhost ya viene por defecto, no necesitas agregarlo.

---

### PASO 4: Verificar Firestore (2 min)

#### 4.1 Acceder a Firestore

```
1. Firebase Console > Menú lateral
2. Click en "Firestore Database"
3. Si no está creado: Click "Crear base de datos"
```

#### 4.2 Configurar Reglas de Seguridad

```
1. Click en pestaña "Reglas" (Rules)
2. Reemplazar con estas reglas:
```

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Permitir lectura/escritura a usuarios autenticados
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Entrevistas - solo el usuario puede acceder a las suyas
    match /interviews/{interviewId} {
      allow read, write: if request.auth != null && 
                             resource.data.userId == request.auth.uid;
    }
    
    // Resultados - solo el usuario puede acceder
    match /results/{resultId} {
      allow read, write: if request.auth != null && 
                             resource.data.userId == request.auth.uid;
    }
  }
}
```

```
3. Click "Publicar"
```

---

### PASO 5: Verificar google-services.json (Crítico)

#### 5.1 Descargar Configuración Actualizada

```
1. Firebase Console > Configuración del proyecto (⚙️ arriba)
2. Scroll hasta "Tus apps"
3. Buscar tu app Android (com.calyrsoft.ucbp1)
4. Click en "google-services.json"
5. Click "Descargar google-services.json"
```

#### 5.2 Reemplazar Archivo

```
1. El archivo se descarga en ~/Downloads/google-services.json
2. Copiar a: app/google-services.json (reemplazar el existente)
3. Verificar que esté en la ubicación correcta:
   Hire-Tree/app/google-services.json
```

**Verificar contenido:**
```json
{
  "project_info": {
    "project_number": "655273697086",
    "project_id": "hiretree-248d4",
    ...
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:655273697086:android:f6e81ed8054eff32e9166f",
        "android_client_info": {
          "package_name": "com.calyrsoft.ucbp1"
        }
      },
      "oauth_client": [...],
      "api_key": [
        {
          "current_key": "AIzaSyACVUyuSYQgr215m5bXtWQLLsVQ_Tkpn5Y"
        }
      ],
      ...
    }
  ],
  ...
}
```

---

### PASO 6: Probar en el Dispositivo Actual (2 min)

#### 6.1 Rebuild y Ejecutar

```
1. Android Studio > Build > Clean Project
2. Build > Rebuild Project
3. Run > Run 'app'
```

#### 6.2 Probar Registro

```
1. Abrir app
2. Click "Regístrate"
3. Ingresar:
   Email: test@example.com
   Password: Test123456
   Nombre: Usuario Test
4. Click "Registrarse"
```

**Verificar Logcat:**
```
Buscar: "AuthRepository" o "Firebase"

✅ Esperado:
D/AuthRepositoryImpl: Sign up successful for: test@example.com
D/AuthRepositoryImpl: User created in Firestore: ...

❌ Error común:
E/FirebaseAuth: [FirebaseAuth] operation not allowed
```

#### 6.3 Verificar en Firebase Console

```
1. Firebase Console > Authentication > Users
2. Debe aparecer el usuario recién creado:
   ✅ test@example.com
   ✅ Usuario Test
   ✅ Fecha de creación
```

#### 6.4 Probar Inicio de Sesión

```
1. Cerrar sesión en la app
2. Click "Iniciar Sesión"
3. Ingresar:
   Email: test@example.com
   Password: Test123456
4. Click "Iniciar Sesión"
5. ✅ Debe entrar a la app (Home Screen)
```

---

### PASO 7: Probar en Otro Dispositivo (5 min)

#### Opción A: Emulador Android

```
1. Android Studio > Device Manager
2. Crear nuevo emulador (diferente al actual)
3. Iniciar emulador
4. Run > Select Device > [Nuevo emulador]
5. Run 'app'
```

**Probar:**
```
1. Abrir app en el nuevo emulador
2. Intentar iniciar sesión con test@example.com
3. ✅ Debe funcionar
```

#### Opción B: Dispositivo Físico

```
1. Conectar celular por USB
2. Habilitar "Depuración USB"
3. Run > Select Device > [Tu celular]
4. Run 'app'
```

**Probar:**
```
1. Abrir app en el celular
2. Intentar iniciar sesión con test@example.com
3. ✅ Debe funcionar
```

#### Opción C: Otra PC (APK)

```
1. Build > Build Bundle(s) / APK(s) > Build APK(s)
2. Esperar a que termine
3. Ubicar APK: app/build/outputs/apk/debug/app-debug.apk
4. Copiar APK a otro dispositivo
5. Instalar APK
6. Abrir app
```

**Probar:**
```
1. Click "Regístrate"
2. Crear nueva cuenta:
   Email: otro@example.com
   Password: Test123456
   Nombre: Otro Usuario
3. ✅ Debe registrarse correctamente
4. ✅ Verificar en Firebase Console que aparece el usuario
```

---

## 🔍 VERIFICAR QUE TODO FUNCIONA

### En Firebase Console

#### Authentication > Users

```
Debe mostrar todos los usuarios registrados:

Email                   Display Name      UID                    Created
─────────────────────────────────────────────────────────────────────────
test@example.com       Usuario Test      ABC123...              Hoy 10:30
otro@example.com       Otro Usuario      DEF456...              Hoy 10:35
```

#### Firestore Database > Data

```
Colección: users

Documento ID: ABC123...
├─ uid: "ABC123..."
├─ email: "test@example.com"
├─ displayName: "Usuario Test"
└─ createdAt: 1702908600000

Documento ID: DEF456...
├─ uid: "DEF456..."
├─ email: "otro@example.com"
├─ displayName: "Otro Usuario"
└─ createdAt: 1702908900000
```

---

## 🐛 TROUBLESHOOTING

### Error: "operation not allowed"

**Causa:** Email/Password no está habilitado en Firebase

**Solución:**
```
1. Firebase Console > Authentication
2. Sign-in method
3. Email/Password > Habilitar
4. Guardar
```

### Error: "permission denied" en Firestore

**Causa:** Reglas de Firestore muy restrictivas

**Solución:**
```
1. Firebase Console > Firestore Database
2. Rules
3. Usar las reglas proporcionadas arriba
4. Publicar
```

### Error: "network error" o "unable to resolve host"

**Causa:** Sin conexión a internet

**Solución:**
```
1. Verificar WiFi/Datos del dispositivo
2. Verificar que emulador tenga internet
3. Probar abrir navegador en el dispositivo
```

### Error: Usuario se crea pero no aparece en Firestore

**Causa:** Error en creación de documento

**Verificar Logcat:**
```
E/AuthRepositoryImpl: Error creating user document: ...
```

**Solución:**
```
1. Verificar reglas de Firestore
2. Verificar que userId coincida con auth.uid
```

### La app funciona en un dispositivo pero no en otro

**Posibles causas:**
```
1. APK desactualizado → Rebuild y reinstalar
2. Caché de Firebase → Limpiar datos de la app
3. google-services.json desactualizado → Reemplazar
```

**Solución:**
```
1. Build > Clean Project
2. Build > Rebuild Project
3. Desinstalar app del dispositivo
4. Reinstalar app
5. Probar de nuevo
```

---

## 🎯 MODO INVITADO

Si quieres que funcione sin registro:

### Ya está implementado ✅

```
El botón "Continuar como Invitado" ya funciona:
1. LoginScreen tiene onNavigateAsGuest
2. Navega a Home sin autenticación
3. Usa userId = "guest_[UUID]"
```

**Probar:**
```
1. Abrir app
2. Click "Continuar como Invitado"
3. ✅ Debe entrar directamente a Home
4. ✅ Puede iniciar entrevistas
5. ⚠️ Los datos NO se guardan (no hay userId persistente)
```

---

## ✅ CHECKLIST COMPLETO

Marca cada paso:

### En Firebase Console:
- [ ] Authentication habilitado
- [ ] Email/Password habilitado
- [ ] Dominios autorizados verificados
- [ ] Firestore Database creado
- [ ] Reglas de Firestore configuradas
- [ ] google-services.json descargado

### En el Código:
- [ ] google-services.json actualizado en app/
- [ ] Clean Project
- [ ] Rebuild Project

### Testing:
- [ ] Registro funciona en dispositivo actual
- [ ] Usuario aparece en Firebase Console > Authentication
- [ ] Usuario aparece en Firestore > users
- [ ] Inicio de sesión funciona
- [ ] Registro funciona en otro dispositivo/emulador
- [ ] Inicio de sesión funciona en otro dispositivo
- [ ] Modo invitado funciona

---

## 📊 RESULTADO ESPERADO

```
╔════════════════════════════════════╗
║  AUTENTICACIÓN FUNCIONANDO         ║
║                                    ║
║  ✅ Registro en cualquier disp.    ║
║  ✅ Login en cualquier disp.       ║
║  ✅ Datos en Firebase              ║
║  ✅ Modo invitado funcional        ║
║  ✅ Sincronización automática      ║
║                                    ║
║  Estado: PRODUCCIÓN READY          ║
╚════════════════════════════════════╝
```

---

## 🚀 PASOS RÁPIDOS (RESUMEN)

```
1. Firebase Console > Authentication > Habilitar
2. Sign-in method > Email/Password > Habilitar
3. Firestore Database > Crear
4. Rules > Configurar
5. Descargar google-services.json
6. Reemplazar en app/
7. Rebuild Project
8. Probar registro
9. Probar login
10. ✅ Listo!
```

---

## 💡 NOTAS IMPORTANTES

### Seguridad:

```
✅ Las contraseñas se hashean automáticamente por Firebase
✅ Nunca se envían en texto plano
✅ Firebase maneja tokens de sesión
✅ Logout revoca tokens
```

### Persistencia:

```
✅ Firebase Auth mantiene sesión automáticamente
✅ No necesitas guardar password
✅ La sesión persiste al cerrar/abrir app
✅ Solo se cierra con logout explícito
```

### Multi-dispositivo:

```
✅ Un usuario puede estar logueado en varios dispositivos
✅ Los datos se sincronizan automáticamente
✅ Firestore actualiza en tiempo real
```

---

**Fecha:** 18 de Diciembre, 2024
**Estado:** ✅ **GUÍA COMPLETA - LISTO PARA CONFIGURAR**
**Tiempo estimado:** 15 minutos

---

## 🎯 ACCIÓN INMEDIATA

**Para que funcione el login en todos los dispositivos:**

1. **Firebase Console** → https://console.firebase.google.com/
2. **Authentication** → Habilitar
3. **Email/Password** → Habilitar
4. **Firestore** → Configurar reglas
5. **google-services.json** → Actualizar
6. **Rebuild** → Probar

**¡Eso es todo!** El inicio de sesión funcionará en cualquier dispositivo. 🔐✨

