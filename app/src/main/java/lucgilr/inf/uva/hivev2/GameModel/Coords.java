package lucgilr.inf.uva.hivev2.GameModel;

/**
 * Representation of a token's position.
 * It is given by three coordinates: x, y, and z.
 * @author Lucía gil Román
 */
public class Coords {

    private int x;
    private int y;
    private int z;

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public Coords(int x, int y, int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *
     * @return
     */
    public int getZ() {
        return z;
    }

    /**
     *
     * @param z
     */
    public void setZ(int z) {
        this.z = z;
    }

    /**
     *
     * @return
     */
    public String printCoords(){
        return " X: "+this.getX()+" Y: "+this.getY()+" Z: "+this.getZ();
    }

}
