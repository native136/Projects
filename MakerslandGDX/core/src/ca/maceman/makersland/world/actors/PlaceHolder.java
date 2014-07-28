package ca.maceman.makersland.world.actors;

import ca.maceman.makersland.world.actors.collisionshapes.Shape;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;

public class PlaceHolder {
	private Model model;
	public Shape placeholderShape;
	

	public PlaceHolder(float x, float y, float z){
		
		if (model== null){
			generateModel();
		}
	}
	
	private void generateModel() {
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		MeshPartBuilder meshBuilder;
		meshBuilder = modelBuilder.part("part1", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,  new Material(ColorAttribute.createDiffuse(MathUtils.random(), MathUtils.random(),MathUtils.random(), 1)));
		meshBuilder.cone(1, 2, 1, 10);
		Node node = modelBuilder.node();
		node.translation.set(0,1,0);
		meshBuilder = modelBuilder.part("part2", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,  new Material(ColorAttribute.createDiffuse(MathUtils.random(), MathUtils.random(),MathUtils.random(), 1)));
		meshBuilder.sphere(1, 1, 1, 10, 10);
		model = modelBuilder.end();
		
	}

	public Model getModel() {
		return model;
	}


	public void setModel(Model model) {
		this.model = model;
	}

	public Shape getPlaceholderShape() {
		return placeholderShape;
	}

	public void setPlaceholderShape(Shape placeholderShape) {
		this.placeholderShape = placeholderShape;
	}

}
