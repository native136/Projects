package maceman.makersland.component;

import maceman.makersland.entity.Entity;

public abstract class Component {
	
	private Entity owner;
	
	public Component(Entity owner){
		this.owner = owner;
	}

}
