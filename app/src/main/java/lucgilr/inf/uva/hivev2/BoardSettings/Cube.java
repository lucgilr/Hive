package lucgilr.inf.uva.hivev2.BoardSettings;

import lucgilr.inf.uva.hivev2.GameModel.Hexagon;

/**
 * @author Narek (https://github.com/starwheel)
 *
 * Original source for this file:
 * https://github.com/omplanet/android-hexagonal-grids/blob/master/HexagonalGrids/app/src/main/java/net/omplanet/hexagonalgrids/model/Cube.java
 * Cube using 3-vector for the coordinates (x, y, z)
 */
public class Cube {
    private int x;
    private int y;
    private int z;

    public Cube(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Hexagon toHex() {
        return new Hexagon(x, z);
    }

    public String toString() {
        return x + ":" + y + ":" + z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
