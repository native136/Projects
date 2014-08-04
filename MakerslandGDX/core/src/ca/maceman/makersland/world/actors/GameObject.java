package ca.maceman.makersland.world.actors;

import ca.maceman.makersland.world.actors.collisionshapes.Shape;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class GameObject extends ModelInstance {
	public Shape shape;
	public boolean moving = false;
	public float speed = 5f;
	public Vector2 position;
	public Vector2 destination;
	public float distanceToDest = 0;
	public boolean handled = false;

	public GameObject(Model model, Vector3 position) {
		super(model, position);
	}

	public boolean isVisible(Camera cam) {
		return shape == null ? false : shape.isVisible(transform, cam);
	}

	public float intersects(Ray ray) {
		return shape == null ? -1f : shape.intersects(transform, ray);
	}

	public void move(float delta){
		if(position!=destination && handled == false){
			
			moving = true;
			distanceToDest =  (float) Math.sqrt(Math.pow(destination.x-position.x,2)+Math.pow(destination.y-position.y,2));
			if (distanceToDest<0.01f){
				position=destination;
				moving = false;
			}
			float directionX = (destination.x-position.x) / distanceToDest;
			float directionY = (destination.y-position.y) / distanceToDest;
			
			position.x += directionX * speed * delta;
			position.y += directionY * speed * delta;
		}else{
			moving = false;
		}
	}
}
