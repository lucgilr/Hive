package lucgilr.inf.uva.hivev2.Controller;

import java.util.ArrayList;

import lucgilr.inf.uva.hivev2.AI.AI;
import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hexagon;
import lucgilr.inf.uva.hivev2.GameModel.Hive;
import lucgilr.inf.uva.hivev2.GameModel.Language;
import lucgilr.inf.uva.hivev2.GameModel.Piece;
import lucgilr.inf.uva.hivev2.GameModel.PieceType;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.UI.GameUI;

/**
 * A Controller is used as middleman between the UI and the Game Model.
 * The user interacts with the UI and will trigger actions that will
 * return information from the Game Model through the Controller.
 * Created by Lucía Gil Román on 09/05/16.
 */
public class GameController {

    private Game model;
    private GameUI viewGrid;
    private AI ai;
    private Language language;

    /**
     * PRUEBA
     * @param model
     * @param view
     */
    public GameController(Game model, GameUI view){
        this.model=model;
        this.viewGrid=view;
        this.language = new Language();
    }

    /**
     *
     * @return
     */
    public Hive getHive(){
        return model.getHive();
    }

    /**
     *
     * @return
     */
    public Player getPlayer(){
        return model.playerTurn();
    }

    /**
     *
     * @param player
     * @return
     */
    public ArrayList<Hexagon> getPlayerHexagons(Player player){
        return model.getHive().getAvailableHexagonsPlayer(player);
    }

    /**
     *
     * @return
     */
    public boolean playerBeeInGame(){
        return getPlayer().isBeeInGame();
    }

    /**
     *
     * @return
     */
    public int getBoardSize(){
        return getHive().getBoard().size();
    }

    /**
     *
     * @return
     */
    public ArrayList<Piece> getBoard(){
        return model.getHive().getBoard();
    }

    /**
     *
     * @return
     */
    public int endGame(){
        return model.beeSurrounded();
    }

    /**
     *
     * @param piece
     * @param hexagon
     */
    public void movePiece(Piece piece, Hexagon hexagon){
        model.getHive().movePiece(piece, hexagon, false);
    }

    /**
     *
     */
    public void oneMoreRound(){
        model.oneMoreRound();
    }

    /**
     *
     */
    public void oneMoreTurn(){
        getPlayer().oneMoreTurn();
    }

    /**
     *
     * @return
     */
    public int getPlayerTurn(){
        return getPlayer().getTurn();
    }

    /**
     *
     * @return
     */
    public ArrayList<Piece> getPiecesFromBox(){
        return getPlayer().getPiecesInTheBox();
    }

    /**
     *
     * @param type
     * @return
     */
    public Piece takePieceByType(PieceType type){
        return getPlayer().inspectPieceFromBox(type);
    }

    /**
     *
     * @param piece
     * @param hexagon
     */
    public void playPiece(Piece piece, Hexagon hexagon){
        model.getHive().addPiece(piece, hexagon);
    }

    /**
     *
     * @param piece
     * @return
     */
    public ArrayList<Hexagon> getPossibleMoves(Piece piece){
        return model.getHive().getPossibleHexagons(piece, false);
    }

    /**
     *
     * @param game
     */
    public void makeAChoice(Game game){
        ai.makeAChoice(game);
    }

    /**
     *
     * @param player
     */
    public void initIA(Player player){
        this.ai = new AI(player);
    }


    public String getEnglish(PieceType type){
        return language.getEnglish(type);
    }

    public String getSpanish(PieceType type){
        return language.getSpanish(type);
    }

    public PieceType stringToPieceType(String bug){
        return language.stringToPieceType(bug);
    }


}
