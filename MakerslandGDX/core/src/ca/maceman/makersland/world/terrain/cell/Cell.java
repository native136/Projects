package ca.maceman.makersland.world.terrain.cell;

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

	private Vector3 corner1;
	private Vector3 corner2;
	private Vector3 corner3;
	private Vector3 corner4;
	private Vector3 leftNormal;
	private Vector3 rightNormal;
	private Color color1;
	private Color color2;
	private Color color3;
	private Color color4;
	private Vector2 texturePos;
		
	/**
	 * @param corner1
	 * @param corner2
	 * @param corner3
	 * @param corner4
	 * @param faceNormal
	 * @param color
	 */
	public Cell(Vector3 corner1, Vector3 corner2, Vector3 corner3, Vector3 corner4, Vector3 leftNormal,Vector3 rightNormal, Color color1, Color color2, Color color3, Color color4) {
		super();
		this.corner1 = corner1;
		this.corner2 = corner2;
		this.corner3 = corner3;
		this.corner4 = corner4;
		this.leftNormal = leftNormal;
		this.rightNormal = rightNormal;
	}

	public Cell(Vector3 corner1, Vector3 corner2, Vector3 corner3, Vector3 corner4, Vector3 leftNormal,Vector3 rightNormal, Color color1, Color color2, Color color3, Color color4, Vector2 texturePos) {
		super();
		this.corner1 = corner1;
		this.corner2 = corner2;
		this.corner3 = corner3;
		this.corner4 = corner4;
		this.leftNormal = leftNormal;
		this.rightNormal = rightNormal;
		this.texturePos = texturePos;
		this.color1 = color1;
		this.color2 = color2;
		this.color3 = color3;
		this.color4 = color4;
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

	public Color getColor1() {
		return color1;
	}

	public void setColor1(Color color1) {
		this.color1 = color1;
	}

	public Color getColor2() {
		return color2;
	}

	public void setColor2(Color color2) {
		this.color2 = color2;
	}

	public Color getColor3() {
		return color3;
	}

	public void setColor3(Color color3) {
		this.color3 = color3;
	}

	public Color getColor4() {
		return color4;
	}

	public void setColor4(Color color4) {
		this.color4 = color4;
	}
}
