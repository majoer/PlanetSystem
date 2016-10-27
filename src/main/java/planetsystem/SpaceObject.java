package planetsystem;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.io.InputStream;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public class SpaceObject {
/*
 * Superclass of Planet, Moon and Ring, containing the general draw() method.
 */
    protected Texture t;
    protected String name;
    protected int slices, stacks;
    protected float radius, distance, revolutionSpeed, rotationSpeed;
    protected Vect axis = new Vect();

    /*
     * Constructor 1 - 
     */
    public SpaceObject(String name, float radius, float distance, float revolutionSpeed, float rotationSpeed) {
        this.name = name;
        this.radius = radius;
        this.distance = distance;
        this.revolutionSpeed = revolutionSpeed;
        this.rotationSpeed = rotationSpeed;
        if(!(this instanceof Moon)) {
            t = load(name, ".jpg");
            t.enable();
        }
        slices = stacks = 16;
    }
    
    /*
     * Constructor 2 - 
     */
    public SpaceObject(String name, float radius) {
        this.name = name;
        this.radius = radius;
        t = load(name, ".jpg");
        t.enable();
        slices = stacks = 16;
    }

    /*
     * Constructor 3 - 
     */
    public SpaceObject() {
        slices = stacks = 16;
    }
    
    /*
     * public methods
     * draw() - draws a sphere with Texture t.
     * loadStatic() - Method used from PlanetarySystem when loading textures for the skybox
     * load() - Method loading a texture with given name and type
     */
    public void setMaterialProperties(GL gl, float[] mat_emission) {
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, mat_emission, 0);
    }

    public void setT(Texture t) {
        this.t = t;
    }

    public void draw(GL gl, GLU glu, float rotation) {
        t.bind();
        GLUquadric texture = glu.gluNewQuadric();
        glu.gluQuadricTexture(texture, true);
        glu.gluQuadricDrawStyle(texture, GLU.GLU_FILL);
        glu.gluQuadricNormals(texture, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(texture, GLU.GLU_OUTSIDE);
        glu.gluSphere(texture, radius, slices, stacks);
        glu.gluDeleteQuadric(texture);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    }

    public static Texture loadStatic(PlanetarySystem c, String name, String type) {
        try {
            InputStream s = c.getClass().getResourceAsStream("/textures/" + name + type);
            TextureData data = TextureIO.newTextureData(s, false, type);
            return TextureIO.newTexture(data);
        } catch (IOException e) {
            e.printStackTrace();
            java.lang.System.exit(0);
        }
        return null;
    }
    
    protected Texture load(String name, String type) {
        try {
            InputStream s = getClass().getResourceAsStream("/textures/" + name + type);
            TextureData data = TextureIO.newTextureData(s, false, type);
            return TextureIO.newTexture(data);
        } catch (IOException e) {
            e.printStackTrace();
            java.lang.System.exit(0);
        }
        return null;
    }
}
