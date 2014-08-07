package ca.maceman.makersland.world.utils.generator;

import java.util.Random;

/**
 * Generates different kinds of noises for use in terrain generation.
 * 
 * @author andy.masse
 *
 */
public class NoiseGenerator {

	private static long seed = 0;
	private static Random r;

	public NoiseGenerator() {

	}

	// generates a array of random values (Noise)
	public static float[][] GenerateWhiteNoise(int width, int height,
			int borderWidth) {

		// new random
		if (seed != 0) {
			r = new Random(seed);
		} else {
			r = new Random();
		}

		float[][] noise = new float[width][height];

		// loop that assigns random values
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (((i <= borderWidth) || (i >= width - borderWidth)
						|| (j <= borderWidth) || (j >= height - borderWidth))
						&& borderWidth != 0) {
					noise[i][j] = (float) 0;
				} else {
					noise[i][j] = (float) r.nextDouble();// % 1;
				}
			}
		}

		return noise;
	}

	public static float[][] GenerateRadialWhiteNoise(int width, int height) {
		
		int borderWidth = 10;
		// new random
		if (seed != 0) {
			r = new Random(seed);
		} else {
			r = new Random();
		}

		float[][] noise = new float[width][height];
		int centerX = width / 2;
		int centerY = height / 2;

		// loop that assign random values
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				float distanceX = (centerX - i) * (centerX - i);
				float distanceY = (centerY - j) * (centerY - j);

				float distanceToCenter = (float) Math.sqrt(distanceX
						+ distanceY);
				if (((i <= borderWidth) || (i >= width - borderWidth)
						|| (j <= borderWidth) || (j >= height - borderWidth))
						&& borderWidth != 0) {
					noise[i][j] = (float) 0;
				} else {
					noise[i][j] = (float) r.nextDouble()
							- (distanceToCenter / 256);
				}
			}
		}

		return noise;
	}

	public static float[][] GenerateEBorderedWhiteNoise(int width, int height,
			int borderWidth, float[] border) {
		// new random
		if (seed != 0) {
			r = new Random(seed);
		} else {
			r = new Random();
		}

		float[][] noise = new float[width][height];

		// loop that assign random values
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (((i <= borderWidth) || (i >= width - borderWidth) || (j >= height
						- borderWidth))
						&& borderWidth != 0 && i >= 5) {
					noise[i][j] = (float) 0;
				} else if (i < 5) {
					noise[i] = border;
				} else {
					noise[i][j] = (float) r.nextDouble();
				}
			}
		}

		return noise;
	}

	public static float[][] GenerateSBorderedWhiteNoise(int width, int height,
			int borderWidth, float[] border) {
		// new random
		if (seed != 0) {
			r = new Random(seed);
		} else {
			r = new Random();
		}

		float[][] noise = new float[width][height];

		// loop that assign random values
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (((i <= borderWidth) || (i >= width - borderWidth) || (j >= height
						- borderWidth))
						&& borderWidth != 0 && j >= 5) {
					noise[i][j] = (float) 0;
				} else if (j < 5) {
					noise[i][j] = border[i];
				} else {
					noise[i][j] = (float) r.nextDouble();
				}
			}
		}

		return noise;
	}

	public static float[][] GenerateSEBorderedWhiteNoise(int width, int height,
			int borderWidth, float[] borderS, float[] borderE) {
		// new random
		if (seed != 0) {
			r = new Random(seed);
		} else {
			r = new Random();
		}

		float[][] noise = new float[width][height];

		// south

		// loop that assign random values
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (((i >= width - borderWidth) || (j >= height - borderWidth))
						&& borderWidth != 0 && j >= 5 && i >= 5) {
					noise[i][j] = (float) 0;
				} else if (i < 5) {
					noise[i] = borderE;
				} else if (j < 5) {
					noise[i][j] = borderS[i];
				} else {
					noise[i][j] = (float) r.nextDouble();
				}
			}
		}

		return noise;
	}

	// uses a white noise array to create a new smooth array
	public static float[][] GenerateSmoothNoise(float[][] baseNoise, int octave) {

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

	/*
	 * creates a perlin noise using an array of smooth noises with increased
	 * octaves. this returns an array with large areas of similar values where
	 * the individual values are different enough so as to leave detail between
	 * the variations creating a smoky appearance
	 */
	public static float[][] GeneratePerlinNoise(float[][] baseNoise,
			int octaveCount) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][][] smoothNoise = new float[octaveCount][][]; // an array of 2D
																// arrays
																// containing

		float persistance = 0.5f;

		// generate smooth noises
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

	public static float Interpolate(float x0, float x1, float alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

	public static long getSeed() {
		return seed;
	}

	public static void setSeed(long seed) {
		NoiseGenerator.seed = seed;
	}
}