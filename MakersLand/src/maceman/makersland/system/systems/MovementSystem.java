package maceman.makersland.system.systems;

import java.util.ArrayList;

import maceman.makersland.component.components.Spacial;
import maceman.makersland.entity.Entity;
import maceman.makersland.entity.EntityManager;
import maceman.makersland.system.System;

public class MovementSystem implements System {

	public ArrayList<Spacial> components;
	public EntityManager em;

	public MovementSystem(EntityManager em) {
		this.em = em;
	}

	@Override
	public void process(int entityID) {

	}

	@Override
	public void addComponentTo(int entityID) {
		if (em.getIndex().size() < entityID) {
			components.add(entityID, new Spacial(new Entity(entityID, true), 0,
					0,true));
		} else {
			components.set(entityID, new Spacial(new Entity(entityID, true), 0,
					0,true));
		}
	}

	@Override
	public void updateComponent(int entityID) {
		components.set(entityID, new Spacial(new Entity(entityID, true), 0, 0,true));
	}

	@Override
	public void removeComponent(int entityID) {
		components.set(entityID, null);
	}

	public void setPosition(Entity entity, int x, int y) {
		components.get((int) entity.id).X = x;
		components.get((int) entity.id).Y = y;
	}

	
	// Movement
	public void moveNorth(Entity entity) {
		components.get((int) entity.id).Y = components.get((int) entity.id).Y - 1;
	}

	public void moveNorthEast(Entity entity) {
		components.get((int) entity.id).X = components.get((int) entity.id).X + 1;
		components.get((int) entity.id).Y = components.get((int) entity.id).Y - 1;
	}

	public void moveEast(Entity entity) {
		components.get((int) entity.id).X = components.get((int) entity.id).X + 1;
	}

	public void moveSouthEast(Entity entity) {
		components.get((int) entity.id).X = components.get((int) entity.id).X + 1;
		components.get((int) entity.id).Y = components.get((int) entity.id).Y + 1;
	}

	public void moveSouth(Entity entity) {
		components.get((int) entity.id).Y = components.get((int) entity.id).Y - 1;
	}

	public void moveSouthWest(Entity entity) {
		components.get((int) entity.id).X = components.get((int) entity.id).X - 1;
		components.get((int) entity.id).Y = components.get((int) entity.id).Y + 1;
	}

	public void moveWest(Entity entity) {
		components.get((int) entity.id).X = components.get((int) entity.id).X + 1;
	}

	public void moveNorthWest(Entity entity) {
		components.get((int) entity.id).X = components.get((int) entity.id).X - 1;
		components.get((int) entity.id).Y = components.get((int) entity.id).Y - 1;
	}

}
