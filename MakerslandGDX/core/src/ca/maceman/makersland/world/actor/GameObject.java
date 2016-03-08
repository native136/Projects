package ca.maceman.makersland.world.actor;

import ca.maceman.makersland.world.actor.shapes.Shape;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class GameObject extends ModelInstance{
	public Shape shape;
	public float distanceToDest = 0;
	public boolean handled = false;
	private boolean hidden = false;

	public GameObject(Model model, Vector3 position) {
		super(model, position);
		
	}
	public GameObject(Model model, Vector3 position,Shape shape) {
		super(model, position);
		this.shape = shape;
	}

	public GameObject(ModelInstance modelInstance, Vector3 position) {
		super(modelInstance);
		modelInstance.transform.set(position, new Quaternion());
	}
	public boolean isVisible(Camera cam) {
		if(hidden){
			return false;
		}
		return shape == null ? false : shape.isVisible(transform, cam);
	}
	
	public float intersects(Ray ray) {
		return shape == null ? -1f : shape.intersects(transform, ray);
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
