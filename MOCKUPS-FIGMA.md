# 🎨 MOCKUPS PARA FIGMA - HireTree Mobile

## 📋 Especificaciones Generales

**Resolución:** 1080 x 2400 px (Android estándar)
**Proporción:** 9:19.5
**DPI:** 420 (xxhdpi)
**Safe Area:** Márgenes de 24dp a cada lado

---

## 🎨 Paleta de Colores

```
Primary (Azul Profesional):    #1E88E5
Primary Dark:                   #1565C0
Primary Light:                  #42A5F5

Secondary (Verde Éxito):        #43A047
Secondary Dark:                 #2E7D32
Secondary Light:                #66BB6A

Accent (Naranja Energía):       #FFA726
Accent Dark:                    #F57C00
Accent Light:                   #FFB74D

Background:                     #F5F5F5
Surface:                        #FFFFFF
Error:                          #E53935

Text Primary:                   #212121
Text Secondary:                 #757575
Text Hint:                      #BDBDBD

Gradient Start:                 #1E88E5
Gradient End:                   #43A047
```

---

## 🖼️ PANTALLA 1: Splash Screen

### Elementos:
- Logo de HireTree (centrado)
- Tagline: "Tu Entrenador de Entrevistas con IA"
- Loading indicator (circular, color Primary)
- Fondo: Gradient vertical (Primary → Secondary)

### Dimensiones:
- Logo: 200x200 dp
- Tagline: 16sp, Text Secondary
- Centrado vertical y horizontal

### Animación sugerida:
- Logo aparece con fade-in (500ms)
- Tagline aparece con slide-up (300ms)

---

## 🖼️ PANTALLA 2: Login / Registro

### Variante A: Login

**Elementos superiores:**
- Logo pequeño (80x80 dp) - top: 48dp
- Título: "Bienvenido de nuevo" (24sp, Bold, Text Primary)
- Subtítulo: "Inicia sesión para continuar" (14sp, Text Secondary)

**Formulario:**
- Campo Email:
  * Label: "Correo electrónico"
  * Icon: 📧 (leading)
  * Placeholder: "ejemplo@email.com"
  * Border radius: 12dp
  
- Campo Password:
  * Label: "Contraseña"
  * Icon: 🔒 (leading)
  * Icon: 👁️ (trailing - toggle visibility)
  * Placeholder: "••••••••"
  * Border radius: 12dp

**Botones:**
- Botón "Iniciar Sesión":
  * Background: Primary
  * Text: Blanco, 16sp, Bold
  * Padding: 16dp vertical
  * Border radius: 24dp
  * Ancho: 100%
  
- Link "¿Olvidaste tu contraseña?":
  * Color: Primary
  * 14sp, Underline
  * Centrado

**Footer:**
- "¿No tienes cuenta? Regístrate"
  * "Regístrate" en color Primary y Bold
  
### Variante B: Registro

**Elementos superiores:**
- Logo pequeño (80x80 dp)
- Título: "Crear cuenta" (24sp, Bold)
- Subtítulo: "Comienza tu viaje profesional"

**Formulario:**
- Campo Nombre completo
- Campo Email
- Campo Password
- Campo Confirmar Password
- Checkbox: "Acepto términos y condiciones"

**Botón:**
- "Crear cuenta" (mismo estilo que Login)

**Footer:**
- "¿Ya tienes cuenta? Inicia sesión"

---

## 🖼️ PANTALLA 3: Home Screen

### Header:
- Avatar circular (48x48 dp) - top right
- Saludo: "¡Hola, [Nombre]!" (20sp, Bold)
- Fecha actual: "Lunes, 18 de Diciembre 2024" (14sp, Secondary)

