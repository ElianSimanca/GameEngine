package engine.entities;

import engine.io.Input;
import engine.main.MainGameLoop;
import engine.models.TexturedModel;
import engine.terrain.Terrain;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector3f;

public class Player extends Entity{
    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    public static final float GRAVITY = -50;
    public static final float JUMP_POWER = 30;
    private static final float TERRAIN_HEIGHT = 0;
    private float currenSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;
    private boolean isInAir = false;

    private Input input;
    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Input input) {
        super(model, position, rotX, rotY, rotZ, scale);
        this.input = input;
    }
    public void move(Terrain terrain){
        checkInputs();
        super.increasedRotation(0,currentTurnSpeed * (float) MainGameLoop.getFrameTimeSeconds(), 0);
        float distance = currenSpeed * (float) MainGameLoop.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasedPosition(dx,0,dz);
        upwardsSpeed += GRAVITY * MainGameLoop.getFrameTimeSeconds();
        super.increasedPosition(0,upwardsSpeed * (float) MainGameLoop.getFrameTimeSeconds(),0);
        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x,super.getPosition().z);

        if(super.getPosition().y < terrainHeight) {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }
    }
    private void jump(){
        if(!isInAir){
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }

    }
    private void checkInputs() {
        if(input.isKeyDown(GLFW.GLFW_KEY_W)) {
            this.currenSpeed = RUN_SPEED;
        } else if (input.isKeyDown(GLFW.GLFW_KEY_S)) {
            this.currenSpeed = -RUN_SPEED;
        }else {
            this.currenSpeed = 0;
        }

        if(input.isKeyDown(GLFW.GLFW_KEY_D)) {
            this.currentTurnSpeed = -TURN_SPEED;
        } else if (input.isKeyDown(GLFW.GLFW_KEY_A)) {
            this.currentTurnSpeed = TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if(input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            jump();
        }
    }
}
