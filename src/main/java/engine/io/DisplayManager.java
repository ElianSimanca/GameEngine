package engine.io;

import org.lwjglx.Sys;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

public class DisplayManager {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "Mi Aplicación LWJGL 3";


    private long window;

    public void createDisplay() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Error al inicializar GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Error al crear la ventana GLFW");
        }

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        GLFW.glfwShowWindow(window);
    }

    public void updateDisplay() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void closeDisplay() {
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    private long lastFrameTime = System.nanoTime();

    public long getWindow() {
        return window;
    }
}



