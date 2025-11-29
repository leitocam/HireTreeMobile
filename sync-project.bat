@echo off
echo ================================================
echo    HIRE TREE - Sincronizacion de Proyecto
echo ================================================
echo.

cd /d "%~dp0"

echo [1/5] Verificando archivos de configuracion...
if not exist "app\google-services.json" (
    echo ERROR: No se encuentra app\google-services.json
    echo Por favor, descarga el archivo desde Firebase Console
    pause
    exit /b 1
)

if not exist "local.properties" (
    echo ERROR: No se encuentra local.properties
    pause
    exit /b 1
)

findstr /C:"GEMINI_API_KEY=YOUR_API_KEY_HERE" local.properties >nul
if %ERRORLEVEL% EQU 0 (
    echo ADVERTENCIA: GEMINI_API_KEY aun tiene el valor por defecto
    echo Por favor, configura tu API key real en local.properties
    pause
)

echo Archivos de configuracion OK
echo.

echo [2/5] Limpiando proyecto...
call gradlew.bat clean
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Fallo al limpiar el proyecto
    pause
    exit /b 1
)

echo.
echo [3/5] Descargando dependencias...
call gradlew.bat --refresh-dependencies
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Fallo al descargar dependencias
    echo Verifica tu conexion a Internet
    pause
    exit /b 1
)

echo.
echo [4/5] Compilando proyecto (puede tardar varios minutos)...
call gradlew.bat build -x test -x lint
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Fallo al compilar el proyecto
    echo Revisa los errores arriba
    pause
    exit /b 1
)

echo.
echo [5/5] Verificando configuracion...
call gradlew.bat dependencies --configuration implementation | findstr "firebase" >nul
if %ERRORLEVEL% EQU 0 (
    echo Firebase configurado correctamente
) else (
    echo ADVERTENCIA: No se detectaron dependencias de Firebase
)

echo.
echo ============================================
echo    SINCRONIZACION COMPLETADA EXITOSAMENTE
echo ============================================
echo.
echo Ahora puedes:
echo 1. Abrir el proyecto en Android Studio
echo 2. Esperar a que termine la indexacion
echo 3. Ejecutar la aplicacion
echo.
echo Si hay errores en Android Studio:
echo - File ^> Invalidate Caches ^> Restart
echo - File ^> Sync Project with Gradle Files
echo.
pause

