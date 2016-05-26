package lucgilr.inf.uva.hivev2.GameModel;


/**
 * A piece represents an insect of the hive.
 * An hexagon is assigned to a piece to represent its position on the board.
 * @author Lucía Gil Román
 */
public class Piece {

    private int id;
    private PieceType type;
    private int value;
    private Player player;
    private boolean inGame;
    private Hexagon hexagon;
    private boolean blocked;
    private boolean beetle;
    private int graphId;

    /**
     *
     */
    public Piece(){
        this.value=0;
        this.inGame=false;
        this.hexagon =new Hexagon(-100,-100,-100);
        this.blocked=false;
        this.beetle=false;
    }

    /**
     *
     * @param type
     * @param id
     * @param player
     */
    public Piece(PieceType type, int id, Player player){
        this.type=type;
        this.value=0;
        this.inGame=false;
        this.hexagon =new Hexagon(100,100,100);
        this.blocked=false;
        this.id=id;
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
     * @param type
     */
    public void setType(PieceType type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }


    /**
     * Tells if the piece is on the board o is in the player's box.
     * @return true if the piece is on the board. Otherwise returns false.
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * Set the situation of the token.
     * @param enJuego true if the piece is on board and false if it isn't.
     */
    public void setInGame(boolean enJuego) {
        this.inGame = enJuego;
    }


    /**
     * If the piece is blocked it can't be moved, so this method allows us to know its blocked condition.
     * @return true if its blocked and false if the piece can be moved.
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * Set the blocked situation of the piece.
     * @param blocked true if it's blocked, false if it isn't.
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    /**
     * If a piece has a BEETLE on top it can't move, so we can say it's blocked.
     * @return true if the piece has a BEETLE on top, false otherwise.
     */
    public boolean isBeetle() {
        return beetle;
    }

    /**
     * Set ...
     * @param beetle true if the piece has a BEETLE on top, and false if it hasn't.
     */
    public void setBeetle(boolean beetle) {
        this.beetle = beetle;
        setBlocked(true);
    }

    /**
     * Gets the Unique Identification number for a player's piece.
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Sets an ID for a player's piece.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
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
     * Every piece is from a player. We can indicate which player with this method.
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
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
    public String pieceInfo(){
        return " id #"+this.id+" "
                + " Type: "+this.type+" "
                + " Value: "+this.value+" "
                + " Player: "+this.player.getColor()+" "
                + " In GameUI: "+this.inGame+" "
                + " X: "+this.hexagon.getQ()+" "
                + " Y: "+this.hexagon.getR()+" "
                + " Z: "+this.hexagon.getD()+" "
                + " Blocked: "+this.blocked+" "
                + " Beetle: "+this.beetle+"\n";
    }

}
