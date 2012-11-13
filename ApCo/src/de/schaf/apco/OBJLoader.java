/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.schaf.apco;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author s_m_w
 */
public class OBJLoader {

    public static MeshObj loadModel(String f) throws FileNotFoundException, IOException {
        //BufferedReader reader = new BufferedReader(new FileReader(f));
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                new BufferedInputStream((new Object() {}).getClass().getClassLoader().getResourceAsStream(f))));
        MeshObj m = new MeshObj();

        String line;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                float x = Float.valueOf(line.split(" ")[1]);
                float y = Float.valueOf(line.split(" ")[2]);
                float z = Float.valueOf(line.split(" ")[3]);
                m.vertices.add(new Vector3f(x, y, z));
            } else if (line.startsWith("vn ")) {
                float x = Float.valueOf(line.split(" ")[1]);
                float y = Float.valueOf(line.split(" ")[2]);
                float z = Float.valueOf(line.split(" ")[3]);
                m.normals.add(new Vector3f(x, y, z));
            } else if (line.startsWith("vt ")) {
                float x = Float.valueOf(line.split(" ")[1]);
                float y = 1f-Float.valueOf(line.split(" ")[2]);
                float z = 0;
                m.texture.add(new Vector3f(x, y, z));
            } else if (line.startsWith("f ")) {
                String data[] = line.split(" ");
                float vertIndex[] = new float[3];
                float normalIndex[] = new float[3];
                float textureIndex[] = new float[3];
                
                vertIndex[0] = Float.parseFloat(data[1].split("/")[0]);
                textureIndex[0] = Float.parseFloat(data[1].split("/")[1]);
                normalIndex[0] = Float.parseFloat(data[1].split("/")[2]);
                
                vertIndex[1] = Float.parseFloat(data[2].split("/")[0]);
                textureIndex[1] = Float.parseFloat(data[2].split("/")[1]);
                normalIndex[1] = Float.parseFloat(data[2].split("/")[2]);
                
                vertIndex[2] = Float.parseFloat(data[3].split("/")[0]);
                textureIndex[2] = Float.parseFloat(data[3].split("/")[1]);
                normalIndex[2] = Float.parseFloat(data[3].split("/")[2]);

                Vector3f vertexIndices = new Vector3f(vertIndex[0], vertIndex[1], vertIndex[2]);
                Vector3f normalIndices = new Vector3f(normalIndex[0], normalIndex[1], normalIndex[2]);
                Vector3f textureIndices = new Vector3f(textureIndex[0], textureIndex[1], textureIndex[2]);

                Face face = new Face(vertexIndices, textureIndices, normalIndices);

                m.faces.add(face);
            }
        }

        m.compile();

        return m;

    }
}
