package de.schaf.apco.util;

import java.awt.geom.Point2D;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import de.schaf.apco.Camera;

/* Author: Mickelukas http://lwjgl.org/forum/index.php?action=printpage;topic=4696.0 */

public final class Math3D {

    private static FloatBuffer modelview = BufferUtils.createFloatBuffer(16); //The model view for caching

    private static FloatBuffer projection = BufferUtils.createFloatBuffer(16); //The projection for caching

    private static IntBuffer viewport = BufferUtils.createIntBuffer(16); //The view port for caching

    private final static FloatBuffer tempBuffer = FloatBuffer.allocate(3); //A temporary buffer

    private final static double[] tempVec = new double[3]; //A temporary double array

    //Generate the private variables before calling the below functions that rely on them
    public static void generateArrays() {
        modelview.clear();
        projection.clear();
        viewport.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
    }

    //Get the position inside the application from a passed position on the applet together with depth, realPos gets set to the return value
    public static void getPoint3DfromPoint2D(final Point2D p, final double zPos, final Vector3f realPos) {
        if (p == null) {
            return;
        }
        //Get the coordinates at the far plane
        tempBuffer.clear();
        GLU.gluUnProject((float)p.getX(), (float)p.getY(), 1f, modelview, projection, viewport, tempBuffer);
        //Get the vector that goes from the UnProject to the camera
        tempVec[0] = Camera.getX() - tempBuffer.get(0);
        tempVec[1] = Camera.getY() - tempBuffer.get(1);
        tempVec[2] = Camera.getZ() - tempBuffer.get(2);
        //Calculate X and Y based on a known Z
        tempBuffer.put(0, (float)((zPos - tempBuffer.get(2)) / tempVec[2] * tempVec[0] + tempBuffer.get(0)));
        tempBuffer.put(1, (float)((zPos - tempBuffer.get(2)) / tempVec[2] * tempVec[1] + tempBuffer.get(1)));
        tempBuffer.put(2, (float)zPos);
        realPos.x = tempBuffer.get(0);
        realPos.y = tempBuffer.get(1);
        realPos.z = tempBuffer.get(2);
    }

}
