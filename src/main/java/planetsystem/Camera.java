package planetsystem;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class Camera {

    private PlanetarySystem system;
    private int planetInFocus = -1;
    private float revolutionSpeed = 0.0f, distance = 0.0f, radius = 0.0f;
    private Vect camPos;
    private float yaw = 0.0f, pitch = 0.0f, viewDistance;
    private boolean inverted, following;
    private float mouseSensitivity = 1.0f;

    public Camera(Vect camPos, float viewDistance) {
        this.camPos = camPos;
        this.viewDistance = viewDistance;
    }

    /*
     * Movement methods
     */
    public void yaw(float amount) {
        yaw += amount * mouseSensitivity;
    }

    public void pitch(float amount) {
        if (!inverted) {
            pitch += amount * mouseSensitivity;
        } else {
            pitch -= amount * mouseSensitivity;
        }
    }

    public void walkForward(float distance) {
        if (!following) {
            camPos.x -= distance * (float) Math.cos(Math.toRadians(yaw));
            camPos.z -= distance * (float) Math.sin(Math.toRadians(yaw));
            camPos.y += distance * (float) Math.tan(Math.toRadians(pitch));
        }
    }

    public void walkBackwards(float distance) {
        if (!following) {
            camPos.x += distance * (float) Math.cos(Math.toRadians(yaw));
            camPos.z += distance * (float) Math.sin(Math.toRadians(yaw));
            camPos.y -= distance * (float) Math.tan(Math.toRadians(pitch));
        }
    }

    public void strafeLeft(float distance) {
        if (!following) {
            camPos.x += distance * (float) Math.cos(Math.toRadians(yaw + 90));
            camPos.z += distance * (float) Math.sin(Math.toRadians(yaw + 90));
        }
    }

    public void strafeRight(float distance) {
        if (!following) {
            camPos.x += distance * (float) Math.cos(Math.toRadians(yaw - 90));
            camPos.z += distance * (float) Math.sin(Math.toRadians(yaw - 90));
        }
    }

    /*
     * Special
     *  handleCamera - Decides whether to run first person, or third person cam.
     *  initFollow() - Used when preparing to follow a planet.
     */
    public void handleCamera(GL gl, float rotation) {
        gl.glTranslatef(0f, -500f, 0f);
        gl.glLoadIdentity();
        if (!following) {
            lookThrough(gl);
        } else {
            follow(gl, rotation);
        }
    }

    public void initFollow(String planetName) {
        String[] planetNames = system.getPlanetNames();
        for (int i = 0; i < planetNames.length; i++) {
            if (planetName.equals(planetNames[i])) {
                revolutionSpeed = system.getRevolutionSpeed(i);
                distance = system.getDistance(i);
                radius = system.getRadius(i);
                following = true;
                return;
            }
        }
        following = false;
    }

    /*
     * Getters and setters
     */
    public void setCamera(GL gl, GLU glu, int width, int height) {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();


        float widthHeightRatio = (float) width / (float) height;
        glu.gluPerspective(45, widthHeightRatio, .3, viewDistance);
        glu.gluLookAt(camPos.x, camPos.y, camPos.z, 500, 0, 0, 0, 1, 0);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void setMouseSensitivity(float s) {
        this.mouseSensitivity = s;
    }

    public void setInverted(boolean b) {
        this.inverted = b;
    }

    public float getSense() {
        return mouseSensitivity;
    }

    public Vect getPos() {
        return camPos;
    }

    public int getFollowIndex() {
        return planetInFocus;
    }

    public void setSystem(PlanetarySystem system) {
        this.system = system;
    }

    public boolean isFollowing() {
        return following;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    /*
     * Private methods
     *  lookThough() - First person cam
     *  follow() - Third person cam
     */
    private void lookThrough(GL gl) {
        gl.glRotatef(pitch, 0.0f, 0.0f, 1.0f);
        gl.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(camPos.x, camPos.y, camPos.z);
    }

    private void follow(GL gl, float rotation) {

        gl.glRotatef(180, 0f, 1f, 0f);
        gl.glTranslatef(-distance - radius * 15f, 500f - radius * 4f, 0);
        gl.glRotatef(-revolutionSpeed * rotation, 0, 1, 0);
    }
}
