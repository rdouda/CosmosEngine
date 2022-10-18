package org.cosmos.core;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double ScrollX, ScrollY;
    private double xPos, yPos, LastY, LastX;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;
    private MouseListener() {
        this.ScrollX = 0.0;
        this.ScrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.LastX = 0.0;
        this.LastY = 0.0;
    }
    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().LastX = get().xPos;
        get().LastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().ScrollX = xOffset;
        get().ScrollY = yOffset;
    }

    public static void endFrame() {
        get().ScrollX = 0;
        get().ScrollY = 0;
        get().LastX = get().xPos;
        get().LastY = get().yPos;
    }

    public static float GetMouseX() {
        return (float)get().xPos;
    }

    public static float GetMouseY() {
        return (float)get().yPos;
    }

    public static float GetDx() {
        return (float)(get().LastX - get().xPos);
    }

    public static float GetDy() {
        return (float)(get().LastY - get().yPos);
    }

    public static float GetScrollX() {
        return (float)get().ScrollX;
    }

    public static float GetScrollY() {
        return (float)get().ScrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean MouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}