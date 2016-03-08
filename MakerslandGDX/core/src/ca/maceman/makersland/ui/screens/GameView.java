package ca.maceman.makersland.ui.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

import ca.maceman.makersland.ui.screens.debug.TerrainDebugWindow;
import ca.maceman.makersland.world.actor.GameObject;
import ca.maceman.makersland.world.terrain.Ocean;
import ca.maceman.makersland.world.terrain.Terrain;

public class GameView extends AbstractScreen {

	public boolean debug = true;
	private CameraInputController camController;
	private Environment environment;
	private float time;
	private ModelBatch worldModelBatch;
	private ModelInstance terrainInstance;
	private ModelInstance oceanInstance;
	private PerspectiveCamera cam;
	private Terrain terrain;
	private Ocean ocean;
	private TerrainDebugWindow terrainDebugWindow;
	private ArrayList<GameObject> vModelList;

	public GameView() {

	}

	public GameView(Terrain terrain) {
		super();
		vModelList = new ArrayList<GameObject>();
		if (debug) {
			terrainDebugWindow = new TerrainDebugWindow(this);
		}

		this.terrain = terrain;

		ocean = new Ocean(5, terrain);
		oceanInstance = ocean.oceanModelInstance;

		create();
		refreshModels();
	}

	public void refreshModels() {
		terrainInstance = new ModelInstance(terrain.getTerrainModel());
		this.ocean = new Ocean(ocean.getSeaLevel(), terrain);
		oceanInstance = ocean.oceanModelInstance;
		vModelList = new ArrayList<GameObject>();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.graphics.getGL20().glClearColor(0.0f, 0.0f, 0.0f, 1);

		time += delta;

		camController.update();

		worldModelBatch.begin(cam);
		worldModelBatch.render(oceanInstance, environment);
		worldModelBatch.render(terrainInstance, environment);

		for (GameObject mi : vModelList) {
			if (mi.isVisible(cam)) {
				worldModelBatch.render(mi);
			}
		}

		worldModelBatch.end();

		if (debug) {
			terrainDebugWindow.updateUI(time);
			terrainDebugWindow.stage.draw();
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void create() {

		/* setup environment */
		prepareEnvironment();

		/* setup Camera */
		prepareCam();
		setupInput();

		/* setup Builders and assets */
		worldModelBatch = new ModelBatch();

		time = 0;

	}

	private void prepareEnvironment() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
		environment.add(new DirectionalLight().set(.9f, .9f, .9f, 100f, -100f, 100f));
	}

	private void prepareCam() {
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(1000f, 1000f, 200f);
		cam.near = 1f;
		cam.far = 10000f;
		cam.lookAt(100f, 100f, 0);
		camController = new CameraInputController(cam);
		camController.scrollFactor = 5f;
		camController.translateUnits = 100f;
		cam.update();
	}

	private void setupInput() {

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(terrainDebugWindow.stage);
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(camController);
		Gdx.input.setInputProcessor(multiplexer);
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public Ocean getOcean() {
		return ocean;
	}

	public void setOcean(Ocean ocean) {
		this.ocean = ocean;
	}

}
