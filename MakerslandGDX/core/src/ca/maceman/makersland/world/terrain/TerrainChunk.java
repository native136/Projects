package ca.maceman.makersland.world.terrain;

import ca.maceman.makersland.world.terrain.cell.Cell;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Part of the terrain model is generated here. Creates different landscapes
 * based on the values given.
 * 
 * @author andy.masse
 * 
 */
public class TerrainChunk {

	private boolean isIsland = false;

	private float scale;
	private float strength;

	private int borderSize;
	private int octave;
	private int octaveCount;
	private int posX;
	private int posZ;

	public Cell[][] cells;
	public float[][] chunkDepths;

	private Terrain parent;

	public static final short height = 40;
	public static final short width = 40;

	public TerrainChunk(Terrain parent, int octave, int octaveCount, float scale,
			float strength, int posX, int posZ, int borderSize, boolean isIsland) {

		this.parent = parent;
		this.octave = octave;
		this.octaveCount = octaveCount;
		this.scale = scale;
		this.strength = strength;
		this.posX = posX;
		this.posZ = posZ;
		this.borderSize = borderSize;
		this.isIsland = isIsland;
		this.cells = new Cell[width][height];

	}

	public TerrainChunk(float[][] chunksDepths, float scale, float strength,
			int posX, int posZ, int borderSize, boolean isIsland) {
		this.chunkDepths = chunksDepths;
		this.scale = scale;
		this.strength = strength;
		this.posX = posX;
		this.posZ = posZ;
		this.borderSize = borderSize;
		this.isIsland = isIsland;
		this.cells = new Cell[width][height];

		buildVertices();

	}

	public void buildVertices() {
		int heightPitch = height;
		int widthPitch = width;

		Vector3 p1;
		Vector3 p2;
		Vector3 p3;
		Vector3 p4;
		Vector3 n1;
		Vector3 n2;
		Color c1;
		Color c2;
		Color c3;
		Color c4;
		float u = 0;
		float v = 0;

		for (int x = 0; x < widthPitch; x++) {
			for (int z = 0; z < heightPitch; z++) {

				/* o--o
				 * |  |
				 * o--X
				 */
				p1 = new Vector3(((posX * width) * scale) + (scale * x),
						(float) Math.pow(1 + chunkDepths[(posX * width) + x][(posZ * height) + z], strength),
						((posZ * height) * scale) + (scale * z));

				/* o--o
				 * |  |
				 * X--o
				 */
				p2 = new Vector3(((posX * width) * scale) + (scale * (x + 1)),
						(float) Math.pow(1 + chunkDepths[(posX * width) + x + 1][(posZ * height) + z], strength),
						((posZ * height) * scale) + (scale * z));
				/* X--o
				 * |  |
				 * o--o
				 */
				p3 = new Vector3(((posX * width) * scale) + (scale * (x + 1)),
						(float) Math.pow(1 + chunkDepths[(posX * width) + x + 1][(posZ * height) + z + 1], strength),
						((posZ * height) * scale) + (scale * (z + 1)));

				/* o--X
				 * |  |
				 * o--o
				 */
				p4 = new Vector3(((posX * width) * scale) + (scale * x),
						(float) Math.pow(1 + chunkDepths[(posX * width) + x][(posZ * height) + z + 1], strength),
						((posZ * height) * scale) + (scale * (z + 1)));

				n1 = calcNormal(p1, p4, p2);
				n2 = calcNormal(p3, p2, p4);

				c1 = findColour(chunkDepths[(posX * width) + x][(posZ * height) + z]);
				c2 = findColour(chunkDepths[(posX * width) + x + 1][(posZ * height) + z]);
				c3 = findColour(chunkDepths[(posX * width) + x + 1][(posZ * height) + z + 1]);
				c4 = findColour(chunkDepths[(posX * width) + x][(posZ * height) + z + 1]);

				u = (x / (float) width);
				v = (z / (float) height);

				cells[x][z] = new Cell(p1, p2, p3, p4, n1, n2, c1, c2, c3, c4, new Vector2(u, v));

			}
		}
	}

	private Color findColour(float f) {
		// COLOR
		Color c = Color.BLACK;
		if (f <= .1) {
			c = new Color(0, .3f, .5f, 1);
		} else if (f > .1 && f <= .2) {
			c = new Color(0, .5f, .79f, 1);
		} else if (f > .2 && f <= .3) {
			c = new Color(.7f, .6f, .3f, 1);
		} else if (f > .3 && f <= .4) {
			c = new Color(0, .45f, 0, 1);
		} else if (f > .4 && f <= .6) {
			c = new Color(0, .2f, 0, 1);
		} else if (f > .6 && f <= .7) {
			c = new Color(.4f, .3f, .2f, 1);
		} else if (f > .7 && f <= .8) {
			c = new Color(.7f, .7f, .7f, 1);
		} else if (f > .8) {
			c = Color.WHITE;
		}
		return c;
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
