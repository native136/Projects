package noisegenerator;

import java.util.Random;

import mapgenerator.Tile;

public class NoiseGenerator {

	public NoiseGenerator() {

	}

	public float[][] GenerateWhiteNoise(int width, int height, int borderWidth,
			int seed) {

		Random random = new Random(seed);

		float[][] noise = new float[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {

				if ((i <= borderWidth) || (i >= width - borderWidth)
						|| (j <= borderWidth) || (j >= height - borderWidth)) {
					noise[i][j] = (float) 0;
				} else {
					noise[i][j] = (float) random.nextDouble() % 1;
				}
			}
		}

		return noise;
	}

	public float[][] GenerateSmoothNoise(float[][] baseNoise, int octave) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][] smoothNoise = new float[width][height];

		int samplePeriod = 1 << octave; // Shift bits the numbers of octaves to
										// the left, example : 1 << 2 means 0001
										// becomes 0100
		float sampleFrequency = 1.0f / samplePeriod;

		for (int i = 0; i < width; i++) {
			// calculate the horizontal sampling indices
			int sample_i0 = (i / samplePeriod) * samplePeriod;
			int sample_i1 = (sample_i0 + samplePeriod) % width; // wrap around
			float horizontal_blend = (i - sample_i0) * sampleFrequency;

			for (int j = 0; j < height; j++) {
				// calculate the vertical sampling indices
				int sample_j0 = (j / samplePeriod) * samplePeriod;
				int sample_j1 = (sample_j0 + samplePeriod) % height; // wrap
																		// around
				float vertical_blend = (j - sample_j0) * sampleFrequency;

				// blend the top two corners
				float top = Interpolate(baseNoise[sample_i0][sample_j0],
						baseNoise[sample_i1][sample_j0], horizontal_blend);

				// blend the bottom two corners
				float bottom = Interpolate(baseNoise[sample_i0][sample_j1],
						baseNoise[sample_i1][sample_j1], horizontal_blend);

				// final blend
				smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
			}
		}

		return smoothNoise;
	}

	public float[][] GeneratePerlinNoise(float[][] baseNoise, int octaveCount) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][][] smoothNoise = new float[octaveCount][][]; // an array of 2D
																// arrays
																// containing

		float persistance = 0.5f;

		// generate smooth noise
		for (int i = 0; i < octaveCount; i++) {
			smoothNoise[i] = GenerateSmoothNoise(baseNoise, i);
		}

		float[][] perlinNoise = new float[width][height];
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;

		// blend noise together
		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}

		// normalisation
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				perlinNoise[i][j] /= totalAmplitude;
			}
		}
		return perlinNoise;

	}

	public float Interpolate(float x0, float x1, float alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

}
