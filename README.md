# Vivarium

An evolution simulator where creatures ("motes") live in a simulated environment, competing for resources, evolving traits over time, and passing them on to their offspring.

The goal isn't to finish it — there's always another trait to add, another environment to simulate, another emergent behavior to discover.

## Devlog

Follow along with development on YouTube: [Simulating Life and Death | Vivarium Devlog #1](https://www.youtube.com/watch?v=hjCSDXI9FBw)

## How It Works

The simulation runs on a tick system rather than per-frame — most meaningful events (death, resource consumption, reproduction) happen at a fixed interval. This makes the simulation behavior predictable and easy to analyze.

At the end of a run, the sim writes a JSON file with population data over time. A companion web app lets you load one or more of these files and visualize population trends across runs.

## Tech

- **Sim** — [Godot 4](https://godotengine.org/) (GDScript)
- **Data visualization** — [SvelteKit](https://svelte.dev/) + [Chart.js](https://www.chartjs.org/)
