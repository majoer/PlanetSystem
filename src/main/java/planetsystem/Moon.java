package planetsystem;

import java.util.Random;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class Moon extends SpaceObject {

    /*
     * Constructor 1: Used when creating moons with fixed attributes
     */
    public Moon(String name, float radius, float distance, float revolutionSpeed, float rotationSpeed, int index, int totalMoons) {
        super(name, radius, distance, revolutionSpeed, rotationSpeed);
        handleInnerSystemAxis(index);
    }

    /*
     * Construcor 2: Used when creating moons with random attributes
     */
    public Moon(int index, float parentRadius, int totalMoons) {
        super();
        name = "moon" + index;
        Random r = new Random();
        int randomNumber = 10 + r.nextInt(30);

        handleAxis(index, totalMoons);
        
        if(index < totalMoons/ 3) {
            distance = parentRadius + 5f + index*2;
        } else {
            distance = parentRadius + index*2;
        }
        radius = parentRadius / (randomNumber);
        revolutionSpeed = randomNumber/10;
        rotationSpeed = 0;
    }

    @Override
    public void draw(GL gl, GLU glu, float rotation) {
        gl.glRotatef(rotation * revolutionSpeed * 100, axis.x, axis.y, axis.z);
        gl.glTranslatef(distance, 0, 0);
        super.draw(gl, glu, rotation);
        gl.glTranslatef(-distance, 0, 0);
    }
    
    /*
     * Private methods used for randomizing axis of rotation
     */
    private void handleAxis(int index, int totalMoons) {
        if (index < totalMoons / 8) {
            axis.x = 1;
            axis.y = 0;
            axis.z = 0;
        } else if (index < (totalMoons * 2) / 8) {
            axis.x = 0;
            axis.y = 1;
            axis.z = 0;
        } else if(index < (totalMoons * 3) / 8){
            axis.x = 0;
            axis.y = 0;
            axis.z = 1;
        } else if(index < (totalMoons * 4) / 8) {
            axis.x = 1;
            axis.y = 1;
            axis.z = 0;
        } else if(index < (totalMoons * 5) / 8) {
            axis.x = 1;
            axis.y = 0;
            axis.z = 1;
        } else if(index < (totalMoons * 6) / 8) {
            axis.x = 0;
            axis.y = 1;
            axis.z = 1;
        } else if(index < (totalMoons * 7) / 8) {
            axis.x = 1;
            axis.y = 1;
            axis.z = 1;
        }
    }

    private void handleInnerSystemAxis(int index) {
        if(index == 0) {
            axis.x = 0;
            axis.y = 1;
            axis.z = 0;
        } else if(index == 1) {
            axis.x = 0;
            axis.y = 0;
            axis.z = 1;
        }
    }
}
