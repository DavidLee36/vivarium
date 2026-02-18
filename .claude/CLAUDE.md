# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Vivarium is a procedurally-generated ecosystem simulation in Java - an "artificial life laboratory" where initial conditions are set and emergent evolutionary behaviors unfold through natural selection. Each run generates a unique world with different species adapting over generations.

## Build Commands

Gradle with Gradle wrapper. Source lives in `src/` (custom `sourceSets`, not the default `src/main/java/`).

```bash
# Run the simulation
./gradlew run

# Compile only
./gradlew compileJava
```

## Architecture

### Project Structure
```
src/vivarium/
├── Vivarium.java        # LibGDX ApplicationAdapter (game class)
├── World.java           # Main simulation loop, owns Map (not yet wired to LibGDX)
├── desktop/
│   └── DesktopLauncher.java  # LWJGL3 window setup, boots Vivarium
├── map/
│   └── Map.java         # Hex grid container
└── organisms/
    ├── animals/         # Animal subclasses (planned)
    └── plants/          # Plant subclasses (planned)
```

### Core Classes
- **DesktopLauncher** - Entry point. Creates LWJGL3 window and boots Vivarium.
- **Vivarium** - LibGDX ApplicationAdapter. Lifecycle: create → render loop → dispose.
- **World** - Main simulation loop. Owns the Map and runs tick cycles. (Not yet integrated with LibGDX)
- **Map** (in `vivarium.map` package) - Hex grid container. Will hold Hex objects with biome data.

### Organism Hierarchy (Planned)
Taxonomic class hierarchy. Diet (herbivore, carnivore, omnivore, scavenger) is a property on Animal, not a class.
```
Organism (abstract)
├── Plant (abstract)
│   └── Grass, Shrub, Tree
└── Animal (abstract)
    ├── Mammal (abstract) — warm-blooded, fur/insulation
    │   └── Wolf, Deer, Bear, Rabbit...
    ├── Bird (abstract) — flight, feathers
    │   └── Eagle, Crow...
    ├── Reptile (abstract) — cold-blooded, temperature-dependent
    │   └── Snake, Lizard...
    ├── Amphibian (abstract) — needs proximity to water
    │   └── Frog...
    └── Fish (abstract) — aquatic
        └── Trout...
```

## Key Technical Decisions

- **Hex-based grid** for the world map (top-down view)
- **Perlin/Simplex noise** for procedural generation (elevation, moisture, temperature → biomes)
- **OOP with deep inheritance hierarchies** - use abstract base classes for shared behavior
- **Data in classes, not JSON** - biome properties, animal stats are hardcoded in class definitions
- **1 tick = 1 hour** simulation time
- **LibGDX 1.12.1** for 2D rendering (integrated, window opens)

## Simulation Systems

### Tick Phases
1. Environment phase (plants grow, seasonal changes)
2. Animal phase (randomized turn order)
3. Reproduction phase
4. Cleanup phase (remove dead organisms, update stats)

### Genetics
Animals have genomes with traits: size, speed, metabolism, reproduction threshold, lifespan, vision range, stamina, climbing ability, swimming ability. Traits inherit with mutations, driving natural selection.

### Encounters
Multi-round resolution system for predator-prey interactions with terrain effects on stamina costs.
