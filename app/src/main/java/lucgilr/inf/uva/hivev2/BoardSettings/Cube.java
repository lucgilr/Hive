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
    private final int x;
    private final int y;
    private final int z;

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
}
