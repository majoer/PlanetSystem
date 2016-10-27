package planetsystem;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public class Ring extends SpaceObject {

    private float outerRadius;

    public Ring(String name, float radius, float outerRadius) {
        super(name, radius);
        this.outerRadius = outerRadius;
    }

    @Override
    public void draw(GL gl, GLU glu, float rotation) {
        t.bind();
        GLUquadric disk = glu.gluNewQuadric();
        glu.gluQuadricTexture(disk, true);
        glu.gluQuadricDrawStyle(disk, GLU.GLU_FILL);
        glu.gluQuadricNormals(disk, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(disk, GLU.GLU_OUTSIDE);
        glu.gluDisk(disk, radius, outerRadius, slices, 2);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    }
}
