package ca.maceman.makersland.utils.physics;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.utils.Array;

import ca.maceman.makersland.world.actors.GameObject;

public class WorldContactListener extends ContactListener {
	private Array<GameObject> instances;

	@Override
	public boolean onContactAdded(int userValue0, int partId0, int index0,
			int userValue1, int partId1, int index1) {
		instances.get(userValue0).moving = false;
		instances.get(userValue1).moving = false;
		return true;
	}
}
