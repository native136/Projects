package data;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mapgenerator.Tile;

public class OutputHandler {
	
	public OutputHandler(){
		
	}
	
	public BufferedImage MakeImageFromMap(Tile[][] tiles) {
		int width = tiles.length;
		int height = tiles[0].length;

		BufferedImage bufferImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {

				bufferImage.setRGB(x, y, tiles[x][y].getColor().getRGB());
			}
		}

		return bufferImage;
	}
	
}
