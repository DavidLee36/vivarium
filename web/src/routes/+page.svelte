<script>
	import Chart from "chart.js/auto";
	import { tick } from "svelte";

	let fileData = $state(null);
	let canvas = $state();

	const fileChange = async (e) => {
		const files = [...e.target.files];
		if (!files.length) return;
		fileData = await Promise.all(files.map(file => file.text().then(JSON.parse)));
		console.log(fileData);
	};

	function rollingAverage(arr, window) {
		return arr.map((_, i) => {
			const start = Math.max(0, i - Math.floor(window / 2));
			const end = Math.min(arr.length, start + window);
			const slice = arr.slice(start, end);
			return slice.reduce((a, b) => a + b, 0) / slice.length;
		});
	}

	const calculateRollingAverage = (data, windowSize) => {
		return data.map((val, idx, arr) => {
			// Determine the start of the window
			const start = Math.max(0, idx - windowSize + 1);
			// Get the subset of data for the current window
			const subset = arr.slice(start, idx + 1);
			// Calculate the average
			const sum = subset.reduce((a, b) => a + b, 0);
			return sum / subset.length;
		});
	};

	const getAverage = () => { // Average for each tick across all runs
		let avgArr = []
		for(let i = 0; i < fileData[0].mote_count.length; i++) {
			let total = 0;
			for(let j = 0; j < fileData.length; j++) {
				total += fileData[j].mote_count[i];
			}
			avgArr[i] = total / fileData.length;
		}
		return avgArr;
	};

	const getFullAverage = () => { // Average for ALL ticks across all runs
		let total = 0;
		let ticksCounted = 0;
		for(let i = 0; i < fileData.length; i++) {
			for(let j = 50; j < fileData[i].mote_count.length; j++) {
				total += fileData[i].mote_count[j];
				ticksCounted++;
			}
		}
		let average = total / ticksCounted;
		return new Array(fileData[0].mote_count.length).fill(average);
	}

	const randomGraphColor = (alpha) => {
		const r = Math.floor(Math.random() * (255 - 1 + 1)) + 1;
		const g = Math.floor(Math.random() * (255 - 1 + 1)) + 1;
		const b = Math.floor(Math.random() * (255 - 1 + 1)) + 1;
		return `rgba(${r}, ${g}, ${b}, ${alpha})`;
	}

	$effect(() => {
		if (!fileData || !canvas) return;

		//const raw = [...fileData.mote_count];
		let chart;
		let cancelled = false;

		//const avg = Array(raw.length).fill(getAverage(raw));
		//const trend = rollingAverage(raw, 10);
		//const rolling = calculateRollingAverage(raw, 10);

		let rollingAverages = []
		for(let i = 0; i < fileData.length; i++) {
			rollingAverages[i] = calculateRollingAverage(fileData[i].mote_count)
		}

		tick().then(() => {
			if (cancelled) return;

			chart = new Chart(canvas, {
				type: "line",
				options: {
					maintainAspectRatio: false,
					animation: false,
					scales: {
						x: { ticks: { maxTicksLimit: 20, color: 'rgba(255,255,255,0.7)' } },
						y: { ticks: { color: 'rgba(255,255,255,0.7)' } }
					},
				},
				data: {
					labels: rollingAverages[0].map((_, i) => i),
					datasets: [],
				},
			});

			const runs = fileData.map(d => [...d.mote_count]);
			const avg = [...getAverage()];
			const fullAvg = [...getFullAverage()];
			const totalPoints = runs[0].length;

			for(let i = 0; i < runs.length; i++) {
				chart.data.datasets.push({
					label: `run${i+1}`,
					data: [],
					borderColor: randomGraphColor(0.3),
					backgroundColor: randomGraphColor(0.05),
					tension: 0.3,
					fill: false,
					pointRadius: 0,
					order: 1,
				});
			}
			chart.data.datasets.push({
				label: "average",
				data: [],
				borderColor: "rgba(220, 220, 220, 1)",
				tension: 0.3,
				fill: false,
				pointRadius: 0,
				order: 0,
			});
			chart.data.datasets.push({
				label: "full average",
				data: [],
				borderColor: "rgba(7, 250, 185, 1)",
				tension: 0.3,
				fill: false,
				pointRadius: 0,
				order: 0,
			});

			const duration = 10000;
			const start = performance.now();

			function draw(now) {
				if (cancelled) return;
				const progress = Math.min((now - start) / duration, 1);
				const count = Math.floor(progress * totalPoints);
				for (let i = 0; i < runs.length; i++) {
					chart.data.datasets[i].data = runs[i].slice(0, count);
				}
				chart.data.datasets[runs.length+1].data = fullAvg.slice(0, count);
				chart.data.datasets[runs.length].data = avg.slice(0, count);
				chart.update("none");
				if (progress < 1) requestAnimationFrame(draw);
			}

			requestAnimationFrame(draw);
		});

		return () => {
			cancelled = true;
			chart?.destroy();
		};
	});
</script>

<div class="wrapper">
	{#if !fileData}
		<input type="file" accept=".json" onchange={fileChange} multiple/>
	{:else}
		<div class="chart-container">
			<canvas bind:this={canvas}></canvas>
		</div>
	{/if}
</div>

<style>
	.wrapper {
		height: 100%;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}

	.chart-container {
		width: 80vw;
		height: 80vh;
	}
</style>
