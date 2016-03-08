package ca.maceman.makersland.world.utils.terrain;

import com.badlogic.gdx.math.Vector3;

public class TerrainUtils {

	public static Vector3 calcNormal(Vector3 p1, Vector3 p2, Vector3 p3) {

		// u = p3 - p1
		float ux = p3.x - p1.x;
		float uy = p3.y - p1.y;
		float uz = p3.z - p1.z;

		// v = p2 - p1
		float vx = p2.x - p1.x;
		float vy = p2.y - p1.y;
		float vz = p2.z - p1.z;

		// n = cross(v, u)
		float nx = ((vy * uz) - (vz * uy));
		float ny = ((vz * ux) - (vx * uz));
		float nz = ((vx * uy) - (vy * ux));

		// // normalize(n)
		float num2 = ((nx * nx) + (ny * ny)) + (nz * nz);
		float num = 1f / (float) Math.sqrt(num2);
		nx *= num;
		ny *= num;
		nz *= num;

		return new Vector3(nx, ny, nz);
	}
}
