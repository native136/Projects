package ca.maceman.makersland.ui.screens;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;

public abstract class AbstractScreen extends InputAdapter implements Screen {

	public AbstractScreen() {
	}

	/**
	 * Create all your resources here
	 */
	public abstract void create();

}
