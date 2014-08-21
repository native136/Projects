package ca.maceman.makersland.ui.screens;

public interface GUI {

	void create();
	void dispose();
	void refresh();
	void HandleTouchDown(int screenX, int screenY, int pointer, int button);

	void HandleTouchUp(int screenX, int screenY, int pointer, int button);

	void HandleTouchDragged(int screenX, int screenY, int pointer);

}
