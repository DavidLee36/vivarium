package vivarium;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.*;

import vivarium.map.*;
import vivarium.utils.Logger;

public class Vivarium extends ApplicationAdapter {
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private ScreenViewport viewport;
	private SpriteBatch batch;
	private BitmapFont font;

	private World world;
	private static final float HEX_SIZE = 40;

	@Override
	public void create() {
		initGdx();
		initSimulation();
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();
	}

	@Override
	public void render() {
		pan();
		draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, false);
	}

	// #region Non-Public Methods

	/**
	 * Handle panning with mouse
	 */
	private void pan() {
		if (Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.MIDDLE)) {
			float dx = -Gdx.input.getDeltaX() * camera.zoom; // How many pixels the mouse moved X since last frame
			float dy = Gdx.input.getDeltaY() * camera.zoom; // How many pixels the mouse moved Y since last frame
			camera.position.x += dx;
			camera.position.y += dy;
		}
		camera.update();
	}

	/**
	 * Initialize all Gdx related objects and settings
	 */
	private void initGdx() {
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
	}

	/**
	 * Initialize all simulation (my shit) objects and settings
	 */
	private void initSimulation() {
		Logger.clear();
		world = new World();
	}

	/**
	 * Main draw method — clears the screen and renders each layer in order.
	 * Order matters: filled hexes first, then outlines on top, then text on top of everything.
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
