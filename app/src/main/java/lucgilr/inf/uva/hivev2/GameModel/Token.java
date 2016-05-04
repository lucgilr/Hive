package lucgilr.inf.uva.hivev2.GameModel;


import lucgilr.inf.uva.hivev2.ModelUI.Hex;

/**
 * A token represents an insect of the hive. 
 * @author Lucía Gil Román
 */
public class Token {

    private int id;
    private TokenType type;
    private int value;
    private Player player;
    private boolean inGame;
    //private Coords coordinates;
    private Hex coordinates;
    private boolean blocked;
    private boolean beetle;
    private int graphId;

    /**
     *
     */
    public Token(){
        this.value=0;
        this.inGame=false;
        //this.coordinates=new Coords(100,100,100);
        this.coordinates=new Hex(100,100,100);
        this.blocked=false;
        this.beetle=false;
    }

    /**
     *
     * @param tipo
     * @param id
     * @param player
     */
    public Token(TokenType tipo,int id,Player player){
        this.type=tipo;
        this.value=0;
        this.inGame=false;
        //this.coordinates=new Coords(100,100,100);
        this.coordinates=new Hex(100,100,100);
        this.blocked=false;
        this.id=id;
        this.player=player;
        this.beetle=false;
    }


    /**
     *
     * @return
     */
    public TokenType getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(TokenType type) {
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
     * Tells if the token is on the board o is in the player's box.
     * @return true if the token is on the board. Otherwise returns false. 
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * Set the situation of the token.
     * @param enJuego true if the token is on board and false if it isn't.
     */
    public void setInGame(boolean enJuego) {
        this.inGame = enJuego;
    }


    /**
     * If the token is blocked it can't move, so this method allows us to know its blocked condition.
     * @return true if its blocked and false if the token can be moved.
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * Set the blocked situation of the token.
     * @param blocked true if it's blocked, false if it isn't.
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    /**
     * If a token has a BEETLE token on top it can't move, so we can say it's blocked.
     * @return true if the token has a BEETLE on top, false otherwise.
     */
    public boolean isBeetle() {
        return beetle;
    }

    /**
     * Set ...
     * @param beetle true if the token has a BEETLE on top, and false if it hasn't.
     */
    public void setBeetle(boolean beetle) {
        this.beetle = beetle;
        setBlocked(true);
    }

    /**
     * Gets the Unique Identification number for a player's token.
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Sets an ID for a player's token.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets coordinates to the token so it can now its position on the board.
     * @param coordinates
     */
    public void setCoordinates(Hex coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Gives the coordinates of the token.
     * @return
     */
    public Hex getCoordinates() {
        return coordinates;
    }

    /**
     * The token belongs to a Player. This method give us which player.
     * @return the Player who is playing with this token. 
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Every token is from a player. We can indicate which player with this method. 
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * When a token is placed on the board for the first time it is given
     * an Unique Identifier so it can be represented as a vertex in a graph.
     * @return
     */
    public int getGraphId() {
        return graphId;
    }

    /**
     * Sets the token ID for the graph.
     * @param graphId
     */
    public void setGraphId(int graphId) {
        this.graphId = graphId;
    }

    /**
     *
     * @return
     */
    public String tokenInfo(){
        return " id #"+this.id+" "
                + " Type: "+this.type+" "
                + " Value: "+this.value+" "
                + " Player: "+this.player.getColor()+" "
                + " In Game: "+this.inGame+" "
                + " X: "+this.coordinates.getQ()+" "
                + " Y: "+this.coordinates.getR()+" "
                + " Z: "+this.coordinates.getD()+" "
                + " Blocked: "+this.blocked+" "
                + " Beetle: "+this.beetle+"\n";
    }

}
