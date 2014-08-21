package ca.maceman.makersland.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class LandManipulationGUI implements GUI {

	public Stage stage;
	private Skin skin;
	private TextButton btnSelectArea;
	private TextButton btnRaiseTerrain;
	private TextButton btnLowerTerrain;
	private Table table;
	
	public LandManipulationGUI(){
		create();
	}
	
	@Override
	public void create() {
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage();
		table = new Table();
		
		table.row();
		table.layout();

		btnSelectArea = new TextButton("Select Area", skin);
		btnRaiseTerrain = new TextButton("Raise Terrain", skin);
		btnLowerTerrain = new TextButton("Lower Terrain", skin);
		Window landWindow = new Window("Land Manipulation", skin);

		landWindow.setPosition(0, 0);		
		
		landWindow.defaults().spaceBottom(10);
		landWindow.row();
		landWindow.add(btnSelectArea);
		landWindow.row();		
		landWindow.add(btnRaiseTerrain);
		landWindow.row();		
		landWindow.add(btnLowerTerrain);
		landWindow.pack();
		
		stage.addActor(landWindow);
		
		btnSelectArea.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				
			}
		});
		btnRaiseTerrain.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				
			}
		});
		btnLowerTerrain.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void dispose() {

	}

	@Override
	public void refresh() {
	}
	
	@Override
	public void HandleTouchDown(int screenX, int screenY, int pointer, int button) {
		

	}

	@Override
	public void HandleTouchUp(int screenX, int screenY, int pointer, int button) {
		

	}

	@Override
	public void HandleTouchDragged(int screenX, int screenY, int pointer) {
		

	}

}
