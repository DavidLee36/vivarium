# Vivarium - Artificial Life Laboratory

## Project Overview
Vivarium is a procedurally-generated ecosystem simulation built in Java. It's an "artificial life laboratory" where you create initial conditions, press play, and watch emergent evolutionary behaviors unfold. Each run generates a unique world with different species, and natural selection drives adaptation over generations.

## Core Concept
- **Not a game** - more of a sandbox simulation to observe
- Heavily OOP architecture with deep class hierarchies
- Infinite expandability - can always add new species, biomes, behaviors
- Focus on emergent behavior from simple rules

---

## Technical Stack
- **Language:** Java
- **Graphics:** LibGDX (for 2D rendering and UI)
- **Architecture:** Object-oriented with inheritance hierarchies
- **Data:** Classes over JSON (hardcode biome properties, etc. in class definitions)

---

## World Generation

### Map Structure
- **Hex-based grid** (top-down view)
- Grid size: configurable (50x50 for testing, 100x100+ for full runs)

### Procedural Generation
1. Use Perlin/Simplex noise to generate:
   - Elevation (mountains, valleys, flatlands)
   - Moisture/rainfall patterns
   - Temperature zones (affected by elevation + latitude)

2. Combine these to determine biomes:
   - Hot + Wet = Jungle
   - Hot + Dry = Desert
   - Cold + Wet = Tundra
   - Temperate + Moderate = Grassland/Forest
   - Water bodies (lakes, rivers)

### Biome Properties
Each biome has:
- Temperature
- Moisture level
- Plant growth rate
- Terrain penalties for different animal types
- Visual hex color

---

## Core Systems

### Time System
- **1 tick = 1 hour** of simulation time
- Each tick:
  1. Environment phase (plants grow, seasonal changes)
  2. Animal phase (randomized turn order)
  3. Reproduction phase
  4. Cleanup phase (remove dead organisms, update stats)

### Organism Hierarchy
```
Organism (abstract)
├── Plant
│   ├── Grass
│   ├── Shrub
│   └── Tree
└── Animal (abstract)
    ├── Herbivore
    │   ├── Deer
    │   ├── Rabbit
    │   └── Mountain Goat
    ├── Carnivore
    │   ├── Wolf
    │   └── Bear
    ├── Omnivore
    └── Scavenger
```

### Energy & Food Chain
- Plants convert sunlight → energy (base of food chain)
- Herbivores eat plants (~10% energy transfer)
- Carnivores eat herbivores (~10% energy transfer)
- Dead organisms decompose
- Energy loss through:
  - Movement (varies by speed/size)
  - Daily metabolism
  - Reproduction costs

---

## Genetics System

### DNA Structure
Each animal has a genome containing genes for:
- **Size** (bigger = more energy storage, slower, easier to spot)
- **Speed** (faster = catch prey/escape, burns more energy)
- **Metabolism** (energy consumption rate)
- **Reproduction threshold** (energy level needed to breed)
- **Lifespan potential**
- **Vision range**
- **Stamina** (for encounters/chases)
- **Climbing ability** (terrain-specific)
- **Swimming ability** (can cross water?)

### Inheritance & Mutation
- Sexual reproduction: combine genes from both parents
- Mutations: small random changes to gene values each generation
- Natural selection: successful traits propagate
- Evolution observable over generations

---

## Encounter System

### Turn Order
Each tick, animals act in randomized order. When an animal's turn comes:
1. Sense surroundings
2. Decide action
3. Check for encounters (predator spots prey, etc.)
4. If encounter triggered → resolve it, mark all participants as "acted"
5. Otherwise execute solo action

### Encounter Resolution
Encounters resolve over multiple rounds (not instant):

**Example: Wolf Pack vs Deer Herd**
```
Round 1:
- Deer flee (cost 15 stamina, speed 10)
- Wolves chase (cost 12 stamina, speed 12)
- Wolves gaining ground

Round 2:
- Wolves close enough to attack
- Target weakest deer (old/injured/young)
- Other deer continue fleeing
- Combat: 3 wolves vs 1 deer → deer dies
- Remaining deer escape, wolves feed
```

### Terrain Effects on Encounters
Different biomes affect stamina costs:
- **Grassland:** Neutral
- **Forest:** Dense vegetation slows large animals
- **Mountains:** Huge penalty for poor climbers, advantage for mountain-adapted species
- **Water:** Some can swim, others can't cross
- **Desert:** Heat increases stamina drain
- **Snow:** Deep snow slows heavy animals

### Simple AI Decision Logic
**Prey:**
- If predator close AND good escape terrain nearby → flee to that terrain
- Else if predator close → flee anywhere
- Else if low stamina → rest

**Predator:**
- If prey in attack range → attack weakest
- Else if prey visible AND enough stamina → chase
- Else if low stamina OR lost sight → give up

### Encounter Outcomes
- Prey dies (predator gains energy)
- Prey escapes (both lose stamina)
- Predator gives up (wasted energy, still hungry)
- Both exhausted (rare, both vulnerable)

---

## Visualization

### Initial Implementation (Simple)
- **Top-down hex grid view**
- Each hex colored by biome
- Animals represented as **colored circles/dots** on hexes
  - Grey circle = wolves
  - Brown circle = deer
  - White circle = rabbits
  - etc.
- Circle size doesn't mean anything initially (can add later)

### Interaction
**Click hex:**
- Shows biome info (type, temperature, moisture, plant density)

