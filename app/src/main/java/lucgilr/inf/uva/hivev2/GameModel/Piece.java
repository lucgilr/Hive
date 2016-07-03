package lucgilr.inf.uva.hivev2.GameModel;


/**
 * @author Lucía Gil Román (https://github.com/lucgilr)
 *
 * A piece represents an insect of the hive.
 * An hexagon is assigned to a piece to represent its position on the board.
 *
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
     * A piece has only a type, a player assigned and a value
     * when its not in the game
     */
    public Piece(PieceType type, Player player,int value){
        this.type=type;
        this.value=value;
        this.hexagon =new Hexagon();
        this.player=player;
        this.beetle=false;
    }


    /**
     * @return Type of a piece
     */
    public PieceType getType() {
        return type;
    }

    /**
     * @return values of a piece
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
     */
    public void setHexagon(Hexagon hexagon) {
        this.hexagon = hexagon;
    }

    /**
     * @return the hexagon of the piece.
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
     */
    public int getGraphId() {
        return graphId;
    }

    /**
     * Sets the piece ID for the graph.
     */
    public void setGraphId(int graphId) {
        this.graphId = graphId;
    }

    /**
     * @return true if the piece is in game
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * Sets to true if the piece is added to the game
     */
    public void setInGame() {
        this.inGame = true;
    }

    /**
     * @return information about the piece
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
