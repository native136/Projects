package maceman.makersland.entity;

import java.util.ArrayList;
import maceman.makersland.component.Component;

public class EntityManager {

	public ArrayList<Entity> index;

	public int getNextID() {
		/*
		 * Get the next available ID
		 */
		for (int i = 0; i < index.size(); i++) {
			if ((index.get(i)) == null) {
				index.add(i,new Entity(i,true));
				return i;

			}
		}
		index.add((index.size() - 1),new Entity((index.size() - 1),true));
		return (index.size() - 1);
	}

	public void removeEntity(int entityID) {
		if (entityID < index.size()) {
			//position.removeDataFor(entityID);

		}
	}

	public ArrayList<Entity> getIndex() {
		// TODO Auto-generated method stub
		return index;
	}

}
