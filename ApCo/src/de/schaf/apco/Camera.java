package de.schaf.apco;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;


public class Camera {
	private static Vector3f Focus = new Vector3f();

	public static void init() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-Game.RENDER_WIDTH /2, Game.RENDER_WIDTH /2, Game.RENDER_HEIGHT /2, -Game.RENDER_HEIGHT /2, 10000, -10000);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void initISO() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-Game.RENDER_WIDTH /2, Game.RENDER_WIDTH /2, Game.RENDER_HEIGHT /2, -Game.RENDER_HEIGHT /2, 10000, -10000);
		
		float dist = (float) Math.sqrt(1 / 3.0f);
		
		float overhead_angle = getOverheadAngle();
		float ground_angle = getGroundAngle();
		
		float height = (float) Math.sin(overhead_angle);
		
		gluLookAt(dist, dist, height,
				0f, 0f, 0f,				
				dist, dist, 0f);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public static void Move(float dx, float dy) {
		Focus.x += dx;
		Focus.y += dy;
	}
	
	public static void setFocus(float x, float y) {
		Focus.x = x;
		Focus.y = y;
	}
	
	public static float getX() {
		float dist = (float) Math.sqrt(1 / 3.0f);
		
		return Focus.x+dist;	
	}
	
	public static float getY() {
		float dist = (float) Math.sqrt(1 / 3.0f);
		
		return Focus.y+dist;	
	}
	
	public static float getZ() {
		float dist = (float) Math.sqrt(1 / 3.0f);
		
		return Focus.z+dist;	
	}

	public static float getGroundAngle() {
		return (float) Math.toRadians(45);
	}

	public static float getOverheadAngle() {
		return (float) Math.toRadians(30);
	}
}
