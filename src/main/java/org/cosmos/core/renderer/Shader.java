package org.cosmos.core.renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader {
    private int ProgramShaderId;
    private String VertexSource;
    private String FragmentSource;
    private String ShaderFilePath;
    public Shader(String ShaderFilePath){
        this.ShaderFilePath = ShaderFilePath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(ShaderFilePath)));
            String[] SplitString = source.split("(#type)( )+([A-Za-z]+)");
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String FirstPattern = source.substring(index, eol).trim();
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String SecondPattern = source.substring(index, eol).trim();
            if(FirstPattern.equals("vertex")){
                VertexSource = SplitString[1];
            } else if (FirstPattern.equals("fragment")) {
                FragmentSource = SplitString[1];
            } else {
                throw new IOException("Unexpected token in Shader file");
            }
            if(SecondPattern.equals("vertex")){
                VertexSource = SplitString[1];
            } else if (SecondPattern.equals("fragment")) {
                FragmentSource = SplitString[1];
            } else {
                throw new IOException("Unexpected token in Shader file");
            }
        } catch(IOException e){
            e.printStackTrace();
            assert false : "Error could not open shader file.";
        }
        System.out.println(VertexSource);
        System.out.println(FragmentSource);
    }
    public void Compile(){

    }
    public void Use(){

    }
    public void Detach(){

    }
}
