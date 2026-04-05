<script>
	import Chart from 'chart.js/auto';
	import { onMount } from 'svelte';

	let fileData = $state(null);
	let canvas = $state();

	const fileChange = async(e) => {
		const file = e.target.files[0];
		if (!file) return;
		const text = await file.text();
		fileData = JSON.parse(text);
	}

	function rollingAverage(arr, window) {
		return arr.map((_, i) => {
			const start = Math.max(0, i - Math.floor(window / 2));
			const end = Math.min(arr.length, start + window);
			const slice = arr.slice(start, end);
			return slice.reduce((a, b) => a + b, 0) / slice.length;
		});
	}

	const skipData = (arr, skip) => {
		return arr.map(_, i)
	}

	const getAverage = (arr) => {
		let total = 0;
		arr.forEach(c => {
			total += c;
		});
		return total / arr.length;
	}

	$effect(() => {
		if (!fileData || !canvas) return;

		const raw = [...fileData.mote_count];

		const chart = new Chart(canvas, {
			type: 'line',
			options: { maintainAspectRatio: false },
			data: {
				labels: raw.map((_, i) => i).filter((_, i) => i % 5 === 0),
				datasets: [
					{
						label: 'Population',
						data: raw,
						borderColor: 'rgba(100, 180, 255, 0.3)',
						backgroundColor: 'rgba(100, 180, 255, 0.05)',
						tension: 0.3,
						fill: true,
						pointRadius: 0,
					},
					{
						label: 'Trend',
						data: rollingAverage(raw, 100),
						borderColor: 'rgb(255, 180, 100)',
						tension: 0.3,
						fill: false,
						pointRadius: 0,
					},
					{
						label: 'Average',
						data: Array(raw.length).fill(getAverage(raw)),
						borderColor: 'rgb(168, 168, 168)',
						fill: false,
						pointRadius: 0,
					},
					{
						label: 'Down Sampled',
						data: raw.filter((_, i) => i % 5 === 0),
						borderColor: 'rgb(100, 100, 0)',
						fill: false,
						pointRadius: 1,
						tension: 0.2,
					}
				]
			}
		});

		return () => chart.destroy();
	});
</script>

<div class="wrapper">
	{#if !fileData}
		<input type="file" accept=".json" onchange={fileChange}>
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