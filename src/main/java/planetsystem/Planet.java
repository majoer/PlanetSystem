package planetsystem;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class Planet extends SpaceObject {

    private Moon[] moons;
    private Texture moonTexture;
    private int moonIndex, totalMoons;
    private Ring ring;

    public Planet(String name, float radius, float distance, float revolutionSpeed, float rotationSpeed, int totalMoons) {
        super(name, radius, distance, revolutionSpeed, rotationSpeed);
        this.moonIndex = 0;
        this.ring = null;
        this.revolutionSpeed = revolutionSpeed;
        this.rotationSpeed = rotationSpeed;
        this.totalMoons = totalMoons;

        moons = new Moon[totalMoons];
    }

    /*
     * Sets an optional ring around the planet.
     */
    public void setRing(String ringName, float innerRadius, float outerRadius) {
        ring = new Ring(ringName, innerRadius, outerRadius);
    }

    /*
     * addMoon
     * 1 - Used for adding a moon with fixed attributes
     * 2 - Used for adding a moon with random attributes
     */
    public void addMoon(String moonName, float radius, float distance, float revolutionSpeed, float rotationSpeed) {
        if (moonIndex == 0) {
            moonTexture = super.load(moonName, ".jpg");
        }
        Moon m = new Moon(moonName, radius, distance, revolutionSpeed, rotationSpeed, moonIndex, totalMoons);
        m.setT(moonTexture);
        moons[moonIndex++] = m;
    }

    public void addMoon(String moonName, float parentRadius, int number) {
        if (moonIndex == 0) {
                moonTexture = super.load(moonName, ".jpg");
            }
        for (int i = 0; i < number; i++) {
            Moon m = new Moon(moonIndex, parentRadius, totalMoons);
            m.setT(moonTexture);
            moons[moonIndex++] = m;
        }
    }

    @Override
    public void draw(GL gl, GLU glu, float rotation) {
        gl.glPushMatrix();

        gl.glRotatef(rotation * revolutionSpeed, 0, 1, 0);
        gl.glTranslatef(distance, 0f, 0f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glRotatef(45, 0, 1, 0);
        gl.glRotatef(rotation * rotationSpeed, 0, .2f, 1);
        super.draw(gl, glu, rotation);

        /*
         * If ring exists, draw ring
         */
        if (ring != null) {
            ring.draw(gl, glu, rotation);
        }

        /*
         * If the planet has moons, draw them. If there are too many moons, draw without loading any texture.
         */
        for (int i = 0; i < moons.length; i++) {
            if (moons[i] != null) {
                    moons[i].draw(gl, glu, rotation);
            }
        }
        gl.glPopMatrix();
    }
}
