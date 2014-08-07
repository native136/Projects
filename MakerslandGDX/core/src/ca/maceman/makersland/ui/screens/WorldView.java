package ca.maceman.makersland.ui.screens;

import ca.maceman.makersland.world.actors.GameObject;
import ca.maceman.makersland.world.actors.PlaceHolder;
import ca.maceman.makersland.world.actors.collisionshapes.Shape;
import ca.maceman.makersland.world.actors.collisionshapes.Sphere;
import ca.maceman.makersland.world.terrain.Terrain;
import ca.maceman.makersland.world.terrain.TerrainChunk;
import ca.maceman.makersland.world.utils.generator.NoiseGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

/**
 * Main game screen, view the terrain and all the game objects.
 * 
 * @author andy.masse
 * 
 */
public class WorldView extends AbstractScreen {

	private boolean debug = true;
	private TerrainDebugWindow terrainDebugWindow;

	private Array<GameObject> gameObjectInstances;
	private GameObject objectInHand;
	private CameraInputController camController;
	private Environment environment;
	private ModelBatch worldModelBatch;
	private ModelInstance terrainInstance;
	private PerspectiveCamera cam;
	private Terrain terrain;
	private AssetManager assets;
	private ModelInstance space;
	private Vector3 position;

	private int selected = -1, selecting = -1;
	private Material selectionMaterial, originalMaterial;
	private boolean dragged;
	private BoundingBox bounds;
	private Shape placeHolderShape;
	private float time;

	public WorldView(Terrain terrain) {
		super();
		this.terrain = terrain;

		if (debug){
			terrainDebugWindow = new TerrainDebugWindow(this);
		}
		create();
		refreshModels();
	}

