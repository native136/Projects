package ca.maceman.makersland;

import ca.maceman.makersland.ui.screens.GameView;
import ca.maceman.makersland.world.terrain.Terrain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MakersLandGame extends Game {

	private GameView gameView;

	public void create() {
		
		gameView = new GameView(new Terrain(4, 5, 8f, 2, 5, 5, 0, true));
		
	}

	@Override
	public void render() {
		
		Gdx.gl.glClearColor(.3f, .55f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		setScreen(gameView);
		super.render();

	}

	@Override
	public void resize(int width, int height) {
		
		
	}

	@Override
	public void dispose() {
		gameView.dispose();
	}
}