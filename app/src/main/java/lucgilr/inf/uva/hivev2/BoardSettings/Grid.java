package lucgilr.inf.uva.hivev2.BoardSettings;

import android.graphics.Point;

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

    public final int radius; //The radius of the grid - the count of rings around the central node
    public final int scale; //The radius of the single node in grid

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
     */
    public Grid(int radius, int scale, ArrayList<Hexagon> gaps, ArrayList<Piece> board) {
        this.radius = radius;
        this.scale = scale;

        //Init derived node properties
        width = (int) (Math.sqrt(3) * scale);
        height = 2 * scale;
        centerOffsetX = width/2;
        centerOffsetY = height/2;

        //Init ArrayList
        this.board = new ArrayList<>();

        //Init nodes
        generateHexagonalShape(radius,gaps,board);
    }

    public Grid(int radius, int scale, ArrayList<Piece> board) {
        this.radius = radius;
        this.scale = scale;

        //Init derived node properties
        width = (int) (Math.sqrt(3) * scale);
        height = 2 * scale;
        centerOffsetX = width/2;
        centerOffsetY = height/2;

        //Init ArrayList
        this.board = new ArrayList<>();

        //Init nodes
        generateHexagonalShape(radius,board);
    }

    public ArrayList<Hexagon> getBoard() {
        return board;
    }

    public Point hexToPixel(Hexagon hexagon) {
        int x = 0;
        int y = 0;

                x = (int) (width * (hexagon.getQ() + 0.5 * hexagon.getR()));
                y = (int) (scale * 1.5 * hexagon.getR());

        return new Point(x, y);
    }

    private void generateHexagonalShape(int radius, ArrayList<Hexagon> gaps, ArrayList<Piece> board) throws ArrayIndexOutOfBoundsException {

        int size = getNodesSize(gaps,board);
        int notFirstDimension = checkBeetles(board);

        nodes = new Cube[size+board.size()-notFirstDimension];
        int i = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                int z = -x-y;
                if (Math.abs(x) <= radius && Math.abs(y) <= radius && Math.abs(z) <= radius) {
                    if(isInPossibleGaps(gaps, new Cube(x, y, z)) || isOnBoard(board, new Cube(x, y, z)))
                        nodes[i++] = new Cube(x, y, z);
                    this.board.add(new Cube(x,y,z).toHex());
                }
            }
        }
    }

    private void generateHexagonalShape(int radius, ArrayList<Piece> board) throws ArrayIndexOutOfBoundsException {

        int notFirstDimension = checkBeetles(board);

        nodes = new Cube[board.size()-notFirstDimension];
        int i = 0;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                int z = -x-y;
                if (Math.abs(x) <= radius && Math.abs(y) <= radius && Math.abs(z) <= radius) {
                    if(isOnBoard(board, new Cube(x, y, z)))
                        nodes[i++] = new Cube(x, y, z);
                    this.board.add(new Cube(x,y,z).toHex());
                }
            }
        }
    }

    private int getNodesSize(ArrayList<Hexagon> gaps,ArrayList<Piece> board){
        int size = 0;

        boolean repeated = false;
        for(int i=0;i<gaps.size();i++){
            for(int j=0;j<board.size();j++){
                if(gaps.get(i).toString2D().equals(board.get(j).getHexagon().toString2D())){
                    repeated=true;
                    break;
                }
            }
            if(!repeated){
                size +=1;
            }
            repeated = false;
        }
        return size;
    }

    private int checkBeetles(ArrayList<Piece> board){
        int count = 0;
        for(int i=0; i<board.size();i++){
            if(board.get(i).getHexagon().getD()!=0) count += 1;
        }
        return count;
    }

    private boolean isInPossibleGaps(ArrayList<Hexagon> gaps, Cube cube){
        for(int i=0;i<gaps.size();i++){
            if(gaps.get(i).toString().equals(cube.toHex().toString())) return true;
        }
        return false;
    }

    private boolean isOnBoard(ArrayList<Piece> gaps, Cube cube){
        for(int i=0;i<gaps.size();i++){
            if(gaps.get(i).getHexagon().toString().equals(cube.toHex().toString())) return true;
        }
        return false;
    }

    public static int getGridWidth(int radius, int scale) {
        return (int) ((2*radius + 1) * Math.sqrt(9) * scale);
    }

    public static int getGridHeight(int radius, int scale) {
        return (int) (scale * ((2 * radius + 1) * 1.5 + 0.5));
    }
}

