package lucgilr.inf.uva.hivev2.UI;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

import lucgilr.inf.uva.hivev2.GameModel.Cube;
import lucgilr.inf.uva.hivev2.GameModel.Hexagon;
import lucgilr.inf.uva.hivev2.GameModel.Piece;

/**
 * @author Narek (https://github.com/starwheel)
 *
 * Original source for this file:
 * https://github.com/omplanet/android-hexagonal-grids/blob/master/HexagonalGrids/app/src/main/java/net/omplanet/hexagonalgrids/model/Grid.java
 *
 * *   * *   *
 *   * *   * *
 * *   * *   *
 *   * *   * *
 * *   * *   *
 * A grid of hex nodes with axial coordinates.
 */
public class Grid {

    public enum Shape {
        RECTANGLE,
        HEXAGON_POINTY_TOP
    }

    public final int radius; //The radius of the grid - the count of rings around the central node
    public final int scale; //The radius of the single node in grid
    public final Shape shape; //The shape of the grid

    //Derived node properties
    public final int width; //The width of the single hexagon node
    public final int height; //The height of the single hexagon node
    public final int centerOffsetX; //Relative center coordinate within one node
    public final int centerOffsetY; //Relative center coordinate within one node

    public Cube[] nodes;

    //Added by Lucía Gil --> ArrayList of Hex generated
    ArrayList<Hexagon> board;

    /**
     * Construing a Grid with a set of cubes, scale, and shape
     * @param radius The count of rings around the central node
     * @param scale The radius of the hexagon in pixels
     * @param shape The shape of the hexagon
     */
    public Grid(int radius, int scale, Shape shape, ArrayList<Hexagon> gaps, ArrayList<Piece> board) {
        this.radius = radius;
        this.scale = scale;
        this.shape = shape;

        //Init derived node properties
        width = (int) (Math.sqrt(3) * scale);
        height = 2 * scale;
        centerOffsetX = width/2;
        centerOffsetY = height/2;

        //Init ArrayList
        this.board = new ArrayList<>();

        //Init nodes
        switch (shape) {
            case HEXAGON_POINTY_TOP:
                generateHexagonalShape(radius,gaps,board);
                break;
            case RECTANGLE:
                generateRectangleShape(radius);
                break;
        }
    }

    public ArrayList<Hexagon> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<Hexagon> board) {
        this.board = board;
    }

    public boolean isInBoard(Hexagon hex){
        for(int i=0;i<getBoard().size();i++)
            if(getBoard().get(i).toString().equals(hex.toString())) return true;
        return false;
    }

    public Point hexToPixel(Hexagon hexagon) {
        int x = 0;
        int y = 0;

        switch (shape) {
            case HEXAGON_POINTY_TOP:
                x = (int) (width * (hexagon.getQ() + 0.5 * hexagon.getR()));
                y = (int) (scale * 1.5 * hexagon.getR());
                break;
            case RECTANGLE:
                //oddR alignment
                x = (int) (width * hexagon.getQ() + 0.5 * width * (hexagon.getR()%2));
                y = (int) (scale * 1.5 * hexagon.getR());
                break;
        }

        return new Point(x, y);
    }

    public Hexagon pixelToHex(float x, float y) {
        float q = (float) (Math.sqrt(3)/3 * x - 1/3 * y) / scale;
        float r = (2/3 * y) / scale;

        return new Hexagon(q, r);

        //TODO RECTANGLE
    }

    private void generateHexagonalShape(int radius, ArrayList<Hexagon> gaps, ArrayList<Piece> board) throws ArrayIndexOutOfBoundsException {
        //nodes = new Cube[getNumberOfNodesInGrid(radius, shape)];
        int hex = gaps.size();
        int pieces = board.size();
        nodes = new Cube[hex+pieces];
        int i = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                int z = -x-y;
                if (Math.abs(x) <= radius && Math.abs(y) <= radius && Math.abs(z) <= radius) {
                    if(isInWhatever(gaps,new Cube(x,y,z)) || isInWhatever2(board,new Cube(x,y,z)))
                        nodes[i++] = new Cube(x, y, z);
                    this.board.add(new Cube(x,y,z).toHex());
                }
            }
        }
    }

    private boolean isInWhatever(ArrayList<Hexagon> gaps, Cube cube){
        for(int i=0;i<gaps.size();i++){
            if(gaps.get(i).toString().equals(cube.toHex().toString())) return true;
        }
        return false;
    }

    private boolean isInWhatever2(ArrayList<Piece> gaps, Cube cube){
        for(int i=0;i<gaps.size();i++){
            if(gaps.get(i).getHexagon().toString().equals(cube.toHex().toString())) return true;
        }
        return false;
    }

    private void generateRectangleShape(int radius) {
        int minQ=0;
        int maxQ=radius*2;
        int minR=0;
        int maxR=radius*2;

        nodes = new Cube[getNumberOfNodesInGrid(radius, shape)];
        int i = 0;

        for (int q = minQ; q <= maxQ; q++) {
            for (int r = -minR; r <= maxR; r++) {
                nodes[i++] = new Hexagon(q,r).oddRHexToCube(); //conversion to cube is different for oddR coordinates
            }
        }
    }

    /**
     * @return Number of hexagons inside of a hex or oddR rectangle shaped grid with the given radius
     */
    public static int getNumberOfNodesInGrid(int radius, Shape shape) {
        switch (shape) {
            case HEXAGON_POINTY_TOP:
                return (int) (3 * Math.pow(radius+1, 2) - 3 * (radius +1) + 1);
            case RECTANGLE:
                return (radius * 2 + 1) * (radius * 2 + 1);
        };

        return 0;
    }

    public static int getGridWidth(int radius, int scale, Shape shape) {
        switch (shape) {
            case HEXAGON_POINTY_TOP:
                return (int) ((2*radius + 1) * Math.sqrt(9) * scale);
            case RECTANGLE:
                return 0; //TODO
        };

        return 0;
    }

    public static int getGridHeight(int radius, int scale, Shape shape) {
        switch (shape) {
            case HEXAGON_POINTY_TOP:
                return (int) (scale * ((2*radius + 1) * 1.5 + 0.5));
            case RECTANGLE:
                return 0; //TODO
        };

        return 0;
    }
}

