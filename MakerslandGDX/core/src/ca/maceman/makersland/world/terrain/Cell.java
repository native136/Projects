package ca.maceman.makersland.world.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Holds the data for a tile on the terrain.
 * 
 * @author andy.masse
 * 
 */
public class Cell {

	public enum CellType{
		DIRT(new Color(.4f, .3f, .2f, 1f)),
		STONE(new Color(.7f, .7f, .7f, 1f)),
		SAND(new Color(.7f, .6f, .5f, 1f));
		
		public Color cellColor;
		
		CellType(Color color){
			cellColor = color;
		}
	}
	private Vector3 corner1;
	private Vector3 corner2;
	private Vector3 corner3;
	private Vector3 corner4;
	private Vector3 leftNormal;
	private Vector3 rightNormal;
	private Color color;
	private Vector2 texturePos;

	public boolean isFlipped = false;
	public Color nSide;
	public Color wSide;
	public Color eSide;
	public Color sSide;
	public CellType type;
	
	/**
	 * @param corner1
	 * @param corner2
	 * @param corner3
	 * @param corner4
	 * @param faceNormal
	 * @param color
	 */

	public Cell(Vector3 corner1, Vector3 corner2, Vector3 corner3, Vector3 corner4, Vector3 leftNormal, Vector3 rightNormal, CellType type, Vector2 texturePos) {
		super();
		this.corner1 = corner1;
		this.corner2 = corner2;
		this.corner3 = corner3;
		this.corner4 = corner4;
		this.leftNormal = leftNormal;
		this.rightNormal = rightNormal;
		this.texturePos = texturePos;
		this.type = type;
		color = type.cellColor;
		nSide =	color;
		wSide = color;
		eSide = color;
		sSide = color;
	}

	public Vector3 getCorner1() {
		return corner1;
	}

	public void setCorner1(Vector3 corner1) {
		this.corner1 = corner1;
	}

	public Vector3 getCorner2() {
		return corner2;
	}

	public void setCorner2(Vector3 corner2) {
		this.corner2 = corner2;
	}

	public Vector3 getCorner3() {
		return corner3;
	}

	public void setCorner3(Vector3 corner3) {
		this.corner3 = corner3;
	}

	public Vector3 getCorner4() {
		return corner4;
	}

	public void setCorner4(Vector3 corner4) {
		this.corner4 = corner4;
	}

	public Vector3 getLeftNormal() {
		return leftNormal;
	}

	public void setLeftNormal(Vector3 leftNormal) {
		this.leftNormal = leftNormal;
	}

	public Vector3 getRightNormal() {
		return rightNormal;
	}

	public void setRightNormal(Vector3 rightNormal) {
		this.rightNormal = rightNormal;
	}

	public Vector2 getTexturePos() {
		return texturePos;
	}

	public void setTexturePos(Vector2 texturePos) {
		this.texturePos = texturePos;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
