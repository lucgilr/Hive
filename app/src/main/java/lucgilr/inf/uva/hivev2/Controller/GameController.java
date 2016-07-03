package lucgilr.inf.uva.hivev2.Controller;

import java.util.ArrayList;

import lucgilr.inf.uva.hivev2.AI.AI;
import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hexagon;
import lucgilr.inf.uva.hivev2.GameModel.Hive;
import lucgilr.inf.uva.hivev2.BoardSettings.Language;
import lucgilr.inf.uva.hivev2.GameModel.Piece;
import lucgilr.inf.uva.hivev2.GameModel.PieceType;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.UI.GameUI;

/**
 * @author Lucía Gil Román (https://github.com/lucgilr)
 *
 * A Controller is used as middleman between the UI and the Game Model.
 * The user interacts with the UI and will trigger actions that will
 * return information from the Game Model through the Controller.
 *
 */
public class GameController {

    private final Game model;
    private final GameUI view;
    private AI ai;
    private final Language language;

    /**
     * The controller has the Game and the View of the application so it can pass
     * information between them
     * It also initializes the language to set the view to the correct one.
     */
    public GameController(Game model, GameUI view){
        this.model=model;
        this.view =view;
        this.language = new Language();
    }

    /**
     * @return the hive
     */
    public Hive getHive(){
        return model.getHive();
    }

    /**
     * Sets the player that has to play this turn
     */
    public void getPlayer(){
        this.view.setPlayer(model.playerTurn());
    }

    /**
     * Obtains the hexagons where the given player can place a piece for the first time.
     */
    public void getPlayerHexagons(Player player){
        view.setHexagons(model.getHive().getAvailableHexagonsPlayer(player));
    }

    /**
     * @return true if tha players bee is in game
     */
    public boolean playerBeeInGame(){
        return view.getPlayer().isBeeInGame();
    }

    /**
     * @return the size of the board --> Number of pieces playing
     */
    public int getBoardSize(){
        return getHive().getBoard().size();
    }

    /**
     * @return the board --> All the pieces that are playing
     */
    public ArrayList<Piece> getBoard(){
        return model.getHive().getBoard();
    }

    /**
     * Get situation of the bees to check if the game is over
     */
    public void endGame(){
        view.setEndGame(model.beeSurrounded());
    }

    /**
     * Moves a piece from one position to another
     */
    public void movePiece(Piece piece, Hexagon hexagon){
        model.getHive().movePiece(piece, hexagon, false);
    }

    /**
     * Increments the games round
     */
    public void oneMoreRound(){
        model.oneMoreRound();
    }

    /**
     * Increments the players turn
     */
    public void oneMoreTurn(){
        view.getPlayer().oneMoreTurn();
    }

    /**
     * @return the turn of the player that is currently making a move
     */
    public int getPlayerTurn(){
        return view.getPlayer().getTurn();
    }

    /**
     * @return list of pieces that a player has not played
     */
    public ArrayList<Piece> getPiecesFromBox(){
        return view.getPlayer().getPiecesInTheBox();
    }

    /**
     * Inspects a piece from the players box given its type
     */
    public void takePieceByType(PieceType type){
        view.setPiece(view.getPlayer().inspectPieceFromBox(type));
    }

    /**
     * Adds a piece to the game
     */
    public void playPiece(Piece piece, Hexagon hexagon){
        model.getHive().addPiece(piece, hexagon);
    }

    /**
     * Sets the possible moves a piece can make
     */
    public void getPossibleMoves(Piece piece){
        view.setPossibleHexagons(model.getHive().getPossibleHexagons(piece,false));
    }

    /**
     * Orders the Artificial Intelligent to make a move
     */
    public void makeAChoice(Game game){
        ai.makeAChoice(game);
    }

    /**
     * Initiates the Artificial Intelligent to start to play with it
     */
    public void initIA(Player player){
        this.ai = new AI(player);
    }


    /**
     * Shows bugs in english
     */
    public void setEnglish(PieceType type){
        view.setBug(language.getEnglish(type));
    }

    /**
     * Shows bugs in spanish
     */
    public void setSpanish(PieceType type){
        view.setBug(language.getSpanish(type));
    }

    /**
     * Gets the type of a bug given its string equivalent
     */
    public void stringToPieceType(String bug){
        view.setBugType(language.stringToPieceType(bug));
    }


}
