package maceman.makersland.component.components;

import maceman.makersland.component.Component;
import maceman.makersland.entity.Entity;

public class Spacial extends Component {

	public int X;
	public int Y;
	public boolean canMove;
	
	public Spacial(Entity owner,int X,int Y, boolean canMove) {
		super(owner);
		this.X = X;
		this.Y = Y;
		this.canMove = canMove;
	}


}
