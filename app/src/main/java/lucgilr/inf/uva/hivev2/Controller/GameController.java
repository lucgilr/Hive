package lucgilr.inf.uva.hivev2.Controller;

import java.util.ArrayList;

import lucgilr.inf.uva.hivev2.AI.AI;
import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hexagon;
import lucgilr.inf.uva.hivev2.GameModel.Hive;
import lucgilr.inf.uva.hivev2.GameModel.Piece;
import lucgilr.inf.uva.hivev2.GameModel.PieceType;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.UI.AIGameUI;
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
    private AIGameUI aiView;
    private AI ai;

    /**
     *
     * @param model
     * @param view
     */
    public GameController(Game model, GameUI view){
        this.model=model;
        this.view=view;
    }

    /**
     *
     * @param model
     * @param view
     */
    public GameController(Game model, AIGameUI view){
        this.model=model;
        this.aiView=view;
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
    public ArrayList<Hexagon> getPlayerGaps(Player player){
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
    public void movetoken(Piece piece, Hexagon hexagon){
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
    public ArrayList<Piece> getTokensFromBox(){
        return getPlayer().getPiecesInTheBox();
    }

    /**
     *
     * @param type
     * @return
     */
    public Piece takeTokenByType(PieceType type){
        return getPlayer().takePieceByType(type);
    }

    /**
     *
     * @param piece
     * @param coords
     */
    public void playToken(Piece piece, Hexagon coords){
        model.getHive().addPiece(piece, coords, true);
    }

    /**
     *
     * @param piece
     * @return
     */
    public ArrayList<Hexagon> getPossibleMoves(Piece piece){
        return model.getHive().getPossibleHexagon(piece, false);
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

}
