package de.schaf.apco.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;

public class Buffer {

    public static FloatBuffer getFloatBuffer(float[] t) {
        ByteBuffer temp = ByteBuffer.allocateDirect(t.length*4);
        temp.order(ByteOrder.nativeOrder());
        return (FloatBuffer) temp.asFloatBuffer().put(t).flip();
    }
    
    public static IntBuffer getIntBuffer(int[] t) {
        ByteBuffer temp = ByteBuffer.allocateDirect(t.length*4);
        temp.order(ByteOrder.nativeOrder());
        return (IntBuffer) temp.asIntBuffer().put(t).flip();
    }

    public static DoubleBuffer getDoubleBuffer(double[] t) {
        ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
        return (DoubleBuffer) temp.asDoubleBuffer().put(t).flip();
    }

    public static FloatBuffer getFloatBufferFromVec3(Vector3f v) {
        float[] b = {v.x, v.y, v.z, 1.0f};
        return getFloatBuffer(b);
    }

    public static FloatBuffer getFloatBufferFromArrayList(List<Vector3f> l) {
        float[] b = new float[l.size() * 3];

        Iterator<Vector3f> it_v = l.iterator();

        int i = 0;

        while (it_v.hasNext()) {
            Vector3f v = it_v.next();
            b[i++] = v.x;
            b[i++] = v.y;
            b[i++] = v.z;
        }
        
        return getFloatBuffer(b);
    }

    public static FloatBuffer getFloatBufferFromArrayList(List<Vector3f> l, boolean onlyxy) {
        float[] b = new float[l.size() * 3];

        Iterator<Vector3f> it_v = l.iterator();

        int i = 0;

        while (it_v.hasNext()) {
            Vector3f v = it_v.next();
            b[i++] = v.x;
            b[i++] = v.y;
        }
        
        return getFloatBuffer(b);
    }

    public static IntBuffer getIntBufferFromArrayList(List<Integer> l) {
        int[] b = new int[l.size()];

        Iterator<Integer> it_v = l.iterator();

        int i = 0;

        while (it_v.hasNext()) {
            int v = it_v.next();
            b[i++] = v;
        }
        
        return getIntBuffer(b);
    }
}
