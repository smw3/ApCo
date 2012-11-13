package de.schaf.apco;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class BackgroundSpriteEntity extends Entity {
	private void doQuad() {
		GL11.glPushMatrix();
		{
			// Billboard technique
			/* Method 1: face camera plane */
			FloatBuffer buf = BufferUtils.createFloatBuffer(16 * 4);
			// Get your current model view matrix from OpenGL.
			GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buf);
			buf.rewind();

			buf.put(0, 1.0f); // Elliminate rotation part of the
								// transformation matrix
			buf.put(1, 0.0f);
			buf.put(2, 0.0f);

			buf.put(4, 0.0f);
			buf.put(5, 1.0f);
			buf.put(6, 0.0f);

			buf.put(8, 0.0f);
			buf.put(9, 0.0f);
			buf.put(10, 1.0f);

			GL11.glLoadMatrix(buf);
			
			GL11.glTranslatef(-.5f, -.5f, 0f);
			GL11.glScalef(getTexture().getImageWidth(),getTexture().getImageHeight(),1f);
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			{
				GL11.glNormal3f(0f, 0f, 1f);

				GL11.glTexCoord2f(0, 0);
				GL11.glVertex2f(0, 0);

				GL11.glTexCoord2f(0, getTexture().getHeight());
				GL11.glVertex2f(0, 1);

				GL11.glTexCoord2f(getTexture().getWidth(), getTexture()
						.getHeight());
				GL11.glVertex2f(1, 0);

				GL11.glTexCoord2f(getTexture().getWidth(), 0);
				GL11.glVertex2f(1, 1);
			}
		}
		GL11.glPopMatrix();
	}
}
