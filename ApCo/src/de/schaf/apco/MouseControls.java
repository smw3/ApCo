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
			click();
			if (!clicked) {
				clicked = true;
				// click();
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
		Vector3f position = getWorldPosition(Mouse.getX(), Mouse.getY());
		System.out.println("Picked: " + position.x + "  " + position.y + "  "
				+ position.z +" for Mouse "+Mouse.getX()+" "+Mouse.getY());
		Game.Instance.TPlane.setPosition(MouseControls.getWorldPosition(Mouse.getX(), Mouse.getY()));
	}

	static public Vector3f getWorldPosition(int mouseX, int mouseY) {
		Vector3f P = new Vector3f();
		// Vector3f homogenized_pos = m_MatrixProjection * WorldPosition;
		int InputOffsetX = (int) (Camera.getX() + mouseX - Game.RENDER_WIDTH / 2);
		int InputOffsetY = (int) (Camera.getY() + mouseY - Game.RENDER_HEIGHT / 2);
		int InputOffsetZ = 0;

		float overhead_angle = Camera.getOverheadAngle();
		float ground_angle = Camera.getGroundAngle();
		
		float vertical_scale = (float) Math.sin(Math.toRadians(30)); // TODO find out what the hell I have to do to get it accurate
				
		float out_x_rotate = -(float) (-Math.sin(ground_angle)*InputOffsetX + Math.cos(ground_angle)*(InputOffsetY/vertical_scale));
		float out_y_rotate = -(float) (Math.cos(ground_angle)*InputOffsetX + Math.sin(ground_angle)*(InputOffsetY/vertical_scale));
		
		float out_x = out_x_rotate;
		float out_y = out_y_rotate;
		
		P.x = out_x;
		P.y = out_y;
		P.z = 0f;
		
		return P;
	}
}
