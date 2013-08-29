import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class NoiseGenMain {

	public static void main(String[] args) {

		int width = 1000;
		int height = 1000;
		int octave = 4; // number of noises added toghether
		int octaveCount = 8; // number of times we should double the octave and
								// add
		int borderWidth = 30;

		// MakeImage(width, height, octave, octaveCount, borderWidth);
		for (int o = 6; o <= 10; o++) {
			for (int oc = 7; oc <= 10; oc++) {
				MakeImage(width, height, o, oc, borderWidth);
			}
		}
	}

	private static void MakeImage(int width, int height, int octave,
			int octaveCount, int borderWidth) {
		float[][] myArray = new float[width][height];
		float[][] myArrayS = new float[width][height];
		float[][] myArrayP = new float[width][height];

		myArray = GenerateWhiteNoise(width, height, borderWidth);
		myArrayS = GenerateSmoothNoise(myArray, octave);
		myArrayP = GeneratePerlinNoise(myArray, octaveCount);

		// BufferedImage bufferImage = new BufferedImage(width, height,
		// BufferedImage.TYPE_INT_RGB);
		// BufferedImage bufferImageS = new BufferedImage(width, height,
		// BufferedImage.TYPE_INT_RGB);
		BufferedImage bufferImageP = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		//
		// Color[][] gradient = new Color[width][height];
		Color[][] gradientS = new Color[width][height];
		Color[][] gradientP = new Color[width][height];

		// gradient = MapGradient(new Color(0, 0, 0), new Color(255, 255, 255),
		// myArray);
		gradientS = MapGradient(new Color(0, 0, 0), new Color(255, 255, 255),
				myArrayS);
		gradientP = MapGradient(new Color(0, 213, 255), new Color(0, 180, 70),
				myArrayP);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// bufferImage.setRGB(x, y, gradient[x][y].getRGB());
				// bufferImageS.setRGB(x, y, gradientS[x][y].getRGB());
				bufferImageP.setRGB(x, y, gradientP[x][y].getRGB());
			}
		}

		// File outputfile = new File("c:\\Noise\\savedxo" + octave + "oc"
		// // + octaveCount + ".jpg");
		// File outputfileS = new File("c:\\Noise\\savedyo" + octave + "oc"
		// + octaveCount + ".png");
		File outputfileP = new File("c:\\Noise\\savedzo" + octave + "oc"
				+ octaveCount + ".png");
		try {
			// ImageIO.write(bufferImage, "jpg", outputfile);
			// ImageIO.write(bufferImageS, "png", outputfileS);
			ImageIO.write(bufferImageP, "png", outputfileP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static float[][] GenerateWhiteNoise(int width, int height, int borderWidth) {
		Random random = new Random();
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

	static float[][] GenerateSmoothNoise(float[][] baseNoise, int octave) {
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

	static float[][] GeneratePerlinNoise(float[][] baseNoise, int octaveCount) {
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

	static Color GetColor(Color gradientStart, Color gradientEnd, float t) {
		float u = 1 - t;

		// Color color = new Color(
		// (int) (gradientStart.getRed() * u + gradientEnd.getRed() * t),
		// (int) (gradientStart.getGreen() * u + gradientEnd.getGreen()
		// * t),
		// (int) (gradientStart.getBlue() * u + gradientEnd.getBlue() * t));

		Color color = new Color(0, 0, 0);
		if (t < 0.1) {
			color = new Color(0, 0, 100);
		} else if ((t >= 0.1) && (t < 0.2)) {
			color = new Color(0, 75, 190);
		} else if ((t >= 0.2) && (t < 0.3)) {
			color = new Color(0, 100, 250);
		} else if ((t >= 0.3) && (t < 0.4)) {
			color = new Color(0, 190, 250);
		} else if ((t >= 0.4) && (t < 0.45)) {
			color = new Color(245, 225, 135);
		} else if ((t >= 0.45) && (t < 0.6)) {
			color = new Color(0, 200, 0);
		} else if ((t >= 0.6) && (t < 0.7)) {
			color = new Color(50, 130, 0);
		} else if ((t >= 0.7) && (t < 0.8)) {
			color = new Color(94, 94, 94);
		} else if ((t >= 0.8) && (t < 0.9)) {
			color = new Color(180, 180, 180);
		} else if (t >= 0.9) {
			color = Color.white;
		}

		return color;
	}

	static Color[][] MapGradient(Color gradientStart, Color gradientEnd,
			float[][] perlinNoise) {
		int width = perlinNoise.length;
		int height = perlinNoise[0].length;

		Color[][] image = new Color[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				image[i][j] = GetColor(gradientStart, gradientEnd,
						perlinNoise[i][j]);
			}
		}

		return image;
	}

	static float Interpolate(float x0, float x1, float alpha) {
		return x0 * (1 - alpha) + alpha * x1;
	}

}
