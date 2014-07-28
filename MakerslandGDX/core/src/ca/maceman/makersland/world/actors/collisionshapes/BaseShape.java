package ca.maceman.makersland.world.actors.collisionshapes;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public abstract class BaseShape implements Shape {
	protected final static Vector3 position = new Vector3();
	public final Vector3 center = new Vector3();
	public final Vector3 dimensions = new Vector3();

	public BaseShape(BoundingBox bounds) {
		center.set(bounds.getCenter());
		dimensions.set(bounds.getDimensions());
	}
}