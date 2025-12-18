@echo off
echo ============================================
echo REFACTORIZACION DE PACKAGE - HIRETREE
echo ============================================
echo.
echo Este script va a:
echo 1. Crear la nueva estructura de carpetas com/hiretree/mobile
echo 2. Eliminar features no relacionadas con HireTree
echo 3. Mover archivos importantes
echo.
pause

cd app\src\main\java

echo.
echo [1/4] Creando nueva estructura de carpetas...
mkdir com\hiretree 2>nul
mkdir com\hiretree\mobile 2>nul
mkdir com\hiretree\mobile\data 2>nul
mkdir com\hiretree\mobile\data\remote 2>nul
mkdir com\hiretree\mobile\data\repository 2>nul
mkdir com\hiretree\mobile\data\notification 2>nul
mkdir com\hiretree\mobile\domain 2>nul
mkdir com\hiretree\mobile\domain\model 2>nul
mkdir com\hiretree\mobile\domain\usecase 2>nul
mkdir com\hiretree\mobile\domain\repository 2>nul
mkdir com\hiretree\mobile\presentation 2>nul
mkdir com\hiretree\mobile\presentation\auth 2>nul
mkdir com\hiretree\mobile\presentation\login 2>nul
mkdir com\hiretree\mobile\presentation\home 2>nul
mkdir com\hiretree\mobile\presentation\interview 2>nul
mkdir com\hiretree\mobile\presentation\profile 2>nul
mkdir com\hiretree\mobile\di 2>nul
mkdir com\hiretree\mobile\navigation 2>nul
mkdir com\hiretree\mobile\ui 2>nul
mkdir com\hiretree\mobile\ui\theme 2>nul

echo [OK] Estructura creada

echo.
echo [2/4] Eliminando features no relacionadas...
echo Eliminando: cardexample, dollar, github, movie, webview, vectorucb...

cd com\calyrsoft\ucbp1\features

if exist cardexample rmdir /s /q cardexample
if exist dollar rmdir /s /q dollar
if exist github rmdir /s /q github
if exist movie rmdir /s /q movie
if exist webview rmdir /s /q webview

cd ..\..\..\..

if exist com\calyrsoft\ucbp1\vectorucb rmdir /s /q com\calyrsoft\ucbp1\vectorucb
if exist com\calyrsoft\ucbp1\__VectorUcb.kt del /q com\calyrsoft\ucbp1\__VectorUcb.kt

echo [OK] Features eliminadas

echo.
echo [3/4] Las features restantes son:
dir com\calyrsoft\ucbp1\features /b

echo.
echo [4/4] IMPORTANTE:
echo.
echo La refactorizacion de paquetes debe hacerse desde Android Studio:
echo.
echo 1. Abre Android Studio
echo 2. En la vista de proyecto, cambia a vista "Project"
echo 3. Click derecho en com.calyrsoft.ucbp1
echo 4. Refactor ^> Rename
echo 5. Cambia a: com.hiretree.mobile
echo 6. Click en "Refactor"
echo 7. Android Studio actualizara todos los imports automaticamente
echo.
echo Presiona cualquier tecla para salir...
pause >nul