**Click animal circle:**
- Opens tree/list view showing all animals on that hex
- Grouped by pack/herd if applicable
- Example display:
  ```
  Wolves on Hex (25, 34):
  ├─ Pack #1 (4 wolves)
  │  ├─ Wolf #142 - Male, Age 4, Energy 85%, Speed 8
  │  ├─ Wolf #156 - Female, Age 3, Energy 72%, Speed 9
  │  └─ ...
  ├─ Lone Wolf #234 - Male, Age 1, Energy 45%, Speed 9
  ```

### Visual Aggregation
- One circle per species per hex (regardless of quantity)
- Clicking reveals individual animals and pack structure
- Keeps map clean even with hundreds of animals

### UI Elements
- Population graphs over time
- Speed controls (pause, 1x, 5x, 10x)
- Generation counter
- Species list with current populations
- Selected animal/hex stat panels

---

## Social Behaviors

### Packs & Herds
- Wolves can form packs (not required, but advantageous)
- Deer form herds for protection
- Lone animals are possible
- Pack/herd membership affects encounter success rates

### Behaviors to Implement
- **Herbivores:** Wander for food, flee from predators, stick with herds
- **Carnivores:** Hunt when hungry, rest when full, coordinate in packs
- **Mating:** Seek partners when high energy, genetic compatibility
- **Territorial:** Some species claim areas (optional expansion)

---

## Data Tracking

### Statistics to Log
- Population count per species over time
- Births/deaths per tick
- Average trait values per species (track evolution)
- Extinction events
- Biome population density
- Predator-prey ratios

### Emergent Patterns to Watch For
- Predator-prey oscillations (Lotka-Volterra cycles)
- Evolution of speed (prey gets faster → predators get faster → cycle)
- Niche specialization (species adapt to specific biomes)
- Migration patterns
- Extinction cascades

---

## Development Roadmap

### Phase 1: Core Foundation (First Weekend)
1. Basic hex grid generation with biome coloring
2. Simple Plant class that can grow/spread
3. One Herbivore that eats plants and reproduces
4. Basic rendering to visualize

### Phase 2: Predator-Prey (Week 2-3)
5. Add Carnivore class
6. Implement basic encounter system
7. Add animal movement between hexes
8. Simple genetics (inherit traits from parents)

### Phase 3: Evolution (Week 4+)
9. Mutation system
10. Track statistics and population graphs
11. Multiple species per category
12. UI improvements (click to inspect)

### Phase 4: Polish & Expansion (Ongoing)
13. Seasons and environmental changes
14. More complex AI behaviors
15. Pack/herd dynamics
16. Better visualization
17. Data export and analysis tools

---

## Future Expansion Ideas

### New Content
- More biomes (swamps, caves, coral reefs, volcanic)
- Aquatic layer (fish, marine life)
- New animal types (amphibians, insects, birds)
- Plant diversity (trees vs grass vs shrubs)
- Parasites and diseases

### New Systems
- Weather patterns and climate events
- Disease spread and immunity
- Migration patterns
- Seasonal breeding cycles
- Tool use / intelligence evolution
- Symbiotic relationships
- Domestication (introduce "human" species)

### Polish
- Replace colored circles with simple sprites
- Animation (idle, walking)
- Particle effects (births, deaths, encounters)
- Sound effects
- Time-lapse recording
- Family tree visualization

---

## Key Design Principles

### Development Philosophy
1. **Make it work** (get something running)
2. **Make it right** (refactor and clean up)
3. **Make it fast** (optimize only if needed)

### Anti-Patterns to Avoid
- Over-engineering before seeing what works
- Premature optimization
- Trying to future-proof everything upfront
- Perfect the first time mentality

### Good Practices
- Expect to refactor as you learn what matters
- Start simple, add complexity when you hit limitations
- Keep classes focused and readable
- Use constants instead of magic numbers
- Track individuals but group visually

---

## Technical Notes

### Class Structure Example
```java
abstract class Biome {
    protected String name;
    protected Color hexColor;
    protected double temperature;
    protected double moisture;
    protected double plantGrowthRate;
    
    abstract double getTerrainPenalty(Animal animal);
    abstract boolean canSupport(Organism organism);
}

class Desert extends Biome {
    public Desert() {
        this.name = "Desert";
        this.hexColor = new Color(210, 180, 140);
        this.temperature = 35.0;
        this.moisture = 0.1;
        this.plantGrowthRate = 0.2;
    }
    
    @Override
    double getTerrainPenalty(Animal animal) {
        return animal.size > 100 ? 20.0 : 5.0;
    }
}
```

### Hex Grid Structure
```java
class Hex {
    int x, y;
    Biome biome;
    ArrayList<Plant> plants;
    ArrayList<Animal> animals;
    
    void update() {
        // Update everything in this hex
    }
}

class World {
    Hex[][] grid;
    
    void simulate() {
        // Run one tick
    }
}
```

---

## Why This Project?

### Learning Goals
- Practice large-scale OOP architecture in Java
- Build something with infinite expandability
- Create a project you can always come back to
- See emergent behavior from simple rules

### What Makes It Cool
- Every run is completely unique
- Watch real evolution happen (fast prey → faster predators)
- Discover unexpected behaviors (mountain goats vs plains wolves)
- No "winning" - just observe and learn
- Satisfying to see complex ecosystems emerge from simple rules

---

## Notes
- Start with colored dots/circles for animals, can hire artist later for sprites (~$400-900 for indie pixel art style)
- Procedural visuals are also an option (animals look different based on genes)
- Track individuals internally but display as aggregated circles on map
- One circle per species per hex, click to see details
- Simulation is CPU-bound, not graphics-bound - Java will handle 10k+ animals fine