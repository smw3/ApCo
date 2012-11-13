package de.schaf.apco.util;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShaderHandler {

    public enum Shader {

        STANDARD("shader.vert","shader.frag");
        public int ProgramHandle;
        public int vertexShaderHandle;
        public int fragmentShaderHandle;

        Shader(String VertShaderLocation, String FragShaderLocation) {
            ProgramHandle = glCreateProgram();
            vertexShaderHandle = glCreateShader(GL_VERTEX_SHADER);
            fragmentShaderHandle = glCreateShader(GL_FRAGMENT_SHADER);

            StringBuilder vertexShaderSource = new StringBuilder();
            StringBuilder fragmentShaderSource = new StringBuilder();

            try {
                //BufferedReader reader = new BufferedReader(new FileReader(".\\res\\shader\\" + VertShaderLocation));
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                        new BufferedInputStream((new Object() {
                }).getClass().getClassLoader().getResourceAsStream("de\\schaf\\apco\\media\\" + VertShaderLocation))));
                String line;
                while ((line = reader.readLine()) != null) {
                    vertexShaderSource.append(line).append('\n');
                }
                reader.close();
            } catch (IOException e) {
                System.err.println("Failed to read vertex shader " + VertShaderLocation);
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                        new BufferedInputStream((new Object() {
                }).getClass().getClassLoader().getResourceAsStream("de\\schaf\\apco\\media\\" + FragShaderLocation))));
                String line;
                while ((line = reader.readLine()) != null) {
                    fragmentShaderSource.append(line).append('\n');
                }
                reader.close();
            } catch (IOException e) {
            	System.err.println("Failed to read fragment shader " + FragShaderLocation);
                e.printStackTrace();
            }

            glShaderSource(vertexShaderHandle, vertexShaderSource);
            glCompileShader(vertexShaderHandle);

            //if (glGetShader(vertexShaderHandle, GL_COMPILE_STATUS) == GL_FALSE) {
            
                System.out.println(System.currentTimeMillis()+" "+glGetShaderInfoLog(vertexShaderHandle,255));
            //    LoggerHandler.fatalError("Failed to compile vertex shader " + VertShaderLocation);
            //}

            glShaderSource(fragmentShaderHandle, fragmentShaderSource);
            glCompileShader(fragmentShaderHandle);

           // if (glGetShader(fragmentShaderHandle, GL_COMPILE_STATUS) == GL_FALSE) {
                System.out.println(System.currentTimeMillis()+" "+glGetShaderInfoLog(fragmentShaderHandle,255));
            //    LoggerHandler.fatalError("Failed to compile fragment shader " + FragShaderLocation);
            //}

            glAttachShader(ProgramHandle, vertexShaderHandle);
            glAttachShader(ProgramHandle, fragmentShaderHandle);
            glLinkProgram(ProgramHandle);
            glValidateProgram(ProgramHandle);
        }

        public void Destroy() {
            glDeleteProgram(ProgramHandle);
            glDeleteShader(vertexShaderHandle);
            glDeleteShader(fragmentShaderHandle);
        }

        public void use() {
            glUseProgram(ProgramHandle);
        }
    }

    public static void Destroy() {
        try {
        for (Shader s : Shader.values()) {
            s.Destroy();
        }
        } catch(Exception e) {}
    }
}
