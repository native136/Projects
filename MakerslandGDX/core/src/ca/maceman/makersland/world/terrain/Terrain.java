package ca.maceman.makersland.world.terrain;

import ca.maceman.makersland.world.terrain.cell.Cell;
import ca.maceman.makersland.world.utils.generator.NoiseGenerator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.sun.javafx.scene.control.skin.CellSkinBase;

/**
 * Holds the Terrain information and model. The terrain Model is made using an array of TerrainChunk.
 * 
 * @author andy.masse
 * 
 */
public class Terrain {

	private boolean isIsland = false;
	
	/* Triangles */
	private VertexInfo v1 = new VertexInfo();
	private VertexInfo v2 = new VertexInfo();
	private VertexInfo v3 = new VertexInfo();
	private VertexInfo v4 = new VertexInfo();
	private VertexInfo v5 = new VertexInfo();
	private VertexInfo v6 = new VertexInfo();

	/* Cell colours */
	private Color colorN;
	private Color colorE;
	private Color colorW;
	private Color colorS;
	private Color colorNW;
	private Color colorNE;
	private Color colorSW;
	private Color colorSE;
	private Color colorC;
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
	private TerrainChunk[] chunks;

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
			int chunksWidth, int chunksHeight, int borderSize, boolean isIsland) {
		super();
		this.octaves = octaves;
		this.octaveCount = octaveCount;
		this.strength = strength;
		this.scale = scale;
		this.chunksWidth = chunksWidth;
		this.chunksHeight = chunksHeight;
		this.borderSize= borderSize;
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

		int chunksNeeded = chunksWidth * chunksHeight;
		chunks = new TerrainChunk[chunksNeeded];

		buildHeightmap();

		/* stitch chunks toghether */
		modelBuilder.begin();

		int c = 0;
		// left triangle

		for (int z = 0; z < chunksHeight; z++) {
			for (int x = 0; x < chunksWidth; x++) {

				meshBuilder.begin(Usage.Position | Usage.Normal | Usage.ColorPacked | Usage.TextureCoordinates, GL20.GL_TRIANGLES);
				chunks[c] = new TerrainChunk(heightMap, scale,
						strength, x, z, chunkBorderWidth, isIsland);

for (int cx = 0; cx < chunks[c].cells.length; cx++) {
	for (int cz = 0; cz < chunks[c].cells[0].length; cz++) {

		colorC = chunks[c].cells[cx][cz].getColor1();
		if (cx != 0
				&& cz != 0
				&& cx != chunks[c].cells.length - 1
				&& cz != chunks[c].cells[0].length - 1) {

			colorN = chunks[c].cells[cx][cz + 1].getColor1();
			colorE = chunks[c].cells[cx - 1][cz].getColor1();
			colorW = chunks[c].cells[cx + 1][cz].getColor1();
			colorS = chunks[c].cells[cx][cz - 1].getColor1();
			colorNW = chunks[c].cells[cx + 1][cz + 1].getColor1();
			colorNE = chunks[c].cells[cx - 1][cz + 1].getColor1();
			colorSW = chunks[c].cells[cx + 1][cz - 1].getColor1();
			colorSE = chunks[c].cells[cx - 1][cz - 1].getColor1();

			/* Check for South East */
			if (colorC.equals(colorS)
					&& colorC.equals(colorE)
					&& colorC.equals(colorSE)
					&& !colorC.equals(colorN)
					&& !colorC.equals(colorNW)
					&& !colorC.equals(colorW)
					) {

				chunks[c].cells[cx][cz].setColor1(colorN);

				v1.setPos(chunks[c].cells[cx][cz].getCorner3()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v2.setPos(chunks[c].cells[cx][cz].getCorner2()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v3.setPos(chunks[c].cells[cx][cz].getCorner4()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());

				v4.setPos(chunks[c].cells[cx][cz].getCorner1()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v5.setPos(chunks[c].cells[cx][cz].getCorner4()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v6.setPos(chunks[c].cells[cx][cz].getCorner2()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());

				meshBuilder.triangle(v1, v2, v3);
				meshBuilder.triangle(v4, v5, v6);

				/* Check for South West */
			} else if (colorC.equals(colorS)
					&& colorC.equals(colorW)
					&& colorC.equals(colorSW)
					&& !colorC.equals(colorN)
					&& !colorC.equals(colorNE)
					&& !colorC.equals(colorE)
					) {

				chunks[c].cells[cx][cz].setColor1(colorS);

				v1.setPos(chunks[c].cells[cx][cz].getCorner1()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v2.setPos(chunks[c].cells[cx][cz].getCorner3()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v3.setPos(chunks[c].cells[cx][cz].getCorner2()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());

				v4.setPos(chunks[c].cells[cx][cz].getCorner3()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v5.setPos(chunks[c].cells[cx][cz].getCorner1()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v6.setPos(chunks[c].cells[cx][cz].getCorner4()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());

				meshBuilder.triangle(v1, v2, v3);
				meshBuilder.triangle(v4, v5, v6);

				/* Check for North West */
			} else if (colorC.equals(colorN)
					&& colorC.equals(colorW)
					&& colorC.equals(colorNW)
					&& !colorC.equals(colorS)
					&& !colorC.equals(colorSE)
					&& !colorC.equals(colorE)
					) {

				chunks[c].cells[cx][cz].setColor1(colorN);

				v1.setPos(chunks[c].cells[cx][cz].getCorner3()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v2.setPos(chunks[c].cells[cx][cz].getCorner2()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v3.setPos(chunks[c].cells[cx][cz].getCorner4()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());

				v4.setPos(chunks[c].cells[cx][cz].getCorner1()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v5.setPos(chunks[c].cells[cx][cz].getCorner4()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v6.setPos(chunks[c].cells[cx][cz].getCorner2()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());

				meshBuilder.triangle(v1, v2, v3);
				meshBuilder.triangle(v4, v5, v6);

				/* Check for North East */
			} else if (colorC.equals(colorN)
					&& colorC.equals(colorE)
					&& colorC.equals(colorNE)
					&& !colorC.equals(colorN)
					&& !colorC.equals(colorSW)
					&& !colorC.equals(colorW)
					) {

				chunks[c].cells[cx][cz].setColor1(colorN);

				v1.setPos(chunks[c].cells[cx][cz].getCorner1()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v2.setPos(chunks[c].cells[cx][cz].getCorner3()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v3.setPos(chunks[c].cells[cx][cz].getCorner2()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorS).setUV(chunks[c].cells[cx][cz].getTexturePos());

				v4.setPos(chunks[c].cells[cx][cz].getCorner4()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v5.setPos(chunks[c].cells[cx][cz].getCorner3()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v6.setPos(chunks[c].cells[cx][cz].getCorner1()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorN).setUV(chunks[c].cells[cx][cz].getTexturePos());

				meshBuilder.triangle(v1, v2, v3);
				meshBuilder.triangle(v4, v5, v6);

				/* Full Square*/
			} else {
				
				v1.setPos(chunks[c].cells[cx][cz].getCorner1()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v2.setPos(chunks[c].cells[cx][cz].getCorner3()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v3.setPos(chunks[c].cells[cx][cz].getCorner2()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());

				v4.setPos(chunks[c].cells[cx][cz].getCorner4()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v5.setPos(chunks[c].cells[cx][cz].getCorner3()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());
				v6.setPos(chunks[c].cells[cx][cz].getCorner1()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());

				meshBuilder.triangle(v1, v2, v3);
				meshBuilder.triangle(v4, v5, v6);
			}

			/* Edge of chunk */
		} else {
			v1.setPos(chunks[c].cells[cx][cz].getCorner1()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());
			v2.setPos(chunks[c].cells[cx][cz].getCorner3()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());
			v3.setPos(chunks[c].cells[cx][cz].getCorner2()).setNor(chunks[c].cells[cx][cz].getLeftNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());

			v4.setPos(chunks[c].cells[cx][cz].getCorner4()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());
			v5.setPos(chunks[c].cells[cx][cz].getCorner3()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());
			v6.setPos(chunks[c].cells[cx][cz].getCorner1()).setNor(chunks[c].cells[cx][cz].getRightNormal()).setCol(colorC).setUV(chunks[c].cells[cx][cz].getTexturePos());

			meshBuilder.triangle(v1, v2, v3);
			meshBuilder.triangle(v4, v5, v6);
		}

						if ((x != 0 || cx != 0)
								&& (z != 0 || cz != 0)
								&& (x != chunksWidth - 1 || cx != chunks[c].cells.length)
								&& (z != chunksHeight - 1 || cz != chunks[c].cells[0].length)) {

						}

					}
				}
				Mesh mesh = meshBuilder.end();
				modelBuilder.part("chunk" + Integer.toString(x) + "." + Integer.toString(z),
						mesh,
						GL20.GL_TRIANGLES,
						new Material());

				c++;

			}
		}
		terrainModel = modelBuilder.end();
	}

	public void buildHeightmap() {
		if (isIsland) {
			heightMap = NoiseGenerator.GeneratePerlinNoise(NoiseGenerator
					.GenerateSmoothNoise(NoiseGenerator
							.GenerateRadialWhiteNoise(
									(chunksWidth * TerrainChunk.width) + 1,
									(chunksHeight * TerrainChunk.height) + 1),
									octave),
									octaveCount);
		} else {
			heightMap = NoiseGenerator.GeneratePerlinNoise(NoiseGenerator
					.GenerateSmoothNoise(NoiseGenerator.GenerateWhiteNoise(
							(chunksWidth * TerrainChunk.width) + 1,
							(chunksHeight * TerrainChunk.height) + 1,
							borderSize),
							octave),
					octaveCount);
		}
	}

	public float getTerrainHeight(float xPos, float zPos) {
		/* we first get the height of four points of the quad underneath the point
		 * Check to make sure this point is not off the map at all
		 * 
		 */
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
		return (float) Math.pow(1 + height, strength);
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

	public int getChunkBorderWidth() {
		return chunkBorderWidth;
	}

	public void setChunkBorderWidth(int chunkBorderWidth) {
		this.chunkBorderWidth = chunkBorderWidth;
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
