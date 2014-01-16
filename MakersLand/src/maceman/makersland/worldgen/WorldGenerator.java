package maceman.makersland.worldgen;

import java.util.Random;

import maceman.makersland.world.tile.Tile;
import maceman.makersland.worldgen.noise.NoiseGenerator;

public class WorldGenerator {

	private NoiseGenerator mNoiseGenerator;
	private Random r;

	public WorldGenerator(NoiseGenerator noiseGenerator) {
		mNoiseGenerator = noiseGenerator;
		r = new Random();
	}

	// Seed not yet implemented TODO
	public WorldGenerator(NoiseGenerator noiseGenerator, int seed) {
		mNoiseGenerator = noiseGenerator;
		r = new Random(seed);
	}

	public Tile[][] GenerateNewWorld(int width, int height, int octaveCount,
			int borderWidth, int seed) {

		Tile[][] tiles = new Tile[width][height];

		float[][] noise = new float[width][height];
		float[][] perlinNoise = new float[width][height];

		noise = mNoiseGenerator.GenerateWhiteNoise(width, height, borderWidth,
				seed);

		perlinNoise = mNoiseGenerator.GeneratePerlinNoise(noise, octaveCount);

		tiles = GenerateTerrain(perlinNoise, seed);


		return tiles;

	}

	public Tile[][] GenerateTerrain(float[][] basePerlinNoise, int seed) {

		r = new Random(seed);
		int width = basePerlinNoise.length;
		int height = basePerlinNoise[0].length;

		Tile[][] tiles = new Tile[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = new Tile(basePerlinNoise[i][j], i, j);
			}
		}

		return tiles;
	}
}
