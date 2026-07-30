# GraphWar Clone: Project "Gra"

Implementation plan for a modern Android clone of Graphwar II with local origins and progressive levels.

## User Review Required

> [!IMPORTANT]
> **Project Initialization**: The current directory is empty. I will initialize a standard Android project structure with Jetpack Compose.
> **Level Data**: I will define 30 levels. If you have specific level designs in mind (e.g., "Level 5 must have a giant wall"), please let me know.
> **Local Origin Logic**: To clarify: when a player at $(X_p, Y_p)$ enters $y = x^2$, the projectile will follow $(X_p + t, Y_p + t^2)$. The graph will be rendered relative to the player's position.

## Proposed Changes

### 1. Project Infrastructure
- Initialize `build.gradle.kts`, `settings.gradle.kts`, and `AndroidManifest.xml`.
- Set up dependencies: Jetpack Compose, Material 3, Navigation, and `exp4j` for math parsing.

### 2. Core Game Engine
- **Math Parser**: Wrapper around `exp4j` to handle functions like `sin(x)`, `cos(x)`, `tan(x)`, `sqrt(x)`, `abs(x)`, `log(x)`, `exp(x)`.
- **Game State**: Management of player position, target position, projectiles, and obstacles.
- **Movement Mechanic**:
    - Toggle between "Shoot" and "Move".
    - Movement limit: 3 charges per level.
    - Player position updates to $(X_p + \Delta x, Y_p + f(\Delta x))$ after movement.
- **Coordinate System**: Utility to map "Game Coordinates" (centered on player) to "Screen Pixels".

### 3. Level System
- **Level Manager**: Handles loading of 30 levels.
- **Level Definition**: JSON or Kotlin DSL for levels (Target positions, Obstacles, Difficulty scaling).

### 4. UI/UX (Modern Design)
- **Minimalist Game Field**: Focus on the graph and the player/target. Clean grid lines.
- **Input System**:
    - Sleek text field for formula input.
    - Switch/Toggle for "Shoot/Move" modes.
    - Move charges indicator (e.g., 3 icons/dots).
- **Animations**:
    - Smooth projectile movement along the graph.
    - Player movement animation (sliding along the function).
    - Particles on hit.
    - Level transition animations.
- **Sound**: basic effects for firing and hitting.

### 5. Screens
- **Main Menu**: Play, Level Select, Settings.
- **Level Selection**: Grid of 30 levels with progress indicators.
- **Game Screen**: The main playground.

## Verification Plan

### Automated Tests
- Unit tests for the Math Parser (verifying complex expressions).
- Unit tests for Coordinate Transformation logic.

### Manual Verification
- Testing on Android Emulator/Device to verify touch responsiveness and rendering performance.
- Verifying each of the 30 levels is beatable.