### Card Principal (Iniciar Entrevista):
```
┌─────────────────────────────────┐
│  🎯                             │
│  ¿Listo para tu próxima         │
│  entrevista?                    │
│                                 │
│  Practica y mejora tus soft     │
│  skills con IA                  │
│                                 │
│  [Iniciar Entrevista →]         │
│                                 │
└─────────────────────────────────┘
```
- Background: Gradient (Primary → Secondary)
- Text: Blanco
- Padding: 24dp
- Border radius: 16dp
- Elevation: 4dp
- Botón: Surface blanco, texto Primary

### Sección Estadísticas:
```
┌──────────┐  ┌──────────┐  ┌──────────┐
│   12     │  │   85%    │  │   8      │
│Entrevistas│ │Promedio │  │Certificados│
└──────────┘  └──────────┘  └──────────┘
```
- 3 cards horizontales
- Background: Surface
- Border radius: 12dp
- Icon arriba, número grande (24sp), label abajo (12sp)

### Sección Últimas Entrevistas:
- Título: "Últimas evaluaciones" (18sp, Bold)
- Lista de 3 items:
  ```
  ┌─────────────────────────────────┐
  │ 📊 Desarrollador Web            │
  │ 15 de Diciembre • 88/100        │
  └─────────────────────────────────┘
  ```
- Background: Surface
- Padding: 16dp
- Border radius: 12dp
- Spacing: 12dp entre items

### Bottom Navigation:
- 4 items: Home, Historial, Perfil, Ajustes
- Active: Primary color
- Inactive: Text Secondary

---

## 🖼️ PANTALLA 4: Chat de Entrevista

### Header:
- Back button (←) - leading
- "Entrevista en curso" (18sp, Bold) - center
- Menu (⋮) - trailing
- Progress bar: "Pregunta 3 de 10" (debajo del título)
  * Progress: Primary color
  * Background: #E0E0E0

### Chat Area:
**Mensaje de IA (izquierda):**
```
┌────────────────────────┐
│ 🤖                     │
│ ¿Cuál es tu profesión  │
│ actual?                │
│                        │
│ 10:23 AM               │
└────────────────────────┘
```
- Background: #F0F0F0
- Border radius: 16dp (esquina inferior izq: 4dp)
- Max width: 75%
- Padding: 12dp
- Align: Start

**Mensaje de Usuario (derecha):**
```
        ┌────────────────────────┐
        │ Soy desarrollador web  │
        │ con 2 años de          │
        │ experiencia            │
        │                        │
        │               10:23 AM │
        └────────────────────────┘
```
- Background: Primary
- Text color: Blanco
- Border radius: 16dp (esquina inferior der: 4dp)
- Max width: 75%
- Padding: 12dp
- Align: End

**Indicador "IA está escribiendo...":**
```
┌────────────────┐
│ 💭 •••         │
└────────────────┘
```
- Animación de puntos (...)
- Background: #F0F0F0
- Text: Secondary

### Input Area (fixed bottom):
```
┌────────────────────────────────────┐
│ [     Escribe tu respuesta...    ]│
│ [📎]                          [🎤] │
└────────────────────────────────────┘
```
- TextField:
  * Background: Surface
  * Border: 1dp, #E0E0E0
  * Border radius: 24dp
  * Padding: 12dp
  * Multiline: true
  * Max lines: 4

- Botón Enviar (cuando hay texto):
  * Background: Primary (circular)
  * Icon: ➤ (blanco)
  * Size: 48x48 dp

### Floating Action Button:
- "Finalizar Entrevista" (opcional)
- Position: bottom-end
- Color: Accent

---

## 🖼️ PANTALLA 5: Resultados de Entrevista

### Header:
- "¡Entrevista Completada!" (24sp, Bold)
- Confetti animation 🎉
- Fecha y hora de finalización

### Gráfico Circular (Radar Chart):
```
       Comunicación
            /\
           /  \
          /    \
    Adaptab. \  / Liderazgo
          \  \/  /
           \    /
            \  /
             \/
       Trabajo eq.  Resolución
```
- 5 ejes (uno por soft skill)
- Área rellena: Primary con 40% opacity
- Línea: Primary, 2dp width
- Puntos: Primary circles
- Labels: 14sp, Text Primary

