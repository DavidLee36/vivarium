package vivarium;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.*;

import vivarium.map.Hex;
import vivarium.utils.Logger;

public class Vivarium extends ApplicationAdapter {
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private ScreenViewport viewport;
	private SpriteBatch batch;
	private BitmapFont font;
	private Timer.Task task;

	private World world;
	private static final float HEX_SIZE = 40;
	private static final float TICK_DELAY = 1f;

	//#region Public Methods

	@Override
	public void create() {
		initEngine();
		initSimulation();
	}

	@Override
	public void render() {
		pan();
		leftClick();
		draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);
	}

	public static float getHexSize() { return HEX_SIZE; }

	//#endregion

	// #region Non-Public Methods

	/**
	 * Handle panning with mouse
	 */
	private void pan() {
		if (Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.MIDDLE)) {
			float dx = -Gdx.input.getDeltaX() * camera.zoom; // How many pixels the mouse moved X since last frame
			float dy = Gdx.input.getDeltaY() * camera.zoom; // How many pixels the mouse moved Y since last frame
			camera.translate(dx, dy);
		}
		camera.update();
	}

	private void leftClick() {
		if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
			Vector3 worldCoord = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			Hex hex = world.getMap().getHexAt(worldCoord);
			if (hex != null) {
				hex.log();
			}
		}
	}

	/**
	 * Initialize all engine related objects and data
	 */
	private void initEngine() {
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera.position.set(0, 0, 0);

		Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
			@Override
			public boolean scrolled(float amountX, float amountY) {
				camera.zoom += amountY * 0.1f;
				camera.zoom = Math.max(0.1f, Math.min(camera.zoom, 5f));
				return true;
			}
		});

		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();
	}

	/**
	 * Initialize all simulation related objects and data
	 */
	private void initSimulation() {
		Logger.clear();
		world = new World();

		task = new Timer.Task() {
			@Override
			public void run() {
				world.runTick();
			}
		};
		Timer.schedule(task, TICK_DELAY, TICK_DELAY);
	}

	/**
	 * Main draw method — clears the screen and renders each layer in order.
	 * Order matters: filled hexes first, then outlines on top, then text on top of
	 * everything.
	 */
	private void draw() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.12f, 1);
		Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);

		drawFilledHexes();
		drawHexOutlines();
		drawLabels();
	}

	/**
	 * Draws each hex filled with its biome color.
	 * Uses ShapeType.Filled — draws solid triangles to fill each hex.
	 */
	private void drawFilledHexes() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for (vivarium.map.Hex hex : world.getMap().getHexes()) {
			shapeRenderer.setColor(hex.getBiome().getColor());
			fillHex(hex.getCenterX(HEX_SIZE), hex.getCenterY(HEX_SIZE), HEX_SIZE);
		}
		shapeRenderer.end();
	}

	/**
	 * Draws white outlines around each hex.
	 * Uses ShapeType.Line — must be a separate begin/end block from Filled.
	 */
	private void drawHexOutlines() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(1, 1, 1, 1);
		for (vivarium.map.Hex hex : world.getMap().getHexes()) {
			drawHex(hex.getCenterX(HEX_SIZE), hex.getCenterY(HEX_SIZE), HEX_SIZE);
		}
		shapeRenderer.end();
	}

	/**
	 * Draws coordinate labels inside each hex.
	 * Uses SpriteBatch — text is rendered as textured images, not shapes,
	 * so it needs its own separate begin/end block.
	 */
	private void drawLabels() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (vivarium.map.Hex hex : world.getMap().getHexes()) {
			String label = hex.getQ() + "," + hex.getR() + "," + hex.getS();
			font.draw(batch, label, hex.getCenterX(HEX_SIZE) - 10, hex.getCenterY(HEX_SIZE) + 5);
		}
		batch.end();
	}

	private void fillHex(float cx, float cy, float size) {
		for (int i = 0; i < 6; i++) {
			float angle1 = (float) Math.toRadians(60 * i);
			float angle2 = (float) Math.toRadians(60 * (i + 1));
			float x1 = cx + size * (float) Math.cos(angle1);
			float y1 = cy + size * (float) Math.sin(angle1);
			float x2 = cx + size * (float) Math.cos(angle2);
			float y2 = cy + size * (float) Math.sin(angle2);
			shapeRenderer.triangle(cx, cy, x1, y1, x2, y2);
		}
	}

	/**
	 * Draws a ingle hexagon outline
	 * 
	 * @param cx   center x in pixel coordinates
	 * @param cy   center y in pixel coordinates
	 * @param size distance from center to corner
	 */
	private void drawHex(float cx, float cy, float size) {
		for (int i = 0; i < 6; i++) {
			float angle1 = (float) Math.toRadians(60 * i);
			float angle2 = (float) Math.toRadians(60 * (i + 1));
			float x1 = cx + size * (float) Math.cos(angle1);
			float y1 = cy + size * (float) Math.sin(angle1);
			float x2 = cx + size * (float) Math.cos(angle2);
			float y2 = cy + size * (float) Math.sin(angle2);
			shapeRenderer.line(x1, y1, x2, y2);
		}
	}

	// #endregion

}
