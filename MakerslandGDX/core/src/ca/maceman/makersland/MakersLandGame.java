package ca.maceman.makersland;

import ca.maceman.makersland.ui.screens.WorldView;
import ca.maceman.makersland.world.terrain.Terrain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MakersLandGame extends Game {
	
	private WorldView worldView;

	public void create () {
		worldView = new WorldView(new Terrain(1, 6, 8f, 10, 3, 3,1,true));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		setScreen(worldView);
		
	    super.render();

	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void dispose () {
		worldView.dispose();
	}
}