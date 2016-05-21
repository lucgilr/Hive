package lucgilr.inf.uva.hivev2.AI;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hex;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.GameModel.Token;
import lucgilr.inf.uva.hivev2.GameModel.TokenMove;
import lucgilr.inf.uva.hivev2.GameModel.TokenType;
/**
 * NOTAS:
 * Si un tenemos un escarabajo en juego y puede bloquear por orden: abeja 1º bloquear 2º subirse encima, otro escarabajo o un saltamontes --> HACEDLO!
 */

/**
 * The AI will have two parts: Rules and Decision Tree.
 * It will start checking if one of the rules can be applied for the current move,
 * if it can't apply any rule then it will decide the next move using a decision tree.
 * TALK ABOUT MINIMAX ALPHA BETA PRUNNING!!!!!!!! --> MAYBE I DON'T IMPLEMENT IT...
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
    //To check if the AI has already move
    boolean move;

    private boolean beeSaved;

    public AI(Player player){
        this.player=player;
        //Opening vars
        this.random=0;
        this.randomOp=0;
        this.opening = new Hex[2];
        this.hex=new Hex();
        this.beeSaved=false;
        this.move=false;
    }

    /**
     * Using this method the AI will decide which will be it next move.
     * @param game
     */
    public void makeAChoice(Game game){
        this.move=false;
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
        }else{
            //Rule #2: Check Bee --> move it if its in danger
            if(game.getPlayer2().isBeeInGame()) {
                saveBeeV2();
                //If the AI bee can't be saved then no move has been done--> try to attack the other player's bee
                if(!this.move){
                    attackOpponent();
                }//else --> heurística?
            }
        }
        //PRINT BOARD
        /*for(int i=0;i<this.game.getHive().getBoard().size();i++){
            Log.d("token...",this.game.getHive().getBoard().get(i).tokenInfo());
        }*/
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
            game.getHive().addToken(t, hex,true);
        }else if(player.getTurn()==2){
            this.random = rand.nextBoolean() ? 0:1;
            //Place Ant (id=8)
            Token t = new Token();
            t = player.takeTokenFromTheBox(8);
            this.opening = game.getHive().vPosition(hex);
            this.hex = opening[random];
            game.getHive().addToken(t,hex,true);
        }else if(player.getTurn()==3){
            //Place Bee (id=0)
            Token t = new Token();
            t = player.takeTokenFromTheBox(0);
            if(random==0){
                hex = opening[1];
            }else{
                hex = opening[0];
            }
            //first: check if there is already a token in that gap
            if(!game.getHive().checkIfGapTaken(this.hex))
                game.getHive().addToken(t,this.hex,true);
            else attackOpponent();
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
            game.getHive().addToken(t,hex,true);
        }else if(player.getTurn()==2){
            this.random = rand.nextBoolean() ? 0:1;
            //Place Spider (id=4)
            Token t = new Token();
            t = player.takeTokenFromTheBox(4);
            this.opening = game.getHive().vPosition(hex);
            this.hex = opening[random];
            game.getHive().addToken(t,hex,true);
        }else if(player.getTurn()==3){
            //Place Spider (id=5)
            Token t = new Token();
            t = player.takeTokenFromTheBox(5);
            if(random==0){
                hex = opening[1];
            }else{
                hex = opening[0];
            }
            //first: check if there is already a token in that gap
            if(!game.getHive().checkIfGapTaken(this.hex))
                game.getHive().addToken(t, this.hex,true);
            else{
                saveBeeV2();
                if(!this.move) attackOpponent();
            }
        }
    }

    /**
     *
     */
    private void saveBeeV2(){
        //Log.d("Save bee","...");
        TokenMove bestmove = new TokenMove();
        Token bee = new Token();
        bee = this.player.inspectTokenInGame(0);
        //First: if the bee is blocked --> Try to move one of its friendly neighbours
        if(this.game.getHive().checkIfGapBlocked(bee)){
            //Log.d("The bee is blocked","...");
            ArrayList<TokenMove> moves = new ArrayList<>();
            //Get friendly tokens
            ArrayList<Token> friends = new ArrayList<>();
            friends = getFriends(bee);
            //Log.d("number friends",String.valueOf(friends.size()));
            if(!friends.isEmpty()){
                //Get possible moves for the friendly token
                for(int i=0;i<friends.size();i++){
                    ArrayList<Hex> gaps = new ArrayList<>();
                    gaps = this.game.getHive().getPossibleGaps(friends.get(i),false);
                    if(!gaps.isEmpty()){
                        //If gaps is not empty --> evaluate the move
                        for(int j=0;j<gaps.size();j++){
                            int points = evalPosition(friends.get(i),gaps.get(j));
                            /*Log.d("moving token",friends.get(i).tokenInfo());
                            Log.d("To",gaps.get(j).toString());
                            Log.d("points",String.valueOf(points));*/
                            moves.add(new TokenMove(friends.get(i), gaps.get(j), points));
                        }
                    }
                }
            }
            if(!moves.isEmpty()){
                bestmove = getBetterMove(moves);
                this.game.getHive().movetoken(bestmove.getToken(),bestmove.getHex());
                this.beeSaved = true;
                this.move = true;
            }
        }else{
            //Second: If the bee is not blocked --> Get number of neighbours
            int n = game.getHive().numberOfNeighbours(bee.getCoordinates());
            //If the bee has more than 2 neighbours --> Move to a safer place.
            if(n>2){
                moveBee(bee);
            }else{
                //The bee is save.
                this.beeSaved=true;
            }
        }
    }

    /**
     * Attacks enemy following some rules order by priority of good moves.
     */
    private void attackOpponent() {
        Log.d("Attack an opponent","...");
        boolean tokenBlocking = false;
        //Array to store the possible moves
        ArrayList<TokenMove> bestMoves = new ArrayList<>();
        //First: Check if a token already in the game can block the enemy's bee
        for(int i=0;i<player.getTokensInGame().size();i++){
            Log.d("analize token",player.getTokensInGame().get(i).tokenInfo());
            //Array to store the moves for the token
            ArrayList<TokenMove> moves = new ArrayList<>();
            //If the token can't be moved --> Do nothing
            ArrayList<Hex> tokenMoves = new ArrayList<>();
            tokenMoves = this.game.getHive().getPossibleGaps(player.getTokensInGame().get(i),false);
            Log.d("moves for the token",String.valueOf(tokenMoves.size()));
            if(!tokenMoves.isEmpty()) {
                //If the token is already blocking the enemy's bee --> Do nothing
                Token[] n = new Token[6];
                n = game.getHive().tokenNeighbours(player.getTokensInGame().get(i).getCoordinates());
                for (int j = 0; j < n.length; j++) {
                    if (n[j] != null) {
                        if (n[j].getType().equals(TokenType.BEE) && n[j].getPlayer().getColor().equals("White")) {
                            Log.d("bee neighb", "yes");
                            if (checkIfUnblockingGap(player.getTokensInGame().get(i), n[j])) {
                                Log.d("Token blocking", "yes");
                                tokenBlocking = true;
                            }
                        }
                    }
                }
                if (!tokenBlocking) {
                    //Evaluate each move
                    for (int j = 0; j < tokenMoves.size(); j++) {
                        int points = evalPosition(player.getTokensInGame().get(i), tokenMoves.get(j));
                        Log.d("moving token", player.getTokensInGame().get(i).tokenInfo());
                        Log.d("To", tokenMoves.get(j).toString());
                        Log.d("points", String.valueOf(points));
                        moves.add(new TokenMove(player.getTokensInGame().get(i), tokenMoves.get(j), points));
                    }
                    //Check if it's current position is better than the new one
                    int current = evalPosition(player.getTokensInGame().get(i),player.getTokensInGame().get(i).getCoordinates());
                    Log.d("current points",String.valueOf(current));
                    TokenMove bestMoveToken = getBetterMove(moves);
                    int bestMove = bestMoveToken.getPoints();
                    Log.d("best move points",String.valueOf(bestMove));
                    if(bestMove>current) bestMoves.add(bestMoveToken);
                }
            }
        }
        //Evaluate the points of the best move
        if(!bestMoves.isEmpty()){
            TokenMove bestmove = getBetterMove(bestMoves);
            this.game.getHive().movetoken(bestmove.getToken(), bestmove.getHex());
            this.move = true;
        }
        //Second: If the tokens in the game can't be in a better position --> Add new ones
        if(!this.move && !this.game.getHive().getPlayerGapsAvailable(this.player).isEmpty()){
            Log.d("Adding a new token!","...");
            Token newToken = new Token();
            //Takes a token from the box
            newToken = getToken();
            Log.d("Token to add",newToken.tokenInfo());
            //Add token to the board

        }else{

        }
        //Third: No tokens to add...


    }

    /**
     * Points: if the neighbour is an enemy: +1
     * if its a friendly token: -1
     * If the token is an enemy: +5 the move can block it.
     * If the enemy is a bee: +10.
     * @param hex
     * @return
     */
    private int evalPosition(Token toMove,Hex hex) {
        Log.d("to move:",toMove.tokenInfo());
        Log.d("destiny",hex.toString());
        //Save current token position
        Hex currentPos = new Hex(toMove.getCoordinates().getQ(),toMove.getCoordinates().getR(),toMove.getCoordinates().getD());
        //Move token to the position to evaluate
        this.game.getHive().movetoken(toMove,hex);

        /*for(int j=0;j<this.game.getHive().getBoard().size();j++)
            Log.d("after checking",this.game.getHive().getBoard().get(j).tokenInfo());
        Log.d("---------------------", "---------------------");*/

        int points = 0;
        Token[] n = new Token[6];
        n = this.game.getHive().tokenNeighbours(hex);
        for(int j=0;j<n.length;j++){
            if(n[j]!=null){
                if(n[j].getPlayer().getColor().equals(this.player.getColor())){
                    Log.d("toCheck",n[j].tokenInfo());
                    Log.d("friend","-1");
                    points += -1;
                    //if(checkIfBlockingGap(toMove,n[j],hex)) {
                    if(game.getHive().checkIfGapBlocked(n[j])){
                        Log.d("Blocking friend","-5");
                        points += -5;
                        if (n[j].getType().equals(TokenType.BEE)) {
                            Log.d("Friend bee","-10");
                            points += -10;
                        }
                    }
                }else{
                    Log.d("toCheck",n[j].tokenInfo());
                    Log.d("Enemy","+1");
                    points += 1;
                    //if(checkIfBlockingGap(toMove,n[j],hex)) {
                    if(game.getHive().checkIfGapBlocked(n[j])){
                        Log.d("blocking enemy","+5");
                        points += 5;
                        if (n[j].getType().equals(TokenType.BEE)) {
                            Log.d("Enemy bee","+10");
                            points += 10;
                        }
                    }
                }
            }
        }
        //Return token to its original position
        this.game.getHive().movetoken(toMove, currentPos);

        /*for(int j=0;j<this.game.getHive().getBoard().size();j++)
            Log.d("after checking",this.game.getHive().getBoard().get(j).tokenInfo());*/

        return points;
    }

    /**
     *
     * @param token
     * @return
     */
    private int evalToken(Token token){
        return 0;
    }

    /**
     *
     * @param toMove
     * @param toBlock
     * @param newPos
     * @return
     */
    public boolean checkIfBlockingGap(Token toMove, Token toBlock, Hex newPos){
        /*Log.d("Moving token",toMove.tokenInfo());
        Log.d("To",newPos.toString());
        Log.d("To check",toBlock.tokenInfo());*/
        //Blocked?
        boolean blocked = false;
        //The grasshoppers and the beetles can't be blocked
        //if(token.getType().equals(TokenType.GRASSHOPPER) || token.getType().equals(TokenType.BEETLE)) blocked=false;
        //Save actual token coordinates
        //Hex currentPos = new Hex(toMove.getCoordinates().getQ(),toMove.getCoordinates().getR(),toMove.getCoordinates().getD());
        //Change coordinates of the token to move
        //this.game.getHive().deleteHex(toMove);
        //this.game.getHive().updateCoordinates(toMove,newPos);
        //this.game.getHive().movetoken(toMove,newPos);

        //Check if the token to check is blocked
        if(game.getHive().checkIfGapBlocked(toBlock)) blocked = true;
        //Return token toMove to its original position
        //this.game.getHive().updateCoordinates(toMove,currentPos);
        //this.game.getHive().movetoken(toMove,currentPos);
        //PRINT BOARD

        Log.d("blocked?", String.valueOf(blocked));
        return blocked;
    }

    /**
     * Returns friendly tokens from a given token.
     * @param bee
     * @return
     */
    private ArrayList<Token> getFriends(Token bee) {
        ArrayList<Token> friends = new ArrayList<>();
        Token[] neighbours = new Token[6];
        neighbours = this.game.getHive().tokenNeighbours(bee.getCoordinates());
        for(int i=0;i<neighbours.length;i++){
            if(neighbours[i]!=null)
                if(neighbours[i].getPlayer().getColor().equals(this.player.getColor())) friends.add(neighbours[i]);
        }
        return friends;
    }

    /**
     * Tries to find a better position for the bee.
     * @param bee
     */
    private void moveBee(Token bee){
        Log.d("Moving bee","...");
        //Array to store the possible moves
        ArrayList<TokenMove> moves = new ArrayList<>();
        //Take possible moves for the bee:
        ArrayList<Hex> beeMoves = new ArrayList<>();
        beeMoves = this.game.getHive().getPossibleGaps(bee,false);
        //Inspect the gaps found --> They have less than 3 neighbours?
        for(int i=0;i<beeMoves.size();i++){
            //Get number neighbours of that gap
            int gaps = this.game.getHive().numberOfNeighbours(beeMoves.get(i));
            //If they have less than 3 neighbours --> inspect those neighbours
            if(gaps<3){
                int points = evalPosition(bee,beeMoves.get(i));
                Log.d("moving token",bee.tokenInfo());
                Log.d("To",beeMoves.get(i).toString());
                Log.d("points",String.valueOf(points));
                moves.add(new TokenMove(bee,beeMoves.get(i),points));
            }
        }
        if(!moves.isEmpty()){
            TokenMove bestmove = getBetterMove(moves);
            this.game.getHive().movetoken(bestmove.getToken(),bestmove.getHex());
            this.beeSaved = true;
            this.move = true;
        }
    }

    /**
     * Checks, if the token in the given coordinates gaps is blocked,
     * if moving the given token would unblock it.
     * @param token
     * @param toCheck
     * @return
     */
    private boolean checkIfUnblockingGap(Token token, Token toCheck){
        //Blocked?
        boolean unBlocked = false;
        //If the token in the given gap is already bloked
        if(game.getHive().checkIfGapBlocked(toCheck)) {
            Log.d("enemy token blocked","...");
            //Delete provisionally the given token from board
            //Save actual token coordinates
            Hex currentPos = new Hex(token.getCoordinates().getQ(),token.getCoordinates().getR(),token.getCoordinates().getD());
            //Change coordinates of the token to move
            //this.game.getHive().deleteHex(toMove);
            //this.game.getHive().updateCoordinates(token, new Hex(-100,-100,-100));
            this.game.getHive().movetoken(token,new Hex(-100,-100,-100));
            //Check if the token is now blocked
            if (!game.getHive().checkIfGapBlocked(toCheck)) unBlocked = true;
            //Return the token to the board
            //this.game.getHive().updateCoordinates(token,currentPos);
            this.game.getHive().movetoken(token,currentPos);
            //PRINT BOARD
            /*for (int i = 0; i < this.game.getHive().getBoard().size(); i++)
                Log.d("before checking", this.game.getHive().getBoard().get(i).getCoordinates().toString());*/
        }
        //PRINT BOARD
        /*for(int j=0;j<this.game.getHive().getBoard().size();j++)
            Log.d("after checking",this.game.getHive().getBoard().get(j).tokenInfo());*/
        Log.d("Unblocked",String.valueOf(unBlocked));
        return unBlocked;
    }


    /**
     * //Bring spiders early in the game: http://gen42.com/images/tipspage1.jpg
     * //Leave Grasshoppers to the last --> fill gaps http://gen42.com/images/tipspage1.jpg
     *
     * @return Token to place in the game.
     */
    public Token getToken(){
        Token token = new Token();
        //Get tokens in the box
        ArrayList<Token> tokens = new ArrayList<>();
        tokens = this.player.getTokensInTheBox();
        //Get possible gaps for the player
        ArrayList<Hex> gaps = new ArrayList<>();
        gaps = this.game.getHive().getPlayerGapsAvailable(this.player);
        //ArrayList to store Tokens and its moves
        ArrayList<TokenMove> moves = new ArrayList<>();
        for(int i=0;i<tokens.size();i++){
            ArrayList<TokenMove> tokenMovesPoints = new ArrayList<>();
            for(int j=0; j<gaps.size();j++){
                tokens.get(i).setCoordinates(gaps.get(j));
                Log.d("token", tokens.get(i).tokenInfo());
                Log.d("place", gaps.get(j).toString());
                //Analyse each pair (token/place) to guess the best move
                //First: get next moves
                ArrayList<Hex> tokenMoves = new ArrayList<>();
                //Add token to the board
                //this.game.getHive().addToken(tokens.get(i),gaps.get(j),false);
                /////
                //Add coordinate to token
                tokens.get(i).setCoordinates(gaps.get(j));
                //Place token on the board
                this.game.getHive().getBoard().add(tokens.get(i));
                //Delete gap from ArrayList of gaps available
                this.game.getHive().removeHexFromAvaliable(tokens.get(i).getCoordinates());
                /////
                //Get its moves
                tokenMoves = this.game.getHive().getPossibleGaps(tokens.get(i),true);
                Log.d("number of moves",String.valueOf(tokenMoves.size()));
                //Delete token from the board
                /////
                //Change token coordinate
                tokens.get(i).setCoordinates(new Hex(-100,-100,-100));
                //Remove token from board
                this.game.getHive().deteleToken(tokens.get(i));
                //Add gap to gaps Available
                this.game.getHive().getAvailableGaps().add(gaps.get(j));
                ////
                //Second: Analyse those moves
                for(int k=0;k<tokenMoves.size();k++){
                    int points = evalPosition(tokens.get(i),tokenMoves.get(k));
                    Log.d("token",tokens.get(i).tokenInfo());
                    Log.d("gap",gaps.get(j).toString());
                    Log.d("points",String.valueOf(points));
                    tokenMovesPoints.add(new TokenMove(tokens.get(i),gaps.get(j),points));
                }
            }
            //Third: Get better move for that token
            TokenMove bestMove = new TokenMove();
            bestMove = getBetterMove(tokenMovesPoints);
            moves.add(bestMove);
        }
        if(!moves.isEmpty()){
            //Get better move
            TokenMove bestToken = new TokenMove();
            bestToken = getBetterMove(moves);
            token = bestToken.getToken();
            token.setCoordinates(bestToken.getHex());
        }
        return token;
    }

    /**
     * Get opponents Tokens in game.
     * @return
     */
    public ArrayList<Token> getOpponentsTokens(){
        ArrayList<Token> tokens = new ArrayList<>();
        for(int i=0;i<game.getHive().getBoard().size();i++){
            if(game.getHive().getBoard().get(i).getPlayer().getColor().equals("White"))
                tokens.add(game.getHive().getBoard().get(i));
        }
        return tokens;
    }

    /**
     *
     * @param hex
     * @param list
     * @return
     */
    public boolean checksIfCoordinateInGivenList(Hex hex, ArrayList<Hex> list){
        for(int i=0;i<list.size();i++){
            if(list.get(i).toString().equals(hex.toString())) return true;
        }
        return false;
    }

    /**
     * Checks if two given lists has at least one element in common.
     * @param list1
     * @param list2
     * @return
     */
    public boolean getCommonFromLists(ArrayList<Hex> list1,ArrayList<Hex> list2){
        ArrayList<Hex> common = new ArrayList<>();
        for(int i=0;i<list1.size();i++){
            for(int j=0;j<list2.size();j++){
                if(list1.get(i).toString().equals(list2.toString())) return true;
            }
        }
        return false;
    }

    /**
     * Returns the empty gaps surrounding a bee
     * @param player
     * @return
     */
    public ArrayList<Hex> getBeeEmptyNeighbours(Player player){
        Token bee = new Token();
        bee = player.inspectTokenInGame(0);
        ArrayList<Hex> n = new ArrayList<>();
        Token[] nb = new Token[6];
        nb = game.getHive().tokenNeighbours(bee.getCoordinates());
        for(int i=0;i<nb.length;i++){
            if(nb[i]==null) n.add(nb[i].getCoordinates());
        }
        return n;
    }

    /**
     *
     * @param min
     * @param max
     * @return
     */
    public int getRandomPos(int min, int max){
        Random r = new Random();
        return r.nextInt(((max-min) + 1) + min);
    }

    /**
     *
     * @param moves
     * @return
     */
    public TokenMove getBetterMove(ArrayList<TokenMove> moves){
        int max = -100;
        TokenMove chosenOne = new TokenMove();
        for(int i=0;i<moves.size();i++){
            if(moves.get(i).getPoints()>max){
                max = moves.get(i).getPoints();
                chosenOne = moves.get(i);
            }
        }
        return chosenOne;
    }

}
