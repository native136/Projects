package ca.maceman.makersland.world.terrain.parts;

public class TerrainTile {

	/*
	 * 3--4
	 * |  |
	 * 1--2
	 */
	public static final int TILE_SIZE = 10;

	private TerrainVector v1;
	private TerrainVector v2;
	private TerrainVector v3;
	private TerrainVector v4;

	private TerrainTriangle bottomTri;
	private TerrainTriangle topTri;

	/**
	 * A terrain Tile. Consists of 2 triangles.
	 * 
	 * @param v1
	 * @param v2
	 * @param v3
	 * @param v4
	 */
	public TerrainTile(TerrainVector v1, TerrainVector v2, TerrainVector v3, TerrainVector v4) {

		if (Math.abs(v1.z - v4.z) > Math.abs(v2.z - v3.z)) {

			bottomTri = new TerrainTriangle(v3, v1, v2);
			topTri = new TerrainTriangle(v4, v3, v2);

		} else {

			bottomTri = new TerrainTriangle(v1, v2, v4);
			topTri = new TerrainTriangle(v4, v3, v1);

		}
	}

	public TerrainVector getV1() {
		return v1;
	}

	public void setV1(TerrainVector v1) {
		this.v1 = v1;
	}

	public TerrainVector getV2() {
		return v2;
	}

	public void setV2(TerrainVector v2) {
		this.v2 = v2;
	}

	public TerrainVector getV3() {
		return v3;
	}

	public void setV3(TerrainVector v3) {
		this.v3 = v3;
	}

	public TerrainVector getV4() {
		return v4;
	}

	public void setV4(TerrainVector v4) {
		this.v4 = v4;
	}

	public TerrainTriangle getBottomTri() {
		return bottomTri;
	}

	public void setBottomTri(TerrainTriangle bottomTri) {
		this.bottomTri = bottomTri;
	}

	public TerrainTriangle getTopTri() {
		return topTri;
	}

	public void setTopTri(TerrainTriangle topTri) {
		this.topTri = topTri;
	}
}
