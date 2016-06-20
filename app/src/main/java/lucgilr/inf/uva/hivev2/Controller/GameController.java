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
 * A Controller is used as middleman between the UI and the Game Model.
 * The user interacts with the UI and will trigger actions that will
 * return information from the Game Model through the Controller.
 * Created by Lucía Gil Román on 09/05/16.
 */
public class GameController {

    private Game model;
    private GameUI view;
    private AI ai;
    private Language language;

    /**
     * @param model
     * @param view
     */
    public GameController(Game model, GameUI view){
        this.model=model;
        this.view =view;
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
    public void getPlayer(){
        this.view.setPlayer(model.playerTurn());
    }

    /**
     *
     * @param player
     * @return
     */
    public void getPlayerHexagons(Player player){
        view.setHexagons(model.getHive().getAvailableHexagonsPlayer(player));
    }

    /**
     *
     * @return
     */
    public boolean playerBeeInGame(){
        return view.getPlayer().isBeeInGame();
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
    public void endGame(){
        view.setEndGame(model.beeSurrounded());
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
        view.getPlayer().oneMoreTurn();
    }

    /**
     *
     * @return
     */
    public int getPlayerTurn(){
        return view.getPlayer().getTurn();
    }

    /**
     *
     * @return
     */
    public ArrayList<Piece> getPiecesFromBox(){
        return view.getPlayer().getPiecesInTheBox();
    }

    /**
     *
     * @param type
     * @return
     */
    public void takePieceByType(PieceType type){
        view.setPiece(view.getPlayer().inspectPieceFromBox(type));
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
    public void getPossibleMoves(Piece piece){
        view.setPossibleHexagons(model.getHive().getPossibleHexagons(piece,false));
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


    public void setEnglish(PieceType type){
        view.setBug(language.getEnglish(type));
    }

    public void setSpanish(PieceType type){
        view.setBug(language.getSpanish(type));
    }

    public void stringToPieceType(String bug){
        view.setBugType(language.stringToPieceType(bug));
    }


}
