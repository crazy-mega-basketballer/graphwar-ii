@echo off
echo ============================================
echo    GraphWar II - GitHub Setup Script
echo ============================================
echo.

echo [1/5] Initializing Git repository...
git init
if %errorlevel% neq 0 (
    echo ERROR: Git initialization failed!
    pause
    exit /b 1
)

echo.
echo [2/5] Adding all files...
git add .
if %errorlevel% neq 0 (
    echo ERROR: Git add failed!
    pause
    exit /b 1
)

echo.
echo [3/5] Creating initial commit...
git commit -m "Initial commit: GraphWar II Clone - Complete game with 30 levels"
if %errorlevel% neq 0 (
    echo ERROR: Git commit failed!
    echo.
    echo Make sure you have configured git:
    echo   git config --global user.email "your@email.com"
    echo   git config --global user.name "Your Name"
    pause
    exit /b 1
)

echo.
echo [4/5] Setup complete!
echo.
echo ============================================
echo    Next Steps:
echo ============================================
echo.
echo 1. Go to https://github.com and create a new repository
echo    Name it: graphwar-ii (or any name you like)
echo    DON'T initialize with README
echo.
echo 2. After creating the repository, run:
echo    git remote add origin https://github.com/YOUR_USERNAME/graphwar-ii.git
echo    git branch -M main
echo    git push -u origin main
echo.
echo 3. GitHub Actions will automatically build APK!
echo    Go to your repository ^> Actions tab ^> Download APK
echo.
echo ============================================
pause
