package ca.maceman.makersland.world.terrain.parts;

import com.badlogic.gdx.math.Vector3;

public class TerrainVector {

	public float x;
	public float y;
	public float z;
	public float u;
	public float v;

	public TerrainVector(float x, float y, float z) {

		this.x = x;
		this.y = y;
		this.z = z;
		this.u = (x / (float) TerrainTile.TILE_SIZE);
		this.v = (x / (float) TerrainTile.TILE_SIZE);
	}
	
	public Vector3 toVector3() {
		return new Vector3(x, y, z);
	}

}
