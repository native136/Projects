package ca.maceman.makersland.world.terrain.parts;


import ca.maceman.makersland.world.terrain.Terrain;

public class TerrainChunk {

	public static final int CHUNK_SIZE = 64;

	private Terrain parentTerrain;
	public TerrainTile[][] tiles;
	private int chunkRow;
	private int chunkCol;
	private int baseX;
	private int baseY;

	/**
	 * A part of the Terrain.
	 * 
	 * @param parentTerrain
	 * @param chunkRow
	 * @param chunkCol
	 */
	public TerrainChunk(Terrain parentTerrain, int chunkRow, int chunkCol) {

		this.parentTerrain = parentTerrain;
		this.chunkRow = chunkRow;
		this.chunkCol = chunkCol;
		tiles = new TerrainTile[CHUNK_SIZE][CHUNK_SIZE];

		baseX = chunkRow * CHUNK_SIZE;
		baseY = chunkCol * CHUNK_SIZE;

		buildChunk();

	}

	/**
	 * Builds the chunk
	 * 
	 */
	private void buildChunk() {

		int x = 0;
		int y = 0;

		for (int i = 0; i < CHUNK_SIZE; i++) {
			for (int j = 0; j < CHUNK_SIZE; j++) {

				x = baseX + i;
				y = baseY + j;

				tiles[i][j] = new TerrainTile(	parentTerrain.vectors[x][y],
												parentTerrain.vectors[x+1][y],
												parentTerrain.vectors[x][y+1],
												parentTerrain.vectors[x+1][y+1],this);

			}
		}
		
		
	}

	public Terrain getParentTerrain() {
		return parentTerrain;
	}

	public void setParentTerrain(Terrain parentTerrain) {
		this.parentTerrain = parentTerrain;
	}

	public int getChunkRow() {
		return chunkRow;
	}

	public void setChunkRow(int chunkRow) {
		this.chunkRow = chunkRow;
	}

	public int getChunkCol() {
		return chunkCol;
	}

	public void setChunkCol(int chunkCol) {
		this.chunkCol = chunkCol;
	}

	public int getBaseX() {
		return baseX;
	}

	public void setBaseX(int baseX) {
		this.baseX = baseX;
	}

	public int getBaseY() {
		return baseY;
	}

	public void setBaseY(int baseY) {
		this.baseY = baseY;
	}

	
}
