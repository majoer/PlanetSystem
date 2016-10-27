package planetsystem;

public class Vect {
    /*
     * Vector class
     */
    public static Vect ZERO = new Vect(0f, 0f, 0f);
    
    public float x, y, z;
    
    public Vect(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vect() {
        
    }
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
    
    
}
