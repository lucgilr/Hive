package lucgilr.inf.uva.hivev2.Controller;

import android.util.Log;

import java.util.ArrayList;

import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hive;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.GameModel.Token;
import lucgilr.inf.uva.hivev2.ModelUI.Hex;
import lucgilr.inf.uva.hivev2.UI.GameUI;

/**
 * Created by gil on 09/05/16.
 */
public class GameController {

    private Game model;
    private GameUI view;

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
        model.getHive().movetoken(token,hex);
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
        model.getHive().addToken(token, coords);
    }

    /**
     *
     * @param token
     * @return
     */
    public ArrayList<Hex> getPossibleMoves(Token token){
        return model.getHive().getPossibleGaps(token);
    }

}