### Cards de Puntuaciones:
```
┌─────────────────────────────────┐
│ 💬 Comunicación                 │
│ ████████████████░░░░   85/100   │
│ Excelente - Muy claro y preciso │
└─────────────────────────────────┘
```
- 5 cards (una por skill)
- Icon de la skill
- Nombre (16sp, Bold)
- Barra de progreso:
  * 0-60: Error color
  * 61-80: Accent color
  * 81-100: Secondary color
- Puntuación (18sp, Bold)
- Feedback breve (14sp, Secondary)

### Puntuación General:
```
┌─────────────────────────────────┐
│        PROMEDIO GENERAL         │
│                                 │
│            82/100               │
│          ⭐⭐⭐⭐☆              │
│                                 │
│        Muy Buen Desempeño       │
└─────────────────────────────────┘
```
- Background: Gradient (Primary → Secondary)
- Text: Blanco
- Centered
- Elevation: 8dp

### Botones de Acción:
```
[Ver Detalles]  [Generar Certificado]
```
- "Ver Detalles": Outline button (Secondary)
- "Generar Certificado": Filled button (Accent)
- Ancho: 48% cada uno
- Spacing: 4% entre ellos

---

## 🖼️ PANTALLA 6: Vista de Certificado

### Certificado (PDF Preview):
```
┌─────────────────────────────────────┐
│                                     │
│         🏆 CERTIFICADO 🏆           │
│                                     │
│      EVALUACIÓN DE SOFT SKILLS      │
│                                     │
│         Se certifica que            │
│                                     │
│          LEONARDO PÉREZ             │
│                                     │
│   Ha completado exitosamente una    │
│   evaluación de soft skills con     │
│      los siguientes resultados:     │
│                                     │
│   • Comunicación:        85/100     │
│   • Liderazgo:          75/100     │
│   • Trabajo en Equipo:   90/100     │
│   • Resolución de Prob.: 80/100     │
│   • Adaptabilidad:      70/100     │
│                                     │
│   PROMEDIO GENERAL: 82/100          │
│                                     │
│   Fecha: 18 de Diciembre 2024       │
│   ID: #HT-2024-12345                │
│                                     │
│   ─────────────────────             │
│   Firma Digital HireTree            │
│                                     │
└─────────────────────────────────────┘
```
- Tamaño: A4 ratio
- Background: Blanco con borde dorado
- Logo HireTree arriba
- Sello/watermark de fondo
- Código QR abajo (para verificación)

### Botones:
```
[Descargar PDF]  [Compartir en LinkedIn]
```
- "Descargar": Primary button
- "Compartir": Secondary button + LinkedIn icon

---

## 🖼️ PANTALLA 7: Historial de Entrevistas

### Filtros (Top):
```
[Todas] [Este Mes] [Este Año]
```
- Chip buttons
- Active: Primary background
- Inactive: Surface with border

### Lista de Entrevistas:
```
┌─────────────────────────────────┐
│ 📊 Desarrollador Web            │
│ 15 Diciembre 2024               │
│                                 │
│ 💬 85  👔 75  🤝 90  🧩 80  🔄 70│
│                                 │
│ Promedio: 82/100  [Ver →]       │
└─────────────────────────────────┘
```
- Card por cada entrevista
- Icons de cada soft skill con puntuación
- Color coding de promedio
- Swipe para eliminar (opcional)

### Empty State (si no hay):
```
┌─────────────────────────────────┐
│            🎯                   │
│                                 │
│   Aún no tienes entrevistas     │
│                                 │
│   [Iniciar tu Primera Entrevista]│
└─────────────────────────────────┘
```

---

## 🖼️ PANTALLA 8: Perfil de Usuario

