# Walkthrough: GraphWar Clone Implementation

I have implemented a complete, modern clone of Graphwar II for Android using Jetpack Compose and `exp4j`.

## Key Features Implemented

### 1. Mathematical Engine
- **Local Origin Logic**: Every formula input by the player treats the player's current position as $(0, 0)$.
- **Parser**: Used `exp4j` to evaluate complex expressions like `sin(x)`, `x^2`, etc.
- **Trajectory Calculation**: Real-time calculation of points along the curve with collision detection for targets and obstacles.

### 2. Game Mechanics
- **30 Levels**: A progressive level system (implemented via `LevelManager`) that increases difficulty by moving targets and adding obstacles.
- **Movement System**: A unique mechanic where players can move along their own graphs (up to 3 times per level).
- **Collision Detection**: Precise detection for target hits and obstacle impact.

### 3. Modern UI/UX
- **Clean Interface**: A minimalist game field with a dark theme grid.
- **HUD**: Easy-to-use input field, mode toggle (Shoot/Move), and charge indicators.
- **Navigation**: Full implementation of Main Menu, Level Selection, and Game screens.
- **Compose Graphics**: High-performance drawing using `Canvas` API.

## Project Structure
- [MainActivity.kt](file:///C:/Users/basda/OneDrive/Рабочий стол/gra/app/src/main/java/com/example/gra/MainActivity.kt): Navigation and entry point.
- [GameViewModel.kt](file:///C:/Users/basda/OneDrive/Рабочий стол/gra/app/src/main/java/com/example/gra/ui/GameViewModel.kt): Core game loop and logic.
- [GameScreen.kt](file:///C:/Users/basda/OneDrive/Рабочий стол/gra/app/src/main/java/com/example/gra/ui/GameScreen.kt): Drawing the grid, player, and projectiles.
- [MathParser.kt](file:///C:/Users/basda/OneDrive/Рабочий стол/gra/app/src/main/java/com/example/gra/engine/MathParser.kt): Evaluating equations.

## Verification
- Verified the coordinate transformation logic (local to world coordinates).
- Verified level progression logic.
- UI responds correctly to mode switches.
