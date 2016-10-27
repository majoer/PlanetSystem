package planetsystem;

import com.sun.opengl.util.FPSAnimator;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JPanel;

public class Graphics extends JPanel implements GLEventListener, MouseMotionListener, MouseWheelListener, KeyListener {
    /*
     * Keybindings
     */

    private final int exit = KeyEvent.VK_ESCAPE;
    private final int forward = KeyEvent.VK_W;
    private final int backward = KeyEvent.VK_S;
    private final int leftward = KeyEvent.VK_A;
    private final int rightward = KeyEvent.VK_D;
    private final int freeCam = KeyEvent.VK_NUMPAD0;
    private final int mercury = KeyEvent.VK_NUMPAD1;
    private final int venus = KeyEvent.VK_NUMPAD2;
    private final int earth = KeyEvent.VK_NUMPAD3;
    private final int mars = KeyEvent.VK_NUMPAD4;
    private final int jupiter = KeyEvent.VK_NUMPAD5;
    private final int saturn = KeyEvent.VK_NUMPAD6;
    private final int uranus = KeyEvent.VK_NUMPAD7;
    private final int neptune = KeyEvent.VK_NUMPAD8;
    private final int pluto = KeyEvent.VK_NUMPAD9;
    /*
     * Main fields
     */
    private PlanetarySystem pSystem;
    private GLCanvas canvas;
    private GLU glu = new GLU();
    private FPSAnimator animator;
    private float rotation = 1.0f;
    /*
     * Camera fields
     */
    private Robot r;
    private boolean robotMovement;
    private int width, height, cx, cy;
    private Camera cam;
    private Vect eyePos = new Vect(0f, 0f, 0f);
    private long time = 0, lastTime = 0;
    private float viewDistance = 20000f;
    private float movementSpeed = 400.0f, mouseSensitivity = 0.15f;
    private float dx = 0.0f, dy = 0.0f, lastX, lastY, dt = 0.0f;
    /*
     * Light fields
     */
    private float[] lightPos = {0, 0, 0, 1};
    private float[] lightColorSpecular = {2.8f, 2.8f, 2.8f, 1f};
    private float[] ambient = {0.2f, 0.2f, 0.2f, 0.0f};

    public Graphics(int width, int height) {
        super();
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(0);
        }
        this.height = height;
        this.width = width;
        cx = width / 2;
        cy = height / 2;
        lastX = cx;
        lastY = cy;
        robotMovement = true;
        r.mouseMove(cx, cy);
        robotMovement = false;

        cam = new Camera(eyePos, viewDistance);
        cam.setInverted(false);
        cam.setMouseSensitivity(mouseSensitivity);

        GLCapabilities capabilities = new GLCapabilities();
        capabilities.setHardwareAccelerated(true);
        capabilities.setDoubleBuffered(true);

        capabilities.setRedBits(8);
        capabilities.setBlueBits(8);
        capabilities.setGreenBits(8);
        capabilities.setAlphaBits(8);

        canvas = new GLCanvas(capabilities);
        canvas.addGLEventListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addMouseWheelListener(this);
        canvas.addKeyListener(this);

        this.add(canvas);
        this.setSize(width, height);
        canvas.setSize(width, height);
        canvas.setVisible(true);
    }

    /*
     * GLEventListener
     */
    public void init(GLAutoDrawable drawable) {
        canvas.requestFocus();
        pSystem = new PlanetarySystem(lightPos, lightColorSpecular, ambient);
        cam.setSystem(pSystem);
        GL gl = drawable.getGL();

        initLight(gl);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        animator = new FPSAnimator(canvas, 60);
        animator.start();

        cam.setCamera(gl, glu, getWidth(), getHeight());
    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);


        time = java.lang.System.currentTimeMillis();
        dt = (time - lastTime) / 1000.0f;
        lastTime = time;

        cam.handleCamera(gl, rotation);


        drawGLScene(gl);
        drawable.swapBuffers();
        gl.glFlush();
        rotation += 0.001f;
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //DO nothing
        GL gl = drawable.getGL();
        gl.glViewport(0, 0, width, height);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        //Do nothing
    }

    /*
     * MouseMotionListener
     */
    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        if (!robotMovement) {
            dx = e.getX() - lastX;
            dy = e.getY() - lastY;
            cam.yaw(dx);
            cam.pitch(dy);
            lastX = e.getX();
            lastY = e.getY();
        }
        if (e.getX() < 20 || e.getX() > width - 20 || e.getY() < 20 || e.getY() > height - 20) {
            robotMovement = true;
            r.mouseMove(cx, cy);
            lastX = cx;
            lastY = cy;
        } else {
            robotMovement = false;
        }
    }

    /*
     * MouseWheelListener
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    /*
     * KeyListener
     */
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        String[] planetNames = pSystem.getPlanetNames();
        switch (e.getKeyCode()) {
            case forward:
                cam.walkForward(movementSpeed * dt);
                break;
            case leftward:
                cam.strafeLeft(movementSpeed * dt);
                break;
            case rightward:
                cam.strafeRight(movementSpeed * dt);
                break;
            case backward:
                cam.walkBackwards(movementSpeed * dt);
                break;
            case freeCam:
                cam.initFollow("none");
                break;
            case mercury:
                cam.initFollow(planetNames[1]);
                break;
            case venus:
                cam.initFollow(planetNames[2]);
                break;
            case earth:
                cam.initFollow(planetNames[3]);
                break;
            case mars:
                cam.initFollow(planetNames[4]);
                break;
            case jupiter:
                cam.initFollow(planetNames[5]);
                break;
            case saturn:
                cam.initFollow(planetNames[6]);
                break;
            case uranus:
                cam.initFollow(planetNames[7]);
                break;
            case neptune:
                cam.initFollow(planetNames[8]);
                break;
            case pluto:
                cam.initFollow(planetNames[9]);
                break;
            case exit:
                System.exit(0);
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    /*
     * Private methods
     */
    private void initLight(GL gl) {
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT1);

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lightColorSpecular, 0);
        gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_TRUE);
        gl.glEnable(GL.GL_FRONT_AND_BACK);
    }

    private void drawGLScene(GL gl) {
        gl.glTranslatef(0f, -500f, 0f);
        pSystem.drawSkybox(gl, glu, eyePos, cam.getPitch(), cam.getYaw(), cam.isFollowing());
        pSystem.drawSystem(gl, glu, rotation);
        gl.glFlush();
    }
}
