package engine.entities;


import engine.io.Input;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector3f;

public class Camera {
    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch = 20, yaw, roll;
    private double lastDeltaYForPitch;
    private Input input;
    private Player player;private double mouseDeltaX;



    public Camera(Player player,Input input) {
        this.input = input;
        this.player = player;
    }
    public void move(){
        input.calculateMouseDeltaX();
        input.calculateMouseDeltaY();
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
        calculateCameraPosition(horizontalDistance,verticalDistance);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
    private void calculateCameraPosition(float horizontalDistance,float verticalDistance ) {
        // Calcular la posición relativa de la cámara detrás del jugador
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }
    private float calculateVerticalDistance() {

        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom() {
        float zoomLevel = (float) (input.getDeltaWheel() * 0.1f);
        distanceFromPlayer -= zoomLevel;
    }
    private void calculatePitch() {
        if (input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            System.out.println("click derecho");
            float pitchChange = (float) (input.getDeltaY() * 0.1f);
            pitch -= pitchChange;
        }
    }
    private void calculateAngleAroundPlayer() {

        if (input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            System.out.println("click izquierdo");
            float angleChange = (float) (input.getDeltaX() * 0.3f);
            angleAroundPlayer -= angleChange;
        }
    }
}
