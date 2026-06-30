@echo off
chcp 65001 >nul
title TFARCENIM Mod 编译脚本

echo ============================================================
echo   TFARCENIM Mod 编译脚本
echo   需要JDK 21 (如果没有会自动下载)
echo ============================================================
echo.

REM 检查Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [警告] 未检测到Java，尝试自动下载JDK 21...
    echo.
    
    REM 下载JDK 21
    set JDK_URL=https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.5%%2B11/OpenJDK21U-jdk_x64_windows_hotspot_21.0.5_11.zip
    set JDK_ZIP=%~dp0jdk21.zip
    set JDK_DIR=%~dp0jdk21
    
    echo 正在下载JDK 21...
    powershell -Command "Invoke-WebRequest -Uri '%JDK_URL%' -OutFile '%JDK_ZIP%' -TimeoutSec 300"
    if not exist "%JDK_ZIP%" (
        echo [错误] JDK下载失败，请手动下载JDK 21并设置JAVA_HOME
        echo 下载地址: https://adoptium.net/temurin/releases/?version=21
        pause
        exit /b 1
    )
    
    echo 正在解压JDK...
    powershell -Command "Expand-Archive -Path '%JDK_ZIP%' -DestinationPath '%JDK_DIR%' -Force"
    for /d %%i in ("%JDK_DIR%\*") do set JAVA_HOME=%%i
    set PATH=%JAVA_HOME%\bin;%PATH%
    echo JDK已就绪: %JAVA_HOME%
    echo.
)

echo ============================================================
echo   开始编译4个自写Mod
echo ============================================================
echo.

set MODS_DIR=%~dp0mods
set SRC_DIR=%~dp0mod-source

REM 编译 tfarcenim-core
echo [1/4] 编译 tfarcenim-core...
cd /d "%SRC_DIR%\tfarcenim-core"
call gradlew build
if %errorlevel% neq 0 (
    echo [失败] tfarcenim-core 编译失败
    pause
    exit /b 1
)
copy /Y "build\libs\tfarcenim-core-*.jar" "%MODS_DIR%\" >nul
del "%MODS%\tfarcenim-core-*-sources.jar" 2>nul
echo [成功] tfarcenim-core
echo.

REM 编译 tfarcenim-portal
echo [2/4] 编译 tfarcenim-portal...
cd /d "%SRC_DIR%\tfarcenim-portal"
call gradlew build
if %errorlevel% neq 0 (
    echo [失败] tfarcenim-portal 编译失败
    pause
    exit /b 1
)
copy /Y "build\libs\tfarcenim-portal-*.jar" "%MODS_DIR%\" >nul
del "%MODS%\tfarcenim-portal-*-sources.jar" 2>nul
echo [成功] tfarcenim-portal
echo.

REM 编译 tfarcenim-recipebook
echo [3/4] 编译 tfarcenim-recipebook...
cd /d "%SRC_DIR%\tfarcenim-recipebook"
call gradlew build
if %errorlevel% neq 0 (
    echo [失败] tfarcenim-recipebook 编译失败
    pause
    exit /b 1
)
copy /Y "build\libs\tfarcenim-recipebook-*.jar" "%MODS_DIR%\" >nul
del "%MODS%\tfarcenim-recipebook-*-sources.jar" 2>nul
echo [成功] tfarcenim-recipebook
echo.

REM 编译 tfarcenim-balance
echo [4/4] 编译 tfarcenim-balance...
cd /d "%SRC_DIR%\tfarcenim-balance"
call gradlew build
if %errorlevel% neq 0 (
    echo [失败] tfarcenim-balance 编译失败
    pause
    exit /b 1
)
copy /Y "build\libs\tfarcenim-balance-*.jar" "%MODS_DIR%\" >nul
del "%MODS%\tfarcenim-balance-*-sources.jar" 2>nul
echo [成功] tfarcenim-balance
echo.

echo ============================================================
echo   全部编译完成！
echo   jar文件已复制到 mods\ 目录
echo ============================================================
echo.
echo 现在你可以:
echo   1. 把 mods\ 目录所有jar复制到 .minecraft\mods\
echo   2. 把 shaderpacks\ 目录所有zip复制到 .minecraft\shaderpacks\
echo   3. 把 datapacks\TFARCENIM-datapack 复制到存档的 datapacks\ 目录
echo   4. 把 config\ 目录所有文件复制到 .minecraft\config\
echo.
pause
