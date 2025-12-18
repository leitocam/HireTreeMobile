# 🧹 LIMPIEZA AUTOMÁTICA - HIRETREE
# Este script elimina todas las features que NO son de HireTree

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "   LIMPIEZA DE PROYECTO - HIRETREE" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Cambiar al directorio de features
$featuresPath = "app\src\main\java\com\calyrsoft\ucbp1\features"
Set-Location $featuresPath

Write-Host "[1/3] Eliminando features NO relacionadas con HireTree..." -ForegroundColor Yellow
Write-Host ""

# Features a ELIMINAR
$featuresToDelete = @(
    "cardexample",
    "dollar",
    "github",
    "movie",
    "webview"
)

foreach ($feature in $featuresToDelete) {
    if (Test-Path $feature) {
        Write-Host "  ❌ Eliminando: $feature" -ForegroundColor Red
        Remove-Item -Recurse -Force $feature
    } else {
        Write-Host "  ⚠️  No existe: $feature" -ForegroundColor DarkGray
    }
}

Write-Host ""
Write-Host "[2/3] Features MANTENIDAS (HireTree):" -ForegroundColor Green
Write-Host "  ✅ auth       - Autenticación" -ForegroundColor Green
Write-Host "  ✅ login      - Login/Registro" -ForegroundColor Green
Write-Host "  ✅ home       - Pantalla principal" -ForegroundColor Green
Write-Host "  ✅ interview  - Entrevista con IA (CORE)" -ForegroundColor Green
Write-Host "  ✅ profile    - Perfil de usuario" -ForegroundColor Green
Write-Host "  ✅ notification - Notificaciones" -ForegroundColor Green
Write-Host "  ✅ logs       - Logging (opcional)" -ForegroundColor Green

Write-Host ""
Write-Host "[3/3] Eliminando archivos UCB específicos..." -ForegroundColor Yellow

# Volver al directorio ucbp1
Set-Location ..

# Eliminar vectorucb si existe
if (Test-Path "vectorucb") {
    Write-Host "  ❌ Eliminando: vectorucb/" -ForegroundColor Red
    Remove-Item -Recurse -Force "vectorucb"
}

# Eliminar __VectorUcb.kt si existe
if (Test-Path "__VectorUcb.kt") {
    Write-Host "  ❌ Eliminando: __VectorUcb.kt" -ForegroundColor Red
    Remove-Item -Force "__VectorUcb.kt"
}

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "   ✅ LIMPIEZA COMPLETADA" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Features eliminadas:" -ForegroundColor Yellow
Write-Host "  - cardexample (ejemplos de tarjetas)" -ForegroundColor DarkGray
Write-Host "  - dollar (conversión de moneda)" -ForegroundColor DarkGray
Write-Host "  - github (integración GitHub)" -ForegroundColor DarkGray
Write-Host "  - movie (películas)" -ForegroundColor DarkGray
Write-Host "  - webview (vista web)" -ForegroundColor DarkGray
Write-Host "  - vectorucb (UCB específico)" -ForegroundColor DarkGray
Write-Host ""
Write-Host "⏭️  SIGUIENTE PASO:" -ForegroundColor Cyan
Write-Host "  1. En Android Studio: File → Sync Project" -ForegroundColor White
Write-Host "  2. Build → Rebuild Project" -ForegroundColor White
Write-Host "  3. Verificar que no haya errores" -ForegroundColor White
Write-Host ""

# Volver al directorio raíz del proyecto
Set-Location ..\..\..\..\..\..

Read-Host "Presiona Enter para salir"

