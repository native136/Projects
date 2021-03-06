package ca.maceman.makersland.world.terrain;

import ca.maceman.makersland.world.terrain.Cell.CellType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Part of the terrain model is generated here. Creates different landscapes
 * based on the values given. Creates an 2D array of Cells.
 * 
 * @author andy.masse
 * 
 */
public class TerrainChunk {

	public Cell[][] cells;

	private float scale;
	private float strength;
	private float[][] chunkDepths;
	private int posX;
	private int posZ;

	private Vector3 p1;
	private Vector3 p2;
	private Vector3 p3;
	private Vector3 p4;

	private Vector3 n1;
	private Vector3 n2;

	private float u = 0;
	private float v = 0;

	/* 70 is used as it is a nice round number and allows for less than the maximum amount of vertices(32,767) in a mesh. 
	 * Every triangle has 3 unique vertices, every square has 2 triangles. (70 * 70 * 6) =  29,400
	 */
	public static final short height = 70;
	public static final short width = 70;

	/**
	 * Creates a Terrain Chunk
	 * 
	 * @param chunksDepths
	 * @param scale
	 * @param strength
	 * @param posX
	 * @param posZ
	 * @param borderSize
	 * @param isIsland
	 */
	public TerrainChunk(float[][] chunksDepths, float scale, float strength,
			int posX, int posZ, int borderSize, boolean isIsland) {
		this.chunkDepths = chunksDepths;
		this.scale = scale;
		this.strength = strength;
		this.posX = posX;
		this.posZ = posZ;
		this.cells = new Cell[width][height];

		buildVertices();

	}

	public void buildVertices() {

		for (int x = 0; x < width; x++) {
			for (int z = 0; z < height; z++) {

				/* o--o
				 * |  |
				 * o--X
				 */
				p1 = new Vector3((
						(posX * width) * scale) + (scale * x),
						(float) Math.pow(1 + chunkDepths[(posX * width) + x][(posZ * height) + z], strength),
						((posZ * height) * scale) + (scale * z));

				/* o--o
				 * |  |
				 * X--o
				 */
				p2 = new Vector3((
						(posX * width) * scale) + (scale * (x + 1)),
						(float) Math.pow(1 + chunkDepths[(posX * width) + x + 1][(posZ * height) + z], strength),
						((posZ * height) * scale) + (scale * z));
				/* X--o
				 * |  |
				 * o--o
				 */
				p3 = new Vector3((
						(posX * width) * scale) + (scale * (x + 1)),
						(float) Math.pow(1 + chunkDepths[(posX * width) + x + 1][(posZ * height) + z + 1], strength),
						((posZ * height) * scale) + (scale * (z + 1)));

				/* o--X
				 * |  |
				 * o--o
				 */
				p4 = new Vector3((
						(posX * width) * scale) + (scale * x),
						(float) Math.pow(1 + chunkDepths[(posX * width) + x][(posZ * height) + z + 1], strength),
						((posZ * height) * scale) + (scale * (z + 1)));

				n1 = calcNormal(p1, p4, p2);
				n2 = calcNormal(p3, p2, p4);

				u = (x / (float) width);
				v = (z / (float) height);

				cells[x][z] = new Cell(p1, p2, p3, p4, n1, n2, getType(chunkDepths[(posX * width) + x][(posZ * height) + z]), new Vector2(u, v));

			}
		}
	}

	private CellType getType(float f) {
		CellType ct = CellType.DIRT;

		if (f < .2) {
			ct = CellType.SAND;
		}
		if (f > .7) {
			ct = CellType.STONE;

		}
		return ct;
	}

	/*
	 * Calculates the normals
	 */
	private Vector3 calcNormal(Vector3 p1, Vector3 p2, Vector3 p3) {

		// u = p3 - p1
		float ux = p3.x - p1.x;
		float uy = p3.y - p1.y;
		float uz = p3.z - p1.z;

		// v = p2 - p1
		float vx = p2.x - p1.x;
		float vy = p2.y - p1.y;
		float vz = p2.z - p1.z;

		// n = cross(v, u)
		float nx = ((vy * uz) - (vz * uy));
		float ny = ((vz * ux) - (vx * uz));
		float nz = ((vx * uy) - (vy * ux));

		// // normalize(n)
		float num2 = ((nx * nx) + (ny * ny)) + (nz * nz);
		float num = 1f / (float) Math.sqrt(num2);
		nx *= num;
		ny *= num;
		nz *= num;

		return new Vector3(nx, ny, nz);
	}
}
