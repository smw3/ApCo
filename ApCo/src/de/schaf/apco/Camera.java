package de.schaf.apco;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.util.vector.Vector3f;


public class Camera {
	private static Vector3f Focus = new Vector3f();
	
	public static void init() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-Game.RENDER_WIDTH / 2, Game.RENDER_WIDTH / 2, Game.RENDER_HEIGHT / 2, -Game.RENDER_HEIGHT / 2, 10000, -10000);
		
		float dist = (float) Math.sqrt(1 / 3.0f);
		
		gluLookAt(dist, dist, dist,
				Focus.x, Focus.y, Focus.z,
				0f, 0f, -1f);
	}
	
	public static void Move(float dx, float dy) {
		Focus.x += dx;
		Focus.y += dy;
	}
	
	public static void setFocus(float x, float y) {
		Focus.x = x;
		Focus.y = y;
	}
}
