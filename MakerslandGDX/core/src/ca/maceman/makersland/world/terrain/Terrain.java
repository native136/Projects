package ca.maceman.makersland.world.terrain;

import ca.maceman.makersland.world.terrain.parts.TerrainChunk;
import ca.maceman.makersland.world.terrain.parts.TerrainTile;
import ca.maceman.makersland.world.terrain.parts.TerrainVector;
import ca.maceman.makersland.world.utils.terrain.NoiseGenerator;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/**
 * Holds the Terrain information and model. The terrain Model is made using an
 * array of Terrain Regions and their models. This is because a model can only
 * have a certain number of vectors.
 * 
 * @author andy.masse
 * 
 */
public class Terrain {

	/* Triangles */
	private VertexInfo vi1 = new VertexInfo();
	private VertexInfo vi2 = new VertexInfo();
	private VertexInfo vi3 = new VertexInfo();

	private boolean isIsland = false;
	private float scale;
	private float strength;
	private float[][] heightMap;
	private int octave;
	private int borderSize;
	private int chunkBorderWidth;
	private int chunksHeight;
	private int chunksWidth;
	private int octaveCount;
	private int octaves;
	private Mesh mesh;
	private Model terrainModel;
	private TerrainChunk[][] chunks;

	public TerrainVector[][] vectors;

	/**
	 * Create a new Terrain
	 * 
	 * @param octaves
	 * @param octaveCount
	 * @param strength
	 * @param scale
	 * @param chunksWidth
	 * @param chunksHeight
	 * @param borderSize
	 * @param isIsland
	 */
	public Terrain(int octaves, int octaveCount, float strength, float scale, int chunksWidth, int chunksHeight, int borderSize, boolean isIsland) {

		this.octaves = octaves;
		this.octaveCount = octaveCount;
		this.strength = strength;
		this.scale = scale;
		this.chunksWidth = chunksWidth;
		this.chunksHeight = chunksHeight;
		this.borderSize = borderSize;
		this.isIsland = isIsland;
		generateModel();

	}

	/**
	 * Generates the terrain model by using @TerrainChunks
	 */
	private void generateModel() {

		/* setup Builders */
		ModelBuilder modelBuilder = new ModelBuilder();
		MeshBuilder meshBuilder = new MeshBuilder();

		chunks = new TerrainChunk[chunksWidth][chunksHeight];

		buildHeightmap();

		modelBuilder.begin();
		/* For each chunk */
		for (int x = 0; x < chunksWidth; x++) {
			for (int y = 0; y < chunksHeight; y++) {

				System.out.println("Chunk = " + x + ", " + y);
				chunks[x][y] = new TerrainChunk(this, x, y);

				/* Create a model part */
				meshBuilder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked | Usage.TextureCoordinates, GL20.GL_TRIANGLES);

				/* using every tile's triangles */
				for (int cx = 0; cx < chunks[x][y].tiles.length; cx++) {
					for (int cy = 0; cy < chunks[x][y].tiles[0].length; cy++) {

						System.out.println("Tile = " + cx + ", " + cy);

						vi1 = chunks[x][y].tiles[cx][cy].getBottomTri().getRIVertexInfo();
						vi2 = chunks[x][y].tiles[cx][cy].getBottomTri().getATVertexInfo();
						vi3 = chunks[x][y].tiles[cx][cy].getBottomTri().getABVertexInfo();

						meshBuilder.triangle(vi1, vi2, vi3);

						vi1 = chunks[x][y].tiles[cx][cy].getTopTri().getRIVertexInfo();
						vi2 = chunks[x][y].tiles[cx][cy].getTopTri().getATVertexInfo();
						vi3 = chunks[x][y].tiles[cx][cy].getTopTri().getABVertexInfo();

						meshBuilder.triangle(vi1, vi2, vi3);

					}
				}

				Mesh mesh = meshBuilder.end();
				modelBuilder.part("chunk" + Integer.toString(x) + "." + Integer.toString(y),
						mesh,
						GL20.GL_TRIANGLES,
						new Material());
			}
		}

