package planetsystem;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class PlanetarySystem {

    /*
     * Main fields
     */
    private Planet[] planets;
    private String[] planetNames = {"sun", "mercury", "venus", "earth", "mars", "jupiter", "saturn", "uranus", "neptune", "pluto"};
    private String[] planetTextureNames = {"sunmap", "mercurymap", "venusmap", "earthmap1k", "mars_1k_color", "jupiter2_1k", "saturnmap", "uranusmap", "neptunemap", "plutomap1k"};
    private String[] ringTextureNames = {"saturnringcolor", "uranusringcolour"};
    /*
     * Planet data
     */
    private float[] planetRadii = {80f, 2.440f, 6.051f, 6.378f, 3.397f, 71.492f, 60.268f, 25.559f, 24.764f, 1.160f};
    private float[] planetDist = {0f, 0.39f, 0.723f, 1f, 1.524f, 5.203f, 9.539f, 19.18f, 30.06f, 39.53f};
    private float[] planetRevolution = {0f, 107132f, 78364f, 66641f, 53980f, 29216f, 21565f, 15234f, 12147f, 10604f};
    private float[] planetRotation = {0f, 6.73f, 4.05f, 1040f, 538f, 28325f, 22892f, 9193f, 6039f, 76.56f};
    private int[] totalMoons = {0, 0, 0, 1, 2, 67, 62, 27, 13, 4};
    private float moonRadius = 1.738f, moonDist = 15f, moonRevolutionSpeed = 2287f, moonRotationSpeed = 0f;
    private float planetDistFactor = 230f, planetRevFactor = 1f / 100f, planetRotFactor = 1f / 10f;
    /*
     * Light handles
     */
    private float[] mat_emission, lightPos, lightColorSpecular, ambient;
    /*
     * Skybox textures
     */
    private Texture front, left, back, right, top, bottom;

    /*
     * Constructor
     */
    public PlanetarySystem(float[] lightPos, float[] lightColorSpecular, float[] ambient) {
        this.lightPos = lightPos;
        this.lightColorSpecular = lightColorSpecular;
        this.ambient = ambient;

        planets = new Planet[10];
        initPlanets(planets);
        loadSkybox();
    }

    /*
     * Public methods
     * drawSystem - sets up the light and calls the draw() method for all the planets.
     * drawSkybox - draws the skybox by temporarily turning off GL_DEPTH_TEST and binding the textures
     */
    public void drawSystem(GL gl, GLU glu, float rotation) {
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, ambient, 1);
        setMaterialProperties(gl);

        /*
         * Sun
         */
        handleSunLight(gl);
        planets[0].draw(gl, glu, rotation);

        /*
         * Other planets
         */
        for (int i = 1; i < planets.length; i++) {
            handlePlanetLight(gl);
            planets[i].draw(gl, glu, rotation);
        }

        drawPaths(gl);
    }

    public void drawSkybox(GL gl, GLU glu, Vect cam, float pitch, float yaw, boolean isFollowing) {
        gl.glPushMatrix();
        gl.glLoadIdentity();
        if (!isFollowing) {
            gl.glRotatef(pitch, 0.0f, 0.0f, 1.0f);
            gl.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        }

        gl.glPushAttrib(GL.GL_ENABLE_BIT);
        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glDisable(GL.GL_LIGHTING);
        gl.glDisable(GL.GL_BLEND);

        gl.glColor4f(1, 1, 1, 1);

//        Left
        left.bind();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(.5f, -.5f, -.5f);

        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-.5f, -.5f, -.5f);

        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-.5f, .5f, -.5f);

        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(.5f, .5f, -.5f);

        gl.glEnd();

        //Front
        front.bind();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(.5f, .5f, -.5f);

        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(.5f, .5f, .5f);

        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(.5f, -.5f, .5f);

        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(.5f, -.5f, -.5f);

        gl.glEnd();

        //Right
        right.bind();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(-.5f, -.5f, .5f);

        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(.5f, -.5f, .5f);

        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(.5f, .5f, .5f);

        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(-.5f, .5f, .5f);

        gl.glEnd();

        //Back
        back.bind();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(-.5f, -.5f, -.5f);

        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-.5f, -.5f, .5f);

        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(-.5f, .5f, .5f);

        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(-.5f, .5f, -.5f);

        gl.glEnd();

        //Top
        top.bind();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(-.5f, .5f, -.5f);

        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-.5f, .5f, .5f);

        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(.5f, .5f, .5f);

        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(.5f, .5f, -.5f);

        gl.glEnd();

        //Bottom
        bottom.bind();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(1, 0);
        gl.glVertex3f(-.5f, -.5f, -.5f);

        gl.glTexCoord2f(0, 0);
        gl.glVertex3f(-.5f, -.5f, .5f);

        gl.glTexCoord2f(0, 1);
        gl.glVertex3f(.5f, -.5f, .5f);

        gl.glTexCoord2f(1, 1);
        gl.glVertex3f(.5f, -.5f, -.5f);

        gl.glEnd();

        gl.glPopAttrib();
        gl.glPopMatrix();
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

    }

    
    /*
     * Getters & setters
     */
    public String[] getPlanetNames() {
        return planetNames;
    }

    public float getRevolutionSpeed(int planet) {
        return planetRevolution[planet] * planetRevFactor;
    }

    public float getDistance(int planet) {
        return planetDist[planet] * planetDistFactor;
    }

    public float getRadius(int planet) {
        return planetRadii[planet];
    }


    /*
     * Private methods
     * drawPaths() - Drawing the circle paths of the planets
     * initPlanets() - Setting up the attributes of the planets, their moons and eventual rings.
     * loadSkybox() - loads the textures used with the skybox
     */
    private void drawPaths(GL gl) {
        gl.glColor4f(1, 1, 1, 1);
        for (int i = 1; i < planetDist.length; i++) {
            gl.glBegin(GL.GL_LINE_STRIP);
            for (float angle = 0.0f; angle < (2.0f * (float) Math.PI); angle += 0.001f) {
                float x = planetDist[i] * planetDistFactor * (float) Math.sin(angle);
                float z = planetDist[i] * planetDistFactor * (float) Math.cos(angle);

                gl.glVertex3f(x, 0, z);
            }
            gl.glEnd();
        }
    }

    private void initPlanets(Planet[] planets) {
        for (int i = 0; i < planets.length; i++) {
            planets[i] = new Planet(
                    planetTextureNames[i], planetRadii[i],
                    planetDist[i] * planetDistFactor, planetRevolution[i] * planetRevFactor,
                    planetRotation[i] * planetRotFactor, totalMoons[i]);
        }
        //Earth's moon
        planets[3].addMoon("moon-4k", moonRadius, moonDist, moonRevolutionSpeed * planetRevFactor, moonRotationSpeed * planetRotFactor);

        //Mars' 2 moons
        planets[4].addMoon("moon-4k", moonRadius, moonDist, moonRevolutionSpeed * planetRevFactor, moonRotationSpeed * planetRotFactor);
        planets[4].addMoon("moon-4k", moonRadius, moonDist + 6, moonRevolutionSpeed * planetRevFactor, moonRotationSpeed * planetRotFactor);

        //Jupiters' 67 moons
        planets[5].addMoon("moon-4k", planetRadii[5], 67);
        //Saturn's 62 moons
        planets[6].addMoon("moon-4k", planetRadii[6], 62);
        //Saturn's ring
        planets[6].setRing(ringTextureNames[0], planetRadii[6] + 10f, planetRadii[6] + 30f);
        //Uranus' 27 moons
        planets[7].addMoon("moon-4k", planetRadii[7], 27);
        //Uranus' ring
        planets[7].setRing(ringTextureNames[1], planetRadii[7] + 10f, planetRadii[7] + 20f);
        //Neptune's 13 moons
        planets[8].addMoon("moon-4k", planetRadii[8], 13);
        //Pluto's 4 moons
        planets[9].addMoon("moon-4k", planetRadii[9], 4);
    }

    private void loadSkybox() {
        front = SpaceObject.loadStatic(this, "starfield_front", ".jpg");
        front.enable();
        left = SpaceObject.loadStatic(this, "starfield_left", ".jpg");
        left.enable();
        back = SpaceObject.loadStatic(this, "starfield_back", ".jpg");
        back.enable();
        right = SpaceObject.loadStatic(this, "starfield_right", ".jpg");
        right.enable();
        top = SpaceObject.loadStatic(this, "starfield_top", ".jpg");
        top.enable();
        bottom = SpaceObject.loadStatic(this, "starfield_top", ".jpg");
        bottom.enable();
    }

    private void setMaterialProperties(GL gl) {
        float[] rgba = {1f, 1f, 1f};
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, rgba, 0);
        gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, 1f);
    }

    private void handleSunLight(GL gl) {
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lightColorSpecular, 0);

        mat_emission = new float[]{1.3f, 1.2f, 1.2f, 1.0f};
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, mat_emission, 0);
    }

    private void handlePlanetLight(GL gl) {
        mat_emission = new float[]{0f, 0f, 0f, 1.0f};
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, mat_emission, 0);
    }
}