### Header con Avatar:
```
        ┌───────────┐
        │    📸     │
        │ Avatar    │
        └───────────┘
    
        Leonardo Pérez
    desarrollador@email.com
```
- Avatar: 120x120 dp, circular
- Botón "Editar" (pequeño, esquina inferior derecha del avatar)
- Nombre: 20sp, Bold
- Email: 14sp, Secondary

### Estadísticas Personales:
```
┌──────────┐  ┌──────────┐  ┌──────────┐
│   28     │  │   92%    │  │  Top 5%  │
│Entrevistas│ │Aciertos  │  │  Ranking │
└──────────┘  └──────────┘  └──────────┘
```

### Secciones:
```
CONFIGURACIÓN
┌─────────────────────────────────┐
│ 🔔 Notificaciones          [>]  │
│ 🌐 Idioma                  [>]  │
│ 🎨 Tema                    [>]  │
└─────────────────────────────────┘

ACERCA DE
┌─────────────────────────────────┐
│ ℹ️ Términos y Condiciones   [>]  │
│ 🔒 Política de Privacidad   [>]  │
│ 📧 Contacto                 [>]  │
│ ⭐ Calificar App            [>]  │
└─────────────────────────────────┘

CUENTA
┌─────────────────────────────────┐
│ 🚪 Cerrar Sesión                │
│ 🗑️ Eliminar Cuenta              │
└─────────────────────────────────┘
```
- Cards con items clickeables
- Icon leading
- Arrow trailing
- Dividers entre items

### Versión:
```
HireTree Mobile v1.0.0
Powered by Google Gemini 2.0
```
- Bottom center
- 12sp, Text Hint

---

## 📱 Componentes Reutilizables

### Botón Primary:
- Height: 48dp
- Border radius: 24dp
- Background: Primary
- Text: Blanco, 16sp, Bold
- Ripple effect: Blanco con 20% opacity

### Botón Secondary (Outline):
- Height: 48dp
- Border: 2dp, Primary
- Background: Transparent
- Text: Primary, 16sp, Bold
- Border radius: 24dp

### Card Estándar:
- Background: Surface
- Border radius: 12dp
- Elevation: 2dp
- Padding: 16dp

### TextField Estándar:
- Height: 56dp
- Border radius: 12dp
- Border: 1dp, #E0E0E0
- Focus border: 2dp, Primary
- Padding: 12dp horizontal

---

## 🎯 Elementos de Marca

### Logo Principal:
- Incluye árbol estilizado + texto "HireTree"
- Colores: Primary y Secondary en gradient
- Versión light y dark

### Iconografía:
- Material Icons (Google)
- Tamaño: 24dp (default)
- Color: Adapta al contexto

### Tipografía:
- Font Family: Roboto (Android default)
- Títulos: Roboto Bold
- Body: Roboto Regular
- Captions: Roboto Light

---

## ✅ Checklist de Mockups

- [ ] Splash Screen
- [ ] Login Screen
- [ ] Registro Screen
- [ ] Home Screen
- [ ] Chat de Entrevista (con mensajes)
- [ ] Resultados con gráfico
- [ ] Vista de Certificado
- [ ] Historial de Entrevistas
- [ ] Perfil de Usuario
- [ ] Estados de error
- [ ] Estados de loading
- [ ] Empty states

---

## 🔗 Links Útiles para Figma

**Plantillas recomendadas:**
- [Android UI Kit](https://www.figma.com/community/file/android-ui-kit)
- [Material Design 3](https://www.figma.com/community/file/material-3)
- [Chat UI Kit](https://www.figma.com/community/file/chat-ui)

**Plugins útiles:**
- Unsplash (fotos)
- Iconify (icons)
- Chart (gráficos)
- Lorem ipsum (texto placeholder)

---

**Proyecto en Figma listo para diseñar! 🎨**

Link del proyecto: [Agregar aquí cuando esté creado]

