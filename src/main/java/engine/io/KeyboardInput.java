package engine.io;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardInput {

    // Esta variable se utiliza para almacenar el estado de las teclas
    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    // Configura un callback para manejar eventos de teclado
    public static void init(long window) {
        GLFW.glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = action != GLFW.GLFW_RELEASE;
            }
        });
    }

    // Verifica si una tecla está presionada
    public static boolean isKeyDown(int key) {
        if (key >= 0 && key < GLFW.GLFW_KEY_LAST) {
            return keys[key];
        }
        return false;
    }
}

