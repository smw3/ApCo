/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.schaf.apco;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author s_m_w
 */
class Face {

    public Vector3f vertexIndices = new Vector3f();
    public Vector3f normalIndices = new Vector3f();
    public Vector3f textureIndices = new Vector3f();
    public Vector3f[] Position = new Vector3f[3];
    public Vector3f[] Normal = new Vector3f[3];
    public Vector3f[] TextureCoord = new Vector3f[3];
    public Vector3f[] Tangent = new Vector3f[3];

    public Face(Vector3f vertex, Vector3f normal) {
        vertexIndices = vertex;
        normalIndices = normal;
    }

    Face(Vector3f vertex, Vector3f texture, Vector3f normal) {
        vertexIndices = vertex;
        normalIndices = normal;
        textureIndices = texture;
    }

    public void setVertexPosition(int index, Vector3f Pos) {
        Position[index] = Pos;
    }

    public void setVertexNormal(int index, Vector3f Norm) {
        Normal[index] = Norm;
    }

    public void setVertexTextureCoord(int index, Vector3f Tex) {
        TextureCoord[index] = Tex;
    }

    public Vector3f[] getVertexPosition() {
        return Position;
    }

    public Vector3f[] getVertexNormal() {
        return Normal;
    }

    public Vector3f[] getVertexTextureCoord() {
        return TextureCoord;
    }

    void setVertexTangent(int index, Vector3f tangent) {
        Tangent[index] = tangent;
    }

    public Vector3f[] getVertexTangent() {
        return Tangent;
    }

    public Vector3f getCenter() {
        Vector3f v1 = getVertexPosition()[0];
        Vector3f v2 = getVertexPosition()[1];
        Vector3f v3 = getVertexPosition()[2];
        Vector3f center = new Vector3f();
        center.x = v1.x + v2.x + v3.x;
        center.x = center.x / 3;
        center.y = v1.y + v2.y + v3.y;
        center.y = center.y / 3;
        center.z = v1.z + v2.z + v3.z;
        center.z = center.z / 3;
        return center;
    }

    Vector3f getFaceNormal() {
        Vector3f v1 = getVertexNormal()[0];
        Vector3f v2 = getVertexNormal()[1];
        Vector3f v3 = getVertexNormal()[2];
        Vector3f normal = new Vector3f();
        
        Vector3f.add(Vector3f.add(v1,v2,null),v3,normal);
        normal.normalise();
        return normal;
    }
}