	@Override
	public void create() {
		/* setup environment */
		prepareEnvironment();

		/* setup Camera */
		prepareCam();

		if (debug) {
			prepareDebugMenu();
		}

		setupInput();
		/* setup Builders and assets */
		worldModelBatch = new ModelBatch();

		assets = new AssetManager();
		assets.load("data/spacesphere.obj", Model.class);
		assets.finishLoading();
		space = new ModelInstance(assets.get("data/spacesphere.obj", Model.class));
		space.transform.scl(200);

		selectionMaterial = new Material();
		selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE));
		originalMaterial = new Material();

		bounds = new BoundingBox();

		position = new Vector3();

		time = 0;
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.graphics.getGL20().glClearColor(0.5f, 0.6f, 0.8f, 1);

		time += delta;

		camController.update();

		worldModelBatch.begin(cam);

		worldModelBatch.render(terrainInstance, environment);

		for (GameObject goInstance : gameObjectInstances) {
			Vector3 tmp = new Vector3();
			goInstance.transform.getTranslation(tmp);
			goInstance.move(delta);
			goInstance.transform.setTranslation(goInstance.position.x, (float) terrain.getTerrainHeight(goInstance.position.x, goInstance.position.y) + 1, goInstance.position.y);

			if (goInstance.isVisible(cam)) {
				worldModelBatch.render(goInstance, environment);
			}
		}
		worldModelBatch.render(space);
		worldModelBatch.end();

		if (debug) {
			terrainDebugWindow.updateUI(time);
			terrainDebugWindow.stage.draw();
		}
	}

	protected void refreshModels() {
		gameObjectInstances = new Array<GameObject>();
		terrainInstance = new ModelInstance(terrain.getTerrainModel());

		/* Generate a bunch of random placeholders */
		for (int x = 0; x < 50; x++) {

			Vector3 v = new Vector3(MathUtils.random(((TerrainChunk.width * terrain.getChunksWidth()) - 1) * terrain.getScale()), 0, MathUtils.random((TerrainChunk.height * terrain.getChunksHeight() - 1) * terrain.getScale()));

			v.add(0, (float) terrain.getHeight(v.x, v.y), 0);

			GameObject unit = (new GameObject(new PlaceHolder(v.x, v.y, v.z).getModel(), v));

			unit.position = new Vector2(v.x, v.z);

			/* random destination */
			unit.destination = new Vector2(MathUtils.random((TerrainChunk.width - 1) * terrain.getScale()), MathUtils.random((TerrainChunk.height - 1) * terrain.getScale()));

			gameObjectInstances.add(unit);

			if (placeHolderShape == null) {
				gameObjectInstances.get(x).calculateBoundingBox(bounds);
				placeHolderShape = new Sphere(bounds);
			}

			gameObjectInstances.get(x).shape = placeHolderShape;
		}
	}

	private void prepareEnvironment() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
		environment.add(new DirectionalLight().set(.9f, .9f, .9f, 100f, -100f, 100f));
	}

	private void prepareCam() {
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 100f, 0f);
		cam.near = 1f;
		cam.far = 30000f;
		cam.lookAt(1000f, 0, 1000f);
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

	private void prepareDebugMenu() {
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		selecting = getObject(screenX, screenY);
		return selecting >= 0;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (selecting < 0)
			return false;
		if (selected == selecting) {
			dragged = true;
			Ray ray = cam.getPickRay(screenX, screenY);
			final float distance = -ray.origin.y / ray.direction.y;
			position.set(ray.direction).scl(distance).add(ray.origin);
			objectInHand = gameObjectInstances.get(selected);
			objectInHand.handled = true;
			objectInHand.transform.setTranslation(new Vector3(position.x, (float) (terrain.getHeight(screenX, screenY) + 10), position.z));
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (selected >= 0) {
			Material mat = gameObjectInstances.get(selected).materials.get(0);

			if (dragged) {
				dragged = false;
				gameObjectInstances.get(selected).transform.translate(0, (float) (terrain.getHeight(screenX, screenY) - 10), 0);
				gameObjectInstances.get(selected).handled = false;
			}
			mat.clear();
			mat.set(originalMaterial);
		}
		if (selecting >= 0) {
			if (selecting == getObject(screenX, screenY)) {
				setSelected(selecting);
			}
			selecting = -1;

			return true;
		}
		return false;
	}

	public void setSelected(int value) {
		if (selected == value)
			return;
		selected = value;
		if (selected >= 0) {
			Material mat = gameObjectInstances.get(selected).materials.get(0);
			originalMaterial.clear();
			originalMaterial.set(mat);
			mat.clear();
			mat.set(selectionMaterial);
		}
	}

	public int getObject(int screenX, int screenY) {
		Ray ray = cam.getPickRay(screenX, screenY);
		int result = -1;
		float distance = -1;
		for (int i = 0; i < gameObjectInstances.size; ++i) {
			final float dist2 = gameObjectInstances.get(i).intersects(ray);
			if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
				result = i;
				distance = dist2;
			}
		}
		return result;
	}

	@Override
	public void resize(int width, int height) {
		terrainDebugWindow.stage.getViewport().update(width, height, true);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Array<GameObject> getGameObjectInstances() {
		return gameObjectInstances;
	}

	public void setGameObjectInstances(Array<GameObject> gameObjectInstances) {
		this.gameObjectInstances = gameObjectInstances;
	}

	public GameObject getObjectInHand() {
		return objectInHand;
	}

	public void setObjectInHand(GameObject objectInHand) {
		this.objectInHand = objectInHand;
	}

	public CameraInputController getCamController() {
		return camController;
	}

	public void setCamController(CameraInputController camController) {
		this.camController = camController;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public ModelBatch getWorldModelBatch() {
		return worldModelBatch;
	}

	public void setWorldModelBatch(ModelBatch worldModelBatch) {
		this.worldModelBatch = worldModelBatch;
	}

	public ModelInstance getTerrainInstance() {
		return terrainInstance;
	}

	public void setTerrainInstance(ModelInstance terrainInstance) {
		this.terrainInstance = terrainInstance;
	}

	public PerspectiveCamera getCam() {
		return cam;
	}

	public void setCam(PerspectiveCamera cam) {
		this.cam = cam;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
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
		worldModelBatch.dispose();
	}
}
