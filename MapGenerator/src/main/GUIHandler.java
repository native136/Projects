package main;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

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
	 * Creates a new map from values giiven by the user
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
		
		tiles = mapGenerator.GenerateTerrainMap(perlinNoise);

	}

	public void MapToPanel() {
		if(tiles!=null){
			gui.getLblImageHolder().setIcon(new ImageIcon((Image) oHandler.MakeImageFromMap(tiles)));
			gui.getLblImageHolder().setSize(tiles.length, tiles[0].length);
			gui.getPnlImg().setPreferredSize(gui.getLblImageHolder().getSize());

			gui.getPnlImg().repaint();
			gui.getPnlImg().revalidate();
			gui.getScrollPane().setViewportView(gui.getPnlImg());
			gui.getScrollPane().repaint();
			gui.getScrollPane().revalidate();
			gui.getMainFrame().repaint();
			gui.getMainFrame().revalidate();
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

}
