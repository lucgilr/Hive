package lucgilr.inf.uva.hivev2.GameModel;


/**
 * A piece represents an insect of the hive.
 * An hexagon is assigned to a piece to represent its position on the board.
 * @author Lucía Gil Román
 */
public class Piece {

    private PieceType type;
    private final int value;
    private Player player;
    private Hexagon hexagon;
    private boolean beetle;
    private int graphId;
    private boolean inGame;

    /**
     *
     */
    public Piece(){
        this.value=0;
        this.hexagon =new Hexagon();
        this.beetle=false;
    }

    /**
     *
     * @param type
     * @param player
     */
    public Piece(PieceType type, Player player,int value){
        this.type=type;
        this.value=value;
        this.hexagon =new Hexagon();
        this.player=player;
        this.beetle=false;
    }


    /**
     *
     * @return
     */
    public PieceType getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * If a piece has a BEETLE on top it can't move, so we can say it's blocked.
     * @return true if the piece has a BEETLE on top, false otherwise.
     */
    public boolean isBeetle() {
        return !beetle;
    }

    /**
     * Set ...
     * @param beetle true if the piece has a BEETLE on top, and false if it hasn't.
     */
    public void setBeetle(boolean beetle) {
        this.beetle = beetle;
    }

    /**
     * Assigns an hexagon to the piece.
     * @param hexagon
     */
    public void setHexagon(Hexagon hexagon) {
        this.hexagon = hexagon;
    }

    /**
     * Gives the hexagon of the piece.
     * @return
     */
    public Hexagon getHexagon() {
        return hexagon;
    }

    /**
     * The piece belongs to a Player. This method give us which player.
     * @return the Player who is playing with this piece.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * When a piece is placed on the board for the first time it is given
     * an Unique Identifier so it can be represented as a vertex in a graph.
     * @return
     */
    public int getGraphId() {
        return graphId;
    }

    /**
     * Sets the piece ID for the graph.
     * @param graphId
     */
    public void setGraphId(int graphId) {
        this.graphId = graphId;
    }

    /**
     *
     * @return
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     *
     */
    public void setInGame() {
        this.inGame = true;
    }

    /**
     *
     * @return
     */
    public String pieceInfo(){
        return " Type: "+this.type+" "
                + " Value: "+this.value+" "
                + " Player: "+this.player.getColor()+" "
                + " X: "+this.hexagon.getQ()+" "
                + " Y: "+this.hexagon.getR()+" "
                + " Z: "+this.hexagon.getL()+" "
                + " graphId: "+this.getGraphId()+" "
                + " inGame: "+this.inGame+" "
                + " Beetle: "+this.beetle+"\n";
    }

}
