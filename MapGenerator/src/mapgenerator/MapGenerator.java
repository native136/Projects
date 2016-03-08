package mapgenerator;

import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import mapgenerator.Tile.Type;
import noisegenerator.NoiseGenerator;

public class MapGenerator {

	private NoiseGenerator noiseGenerator;
	private Random r;

	public MapGenerator(NoiseGenerator noiseGenerator) {
		this.noiseGenerator = noiseGenerator;

	}

	public Tile[][] GenerateTerrainMap(float[][] basePerlinNoise, int seed) {

		r = new Random(seed);
		int width = basePerlinNoise.length;
		int height = basePerlinNoise[0].length;

		Tile[][] tiles = new Tile[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = new Tile(basePerlinNoise[i][j], i, j);
			}
		}

		tiles = AddRivers(tiles, r);
		return tiles;
	}

	private Tile[][] AddRivers(Tile[][] tiles, Random r) {
		int width = tiles.length;
		int height = tiles[0].length;
		int c = 0;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (tiles[i][j].getType() == Tile.Type.MOUNTAIN) {

					// Generate river Sources
					c = r.nextInt(100);
					if (c >= 90) {
						tiles[i][j].setType(Tile.Type.RIVER_SOURCE);

						// Rivers go to ocean by path of least resistance or
						// stop when no other way is found

						Point closestTile = findClosestOfType(tiles,
								Type.SHALLOW_WATER, i, j, 301); // May have to
																// make these as
																// runnables to
																// fix long map
																// gen times

						PlotRiver(tiles, i, j, closestTile);
						if (i + 10 < width) {
							i = i + 10;
						}
						if (j + 10 < height) {
							j = j + 10;
						}

					}

				}
			}
		}

		return tiles;
	}

	// using recursion we make a path to water by using the path of least
	// resistance; using the depth of the tile as the weight of said resistance.
	private Tile[][] PlotRiver(Tile[][] tiles, int i, int j, Point closestTile) {

		// Make sure we dont go out of bounds
		// if the tile is an ocean tile finish the series of recursions

		if ((i != 0) && (j != 0) && (i < tiles.length - 1)
				&& (j < tiles[0].length - 1)) {
			float d = 10000; // smallest depth so far
			int m = 0; // x-value of tile with lowest depth so far
			int n = 0; // y-value of tile with lowest depth so far

			for (int x = i - 1; x <= i + 1; x++) {
				for (int y = j - 1; y <= j + 1; y++) {
					// make sure we don't go back (Could probably add a last
					// x
					// and y var so that rivers can join together)
					if ((tiles[x][y].getType() != Tile.Type.RIVER_SOURCE)
							&& (tiles[x][y].getType() != Tile.Type.RIVER)
							&& ((x == i) || (y == j))
							&& (!((x == i) && (y == j)))) {
						if (getDifficulty(x, y, closestTile.x, closestTile.y,
								tiles) < d) {
							d = getDifficulty(x, y, closestTile.x,
									closestTile.y, tiles);
							m = x;
							n = y;
						}
					}

				}
			}
			if (tiles[m][n].getType() != Tile.Type.SHALLOW_WATER) {
				tiles[m][n].setType(Type.RIVER);
				PlotRiver(tiles, m, n, closestTile);
			}

		}
		return tiles;
	}

	/*
	 * private Tile[][] AddRoads(Tile[][] tiles, Random r) { int width =
	 * tiles.length; int height = tiles[0].length; int c = 0;
	 * 
	 * for (int i = 0; i < width; i++) { for (int j = 0; j < height; j++) { if
	 * (tiles[i][j].getType() == Tile.Type.MOUNTAIN) {
	 * 
	 * // // Generate river Sources // c = r.nextInt(100); // if (c >= 90) { //
	 * tiles[i][j].setType(Tile.Type.RIVER_SOURCE); // // // // Rivers go to
	 * ocean by path of least resistance or // // stop when no other way is
	 * found // // Point closestTile =
	 * findClosestOfType(tiles,Type.SHALLOW_WATER,i, j, 301); // May have to
	 * make these as runnables to fix long map gen times // // PlotRiver(tiles,
	 * i, j, closestTile); // if (i + 10 < width) { // i = i + 10; // } // if (j
	 * + 10 < height) { // j = j + 10; // } // // }
	 * 
	 * } } }
	 * 
	 * return tiles; }
	 */
	// using recursion we make a path to water by using the path of least
	// resistance; using the depth of the tile as the weight of said resistance.
	/*
	 * private Tile[][] PlotRoads(Tile[][] tiles, int i, int j, Point
	 * closestTile) {
	 * 
	 * // Make sure we dont go out of bounds // if the tile is an ocean tile
	 * finish the series of recursions
	 * 
	 * if ((i != 0) && (j != 0) && (i < tiles.length - 1) && (j <
	 * tiles[0].length - 1)) { float d = 10000; // smallest depth so far int m =
	 * 0; // x-value of tile with lowest depth so far int n = 0; // y-value of
	 * tile with lowest depth so far
	 * 
	 * for (int x = i - 1; x <= i + 1; x++) { for (int y = j - 1; y <= j + 1;
	 * y++) { // // make sure we don't go back (Could probably add a last TODO
	 * // // x // // and y var so that rivers can join together) // if
	 * ((tiles[x][y].getType() != Tile.Type.RIVER_SOURCE) // &&
	 * (tiles[x][y].getType() != Tile.Type.RIVER) // && ((x == i) || (y == j))
	 * // && (!((x == i) && (y == j)))) { // if (getDifficulty(x, y,
	 * closestTile.x, closestTile.y, tiles) < d) { // d = getDifficulty(x, y,
	 * closestTile.x, closestTile.y, // tiles); // m = x; // n = y; // } // }
	 * 
	 * } } if (tiles[m][n].getType() != Tile.Type.SHALLOW_WATER) {
	 * tiles[m][n].setType(Type.RIVER); PlotRiver(tiles, m, n, closestTile); }
	 * 
	 * } return tiles; }
	 */
	// finds a path and stores the coordinates of each tile along the path into
	// a point array
	private float getDifficulty(int x, int y, int destX, int destY,
			Tile[][] tiles) {
		float g = 0; // cost to move from current tile to next tile
		float h = 0; // estimated move cost from a tile to destination
		float f = 0; // value of g and h to calculate which path is easiest to
						// take

		g = tiles[x][y].getDepth() * 2;
		h = EstimateHeuristic(x, y, destX, destY, tiles);

		f = g + h;

		return f;

	}

	private float EstimateHeuristic(int x, int y, int destX, int destY,
			Tile[][] tiles) {
		float h = 0;
		int width = tiles.length;
		int height = tiles[0].length;

		for (int px = 0; px < width; px++) {
			for (int py = 0; py < height; py++) {
				// if 0, point is on line
				if (Line2D.ptSegDist(x, y, destX, destY, px, py) <= 0.5) {
					h += tiles[px][py].getDepth();
				}
			}
		}

		return h;

	}

	// something here is wrong TODO
	private Point findClosestOfType(Tile[][] tiles, Type type, int x, int y,
			int radius) {

		Point p = null;
		for (int px = 0; px < tiles.length; px++) {
			for (int py = 0; py < tiles[0].length; py++) {
				// if 0, point is on line
				// this if is never true.
				if ((((((px - x) * (px - x))) +	((py - y) * (py - y))) <= (radius * radius)
						+ radius)
						&& ((((px - x) * (px - x))) + ((py - y) * (py - y))) >= (radius * radius)
								- radius) {
					if ((px >= 0) && (py >= 0) && (px < tiles.length - 1)
							&& (py < tiles[0].length - 1)) {

						if (tiles[px][py].getType() == type) {
							p = new Point(px, py);
							return p;
						}
					}
				}
			}
		}

		if (p == null) {
			p = findClosestOfType(tiles, type, x, y, radius + 1);
			return p;
		}

		return p;
	}

	private Point findLowestPoint(Tile[][] tiles, int x, int y, int radius) {
		float d = 10000;
		Point p = new Point(0, 0);

		for (int px = 0; px < tiles.length; px++) {
			for (int py = 0; py < tiles[0].length; py++) {
				if ((((((px - x) * (px - x))) + ((py - y) * (py - y))) <= (radius * radius)
						+ radius)
						&& ((((px - x) * (px - x))) + ((py - y) * (py - y))) >= (radius * radius)
								- radius) {
					if ((px >= 0) && (py >= 0) && (px < tiles.length - 1)
							&& (py < tiles[0].length - 1)) {
						if (getDifficulty(px, py, x, y, tiles) < d) {
							d = getDifficulty(px, py, x, y, tiles);
							p.x = x;
							p.y = y;
						}
					}
				}
			}
		}
		return p;
	}
}
