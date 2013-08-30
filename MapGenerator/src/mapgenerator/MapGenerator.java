package mapgenerator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import noisegenerator.NoiseGenerator;

public class MapGenerator {

	private NoiseGenerator noiseGenerator;

	public MapGenerator(NoiseGenerator noiseGenerator) {
		this.noiseGenerator = noiseGenerator;
	}

	public Tile[][] GenerateTerrainMap(float[][] basePerlinNoise) {
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
