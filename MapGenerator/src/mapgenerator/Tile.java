package mapgenerator;

import java.awt.Color;

public class Tile {
	
	public enum Type {
		EMPTY,BLACK_WATER, DEEP_WATER, WATER, SHALLOW_WATER, SAND, GRASS, FOREST, BASE_MOUNTAIN, MOUNTAIN, SNOW
	}

	private float depth;
	private int x,y, width, height;
	private Type type;
	private Color color;
	


	public Tile(float depth, int x, int y, int width, int height, Type type) {
		super();
		this.depth = depth;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
	}

	public Tile(float depth, int x, int y) {
		super();
		this.depth = depth;
		this.x = x;
		this.y = y;
		width = 1;
		height = 1;
		
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Color getColor() {
		Color color = Color.white;
		
		if (type == Type.BLACK_WATER) {
			color = new Color(0, 0, 100);	
		} else if (type == Type.DEEP_WATER) {
			color = new Color(0, 75, 190);
		} else if (type == Type.WATER) {
			color = new Color(0, 100, 250);
		} else if (type == Type.SHALLOW_WATER) {
			color = new Color(0, 190, 250);
		} else if (type == Type.SAND) {
			color = new Color(245, 225, 135);
		} else if (type == Type.GRASS) {
			color = new Color(0, 200, 0);
		} else if (type == Type.FOREST) {
			color = new Color(50, 130, 0);
		} else if (type == Type.BASE_MOUNTAIN) {
			color = new Color(94, 94, 94);
		} else if (type == Type.MOUNTAIN) {
			color = new Color(180, 180, 180);
		} else if (type == Type.SNOW) {
			color = Color.white;
		}
		
		return color;
	}

	

	
	
}
