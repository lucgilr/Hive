package lucgilr.inf.uva.hivev2.AI;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hex;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.GameModel.Token;

/**
 * Created by gil on 11/05/16.
 */
public class AI {

    private Game game;
    //AI is player2
    private Player player;
    //Opening sets vars
    private int random;
    ArrayList<Hex> opening;

    public AI(Player player){
        this.player=player;
        this.random=0;
        this.opening = new ArrayList<>();
    }

    /**
     *
     * @param game
     */
    public void makeAChoice(Game game){
        this.game=game;
        //First: Check Rules
        if(player.getTurn()<4){
            //Random opening
            Random rand = new Random();
            random = rand.nextBoolean() ? 0:1;
            if(random==0) openingOne();
            else openingTwo();
        }
    }

    /**
     * http://gen42.com/images/tipspage3.jpg
     */
    private void openingOne(){
        Random rand = new Random();
        if(player.getTurn()==1){
            //Size of the possible positions
            int size = game.getHive().getPlayerGapsAvailable(player).size();
            //Generate a random number (To choose a random position)
            random = rand.nextInt((size - 1) + 1);
            //Place spider (id=4)
            Token t = new Token();
            t = player.takeTokenFromTheBox(4);
            opening = game.getHive().getPlayerGapsAvailable(player);
            Hex hex = opening.get(random);
            game.getHive().addToken(t,hex);
        }else if(player.getTurn()==2){
            random = rand.nextBoolean() ? 0:2;
            //Place Ant (id=8)
            Token t = new Token();
            t = player.takeTokenFromTheBox(8);
            opening = game.getHive().getPlayerGapsAvailable(player);
            Hex hex = opening.get(random);
            game.getHive().addToken(t,hex);
        }else if(player.getTurn()==3){
            //Place Bee (id=0)
            Token t = new Token();
            t = player.takeTokenFromTheBox(0);
            Hex hex = new Hex();
            if(random==0){
                hex = opening.get(2);
            }else{
                hex = opening.get(0);
            }
            game.getHive().addToken(t,hex);
        }
    }

    private void openingTwo() {
    }


}
