package ca.maceman.makersland.world.area;

import com.badlogic.gdx.utils.Array;

import ca.maceman.makersland.world.area.modifier.Modifier;

public abstract class Area {

	private Array<Modifier> modifiers;
	
	public void addModifier(Modifier mod){
		modifiers.add(mod);
	};
	
	public void getModifier(int id){
		
	};
	
}
