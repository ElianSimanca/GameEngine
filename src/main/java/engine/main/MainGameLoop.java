package engine.main;

import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.entities.Player;
import engine.graphics.Loader;
import engine.graphics.MasterRenderer;
import engine.graphics.OBJLoader;
import engine.guis.GuiRenderer;
import engine.guis.GuiTexture;
import engine.io.DisplayManager;
import engine.io.Input;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.terrain.Terrain;
import engine.textures.ModelTexture;
import engine.textures.TerrainTexture;
import engine.textures.TerrainTexturePack;
import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {
    private static final int targetFPS = 60;
    private static final int targetUPS = 120;
    private static final long targetFrameTime = 1000000000 / targetFPS;
    private static final long targetUpdateTime = 1000000000 / targetUPS;
    private static double delta;


    public static void main(String[] args) {
        DisplayManager displayManager = new DisplayManager();
        displayManager.createDisplay();
        GLFW.glfwSetInputMode(displayManager.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        long lastUpdateTime = System.nanoTime();
        long lastRenderTime = System.nanoTime();
        long lastFPSTime = 0;
        int frames = 0;
        int updates = 0;

        Loader loader = new Loader();

        TerrainTexture backGroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack  texturePack = new TerrainTexturePack(backGroundTexture,rTexture,
                gTexture,bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        RawModel model = OBJLoader.loadObjModel("tree", loader);

        TexturedModel tree = new TexturedModel(model, new ModelTexture(
                loader.loadTexture("tree")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
                new ModelTexture(loader.loadTexture("grassTexture")));
        TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),
                new ModelTexture(loader.loadTexture("flower")));
        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fernAtlas"));
        fernTextureAtlas.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern",loader), fernTextureAtlas);
        TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree",loader),
                new ModelTexture(loader.loadTexture("lowPolyTree")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        flower.getTexture().setHasTransparency(true);
        flower.getTexture().setUseFakeLighting(true);
        fern.getTexture().setHasTransparency(true);

        Terrain terrain = new Terrain(0,-1,loader,texturePack,blendMap, "heightMap");

        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);
        for(int i = 0;i < 400; i++) {
            if (i % 20 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x,z);

                entities.add(new Entity(fern, random.nextInt(4),new Vector3f(x,y,z),0, random.nextFloat() * 360, 0,0.9f));

            }
            if (i % 5 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x,z);

                entities.add(new Entity(bobble, new Vector3f(x,y,z),0, random.nextFloat() * 360, 0,
                        random.nextFloat() * 0.1f + 0.6f));
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x,z);
                entities.add(new Entity(tree, new Vector3f(x,y,z),0,0,0,
                        random.nextFloat() * 1 + 4));
            }
        }


        Light light = new Light(new Vector3f(20000,40000,20000), new Vector3f(1,1,1));



        Input input = new Input();
        GLFW.glfwSetKeyCallback(displayManager.getWindow(), input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(displayManager.getWindow(), input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(displayManager.getWindow(), input.getMouseButtonsCallback());



        MasterRenderer renderer = new MasterRenderer();

        RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
        TexturedModel stanfordBunny  = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("playerTexture")));

        Player player = new Player(stanfordBunny, new Vector3f(100,0,-50),0,180,0,0.6f, input);
        Camera camera = new Camera(player,input);

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"),new Vector2f(0.5f,0.5f), new Vector2f(0.25f,0.25f) );
        guis.add(gui);
        GuiTexture gui2 = new GuiTexture(loader.loadTexture("thinmatrix"),new Vector2f(0.30f,0.74f), new Vector2f(0.4f,0.4f) );
        guis.add(gui2);

        GuiRenderer guiRenderer = new GuiRenderer(loader);


        while (!displayManager.shouldClose()) {
            long now = System.nanoTime();
            long elapsedUpdateTime = now - lastUpdateTime;
            long elapsedRenderTime = now - lastRenderTime;
            delta = (double)elapsedUpdateTime / 1000000000.0; // Convierte nanosegundos a segundos


            if (elapsedUpdateTime >= targetUpdateTime) {
                //Lógica de actualización del juego

                GLFW.glfwPollEvents();
                camera.move();
                player.move(terrain);

                lastUpdateTime = now - (elapsedUpdateTime % targetUpdateTime);
                updates++;
            }

            if (elapsedRenderTime >= targetFrameTime) {

                renderer.processTerrain(terrain);
                renderer.processEntity(player);
                for(Entity entity:entities){
                    renderer.processEntity(entity);
                }
                renderer.render(light, camera);
                guiRenderer.render(guis);
                displayManager.updateDisplay();

                lastRenderTime = now - (elapsedRenderTime % targetFrameTime);
                frames++;
            }

            if (now - lastFPSTime >= 1000000000) {
                System.out.println("FPS: " + frames + ", UPS: " + updates);
                frames = 0;
                updates = 0;
                lastFPSTime = now;
            }
        }
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUP();
        displayManager.closeDisplay();
    }


    public static double getFrameTimeSeconds() {
        return delta;
    }
}

