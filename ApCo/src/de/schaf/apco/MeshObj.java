package de.schaf.apco;

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import de.schaf.apco.util.Buffer;
import de.schaf.apco.util.ShaderHandler;

public class MeshObj extends Entity {
    public List<Vector3f> vertices = new ArrayList<Vector3f>();
    public List<Vector3f> normals = new ArrayList<Vector3f>();
    public List<Vector3f> tangents = new ArrayList<Vector3f>();
    public List<Vector3f> texture = new ArrayList<Vector3f>();
    public List<Integer> Index = new ArrayList<Integer>();
    public FloatBuffer tangentBuffer;
    public List<Face> faces = new ArrayList<Face>();
    public int NumberOfVertices = 0;
    public int vboVertexHandle;
    public int vboNormalHandle;
    public int vboTextureHandle;
    public int vboTangentHandle;
    public int vboIndexHandle;
    
    public float min_x, max_x; // Maximum size of the mesh
    public float min_y, max_y;
    public float min_z, max_z;
    
    private Texture Texture;
    
    public int NormalHandle = -1;

    public MeshObj() {
    }

    public void compile() {
        List<Vector3f> redundantVertices = new ArrayList<Vector3f>();
        List<Vector3f> redundantNormals = new ArrayList<Vector3f>();
        List<Vector3f> redundantTextures = new ArrayList<Vector3f>();

        for (Vector3f vert : vertices) {
        	min_x = Math.min(vert.x, min_x);
        	max_x = Math.max(vert.x, max_x);
        	
        	min_y = Math.min(vert.y, min_y);
        	max_y = Math.max(vert.y, max_y);
        	
        	min_z = Math.min(vert.z, min_z);
        	max_z = Math.max(vert.z, max_z);
        }
        
        // Convert indices to values
        
        for (Face face : faces) {
            int PosIndexVert1 = (int) face.vertexIndices.x - 1;
            int PosIndexVert2 = (int) face.vertexIndices.y - 1;
            int PosIndexVert3 = (int) face.vertexIndices.z - 1;

            int NormalIndexVert1 = (int) face.normalIndices.x - 1;
            int NormalIndexVert2 = (int) face.normalIndices.y - 1;
            int NormalIndexVert3 = (int) face.normalIndices.z - 1;

            int TexIndexVert1 = (int) face.textureIndices.x - 1;
            int TexIndexVert2 = (int) face.textureIndices.y - 1;
            int TexIndexVert3 = (int) face.textureIndices.z - 1;

            Index.add(PosIndexVert1);
            Index.add(PosIndexVert2);
            Index.add(PosIndexVert3);

            face.setVertexPosition(0, vertices.get(PosIndexVert1));
            face.setVertexPosition(1, vertices.get(PosIndexVert2));
            face.setVertexPosition(2, vertices.get(PosIndexVert3));

            face.setVertexNormal(0, normals.get(NormalIndexVert1));
            face.setVertexNormal(1, normals.get(NormalIndexVert2));
            face.setVertexNormal(2, normals.get(NormalIndexVert3));

            face.setVertexTextureCoord(0, texture.get(TexIndexVert1));
            face.setVertexTextureCoord(1, texture.get(TexIndexVert2));
            face.setVertexTextureCoord(2, texture.get(TexIndexVert3));

            // Tangent
            Vector3f tangent = new Vector3f();


            Vector3f Vert1 = vertices.get(PosIndexVert1);
            Vector3f Vert2 = vertices.get(PosIndexVert2);
            Vector3f Vert3 = vertices.get(PosIndexVert3);

            Vector3f TexCoord1 = texture.get(TexIndexVert1);
            Vector3f TexCoord2 = texture.get(TexIndexVert2);
            Vector3f TexCoord3 = texture.get(TexIndexVert3);

            Vector3f Edge1 = Vector3f.sub(Vert2, Vert1, null);
            Vector3f Edge2 = Vector3f.sub(Vert3, Vert1, null);

            Vector3f Edge1UV = Vector3f.sub(TexCoord2, TexCoord1, null);
            Vector3f Edge2UV = Vector3f.sub(TexCoord3, TexCoord1, null);

            float coef = 1 / (Edge1UV.x * Edge2UV.y - Edge2UV.x * Edge2UV.y);

            tangent.x = coef * ((Edge1.x * Edge2UV.y) + (Edge2.x * -Edge2UV.y));
            tangent.y = coef * ((Edge1.y * Edge2UV.y) + (Edge2.y * -Edge2UV.y));
            tangent.z = coef * ((Edge1.z * Edge2UV.y) + (Edge2.z * -Edge2UV.y));

            face.setVertexTangent(0, tangent);
            face.setVertexTangent(1, tangent);
            face.setVertexTangent(2, tangent);
        }

        // Compute bounding box
        

        List<Vector3f> Tangents = new ArrayList<Vector3f>();

        // Compute faces
        
        for (Face face : faces) {
            redundantVertices.add(face.getVertexPosition()[0]);
            redundantVertices.add(face.getVertexPosition()[1]);
            redundantVertices.add(face.getVertexPosition()[2]);

            redundantNormals.add(face.getVertexNormal()[0]);
            redundantNormals.add(face.getVertexNormal()[1]);
            redundantNormals.add(face.getVertexNormal()[2]);

            redundantTextures.add(face.getVertexTextureCoord()[0]);
            redundantTextures.add(face.getVertexTextureCoord()[1]);
            redundantTextures.add(face.getVertexTextureCoord()[2]);

            Tangents.add(face.getVertexTangent()[0]);
            Tangents.add(face.getVertexTangent()[1]);
            Tangents.add(face.getVertexTangent()[2]);
        }

        // Compute VBO
        
        NumberOfVertices = redundantVertices.size();

        // Turns a list of Vector3fs into a floatbuffer. Works.
        FloatBuffer vertexData = Buffer.getFloatBufferFromArrayList(redundantVertices);
        FloatBuffer normalData = Buffer.getFloatBufferFromArrayList(redundantNormals);
        FloatBuffer textureData = Buffer.getFloatBufferFromArrayList(redundantTextures, true);
        FloatBuffer tangentData = Buffer.getFloatBufferFromArrayList(Tangents);
        IntBuffer indexData = Buffer.getIntBufferFromArrayList(Index);

        tangentBuffer = tangentData;

        vboVertexHandle = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

        vboNormalHandle = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
        glBufferData(GL_ARRAY_BUFFER, normalData, GL_STATIC_DRAW);

        vboTextureHandle = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, textureData, GL_STATIC_DRAW);

