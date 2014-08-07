package ca.maceman.makersland.ui.screens;

import ca.maceman.makersland.world.terrain.Terrain;
import ca.maceman.makersland.world.utils.generator.NoiseGenerator;

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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class TerrainDebugWindow {

	// debug menu
	private Label lblFPS,
			lblBorder,
			lblChunksHeight,
			lblChunksWidth,
			lblOctaveCount,
			lblOctaves,
			lblScale,
			lblStrength;
	private Skin skin;
	private Slider sliderBorder,
			sliderChunksHeight,
			sliderChunksWidth,
			sliderOctaveCount,
			sliderOctaves,
			sliderScale,
			sliderStrength;
	public Stage stage;
	private TextField txtSeed;
	private WorldView worldView;

	public TerrainDebugWindow(WorldView worldView) {
		this.worldView = worldView;

		setupWindow();
	}

	/**
	 *  Builds the window
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
		sliderChunksWidth.setValue(1f);

		lblChunksHeight = new Label("Chunks Length:", skin);
		sliderChunksHeight = new Slider(1, 10, 1, false, skin);
		sliderChunksHeight.setValue(1f);

		lblOctaves = new Label("Octave:", skin);
		sliderOctaves = new Slider(1, 10, 1, false, skin);

		lblOctaveCount = new Label("Octave Count:", skin);
		sliderOctaveCount = new Slider(1, 10, 1, false, skin);
		sliderOctaveCount.setValue(6f);

		lblStrength = new Label("Strength:", skin);
		sliderStrength = new Slider(1, 20, 0.5f, false, skin);

		lblScale = new Label("Scale:", skin);
		sliderScale = new Slider(2, 128, 1f, false, skin);
		sliderScale.setValue(8f);

		lblBorder = new Label("Border:", skin);
		sliderBorder = new Slider(0, 10, 1, false, skin);

		Button buttonGenerate = new TextButton("Generate New World", skin);

		lblFPS = new Label("fps:", skin);

		Window debugWindow = new Window("Terrain Debug", skin);
		TextButton btnX = new TextButton("X", skin);
		btnX.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				worldView.setDebug(false);
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
		debugWindow.add(lblBorder);
		debugWindow.row().fill().expandX();
		debugWindow.add(sliderBorder);
		debugWindow.row();
		debugWindow.add(buttonGenerate);
		debugWindow.row();
		debugWindow.add(lblFPS).colspan(4);
		debugWindow.pack();

		stage.addActor(debugWindow);

		buttonGenerate.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				try {
					NoiseGenerator.setSeed(Long.parseLong(txtSeed.getText()));
				} catch (Exception e) {

				}
				worldView.setTerrain(new Terrain(
						(int) sliderOctaves.getValue(),
						(int) sliderOctaveCount.getValue(),
						sliderStrength.getValue(),
						sliderScale.getValue(),
						(int) sliderChunksWidth.getValue(),
						(int) sliderChunksHeight.getValue(),
						(int) sliderBorder.getValue(),
						cbIsland.isChecked()));
				stage.setKeyboardFocus(null);
				worldView.refreshModels();
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
		lblBorder.setText("Border: " + sliderBorder.getValue());
	}
}
