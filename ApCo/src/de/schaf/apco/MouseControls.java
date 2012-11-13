package de.schaf.apco;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;


public class MouseControls {
	private static boolean clicked = false;
	private static Point dragStart = null;

	public static void Do() {
		if (Mouse.isButtonDown(1)) {
			if (dragStart != null) {
				drag();
			} else {
				dragStart = new Point(Mouse.getX(), Mouse.getY());
			}
		} else {
			dragStart = null;
		}

		if (Mouse.isButtonDown(0)) {
			if (!clicked) {
				clicked = true;
				click();
			}
		} else {
			clicked = false;
		}
	}

	private static void drag() {
		int dMousex = (int) dragStart.x - Mouse.getX();
		int dMousey = (int) dragStart.y - Mouse.getY();
		dragStart.x = Mouse.getX();
		dragStart.y = Mouse.getY();
		Camera.Move(dMousex, dMousey);
	}

	private static void click() {
		Pick();
	}

	public static Point getMousePoint() {
		return new Point(Mouse.getX(), Mouse.getY());
	}

	public static void Pick() {
		FloatBuffer position = getMousePosition(Mouse.getX(), Mouse.getY());
		System.out.println("Picked: "+position.get(0)+"  "+position.get(1)+"  "+0f);
		Game.Instance.doSomethingWithAPick(position.get(0), position.get(1),0f);
	}

	static public FloatBuffer getMousePosition(int mouseX, int mouseY) {
		// TODO: fix this
		Camera.initISO();
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		FloatBuffer winZ = BufferUtils.createFloatBuffer(1);
		float winX, winY;
		FloatBuffer position = BufferUtils.createFloatBuffer(3);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
		winX = (float) mouseX;
		// winY = (float) viewport.get(3) - (float) mouseY;
		winY = (float) mouseY;
		GL11.glReadPixels(mouseX, (int) winY, 1, 1, GL11.GL_DEPTH_COMPONENT,
				GL11.GL_FLOAT, winZ);
		//GLU.gluUnProject(winX, winY, winZ.get(), modelview, projection,
		//		viewport, position);
		float winz_out = .5f;
		System.out.println(winz_out);
		GLU.gluUnProject(winX, winY, winz_out, modelview, projection,
				viewport, position);
		return position;
	}
	// If you get a DirectBuffer exception when reading the position from the
	// FloatBuffer, just use position.get(0) for X, position.get(1) for Y and
	// position.get(2) for Z. Enjoy!

}
