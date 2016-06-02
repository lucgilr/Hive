package lucgilr.inf.uva.hivev2.GameModel;

import java.util.ArrayList;


/**
 * A player is the person/AI who plays the GameUI.
 * It has assigned a color to distinguish itself from the other player.
 * When a new player is set it receives a box with eleven pieces to play
 * and a turn count that increments every time the player makes a move
 * or adds a piece to the board.
 * @author Lucía Gil Román
 */
public class Player {

    private String color;
    private int turn;
    //private ArrayList<Piece> piecesInGame;
    //private ArrayList<Piece> piecesInTheBox;
    private ArrayList<Piece> pieces;
    private boolean beeInGame;

    /**
     *
     * @param color
     */
    public Player(String color){
        this.color=color;
        this.turn=1;
        this.pieces = new ArrayList<>();
        this.pieces = setPieces();
        //this.piecesInTheBox = new ArrayList<>();
        //this.piecesInTheBox = setPieces();
        //this.piecesInGame = new ArrayList<>();
    }

    /**
     * @return color assigned to the player.
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets a color to a player
     * @param color
     */
    public void setColor(String color) {
        this.color=color;
    }

    /**
     * @return the number of turns the player has played.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Assigns a turn to the player.
     * @param turn
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    /**
     * @return true if the player's bee is on the board, false otherwise.
     */
    public boolean isBeeInGame() {
        return beeInGame;
    }

    /**
     * Assigns true if the bee's player has been set on the board.
     * @param beeInGame
     */
    public void setBeeInGame(boolean beeInGame) {
        this.beeInGame = beeInGame;
    }

    /**
     * @return all possible pieces to the player
     */
    private ArrayList<Piece> setPieces() {
        this.pieces.add(new Piece(PieceType.BEE, 0, this, 20));
        this.pieces.add(new Piece(PieceType.GRASSHOPPER,1,this,2));
        this.pieces.add(new Piece(PieceType.GRASSHOPPER,2,this,2));
        this.pieces.add(new Piece(PieceType.GRASSHOPPER,3,this,2));
        this.pieces.add(new Piece(PieceType.SPIDER,4,this,6));
        this.pieces.add(new Piece(PieceType.SPIDER,5,this,6));
        this.pieces.add(new Piece(PieceType.BEETLE,6,this,4));
        this.pieces.add(new Piece(PieceType.BEETLE,7,this,4));
        this.pieces.add(new Piece(PieceType.ANT,8,this,8));
        this.pieces.add(new Piece(PieceType.ANT,9,this,8));
        this.pieces.add(new Piece(PieceType.ANT,10,this,8));
        return this.pieces;
    }

    /**
     * Increments the players turn count.
     */
    public void oneMoreTurn(){
        this.turn+=1;
    }

    /**
     * Returns a piece given its id.
     * @param id
     * @return
     */
    public Piece inspectPieceById(int id){
        for(int i=0;i<this.pieces.size();i++){
            if(this.pieces.get(i).getId()==id)
                return this.pieces.get(i);
        }
        return null;
    }

    /**
     * Returns a piece given its id.
     * @param id
     * @return
     */
    public Piece inspectPieceByIdFromBox(int id){
        for(int i=0;i<this.pieces.size();i++){
            if(this.pieces.get(i).getId()==id && !this.pieces.get(i).isInGame())
                return this.pieces.get(i);
        }
        return null;
    }

    /**
     * Returns the Piece given its type.
     * @param type
     * @return
     */
    public Piece inspectPieceByTypeFromBox(PieceType type){
        for(int i=0;i<this.pieces.size();i++){
            if(this.pieces.get(i).getType().equals(type) && !this.pieces.get(i).isInGame())
                return this.pieces.get(i);
        }
        return null;
    }

    /**
     *
     * @return
     */
    public ArrayList<Piece> getPiecesInGame(){
        ArrayList<Piece> piecesInGame = new ArrayList<>();
        for(int i=0; i < getPieces().size();i++){
            if(getPieces().get(i).isInGame()) piecesInGame.add(getPieces().get(i));
        }
        return piecesInGame;
    }

    /**
     *
     * @return
     */
    public ArrayList<Piece> getPiecesInTheBox(){
        ArrayList<Piece> piecesInGame = new ArrayList<>();
        for(int i=0; i < getPieces().size();i++){
            if(!getPieces().get(i).isInGame()) piecesInGame.add(getPieces().get(i));
        }
        return piecesInGame;
    }

}
