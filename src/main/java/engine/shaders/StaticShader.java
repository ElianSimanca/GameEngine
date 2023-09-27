package engine.shaders;


import engine.entities.Camera;
import engine.entities.Light;
import engine.toolbox.Maths;
import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector2f;
import org.lwjglx.util.vector.Vector3f;

public class StaticShader extends ShaderProgram{
    private static final String VERTEX_FILE = "src/main/java/engine/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/main/java/engine/shaders/fragmentShader.txt";
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColour;
    private int location_numberOfRows;
    private int location_offset;


    public StaticShader() {
        super(VERTEX_FILE,FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix= super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColour = super.getUniformLocation("lightColour");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColour = super.getUniformLocation("skyColour");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_offset = super.getUniformLocation("offset");
    }
    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(location_shineDamper,damper);
        super.loadFloat(location_reflectivity,reflectivity);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoordinates");
        super.bindAttribute(2, "normal");
    }
    public void loadNumberOfRows(int numberOfRows){
        super.loadFloat(location_numberOfRows, numberOfRows);
    }
    public void loadOffset(float x,float y){
        super.load2DVector(location_offset, new Vector2f(x,y));
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }
    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix,viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(location_projectionMatrix, projection);
    }
    public void loadLight(Light light) {
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColour, light.getColor());
    }

    public void loadFakeLightingVariable(boolean useFake) {
        super.loadBoolean(location_useFakeLighting, useFake);
    }

    public void loadSkyColour(float r,float b, float g) {
        super.loadVector(location_skyColour, new Vector3f(r,g,b));
    }


}
