# 🚀 Инструкция по настройке GitHub Actions для автоматической сборки APK

## 📋 Шаги

### 1. Инициализация Git репозитория

Откройте PowerShell в папке проекта и выполните:

```powershell
# Инициализация репозитория
git init

# Добавление всех файлов
git add .

# Первый коммит
git commit -m "Initial commit: GraphWar II Clone game"
```

### 2. Создание репозитория на GitHub

1. Перейдите на https://github.com
2. Нажмите кнопку "+" в правом верхнем углу → "New repository"
3. Укажите название: `graphwar-ii` (или любое другое)
4. **НЕ** отмечайте "Initialize with README" (у вас уже есть файлы)
5. Нажмите "Create repository"

### 3. Подключение к GitHub

GitHub покажет команды. Выполните их в PowerShell:

```powershell
# Добавление удаленного репозитория (замените YOUR_USERNAME на ваш логин GitHub)
git remote add origin https://github.com/YOUR_USERNAME/graphwar-ii.git

# Переименование ветки в main (если нужно)
git branch -M main

# Отправка кода на GitHub
git push -u origin main
```

### 4. GitHub Actions автоматически запустится!

После `git push` GitHub Actions:
- ✅ Автоматически обнаружит файл `.github/workflows/build-apk.yml`
- ✅ Запустит сборку APK
- ✅ Соберет Debug и Release версии
- ✅ Сохранит APK файлы как артефакты

### 5. Скачивание APK

1. Перейдите в ваш репозиторий на GitHub
2. Откройте вкладку **Actions**
3. Выберите последний запуск (Build APK)
4. Прокрутите вниз до секции **Artifacts**
5. Скачайте:
   - `app-debug` - для тестирования
   - `app-release` - для распространения

## 🎯 Что делает GitHub Actions?

Файл `.github/workflows/build-apk.yml` настроен на:

- **Триггеры:**
  - Push в ветки main/master
  - Pull requests
  - Ручной запуск (workflow_dispatch)

- **Действия:**
  1. Установка JDK 17
  2. Кеширование Gradle для ускорения сборки
  3. Сборка Debug APK
  4. Сборка Release APK (unsigned)
  5. Загрузка APK как артефактов

## 🔐 Подписание Release APK (опционально)

Для подписанного release APK нужно:

1. Создать keystore:
```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

2. Добавить secrets в GitHub:
   - Settings → Secrets and variables → Actions → New repository secret
   - Добавьте: `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`
   - Keystore файл нужно закодировать в base64 и добавить как secret

## 📦 Автоматический Release (бонус)

Хотите создавать автоматические релизы с APK при создании тега?

Дайте знать, и я создам дополнительный workflow!

## ❓ Возможные проблемы

### Ошибка: "Permission denied"
```powershell
git config --global user.email "your@email.com"
git config --global user.name "Your Name"
```

### Ошибка: "Authentication failed"
Используйте Personal Access Token вместо пароля:
1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Generate new token (classic)
3. Отметьте права: `repo`, `workflow`
4. Используйте токен как пароль при `git push`

---

## ⚡ Быстрые команды для копирования

```powershell
# Полная последовательность (замените YOUR_USERNAME!)
git init
git add .
git commit -m "Initial commit: GraphWar II Clone"
git remote add origin https://github.com/YOUR_USERNAME/graphwar-ii.git
git branch -M main
git push -u origin main
```

После этого перейдите на GitHub → Actions и скачайте готовые APK! 🎉
