package lucgilr.inf.uva.hivev2.GameModel;

import java.util.ArrayList;


/**
 * @author Lucía Gil Román
 *
 * A player is the person/AI who plays the game.
 * It has assigned a color to distinguish itself from the other player.
 * When a new player is set it receives a box with eleven pieces to play
 * and a turn count that increments every time the player makes a move
 * or adds a piece to the board.
 */
public class Player {

    private String color;
    private int turn;
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
     */
    public void setBeeInGame() {
        this.beeInGame = true;
    }

    /**
     * @return all possible pieces to the player
     */
    private ArrayList<Piece> setPieces() {
        this.pieces.add(new Piece(PieceType.BEE, this, 30));
        this.pieces.add(new Piece(PieceType.GRASSHOPPER,this,2));
        this.pieces.add(new Piece(PieceType.GRASSHOPPER,this,2));
        this.pieces.add(new Piece(PieceType.GRASSHOPPER,this,2));
        this.pieces.add(new Piece(PieceType.SPIDER,this,6));
        this.pieces.add(new Piece(PieceType.SPIDER,this,6));
        this.pieces.add(new Piece(PieceType.BEETLE,this,4));
        this.pieces.add(new Piece(PieceType.BEETLE,this,4));
        this.pieces.add(new Piece(PieceType.ANT,this,8));
        this.pieces.add(new Piece(PieceType.ANT,this,8));
        this.pieces.add(new Piece(PieceType.ANT,this,8));
        return this.pieces;
    }

    /**
     * Increments the players turn count.
     */
    public void oneMoreTurn(){
        this.turn+=1;
    }

    /**
     * Returns a piece given its type.
     * @param type
     * @return
     */
    public Piece inspectPiece(PieceType type){
        for(int i=0;i<this.pieces.size();i++){
            if(this.pieces.get(i).getType().equals(type))
                return this.pieces.get(i);
        }
        return null;
    }

    /**
     * Returns the Piece from the box given its type.
     * @param type
     * @return
     */
    public Piece inspectPieceFromBox(PieceType type){
        for(int i=0;i<this.pieces.size();i++){
            if(this.pieces.get(i).getType().equals(type) && !this.pieces.get(i).isInGame())
                return this.pieces.get(i);
        }
        return null;
    }

    /**
     * Returns pieces that are in game
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
     * Returns pieces that are in the box
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
