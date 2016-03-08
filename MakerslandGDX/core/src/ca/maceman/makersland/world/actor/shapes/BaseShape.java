package ca.maceman.makersland.world.actor.shapes;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public abstract class BaseShape implements Shape {
	public Vector3 position = new Vector3();
	public Vector3 center = new Vector3();
	public Vector3 dimensions = new Vector3();

	public BaseShape(BoundingBox bounds) {
		center = bounds.getCenter();
		dimensions = bounds.getDimensions();
	}
}