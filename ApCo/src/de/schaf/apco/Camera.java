package de.schaf.apco;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.util.vector.Vector3f;


public class Camera {
	private static Vector3f Focus = new Vector3f();
	
	public static void init() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Game.RENDER_WIDTH, Game.RENDER_HEIGHT, 0, 10, -10);
		
		float dist = (float) Math.sqrt(1 / 3.0f);
		
		gluLookAt(dist, dist, dist,
				Focus.x, Focus.y, Focus.z,
				0f, 1f, 0f);
	}
}
