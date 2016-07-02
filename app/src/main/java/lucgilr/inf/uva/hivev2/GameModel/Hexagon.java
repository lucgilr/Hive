package lucgilr.inf.uva.hivev2.GameModel;

/**
 * @author Narek (https://github.com/starwheel)
 * @author Lucía Gil Román - Adding d for a 3 dimension coordinate system.
 *
 * The original file can be found in:
 * https://github.com/omplanet/android-hexagonal-grids/blob/master/HexagonalGrids/app/src/main/java/net/omplanet/hexagonalgrids/model/Hex.java
 * Non-cube hex coordinates (q, r)
 *
 * An Hexagon is identified by 3 coordinates: Column (q), Row (r) and a level (l)
 *
 */
public class Hexagon {

    private final int q; //column
    private final int r; //row
    private final int l; //level

    /**
     * The initial values of the coordinates are (0,0,0)
     */
    public Hexagon(){
        this.q=-100;
        this.r=-100;
        this.l=-100;
    }

    /**
     * Constructor for a 2 dimension coordinate.
     * @param q //Column
     * @param r //Row
     */
    public Hexagon(int q, int r) {
        this.q = q;
        this.r = r;
        this.l = 0;
    }

    /**
     * Constructor for a 3 dimension coordinate,
     * @param q //Column
     * @param r //Row
     * @param l //level
     */
    public Hexagon(int q, int r, int l){
        this.q=q;
        this.r=r;
        this.l=l;
    }

    public String toString() {
        return q + ":" + r + ":" + l;
    }

    public String toString2D(){
        return q + ":" + r;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }

    public int getL() {
        return l;
    }

}
