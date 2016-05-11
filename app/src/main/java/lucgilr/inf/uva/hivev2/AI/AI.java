package lucgilr.inf.uva.hivev2.AI;

import android.util.Log;

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
    private int randomOp;
    Hex[] opening;
    Hex hex;

    public AI(Player player){
        this.player=player;
        //Opening vars
        this.random=0;
        this.randomOp=0;
        this.opening = new Hex[2];
        this.hex=new Hex();
    }

    /**
     *
     * @param game
     */
    public void makeAChoice(Game game){
        this.game=game;
        //First: Check Rules
        if(player.getTurn()==1){
            Random r = new Random();
            this.randomOp = r.nextBoolean() ? 0:1;
        }
        if(player.getTurn()<4){
            //Random opening
            if(this.randomOp==0) openingOne();
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
            this.random = rand.nextInt((size - 1) + 1);
            //Place spider (id=4)
            Token t = new Token();
            t = player.takeTokenFromTheBox(4);
            this.hex = game.getHive().getPlayerGapsAvailable(player).get(random);
            game.getHive().addToken(t, hex);
        }else if(player.getTurn()==2){
            this.random = rand.nextBoolean() ? 0:1;
            //Place Ant (id=8)
            Token t = new Token();
            t = player.takeTokenFromTheBox(8);
            this.opening = game.getHive().vPosition(hex);
            this.hex = opening[random];
            game.getHive().addToken(t,hex);
        }else if(player.getTurn()==3){
            //Place Bee (id=0)
            Token t = new Token();
            t = player.takeTokenFromTheBox(0);
            if(random==0){
                hex = opening[1];
            }else{
                hex = opening[0];
            }
            game.getHive().addToken(t,this.hex);
        }
    }

    /**
     * http://gen42.com/images/tipspage4.jpg
     */
    private void openingTwo() {
        Random rand = new Random();
        if(player.getTurn()==1){
            //Size of the possible positions
            int size = game.getHive().getPlayerGapsAvailable(player).size();
            //Generate a random number (To choose a random position)
            this.random = rand.nextInt((size - 1) + 1);
            //Place Bee (id=0)
            Token t = new Token();
            t = player.takeTokenFromTheBox(0);
            this.hex = game.getHive().getPlayerGapsAvailable(player).get(random);
            game.getHive().addToken(t,hex);
        }else if(player.getTurn()==2){
            this.random = rand.nextBoolean() ? 0:1;
            //Place Spider (id=4)
            Token t = new Token();
            t = player.takeTokenFromTheBox(4);
            this.opening = game.getHive().vPosition(hex);
            this.hex = opening[random];
            game.getHive().addToken(t,hex);
        }else if(player.getTurn()==3){
            //Place Spider (id=5)
            Token t = new Token();
            t = player.takeTokenFromTheBox(5);
            if(random==0){
                hex = opening[1];
            }else{
                hex = opening[0];
            }
            game.getHive().addToken(t,this.hex);
        }
    }

    private void print() {
        Log.d("First", opening[0].toString());
        Log.d("Second", opening[1].toString());
    }


}
