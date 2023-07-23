package org.cosmos.core.renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int ProgramShaderId;
    private String VertexSource;
    private String FragmentSource;
    private String ShaderFilePath;
    public Shader(String ShaderFilePath) {
        this.ShaderFilePath = ShaderFilePath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(ShaderFilePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Find the first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // Find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                VertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                FragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                VertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                FragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }
        } catch(IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + ShaderFilePath + "'";
        }
    }
    public void Compile(){
        //compile
        int vertexID;
        int fragmentID;
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, VertexSource);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+ ShaderFilePath +"'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "ERROR: '"+ ShaderFilePath +"'\n\tVertex shader compilation failed.";
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, FragmentSource);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+ ShaderFilePath +"'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "ERROR: '"+ ShaderFilePath +"'\n\tFragment shader compilation failed.";
        }
        //link
        ProgramShaderId = glCreateProgram();
        glAttachShader(ProgramShaderId, vertexID);
        glAttachShader(ProgramShaderId, fragmentID);
        glLinkProgram(ProgramShaderId);

        success = glGetProgrami(ProgramShaderId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(ProgramShaderId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+ ShaderFilePath +".glsl'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(ProgramShaderId, len));
            assert false : "ERROR: '"+ ShaderFilePath +".glsl'\n\tLinking of shaders failed.";
        }
    }
    public void Use(){
        glUseProgram(ProgramShaderId);
    }
    public void Detach(){
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(ProgramShaderId, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
    }
}
