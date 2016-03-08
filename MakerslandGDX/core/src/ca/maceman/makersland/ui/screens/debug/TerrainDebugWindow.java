package ca.maceman.makersland.ui.screens.debug;

import ca.maceman.makersland.ui.screens.GameView;
import ca.maceman.makersland.world.terrain.Terrain;
import com.badlogic.gdx.Gdx;
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

public class TerrainDebugWindow {

	// debug menu
	private Label lblFPS,
			lblChunksHeight,
			lblChunksWidth,
			lblOctaveCount,
			lblOctaves,
			lblScale,
			lblSeaLevel,
			lblStrength;
	private Skin skin;
	private Slider 
			sliderChunksHeight,
			sliderChunksWidth,
			sliderOctaveCount,
			sliderOctaves,
			sliderScale,
			sliderSeaLevel,
			sliderStrength;
	public Stage stage;
	private TextField txtSeed;
	private GameView gameView;

	public TerrainDebugWindow(GameView gameView) {
		this.gameView = gameView;

		setupWindow();
	}

	/**
	 * Builds the window
	 */
	private void setupWindow() {

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage();
		Table debugTable = new Table();
		debugTable.row();

		debugTable.layout();

		final CheckBox cbIsland = new CheckBox("Island", skin);
		cbIsland.setChecked(true);

		txtSeed = new TextField("seed", skin);

		lblChunksWidth = new Label("Chunks Width:", skin);
		sliderChunksWidth = new Slider(1, 10, 1, false, skin);
		sliderChunksWidth.setValue(4f);

		lblChunksHeight = new Label("Chunks Length:", skin);
		sliderChunksHeight = new Slider(1, 10, 1, false, skin);
		sliderChunksHeight.setValue(4f);

		lblOctaves = new Label("Octave:", skin);
		sliderOctaves = new Slider(1, 10, 1, false, skin);

		lblOctaveCount = new Label("Octave Count:", skin);
		sliderOctaveCount = new Slider(1, 10, 1, false, skin);
		sliderOctaveCount.setValue(7f);

		lblStrength = new Label("Strength:", skin);
		sliderStrength = new Slider(1, 5, 0.2f, false, skin);
		sliderStrength.setValue(3.5f);

		lblScale = new Label("Scale:", skin);
		sliderScale = new Slider(1, 10, 1f, false, skin);
		sliderScale.setValue(4f);

		lblSeaLevel = new Label("Sea level:", skin);
		sliderSeaLevel = new Slider(1, 20, 1f, false, skin);
		sliderSeaLevel.setValue(4f);

		Button buttonGenerate = new TextButton("Generate New World", skin);
		Button buttonUpdateVals = new TextButton("Update values", skin);
		lblFPS = new Label("fps:", skin);

		Window debugWindow = new Window("Terrain Debug", skin);
		TextButton btnX = new TextButton("X", skin);
		btnX.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameView.debug = false;
			}
		});

		debugWindow.getButtonTable().add(btnX).height(debugWindow.getPadTop());

		debugWindow.setPosition(0, 0);
		debugWindow.defaults().spaceBottom(10);
		debugWindow.row();
		debugWindow.add(txtSeed);
		debugWindow.row();
		debugWindow.add(cbIsland);
		debugWindow.row().fill().expandX();
		debugWindow.add(lblChunksWidth);
		debugWindow.row().fill().expandX();
		debugWindow.add(sliderChunksWidth);
		debugWindow.row();
		debugWindow.add(lblChunksHeight);
		debugWindow.row().fill().expandX();
		debugWindow.add(sliderChunksHeight);
		debugWindow.row();
		debugWindow.add(lblOctaves);
		debugWindow.row().fill().expandX();
		debugWindow.add(sliderOctaves);
		debugWindow.row();
		debugWindow.add(lblOctaveCount);
		debugWindow.row().fill().expandX();
		debugWindow.add(sliderOctaveCount);
		debugWindow.row();
		debugWindow.add(lblStrength);
		debugWindow.row().fill().expandX();
		debugWindow.add(sliderStrength);
		debugWindow.row();
		debugWindow.add(lblScale);
		debugWindow.row().fill().expandX();
		debugWindow.add(sliderScale);
		debugWindow.row();
		debugWindow.add(lblSeaLevel);
		debugWindow.row().fill().expandX();
		debugWindow.add(sliderSeaLevel);
		debugWindow.row();
		debugWindow.add(buttonUpdateVals);
		debugWindow.row();
		debugWindow.add(buttonGenerate);
		debugWindow.row();
		debugWindow.add(lblFPS).colspan(4);
		debugWindow.pack();

		stage.addActor(debugWindow);

		buttonGenerate.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				long seed = 0;
				try {
					seed = (Long.parseLong(txtSeed.getText()));
				} catch (Exception e) {

				}
				gameView.setTerrain(new Terrain(
						(int) sliderOctaves.getValue(),
						(int) sliderOctaveCount.getValue(),
						sliderStrength.getValue(),
						sliderScale.getValue(),
						(int) sliderChunksWidth.getValue(),
						(int) sliderChunksHeight.getValue(),
						0,
						cbIsland.isChecked(), seed));
				stage.setKeyboardFocus(null);
				gameView.refreshModels();
			}
		});

		buttonUpdateVals.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameView.getTerrain().setScale(sliderScale.getValue());
				gameView.getTerrain().setStrength(sliderStrength.getValue());
				gameView.getOcean().setSeaLevel(sliderSeaLevel.getValue());
				gameView.refreshModels();
			}
		});

		sliderSeaLevel.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameView.getOcean().setSeaLevel(sliderSeaLevel.getValue());
				gameView.refreshModels();
			}
		});
	}

	/**
	 * updates UI with new values
	 */
	public void updateUI(float time) {
		lblFPS.setText("fps: " + Gdx.graphics.getFramesPerSecond() + " Time: " + Float.toString(Math.round(time)));
		lblChunksWidth.setText("Chunks Width: " + sliderChunksWidth.getValue());
		lblChunksHeight.setText("Chunks Height: " + sliderChunksHeight.getValue());
		lblScale.setText("Scale: " + sliderScale.getValue());
		lblOctaves.setText("Octave: " + sliderOctaves.getValue());
		lblOctaveCount.setText("Octave Count: " + sliderOctaveCount.getValue());
		lblStrength.setText("Strength: " + sliderStrength.getValue());
		lblSeaLevel.setText("Sea Level: " + sliderSeaLevel.getValue());
	}
}
