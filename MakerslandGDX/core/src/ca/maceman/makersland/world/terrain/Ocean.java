package ca.maceman.makersland.world.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Ocean {

	private float seaLevel;
	private Model oceanModel;
	public ModelInstance oceanModelInstance;

	public Ocean(float seaLevel, Terrain terrain) {
		this.seaLevel = seaLevel;
		ModelBuilder mb = new ModelBuilder();

		oceanModel = mb.createCylinder(terrain.getWidthUnits() * 10, this.seaLevel, terrain.getWidthUnits() * 10, 36, new Material(ColorAttribute.createDiffuse(Color.TEAL)),
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		oceanModelInstance = new ModelInstance(oceanModel);
		oceanModelInstance.transform.setToRotation(Vector3.X, 90);
		oceanModelInstance.transform.translate(
				terrain.getWidthUnits() / 2,
				0,
				-terrain.getWidthUnits() / 2);

	}

	public float getSeaLevel() {
		return seaLevel;
	}

	public void setSeaLevel(float seaLevel) {
		this.seaLevel = seaLevel;
	}

}
