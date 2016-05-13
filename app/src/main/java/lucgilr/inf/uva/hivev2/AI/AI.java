package lucgilr.inf.uva.hivev2.AI;

import java.util.ArrayList;
import java.util.Random;

import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hex;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.GameModel.Token;
import lucgilr.inf.uva.hivev2.GameModel.TokenType;

/**
 * The AI will have two parts: Rules and Decision Tree.
 * It will start checking if one of the rules can be applied for the current move,
 * if it can't apply any rule then it will decide the next move using a decision tree.
 * TALK ABOUT MINIMAX ALPHA BETA PRUNNING!!!!!!!!
 * Created by Lucía Gil Román on 11/05/16.
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

    private boolean beeSaved;

    public AI(Player player){
        this.player=player;
        //Opening vars
        this.random=0;
        this.randomOp=0;
        this.opening = new Hex[2];
        this.hex=new Hex();
        this.beeSaved=false;
    }

    /**
     * Using this method the AI will decide which will be it next move.
     * @param game
     */
    public void makeAChoice(Game game){
        this.game=game;
        //First: Check Rules
        //Rule #1: First 3 moves
        if(player.getTurn()==1){
            Random r = new Random();
            this.randomOp = r.nextBoolean() ? 0:1;
        }
        if(player.getTurn()<4){
            //Random opening
            if(this.randomOp==0) openingOne();
            else openingTwo();
        }else {
            //Rule #2: Check Bee --> move it if its in danger
            if(game.getPlayer2().isBeeInGame()) {
                beeState();
                //If the AI bee is saved --> check the other player's bee
                if(this.beeSaved){
                    player1Bee();
                }//else --> heurística?
            }
        }
    }

    /**
     * First good opening advised in the official board game site:
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
     * Second good opening advised in the official board game site:
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

    /**
     * We have 3 ways of saving the bee:
     * 1st. It's not actually in danger --> Has less than 3 neighbours
     * 2nd. Is not blocked, then it can be moved to a new position or stays where it is.
     * 3rd. It's blocked but one of its neighbours can be moved to a new position.
     */
    private void beeState() {

        Token bee = new Token();
        Hex nextMove = new Hex();
        bee = game.getPlayer2().getTokenById(0);
        boolean hasBee = false;

        //If bee is blocked --> Check if one of the surrounding pieces can be move
        if(game.getHive().checkIfGapBlocked(bee.getCoordinates())){
            Token[] neighbours = new Token[6];
            neighbours = game.getHive().tokenNeighbours(bee.getCoordinates());
            for(int i=0;i<neighbours.length;i++){
                //If the token is from the AI player --> check if it can me moved
                if(neighbours[i].getPlayer().getColor().equals(this.player.getColor())){
                    ArrayList<Hex> pos = new ArrayList<>();
                    pos = game.getHive().getPossibleGaps(neighbours[i]);
                    //Check if it has a position not touching the bee
                    int size = pos.size();
                    if(size!=0) {
                        for (int j = 0; j < pos.size(); j++) {
                            Token[] newN = new Token[6];
                            newN = game.getHive().tokenNeighbours(pos.get(i));
                            for (int k = 0; k < newN.length; k++) {
                                //If the neighbour found has the AI bee
                                if(newN[k].getType()== TokenType.BEE && newN[k].getPlayer().getColor().equals(this.player.getColor())){
                                    //Don't move to this position
                                    hasBee=true;
                                }
                            }
                            if(!hasBee){
                                //If the new position doesn't touch the AI bee --> move to that position
                                game.getHive().movetoken(bee,pos.get(j));
                                this.beeSaved=true;
                                break;
                            }
                        }
                    }
                }
            }
        }else{
            //Check how many neighbours are surrounding it
            int neighbours = game.getHive().numberOfNeighbours(bee.getCoordinates());
            //If the number of neighbours is greater than 2 --> move bee to a better position
            if(neighbours>2){
                //Get the possible positions to move the bee
                ArrayList<Hex> pos = new ArrayList<>();
                pos = game.getHive().getPossibleGaps(bee);
                //Check how many neighbours have those gaps
                int maxN = 6;
                for(int i=0;i<pos.size();i++){
                    int n = game.getHive().numberOfNeighbours(pos.get(i));
                    if(n<maxN){
                        maxN=n;
                        hex = pos.get(i);
                    }
                }
                //If the new found gap has less than 3 neighbours --> better place for the bee than the current one
                if(maxN<3){
                    game.getHive().movetoken(bee,hex);
                    this.beeSaved=true;
                }
            }else{
                //The bee is not in danger
                this.beeSaved=true;
            }
        }
    }

    /**
     * Attacks the enemy's bee.
     */
    private void player1Bee() {
    }

}
