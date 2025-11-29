@echo off
echo ================================================
echo    DIAGNOSTICO - Hire Tree
echo ================================================
echo.

cd /d "%~dp0"

echo Verificando configuracion del proyecto...
echo.

echo [1] Verificando google-services.json...
if exist "app\google-services.json" (
    echo    ✓ app\google-services.json existe
    findstr /C:"hiretreemobile" app\google-services.json >nul
    if %ERRORLEVEL% EQU 0 (
        echo    ✓ project_id configurado
    ) else (
        echo    ✗ project_id NO configurado o incorrecto
    )
) else (
    echo    ✗ app\google-services.json NO ENCONTRADO
)
echo.

echo [2] Verificando local.properties...
if exist "local.properties" (
    echo    ✓ local.properties existe
    findstr /C:"GEMINI_API_KEY" local.properties >nul
    if %ERRORLEVEL% EQU 0 (
        echo    ✓ GEMINI_API_KEY configurada
        findstr /C:"YOUR_API_KEY_HERE" local.properties >nul
        if %ERRORLEVEL% EQU 0 (
            echo    ⚠ GEMINI_API_KEY tiene valor por defecto
        )
    ) else (
        echo    ✗ GEMINI_API_KEY NO configurada
    )
) else (
    echo    ✗ local.properties NO ENCONTRADO
)
echo.

echo [3] Verificando estructura de carpetas...
if exist "app\src\main\java\com\calyrsoft\ucbp1\features\auth" (
    echo    ✓ features/auth existe
) else (
    echo    ✗ features/auth NO existe
)

if exist "app\src\main\java\com\calyrsoft\ucbp1\features\home" (
    echo    ✓ features/home existe
) else (
    echo    ✗ features/home NO existe
)
echo.

echo [4] Verificando conexion a Internet...
ping -n 1 google.com >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo    ✓ Conexion a Internet OK
) else (
    echo    ✗ Sin conexion a Internet
)
echo.

echo [5] Verificando Gradle wrapper...
if exist "gradlew.bat" (
    echo    ✓ gradlew.bat existe
) else (
    echo    ✗ gradlew.bat NO existe
)
echo.

echo [6] Intentando listar dependencias de Firebase...
echo    (Esto puede tardar un momento...)
call gradlew.bat dependencies --configuration implementation 2>nul | findstr /C:"firebase-auth" >nul
if %ERRORLEVEL% EQU 0 (
    echo    ✓ firebase-auth encontrada en dependencias
) else (
    echo    ⚠ firebase-auth NO encontrada (necesita sincronizacion)
)

call gradlew.bat dependencies --configuration implementation 2>nul | findstr /C:"firebase-firestore" >nul
if %ERRORLEVEL% EQU 0 (
    echo    ✓ firebase-firestore encontrada en dependencias
) else (
    echo    ⚠ firebase-firestore NO encontrada (necesita sincronizacion)
)

call gradlew.bat dependencies --configuration implementation 2>nul | findstr /C:"generativeai" >nul
if %ERRORLEVEL% EQU 0 (
    echo    ✓ generativeai encontrada en dependencias
) else (
    echo    ⚠ generativeai NO encontrada (necesita sincronizacion)
)
echo.

echo [7] Verificando cache de Gradle...
if exist "%USERPROFILE%\.gradle\caches" (
    echo    ✓ Cache de Gradle existe
    for /f %%A in ('dir /b /s "%USERPROFILE%\.gradle\caches\modules-2\files-2.1\com.google.firebase" 2^>nul ^| find /c /v ""') do set count=%%A
    if defined count (
        if !count! GTR 0 (
            echo    ✓ Archivos de Firebase en cache
        ) else (
            echo    ⚠ No hay archivos de Firebase en cache
        )
    )
) else (
    echo    ⚠ Cache de Gradle no existe
)
echo.

echo ================================================
echo    RESUMEN DEL DIAGNOSTICO
echo ================================================
echo.
echo Si ves ✓ en todo = Configuracion OK
echo Si ves ⚠ en Firebase = Necesitas sincronizar
echo Si ves ✗ = Hay un problema que debe resolverse
echo.
echo RECOMENDACION:
echo.
echo 1. Si hay ✗, resuelve esos problemas primero
echo 2. Si solo hay ⚠ en dependencias, ejecuta:
echo    - sync-project.bat
echo    - Luego abre Android Studio
echo 3. En Android Studio:
echo    - File ^> Invalidate Caches ^> Restart
echo    - File ^> Sync Project with Gradle Files
echo.
pause

