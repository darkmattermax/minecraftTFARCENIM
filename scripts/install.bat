@echo off
chcp 65001 >nul
title TFARCENIM 一键安装到Minecraft

echo ============================================================
echo   TFARCENIM 整合包安装脚本
echo   自动安装到 .minecraft 目录
echo ============================================================
echo.

REM 查找 .minecraft 目录
set MC_DIR=%APPDATA%\.minecraft

if not exist "%MC_DIR%" (
    echo [错误] 未找到 .minecraft 目录: %MC_DIR%
    echo 请确保已安装 Minecraft 并至少运行过一次
    pause
    exit /b 1
)

echo 检测到 Minecraft 目录: %MC_DIR%
echo.

REM 确认
set /p confirm=确认安装到此目录? (Y/N): 
if /i not "%confirm%"=="Y" (
    echo 已取消
    pause
    exit /b 0
)

echo.
echo ============================================================
echo   [1/4] 复制 Mod 文件
echo ============================================================
mkdir "%MC_DIR%\mods" 2>nul
copy /Y "%~dp0..\mods\*.jar" "%MC_DIR%\mods\" >nul
echo Mod文件已复制
echo.

echo ============================================================
echo   [2/4] 复制光影文件
echo ============================================================
mkdir "%MC_DIR%\shaderpacks" 2>nul
copy /Y "%~dp0..\shaderpacks\*.zip" "%MC_DIR%\shaderpacks\" >nul
echo 光影文件已复制
echo.

echo ============================================================
echo   [3/4] 复制配置文件
echo ============================================================
mkdir "%MC_DIR%\config" 2>nul
copy /Y "%~dp0..\config\*" "%MC_DIR%\config\" >nul
echo 配置文件已复制
echo.

echo ============================================================
echo   [4/4] 数据包说明
echo ============================================================
echo.
echo 数据包需要手动放到存档目录:
echo   1. 创建新存档
echo   2. 打开 %%APPDATA%%\.minecraft\saves\你的存档名\datapacks\
echo   3. 把 %~dp0..\datapacks\TFARCENIM-datapack 文件夹复制进去
echo   4. 游戏内输入 /reload 刷新
echo.

echo ============================================================
echo   安装完成！
echo ============================================================
echo.
echo 下一步:
echo   1. 用 PCL2 启动 Minecraft 1.21.10 + Fabric 0.17.2
echo   2. 创建新存档
echo   3. 把数据包放到存档的 datapacks 目录
echo   4. 游戏内 /reload
echo.
pause
