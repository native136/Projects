package ca.maceman.makersland.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import ca.maceman.makersland.MakersLandGame;

public class TerrainDebugScreen extends AbstractScreen {

	private static String[] mapSizeListEntries = { "Tiny", "Small", "Medium", "Large",
			"Gigantic" };
	private static String[] mapTypeListEntries = { "Island", "Coast", "Archipelago" };

	private Skin skin;
	private Stage stage;
	private Label fpsLabel;
	private Label lblOctaves;
	private Label lblOctaveCount;
	private Label lblStrength;
	private Slider sliderOctaves;
	private Slider sliderOctaveCount;
	private Slider sliderStrength;

	public TerrainDebugScreen(MakersLandGame game) {
		super();
		create();
	}

	@Override
	public void render(float delta) {
		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		lblOctaves.setText("Octave: " + sliderOctaves.getValue());
		lblOctaveCount.setText("Octave Count: " + sliderOctaveCount.getValue());
		lblStrength.setText("Strength: " + sliderStrength.getValue());

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
//		Table.drawDebug(stage);

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
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
	public void create() {

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		Button buttonGenerate = new TextButton("Generate New World", skin);

		Table debugTable = new Table();
		debugTable.row();

		debugTable.layout();

		lblOctaves = new Label("Octave:", skin);
		sliderOctaves = new Slider(0, 10, 1, false, skin);

		lblOctaveCount = new Label("Octave Count:", skin);
		sliderOctaveCount = new Slider(0, 10, 1, false, skin);

		lblStrength = new Label("Strength:", skin);
		sliderStrength = new Slider(0, 5, 0.5f, false, skin);

		SelectBox<String> ddbMapSize = new SelectBox<String>(skin);
		ddbMapSize.setItems(mapSizeListEntries);
		ddbMapSize.setSelectedIndex(0);

		SelectBox<String> ddbMapType = new SelectBox<String>(skin);
		ddbMapType.setItems(mapTypeListEntries);
		ddbMapType.setSelectedIndex(0);

		fpsLabel = new Label("fps:", skin);

		Window terrainDebugWindow = new Window("Terrain Debug", skin);
		terrainDebugWindow.getButtonTable().add(new TextButton("X", skin))
				.height(terrainDebugWindow.getPadTop());

		terrainDebugWindow.setPosition(0, 0);
		terrainDebugWindow.defaults().spaceBottom(10);
		terrainDebugWindow.row().fill().expandX();
		terrainDebugWindow.add(ddbMapSize);
		terrainDebugWindow.row().fill().expandX();
		terrainDebugWindow.add(ddbMapType);
		terrainDebugWindow.row();
		terrainDebugWindow.add(lblOctaves);
		terrainDebugWindow.row().fill().expandX();
		terrainDebugWindow.add(sliderOctaves);
		terrainDebugWindow.row();
		terrainDebugWindow.add(lblOctaveCount);
		terrainDebugWindow.row().fill().expandX();
		terrainDebugWindow.add(sliderOctaveCount);
		terrainDebugWindow.row();
		terrainDebugWindow.add(lblStrength);
		terrainDebugWindow.row().fill().expandX();
		terrainDebugWindow.add(sliderStrength);
		terrainDebugWindow.row();
		terrainDebugWindow.add(buttonGenerate);
		terrainDebugWindow.row();
		terrainDebugWindow.add(fpsLabel).colspan(4);
		terrainDebugWindow.pack();

		stage.addActor(terrainDebugWindow);
	}
}
