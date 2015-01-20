package ca.maceman.makersland.world.terrain.parts;

import ca.maceman.makersland.world.utils.terrain.TerrainUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector3;

public class TerrainTriangle {

	private VertexInfo vi;

	public TerrainVector vRA;
	public TerrainVector vAT;
	public TerrainVector vAB;
	public Vector3 normal;
	public Color colour;
	public int type;

	/*
	 * Types (short sides) :
	 * Bottom-Left = 1
	 * BR = 2
	 * TL = 3
	 * TR = 4
	 * 
	 */

	/**
	 * 
	 * @param vri
	 *            : right angle of tri
	 * @param vat
	 *            : top acute
	 * @param vab
	 *            : bottom acute
	 */
	public TerrainTriangle(TerrainVector var, TerrainVector vat, TerrainVector vab) {

		this.vRA = var;
		this.vAT = vat;
		this.vAB = vab;

		normal = TerrainUtils.calcNormal(vRA.toVector3(), vAB.toVector3(), vAT.toVector3());

		colour = new Color(.45f, .3f, .2f, 1f);

		vi = new VertexInfo();

	}

	public VertexInfo getRIVertexInfo() {

		vi = new VertexInfo();
		vi.setPos(vRA.toVector3()).setNor(normal).setCol(colour).setUV(vRA.u, vRA.v);

		return vi;
	}

	public VertexInfo getATVertexInfo() {

		vi = new VertexInfo();
		vi.setPos(vAT.toVector3()).setNor(normal).setCol(colour).setUV(vAT.u, vAT.v);

		return vi;
	}

	public VertexInfo getABVertexInfo() {

		vi = new VertexInfo();

		vi.setPos(vAB.toVector3()).setNor(normal).setCol(colour).setUV(vAB.u, vAB.v);

		return vi;
	}

}
