package ca.maceman.makersland.world.terrain;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Holds the Terrain information and model.
 * 
 * @author andy.masse
 * 
 */
public class Terrain {

	private boolean isIsland = false;

	private float scale;
	private float strength;
	private int chunkBorderWidth;
	private int chunksHeight;
	private int chunksWidth;
	private int octaveCount;
	private int octaves;
	private Mesh mesh;
	private Model terrainModel;
	private TerrainChunk[] chunks;

	public float[][] heightMap;

	/**
	 * Creates a new custom Terrain
	 * 
	 * @param octaves
	 * @param octaveCount
	 * @param strength
	 * @param chunksWidth
	 * @param chunksHeight
	 */
	public Terrain(int octaves, int octaveCount, float strength, float scale,
			int chunksWidth, int chunksHeight, int border, boolean isIsland) {
		super();
		this.octaves = octaves;
		this.octaveCount = octaveCount;
		this.strength = strength;
		this.scale = scale;
		this.chunksWidth = chunksWidth;
		this.chunksHeight = chunksHeight;
		this.chunkBorderWidth = border;
		this.isIsland = isIsland;
		generateModel();
	}

	/**
	 * Generates the terrain model by using @TerrainChunks
	 */
	private void generateModel() {
		
		/* setup Builders */
		ModelBuilder modelBuilder = new ModelBuilder();

		int chunksNeeded = chunksWidth * chunksHeight;

		chunks = new TerrainChunk[chunksNeeded];

		heightMap = new float[(chunksWidth * TerrainChunk.width) + 1][(chunksHeight * TerrainChunk.height) + 1];
		/* stitch chunks toghether */
		modelBuilder.begin();
		int c = 0;
		for (int z = 0; z < chunksHeight; z++) {
			for (int x = 0; x < chunksWidth; x++) {

				chunks[c] = new TerrainChunk(this, octaves, octaveCount, scale,
						strength, x, z, chunkBorderWidth, isIsland);

				Mesh mesh = new Mesh(false, chunks[c].vertices.length / 3,
						chunks[c].indices.length,
						new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
						new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE),
						new VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
						new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE));

				mesh.setVertices(chunks[c].vertices);
				mesh.setIndices(chunks[c].indices);

				modelBuilder.node();
				modelBuilder.part("chunk" + Integer.toString(x) + "-" + Integer.toString(z),
						mesh,
						GL20.GL_TRIANGLES,
						new Material());
				c++;
			}
		}

		terrainModel = modelBuilder.end();
	}
	public float getTerrainHeight(float xPos, float zPos) {
		// we first get the height of four points of the quad underneath the point
		// Check to make sure this point is not off the map at all
		int x = (int) (xPos / scale);
		int z = (int) (zPos / scale);

		if (x >= 180 || z >= 180) {
			return 0;
		}
		int xPlusOne = x + 1;
		int zPlusOne = z + 1;

		float triZ0 = (heightMap[x][z]);
		float triZ1 = (heightMap[xPlusOne][z]);
		float triZ2 = (heightMap[x][zPlusOne]);
		float triZ3 = (heightMap[xPlusOne][zPlusOne]);

		float height = 0.0f;
		float sqX = (xPos / scale) - x;
		float sqZ = (zPos / scale) - z;
		if ((sqX + sqZ) < 1) {
			height = triZ0;
			height += (triZ1 - triZ0) * sqX;
			height += (triZ2 - triZ0) * sqZ;
		} else {
			height = triZ3;
			height += (triZ1 - triZ3) * (1.0f - sqZ);
			height += (triZ2 - triZ3) * (1.0f - sqX);
		}
		return height;
	}
	
	public float getHeight(float x, float z) {
		float height = 0;

		return height;
	}

	public float getOctaves() {
		return octaves;
	}

	public void setOctaves(int octaves) {
		this.octaves = octaves;
	}

	public float getOctaveCount() {
		return octaveCount;
	}

	public void setOctaveCount(int octaveCount) {
		this.octaveCount = octaveCount;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public int getChunksWidth() {
		return chunksWidth;
	}

	public void setChunksWidth(int chunksWidth) {
		this.chunksWidth = chunksWidth;
	}

	public int getChunksHeight() {
		return chunksHeight;
	}

	public void setChunksHeight(int chunksHeight) {
		this.chunksHeight = chunksHeight;
	}

	public Model getTerrainModel() {
		return terrainModel;
	}

	public void setTerrainModel(Model terrainModel) {
		this.terrainModel = terrainModel;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public TerrainChunk[] getChunks() {
		return chunks;
	}

	public void setChunks(TerrainChunk[] chunks) {
		this.chunks = chunks;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public int getChunkBorder() {
		return chunkBorderWidth;
	}

	public void setChunkBorder(int chunkBorder) {
		this.chunkBorderWidth = chunkBorder;
	}

	public boolean isIsland() {
		return isIsland;
	}

	public void setIsland(boolean isIsland) {
		this.isIsland = isIsland;
	}

	// @SuppressWarnings("unused")
	// @Deprecated
	// private void roughMerge(int x, int z, int c) {
	// if (x == 0 && z > 0 && roughMerge) {
	// /* SOUTH HAS NEIGHBOUR */
	//
	// float[] tmp = new float[TerrainChunk.width + 1];
	//
	// for (int f = 0; f <= TerrainChunk.width; f++) {
	// tmp[f] = chunks[c - chunksWidth].depths[f][TerrainChunk.height - 1];
	// }
	//
	// chunks[c] = new TerrainChunk(octaves, octaveCount, scale,
	// strength, x, z, chunkBorderWidth, tmp, null);
	// } else if (x > 0 && z == 0 && roughMerge) {
	// /* EAST HAS NEIGHBOUR */
	// chunks[c] = new TerrainChunk(octaves, octaveCount, scale,
	// strength, x, z, chunkBorderWidth, null,
	// chunks[c - 1].depths[TerrainChunk.width - 1]);
	// } else if (x > 0 && z > 0 && roughMerge) {
	// float[] tmp = new float[TerrainChunk.width + 1];
	//
	// for (int f = 0; f <= TerrainChunk.width; f++) {
	// tmp[f] = chunks[c - chunksWidth].depths[f][TerrainChunk.height - 1];
	// }
	// /* EAST HAS NEIGHBOUR */
	// chunks[c] = new TerrainChunk(octaves, octaveCount, scale,
	// strength, x, z, chunkBorderWidth, tmp,
	// chunks[c - 1].depths[TerrainChunk.width - 1]);
	// } else {
	//
	// }
	// }
	//
	// @SuppressWarnings("unused")
	// @Deprecated
	// private void bigMapMerge(int x, int z, int c) {
	// chunkDepths = new float[TerrainChunk.width+1][TerrainChunk.height+1];
	// for (int i = 0; i <= TerrainChunk.width; i++) {
	// for (int j = 0; j <= TerrainChunk.height; j++) {
	// chunkDepths[i][j] = bigMap[i+(150*x)][j+(150*z)];
	// }
	// }
	// chunks[c] = new TerrainChunk(chunkDepths, scale,
	// strength, x, z);
	// }
}