        vboTangentHandle = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboTangentHandle);
        glBufferData(GL_ARRAY_BUFFER, tangentData, GL_STATIC_DRAW);

        vboIndexHandle = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexHandle);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void render() {
        //glPushMatrix();

        int Shader = ShaderHandler.Shader.PPL.ProgramHandle;
        //int Shader = -1;
        //if (Shader != -1) glUseProgram(Shader);

        //if (Texture != null) {
            //glEnable(GL_TEXTURE_2D);
            Texture.bind();
       // } else {
        //    glDisable(GL_TEXTURE_2D);
        //}

        //MaterialHandler.Material.STANDARD.bind();

        glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
        glNormalPointer(GL_FLOAT, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, vboTextureHandle);
        glTexCoordPointer(2, GL_FLOAT, 0, 0L);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        
        // Relay tangent information to shader (if nessecary). I THINK it works.
        if (Shader == ShaderHandler.Shader.BUMP.ProgramHandle) {
            int TangentIndex = glGetAttribLocation(Shader, "tangent");     

            glBindBuffer(GL_ARRAY_BUFFER, vboTangentHandle);
            glVertexAttribPointer(TangentIndex, 4, GL_FLOAT, false, 0, 0L);
        }
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexHandle);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);

        glDrawArrays(GL_TRIANGLES, 0, NumberOfVertices);

        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        //glUseProgram(0);
        //glPopMatrix();
    }

    public void Destroy() {
        glDeleteBuffers(vboNormalHandle);
        glDeleteBuffers(vboVertexHandle);
    }

    public void setActiveTexture(Texture t) {
        Texture = t;
    }

    public void setNormalMap(int texture) {
        NormalHandle = texture;
    }

    private void drawFaceNormals() {
        glDisable(GL_TEXTURE_2D);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f,0.5f,0.5f);
        for (Face face : faces) {
            Vector3f FaceCenter = face.getCenter();
            Vector3f FaceNormal = face.getFaceNormal();

            
            GL11.glLineWidth(1f);
            GL11.glColor3f(0f, 0f, 1f);
            glBegin(GL_LINES);
                glVertex3f(FaceCenter.x, FaceCenter.y, FaceCenter.z);
                glVertex3f(FaceCenter.x+FaceNormal.x, FaceCenter.y+FaceNormal.y, FaceCenter.z+FaceNormal.z);
            glEnd();
            //glEnable(GL_TEXTURE_2D);
        }
        GL11.glPopMatrix();
    }

	public float getMaxWidth() {
		return max_x*2;
	}

	public float getMaxHeight() {
		return max_y*2;
	}

	public float getMaxDepth() {
		return max_z*2;
	}
}
