package ca.maceman.makersland.world.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Ocean {
	

	private float seaLevel;
	public Model oceanModel;
	
	public Ocean(float seaLevel) {
	
		ModelBuilder mb = new ModelBuilder();
		
		oceanModel = mb.createCylinder(100000f, 8, 100000f  , 30,  new Material(ColorAttribute.createDiffuse(Color.CYAN)),
		         Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		
		
	}

}
