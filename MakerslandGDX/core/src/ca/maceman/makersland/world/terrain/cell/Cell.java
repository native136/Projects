package ca.maceman.makersland.world.terrain.cell;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class Cell {

	private Vector3 corner1;
	private Vector3 corner2;
	private Vector3 corner3;
	private Vector3 corner4;
	private Vector3 leftNormal;
	private Vector3 rightNormal;
	private Color color;
	
	/**
	 * @param corner1
	 * @param corner2
	 * @param corner3
	 * @param corner4
	 * @param faceNormal
	 * @param color
	 */
	public Cell(Vector3 corner1, Vector3 corner2, Vector3 corner3, Vector3 corner4, Vector3 faceNormal, Color color) {
		super();
		this.corner1 = corner1;
		this.corner2 = corner2;
		this.corner3 = corner3;
		this.corner4 = corner4;
		this.faceNormal = faceNormal;
		this.color = color;
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

	public Vector3 getFaceNormal() {
		return faceNormal;
	}

	public void setFaceNormal(Vector3 faceNormal) {
		this.faceNormal = faceNormal;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
