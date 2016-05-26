package lucgilr.inf.uva.hivev2.GameModel;

import java.util.ArrayList;


/**
 * A player is the person/AI who plays the GameUI.
 * It has assigned a color to distinguish itself from the other player.
 * When a new player is set it receives a box with eleven pieces to play
 * and a turn count that increments every time the player makes a move
 * or adds a piece to the board game.
 * @author Lucía Gil Román
 */
public class Player {

    private String color;
    private int turn;
    private ArrayList<Piece> piecesInGame;
    private ArrayList<Piece> piecesInTheBox;
    private int playedPieces;
    private boolean beeInGame;

    /**
     *
     * @param color
     */
    public Player(String color){
        this.color=color;
        this.turn=1;
        this.piecesInTheBox = new ArrayList<>();
        this.piecesInTheBox = setPiecesBox();
        this.piecesInGame = new ArrayList<>();
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
     * @return
     */
    public int getPlayedPieces() {
        return playedPieces;
    }

    /**
     *
     * @param playedPieces
     */
    public void setPlayedPieces(int playedPieces) {
        this.playedPieces = playedPieces;
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

    /**
     * @return pieces that are already on the board.
     */
    public ArrayList<Piece> getPiecesInGame() {
        return piecesInGame;
    }

    /**
     *
     * @param piecesInGame
     */
    public void setPiecesInGame(ArrayList<Piece> piecesInGame) {
        this.piecesInGame = piecesInGame;
    }

    /**
     * @return pieces that the player has not played yet.
     */
    public ArrayList<Piece> getPiecesInTheBox() {
        return piecesInTheBox;
    }

    /**
     *
     * @param piecesInTheBox
     */
    public void setPiecesInTheBox(ArrayList<Piece> piecesInTheBox) {
        this.piecesInTheBox = piecesInTheBox;
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
    private ArrayList<Piece> setPiecesBox() {
        this.piecesInTheBox.add(new Piece(PieceType.BEE,0,this));
        this.piecesInTheBox.add(new Piece(PieceType.GRASSHOPPER,1,this));
        this.piecesInTheBox.add(new Piece(PieceType.GRASSHOPPER,2,this));
        this.piecesInTheBox.add(new Piece(PieceType.GRASSHOPPER,3,this));
        this.piecesInTheBox.add(new Piece(PieceType.SPIDER,4,this));
        this.piecesInTheBox.add(new Piece(PieceType.SPIDER,5,this));
        this.piecesInTheBox.add(new Piece(PieceType.BEETLE,6,this));
        this.piecesInTheBox.add(new Piece(PieceType.BEETLE,7,this));
        this.piecesInTheBox.add(new Piece(PieceType.ANT,8,this));
        this.piecesInTheBox.add(new Piece(PieceType.ANT,9,this));
        this.piecesInTheBox.add(new Piece(PieceType.ANT,10,this));
        return this.piecesInTheBox;
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
    public Piece getPieceById(int id){
        for(int i=0;i<this.piecesInTheBox.size();i++){
            if(this.piecesInTheBox.get(i).getId()==id)
                return this.piecesInTheBox.get(i);
        }
        for(int j=0;j<this.piecesInGame.size();j++){
            if(this.piecesInGame.get(j).getId()==id)
                return this.piecesInGame.get(j);
        }
        return null;
    }

    /**
     * Takes a piece from the box to place on the board.
     * @param id of the piece to play.
     * @return
     */
    public Piece takePieceFromTheBox(int id){
        //Get piece
        Piece t = getPieceById(id);
        //Add piece to piecesInGame
        getPiecesInGame().add(t);
        //Delete piece from the box
        getPiecesInTheBox().remove(t);
        return t;
    }

    /**
     * Returns the Piece given its type.
     * @param type
     * @return
     */
    public Piece takePieceByType(PieceType type){
        Piece t = new Piece();
        //Get piece
        for(int i=0;i<this.piecesInTheBox.size();i++){
            if(this.piecesInTheBox.get(i).getType().equals(type))
                t = this.piecesInTheBox.get(i);
        }
        //Add piece to piecesInGame
        getPiecesInGame().add(t);
        //Delete piece from the box
        getPiecesInTheBox().remove(t);
        return t;
    }

    /**
     * Guess if a piece has been played given its id.
     * @param id
     * @return
     */
    public Piece inspectPieceInGame(int id){
        Piece piece = new Piece();
        for(int i=0;i<this.piecesInGame.size();i++){
            if(this.piecesInGame.get(i).getId()==id){
                piece = this.piecesInGame.get(i);
            }
        }
        return piece;
    }

    /**
     * Guess if a piece of a determinate type still is in the box.
     * @param type
     * @return
     */
    public boolean isPieceInBox(PieceType type){
        for(int i=0;i<this.getPiecesInTheBox().size();i++){
            if(this.getPiecesInTheBox().get(i).getType().equals(type)) return true;
        }
        return false;
    }

}
