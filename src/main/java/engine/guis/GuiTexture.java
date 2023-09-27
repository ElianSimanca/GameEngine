package engine.guis;

import org.lwjglx.util.vector.Vector2f;

public class GuiTexture {
    private int Texture;
    private Vector2f position;
    private Vector2f scale;

    public GuiTexture(int texture, Vector2f position, Vector2f scale) {
        Texture = texture;
        this.position = position;
        this.scale = scale;
    }

    public int getTexture() {
        return Texture;
    }

    public void setTexture(int texture) {
        Texture = texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }
}
