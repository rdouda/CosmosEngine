package org.cosmos.core;

import org.cosmos.core.editor.EditorWindow;
import org.cosmos.core.game.LevelScene;
import org.cosmos.utils.Time;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;
    private static Window window = null;
    public static Scene CurrentScene;
    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Cosmos Engine";
    }
    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }
    public void Run() {
        init();
        loop();
    }

    public static void ChangeScene(int scene)
    {
        switch (scene)
        {
            case 0:
                CurrentScene = new EditorWindow();
                CurrentScene.Init();
                break;
            case 1:
                CurrentScene = new LevelScene();
                CurrentScene.Init();
                break;
            default:
                assert false:"Unknown Scene " + scene;
                break;
        }
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        Window.ChangeScene(0);
    }
    public void loop() {
        float BeginTime = Time.getTime();
        float EndTime;
        float DeltaTime = -1.0f;
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            if(DeltaTime >= 0)
                CurrentScene.Update(DeltaTime);
            glfwSwapBuffers(glfwWindow);
            EndTime = Time.getTime();
            DeltaTime = EndTime - BeginTime;
            BeginTime = EndTime;
        }
    }
}