		/* Terrain model finished */
		terrainModel = modelBuilder.end();

	}

	public void buildHeightmap() {

		/* 1 extra vector for outer edges. */
		int width = (chunksWidth * TerrainChunk.CHUNK_SIZE) + 1;
		int height = (chunksHeight * TerrainChunk.CHUNK_SIZE) + 1;

		if (isIsland) {
			heightMap = NoiseGenerator.GeneratePerlinNoise(
					NoiseGenerator.GenerateSmoothNoise(
							NoiseGenerator.GenerateRadialWhiteNoise(width, height),
							octave),
					octaveCount);
		} else {
			heightMap = NoiseGenerator.GeneratePerlinNoise(
					NoiseGenerator.GenerateSmoothNoise(
							NoiseGenerator.GenerateWhiteNoise(width, height, borderSize),
							octave),
					octaveCount);
		}

		vectors = new TerrainVector[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				vectors[x][y] = new TerrainVector(x * TerrainTile.TILE_SIZE * scale,
						y * TerrainTile.TILE_SIZE * scale,
						1 + (heightMap[x][y] * 100 * scale));
			}
		}

	}

	/**
	 * Gets the Depth at x and y. TODO, needs some tweaking.
	 * 
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	public float getTerrainHeight(float xPos, float yPos) {

		/* we first get the height of four points of the quad underneath the point
		 * Check to make sure this point is not off the map at all
		 * 
		 */
		int x = (int) (xPos / scale);
		int y = (int) (yPos / scale);

		if (x >= 180 || y >= 180) {
			return 0;
		}
		int xPlusOne = x + 1;
		int yPlusOne = y + 1;

		float triZ0 = (heightMap[x][y]);
		float triZ1 = (heightMap[xPlusOne][y]);
		float triZ2 = (heightMap[x][yPlusOne]);
		float triZ3 = (heightMap[xPlusOne][yPlusOne]);

		float height = 0.0f;
		float sqX = (xPos / scale) - x;
		float sqy = (yPos / scale) - y;

		if ((sqX + sqy) < 1) {

			height = triZ0;
			height += (triZ1 - triZ0) * sqX;
			height += (triZ2 - triZ0) * sqy;

		} else {

			height = triZ3;
			height += (triZ1 - triZ3) * (1.0f - sqy);
			height += (triZ2 - triZ3) * (1.0f - sqX);

		}

		return (float) Math.pow(1 + height, strength);

	}

	public boolean isIsland() {
		return isIsland;
	}

	public void setIsland(boolean isIsland) {
		this.isIsland = isIsland;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public float[][] getHeightMap() {
		return heightMap;
	}

	public void setHeightMap(float[][] heightMap) {
		this.heightMap = heightMap;
	}

	public int getOctave() {
		return octave;
	}

	public void setOctave(int octave) {
		this.octave = octave;
	}

	public int getBorderSize() {
		return borderSize;
	}

	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
	}

	public int getChunkBorderWidth() {
		return chunkBorderWidth;
	}

	public void setChunkBorderWidth(int chunkBorderWidth) {
		this.chunkBorderWidth = chunkBorderWidth;
	}

	public int getChunksHeight() {
		return chunksHeight;
	}

	public void setChunksHeight(int chunksHeight) {
		this.chunksHeight = chunksHeight;
	}

	public int getChunksWidth() {
		return chunksWidth;
	}

	public void setChunksWidth(int chunksWidth) {
		this.chunksWidth = chunksWidth;
	}

	public int getOctaveCount() {
		return octaveCount;
	}

	public void setOctaveCount(int octaveCount) {
		this.octaveCount = octaveCount;
	}

	public int getOctaves() {
		return octaves;
	}

	public void setOctaves(int octaves) {
		this.octaves = octaves;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Model getTerrainModel() {
		return terrainModel;
	}

	public void setTerrainModel(Model terrainModel) {
		this.terrainModel = terrainModel;
	}

	public TerrainChunk[][] getChunks() {
		return chunks;
	}

	public void setChunks(TerrainChunk[][] chunks) {
		this.chunks = chunks;
	}

}
