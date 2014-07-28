package ca.maceman.makersland.world.terrain;

import ca.maceman.makersland.world.generator.NoiseGenerator;

import com.badlogic.gdx.graphics.Color;

/**
 * Part of the terrain model is generated here. Creates different landscapes based on the values given.
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

	public static final int VERTEX_SIZE = 9;
	public final float[] vertices;
	public final short[] indices;
	public float[][] chunkDepths;

	private Terrain parent;
	
	public static final short height = 180;
	public static final short width = 180;

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
		this.vertices = new float[(width + 1) * (height + 1) * VERTEX_SIZE];
		this.indices = new short[width * height * 6];

		buildHeightmap();
		buildIndices();
		buildVertices();

		calcNormals(indices, vertices);

	}	

	public void buildHeightmap() {
		if (isIsland) {
			chunkDepths = NoiseGenerator.GeneratePerlinNoise(NoiseGenerator
					.GenerateSmoothNoise(NoiseGenerator
							.GenerateRadialWhiteNoise(width + 1, height + 1),
							octave), octaveCount);
		} else{
			chunkDepths = NoiseGenerator.GeneratePerlinNoise(NoiseGenerator
					.GenerateSmoothNoise(NoiseGenerator.GenerateWhiteNoise(
							width + 1, height + 1, borderSize), octave),
					octaveCount);
		}
	}

	public void buildVertices() {
		int heightPitch = height + 1;
		int widthPitch = width + 1;

		int idx = 0;

		for (int x = 0; x < widthPitch; x++) {
			for (int z = 0; z < heightPitch; z++) {

				// POSITION
				vertices[idx++] = ((posX * width) * scale) + (scale * x);
				vertices[idx++] = (float) Math.round((Math.pow(1 + chunkDepths[x][z],strength)));
				parent.heightMap[x+(width*posX)][z+(width*posZ)] = (float) Math.round((Math.pow(1 + chunkDepths[x][z], strength)));
				vertices[idx++] = ((posZ * height) * scale) + (scale * z);

				// NORMAL, skip these for now
				idx += 3;

				// COLOR
				if (chunkDepths[x][z] <= .1) {
					vertices[idx++] = Color.toFloatBits(0, 120, 200, 255);
				} else if (chunkDepths[x][z] > .1 && chunkDepths[x][z] <= .2) {
					vertices[idx++] = Color.toFloatBits(0, 190, 250, 255);
				} else if (chunkDepths[x][z] > .2 && chunkDepths[x][z] <= .25) {
					vertices[idx++] = Color.toFloatBits(245, 225, 135, 255);
				} else if (chunkDepths[x][z] > .25 && chunkDepths[x][z] <= .4) {
					vertices[idx++] = Color.toFloatBits(0, 200, 0, 255);
				} else if (chunkDepths[x][z] > .4 && chunkDepths[x][z] <= .6) {
					vertices[idx++] = Color.toFloatBits(0, 130, 0, 255);
				} else if (chunkDepths[x][z] > .6 && chunkDepths[x][z] <= .7) {
					vertices[idx++] = Color.toFloatBits(94, 94, 94, 255);
				} else if (chunkDepths[x][z] > .7 && chunkDepths[x][z] <= .8) {
					vertices[idx++] = Color.toFloatBits(180, 180, 180, 255);
				} else {
					vertices[idx++] = Color.WHITE.toFloatBits();
				}

				// TEXTURE
				vertices[idx++] = (x / (float) width);
				vertices[idx++] = (z / (float) height);

				
			}
		}
	}

	private void buildIndices() {
		int idx = 0;
		short pitch = (short) (width + 1);
		short i1 = 0;
		short i2 = 1;
		short i3 = (short) (1 + pitch);
		short i4 = pitch;

		short row = 0;

		for (int z = 0; z < height; z++) {
			for (int x = 0; x < width; x++) {
				indices[idx++] = i1;
				indices[idx++] = i2;
				indices[idx++] = i3;

				indices[idx++] = i3;
				indices[idx++] = i4;
				indices[idx++] = i1;
				i1++;
				i2++;
				i3++;
				i4++;
			}

			row += pitch;
			i1 = row;
			i2 = (short) (row + 1);
			i3 = (short) (i2 + pitch);
			i4 = (short) (row + pitch);
		}
	}

	// Gets the index of the first float of a normal for a specific vertex
	private int getNormalStart(int vertIndex) {
		return vertIndex * VERTEX_SIZE + 3;
	}

	// Gets the index of the first float of a specific vertex
	private int getPositionStart(int vertIndex) {
		return vertIndex * VERTEX_SIZE;
	}

	// Adds the provided value to the normal
	private void addNormal(int vertIndex, float[] verts, float x, float y,
			float z) {

		int i = getNormalStart(vertIndex);

		float rx = (float) ((x * Math.cos(180)) - (y * Math.sin(180)));
		float ry = (float) ((x * Math.sin(180)) + (y * Math.cos(180)));
		x = rx;
		y = ry;
		verts[i] += x;
		verts[i + 1] += y;
		verts[i + 2] += z;
	}

	/*
	 * Normalizes normals
	 */
	private void normalizeNormal(int vertIndex, float[] verts) {

		int i = getNormalStart(vertIndex);

		float x = verts[i];
		float y = verts[i+1];
		float z = verts[i+2];

		float num2 = ((x * x) + (z * z)) + (y * y);
		float num = (float) Math.sqrt(num2);
		x *= num;
		y *= num;
		z *= num;

		verts[i] = x;
		verts[i + 1] = y;
		verts[i + 2] = z;
	}

	/*
	 * Calculates the normals
	 */
	private void calcNormals(short[] indices, float[] verts) {

		for (int i = 0; i < indices.length; i += 3) {
			int i1 = getPositionStart(indices[i]);
			int i2 = getPositionStart(indices[i + 1]);
			int i3 = getPositionStart(indices[i + 2]);

			// p1
			float x1 = verts[i1];
			float y1 = verts[i1 + 1];
			float z1 = verts[i1 + 2];

			// p2
			float x2 = verts[i2];
			float y2 = verts[i2 + 1];
			float z2 = verts[i2 + 2];

			// p3
			float x3 = verts[i3];
			float y3 = verts[i3 + 1];
			float z3 = verts[i3 + 2];

			// u = p3 - p1
			float ux = x3 - x1;
			float uy = y3 - y1;
			float uz = z3 - z1;

			// v = p2 - p1
			float vx = x2 - x1;
			float vy = y2 - y1;
			float vz = z2 - z1;

			// n = cross(v, u)
			float nx = ((vy * uz) - (vz * uy));
			float ny = ((vz * ux) - (vx * uz));
			float nz = ((vx * uy) - (vy * ux));

			// normalize(n)
			float num2 = ((nx * nx) + (ny * ny)) + (nz * nz);
			float num = 1f / (float) Math.sqrt(num2);
			nx *= num;
			ny *= num;
			nz *= num;
			
			addNormal(indices[i], verts, nx,ny, nz);
			addNormal(indices[i + 1], verts, nx, ny, nz);
			addNormal(indices[i + 2], verts, nx, ny, nz);
		}

		for (int i = 0; i < (verts.length / VERTEX_SIZE); i++) {
			normalizeNormal(i, verts);
		}
	}
	
	/**
	 * Constructs a chunk that is bordered by another. To designate a single side, make the unused border null.
	 * 
	 * @param octave
	 * @param octaveCount
	 * @param scale
	 * @param strength
	 * @param posX
	 * @param posZ
	 * @param borderSize
	 * @param sBorder
	 * @param wBorder
	 */
