package lucgilr.inf.uva.hivev2.Controller;

import java.util.ArrayList;

import lucgilr.inf.uva.hivev2.AI.AI;
import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hive;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.GameModel.Token;
import lucgilr.inf.uva.hivev2.GameModel.Hex;
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
    public ArrayList<Hex> getPlayerGaps(Player player){
        return model.getHive().getPlayerGapsAvailable(player);
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
    public ArrayList<Token> getBoard(){
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
     * @param token
     * @param hex
     */
    public void movetoken(Token token, Hex hex){
        model.getHive().movetoken(token, hex);
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
    public ArrayList<Token> getTokensFromBox(){
        return getPlayer().getTokensInTheBox();
    }

    /**
     *
     * @param type
     * @return
     */
    public Token takeTokenByType(String type){
        return getPlayer().takeToken(type);
    }

    /**
     *
     * @param token
     * @param coords
     */
    public void playToken(Token token, Hex coords){
        model.getHive().addToken(token, coords,true);
    }

    /**
     *
     * @param token
     * @return
     */
    public ArrayList<Hex> getPossibleMoves(Token token){
        return model.getHive().getPossibleGaps(token,false);
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
