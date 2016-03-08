package ca.maceman.makersland.ui.screens;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;

public abstract class AbstractScreen extends InputAdapter implements Screen {

	public AbstractScreen() {
	}

	public abstract void create();

}
