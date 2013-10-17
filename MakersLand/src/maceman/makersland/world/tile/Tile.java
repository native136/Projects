package maceman.makersland.world.tile;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Tile {

	public enum Type {
		EMPTY, BLACK_WATER, DEEP_WATER, WATER, SHALLOW_WATER, SAND, GRASS, FOREST, BASE_MOUNTAIN, MOUNTAIN, SNOW, RIVER_SOURCE, RIVER, ROAD, TOWNCENTER, BUILDING, WALL
	}

	private float depth;
	private int posX, posY, tileWidth, tileHeight, tileID;
	private Type type;

	public Tile(float depth, int x, int y, int width, int height, Type type) {
		super();
		this.depth = depth;
		this.posX = x;
		this.posY = y;
		this.tileWidth = width;
		this.tileHeight = height;
		this.type = type;
	}

	public Tile(float depth, int x, int y) {
		super();
		this.depth = depth;
		this.posX = x;
		this.posY = y;
		tileWidth = 1;
		tileHeight = 1;

		if (depth < 0.1) {
			type = Type.BLACK_WATER;
		} else if ((depth >= 0.1) && (depth < 0.2)) {
			type = Type.DEEP_WATER;
		} else if ((depth >= 0.2) && (depth < 0.3)) {
			type = Type.WATER;
		} else if ((depth >= 0.3) && (depth < 0.4)) {
			type = Type.SHALLOW_WATER;
		} else if ((depth >= 0.4) && (depth < 0.45)) {
			type = Type.SAND;
		} else if ((depth >= 0.45) && (depth < 0.6)) {
			type = Type.GRASS;
		} else if ((depth >= 0.6) && (depth < 0.7)) {
			type = Type.FOREST;
		} else if ((depth >= 0.7) && (depth < 0.8)) {
			type = Type.BASE_MOUNTAIN;
		} else if ((depth >= 0.8) && (depth < 0.9)) {
			type = Type.MOUNTAIN;
		} else if (depth >= 0.9) {
			type = Type.SNOW;
		} else {
			type = Type.EMPTY;
		}

	}

	public void draw(Canvas canvas, int x, int y, int width, int height, Paint paint) {
		tileWidth = width;
		tileHeight = height;
		posX = x;
		posY = y;

		paint.setColor(getColor());
		canvas.drawRect(posX, posY, posX + tileWidth, posY +tileHeight, paint);

	}

	public int getColor() {
		int color = Color.rgb(0, 0, 100);

		if (type == Type.BLACK_WATER) {
			color = Color.rgb(0, 0, 100);
		} else if (type == Type.DEEP_WATER) {
			color = Color.rgb(0, 75, 190);
		} else if (type == Type.WATER) {
			color = Color.rgb(0, 100, 250);
		} else if (type == Type.SHALLOW_WATER) {
			color = Color.rgb(0, 190, 250);
		} else if (type == Type.SAND) {
			color = Color.rgb(245, 225, 135);
		} else if (type == Type.GRASS) {
			color = Color.rgb(0, 200, 0);
		} else if (type == Type.FOREST) {
			color = Color.rgb(50, 130, 0);
		} else if (type == Type.BASE_MOUNTAIN) {
			color = Color.rgb(94, 94, 94);
		} else if (type == Type.MOUNTAIN) {
			color = Color.rgb(180, 180, 180);
		} else if (type == Type.SNOW) {
			color = Color.rgb(255, 255, 255);
		}// TODO
		// } else if (type == Type.RIVER_SOURCE) {
		// color = Color.CYAN;
		// } else if (type == Type.RIVER) {
		// color = Color.CYAN;
		// } else if (type == Type.TOWNCENTER) {
		// color = Color.orange;
		// } else if (type == Type.ROAD) {
		// color = Color.ORANGE;
		// } else if (type == Type.BUILDING) {
		// color = Color.RED;
		// } else if (type == Type.WALL) {
		// color = Color.BLACK;
		// }

		return color;
	}

	public int getWidth() {
		return tileWidth;
	}

	public void setWidth(int width) {
		this.tileWidth = width;
	}

	public int getHeight() {
		return tileHeight;
	}

	public void setHeight(int height) {
		this.tileHeight = height;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

	public int getX() {
		return posX;
	}

	public void setX(int x) {
		this.posX = x;
	}

	public int getY() {
		return posY;
	}

	public void setY(int y) {
		this.posY = y;
	}

	public int getTileID() {
		return tileID;
	}

	public void setTileID(int tileID) {
		this.tileID = tileID;
	}

}
