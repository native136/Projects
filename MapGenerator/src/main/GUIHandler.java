package main;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import data.OutputHandler;
import noisegenerator.NoiseGenerator;
import main.gui.MapGenGui;
import mapgenerator.MapGenerator;
import mapgenerator.Tile;

public class GUIHandler {

	private MapGenerator mapGenerator;
	private NoiseGenerator noiseGenerator;
	private OutputHandler oHandler;
	private MapGenGui gui;
	private Random r;
	private Tile[][] tiles;

	public GUIHandler(MapGenGui mapGenGui) {
		noiseGenerator = new NoiseGenerator();
		mapGenerator = new MapGenerator(noiseGenerator);
		oHandler = new OutputHandler();
		gui = mapGenGui;
		r = new Random();
	}

	/*
	 * Creates a new map from values given by the user
	 */
	public void GenerateNewMap(String sWidth, String sHeight, int octaveCount,
			String sBorderWidth, String sSeed) {
		int width;
		int height;
		int borderWidth;
		int seed;

		try {
			width = Integer.parseInt(sWidth);
		} catch (Exception e) {
			width = 500;
		}

		try {
			height = Integer.parseInt(sHeight);
		} catch (Exception e) {
			height = 500;
		}

		try {
			borderWidth = Integer.parseInt(sBorderWidth);
		} catch (Exception e) {
			borderWidth = 0;
		}

		try {
			seed = Integer.parseInt(sSeed);
		} catch (Exception e) {
			seed = r.nextInt();
		}

		float[][] noise = new float[width][height];
		float[][] perlinNoise = new float[width][height];
		tiles = new Tile[width][height];

		noise = noiseGenerator.GenerateWhiteNoise(width, height, borderWidth,
				seed);

		perlinNoise = noiseGenerator.GeneratePerlinNoise(noise, octaveCount);

		tiles = mapGenerator.GenerateTerrainMap(perlinNoise, seed);

	}

	public void MapToPanel() {
		try {
			if (tiles != null) {
				gui.getLblImageHolder()
						.setIcon(
								new ImageIcon((Image) oHandler
										.MakeImageFromMap(tiles)));
				gui.getLblImageHolder().setSize(
						new Dimension(tiles.length, tiles[0].length));

				gui.getPnlImg().setPreferredSize(
						gui.getLblImageHolder().getSize());
				gui.getPnlImg().repaint();
				gui.getPnlImg().revalidate();

				gui.getScrollPane().setViewportView(gui.getPnlImg());
				gui.getScrollPane().repaint();
				gui.getScrollPane().revalidate();

				gui.getMainFrame().repaint();
				gui.getMainFrame().revalidate();
			}
		} catch (Exception e) {
			showErr(e.toString());
		}
	}

	public void scaledImage(int i) {
		try {
			if ((tiles != null) && (gui.getLblImageHolder().getIcon() != null)) {
				BufferedImage orig = oHandler.MakeImageFromMap(tiles);
				int width = orig.getWidth();
				int height = orig.getHeight();

				BufferedImage newImg = new BufferedImage(width * i, height * i,
						BufferedImage.TYPE_INT_ARGB);
				AffineTransform t = new AffineTransform();
				t.scale((double) i, (double) i);
				AffineTransformOp scale = new AffineTransformOp(t,
						AffineTransformOp.TYPE_BILINEAR);
				newImg = scale.filter(orig, newImg);

				gui.getLblImageHolder().setIcon(new ImageIcon((Image) newImg));

				gui.getLblImageHolder().setSize(
						new Dimension(newImg.getWidth(), newImg.getHeight()));
				gui.getPnlImg().setPreferredSize(
						gui.getLblImageHolder().getSize());
				gui.getPnlImg().repaint();
				gui.getPnlImg().revalidate();

				gui.getScrollPane().setViewportView(gui.getPnlImg());
				gui.getScrollPane().repaint();
				gui.getScrollPane().revalidate();

				gui.getMainFrame().repaint();
				gui.getMainFrame().revalidate();
			}
		} catch (Exception e) {
			showErr(e.toString());
		}
	}

	public MapGenerator getMapGenerator() {
		return mapGenerator;
	}

	public NoiseGenerator getNoiseGenerator() {
		return noiseGenerator;
	}

	public OutputHandler getoHandler() {
		return oHandler;
	}

	public void showErr(String e) {
		JOptionPane.showMessageDialog(gui.getMainFrame(),
				"An error has occured: " + e, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

}