//	public TerrainChunk(int octave, int octaveCount, float scale,
//			float strength, int posX, int posZ, int borderSize,float[] sBorder,float[] eBorder) {
//
//		this.octave = octave;
//		this.octaveCount = octaveCount;
//		this.scale = scale;
//		this.strength = strength;
//		this.posX = posX;
//		this.posZ = posZ;
//		this.borderSize = borderSize;
//		this.eBorder = eBorder;
//		this.sBorder = sBorder;
//		this.vertices = new float[(width + 1) * (height + 1) * vertexSize];
//		this.indices = new short[width * height * 6];
//
//		buildHeightmap();
//		buildIndices();
//		buildVertices();
//
//		calcNormals(indices, vertices);
//
//	}
//	
//	public void buildHeightmap() {
//		if (isIsland) {
//			depths = NoiseGenerator.GeneratePerlinNoise(NoiseGenerator
//					.GenerateSmoothNoise(NoiseGenerator
//							.GenerateRadialWhiteNoise(width + 1, height + 1),
//							octave), octaveCount);
//		} else if (sBorder!=null && eBorder==null) {
//			depths = NoiseGenerator.GeneratePerlinNoise(
//					NoiseGenerator.GenerateSmoothNoise(
//							NoiseGenerator.GenerateSBorderedWhiteNoise(
//							width + 1, 
//							height + 1,
//							borderSize,
//							sBorder), 
//						octave),
//				octaveCount);
//		} else if (sBorder==null && eBorder!=null) {
//			depths = NoiseGenerator.GeneratePerlinNoise(
//					NoiseGenerator.GenerateSmoothNoise
//					(NoiseGenerator.GenerateEBorderedWhiteNoise(
//							width + 1,
//							height + 1,
//							borderSize,
//							eBorder),
//						octave),
//					octaveCount);
//		} else if (sBorder!=null && eBorder!=null) {
//			depths = NoiseGenerator.GeneratePerlinNoise(
//					NoiseGenerator.GenerateSmoothNoise(
//							NoiseGenerator.GenerateSEBorderedWhiteNoise(
//							width + 1,
//							height + 1,
//							borderSize,
//							sBorder,
//							eBorder),
//							octave),
//					octaveCount);
//		} else {
//			depths = NoiseGenerator.GeneratePerlinNoise(NoiseGenerator
//					.GenerateSmoothNoise(NoiseGenerator.GenerateWhiteNoise(
//							width + 1, height + 1, borderSize), octave),
//					octaveCount);
//		}
//	}
//	public TerrainChunk(float[][] depths,float scale,float strength, int posX, int posZ) {
//	this.depths = depths;
//	this.scale = scale;
//	this.strength = strength;
//	this.posX = posX;
//	this.posZ = posZ;
//	this.vertices = new float[(width + 1) * (height + 1) * vertexSize];
//	this.indices = new short[width * height * 6];
//
//	buildHeightmap();
//	buildIndices();
//	buildVertices();
//
//	calcNormals(indices, vertices);
//
//}
}
