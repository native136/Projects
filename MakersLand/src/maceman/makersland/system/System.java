package maceman.makersland.system;

import java.util.ArrayList;

import maceman.makersland.component.Component;

public interface System  {
	

	public void process(int entityID);
	
	public void addComponentTo(int entityID);

	public void updateComponent(int entityID);

	public void removeComponent(int entityID);
}
