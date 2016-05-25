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

    public AI(Player player){
        this.player=player;
        //Opening vars
        this.random=0;
        this.randomOp=0;
        this.opening = new Hex[2];
        this.hex=new Hex();
        this.move=false;
    }

    /**
     * Using this method the AI will decide which will be it next move.
     * @param game
     */
    public void makeAChoice(Game game){
        this.move=false;
        this.game=game;
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
                beeStatus();
                //Rule #3: If the AI bee can't be saved then no move has been done --> try to attack the other player
                if(!this.move){
                    attackOpponent();
                }//else --> heurística?
                //If the AI didn't make any move --> Next player
                if(!this.move){
                    //Alert window? Next Player
                }
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
            game.getHive().addToken(t, hex, true);
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
                beeStatus();
                if(!this.move) attackOpponent();
            }
        }
    }

    /**
     * The first step the AI will take when deciding a move will be to check the bee status.
     * First: If the bee is blocked it will try to move one of his neighbours. If it can't --> bee not saved.
     * Second: If the bee is not blocked but surrounded by more than 2 tokens it will move the bee to a safest place if possible.
     * Third: If the bee is not blocked and surrounded by less than 3 tokens --> The bee is already save.
     */
    private void beeStatus(){
        Token bee = new Token();
        bee = this.player.inspectTokenInGame(0);
        //First: if the bee is blocked --> Try to move one of its friendly neighbours
        if(this.game.getHive().checkIfTokenBlocked(bee)){
            saveBee(bee);
        }else{
            //Second: If the bee is not blocked --> Get number of neighbours
            int n = game.getHive().numberOfNeighbours(bee.getCoordinates());
            //If the bee has more than 2 neighbours --> Move to a safer place.
            if(n>2){
                moveBee(bee);
            }
        }
    }

    /**
     * If the bee is blocked we can try to save it by moving one of his
     * neighbours if this one is a friend.
     * @param bee
     * @return
     */
    private void saveBee(Token bee){
        ArrayList<TokenMove> moves = new ArrayList<>();
        //Get friendly tokens
        ArrayList<Token> friends = new ArrayList<>();
        friends = getFriends(bee);
        if(!friends.isEmpty()){
            //Get possible moves for the friendly token
            for(int i=0;i<friends.size();i++){
                ArrayList<Hex> gaps = new ArrayList<>();
                gaps = this.game.getHive().getPossibleGaps(friends.get(i),false);
                //PRINT GAPS
                Log.d("Gaps for",friends.get(i).tokenInfo());
                for(int a=0;a<gaps.size();a++)
                    Log.d("gap...",gaps.get(a).toString());
                ////////////
                if(!gaps.isEmpty()){
                    //If gaps is not empty --> evaluate the move
                    for(int j=0;j<gaps.size();j++){
                        int points = evalPosition(friends.get(i),gaps.get(j));
                        moves.add(new TokenMove(friends.get(i), gaps.get(j), points));
                    }
                }
            }
        }
        if(!moves.isEmpty()){
            TokenMove bestmove = new TokenMove();
            bestmove = getBetterMove(moves);
            this.game.getHive().movetoken(bestmove.getToken(),bestmove.getHex(),false);
            this.move = true;
        }
    }

    /**
     * Tries to find a better position for the bee.
     * @param bee
     */
    private void moveBee(Token bee){
        //Array to store the possible moves
        ArrayList<TokenMove> moves = new ArrayList<>();
        //Take possible moves for the bee:
        ArrayList<Hex> beeMoves = new ArrayList<>();
        beeMoves = this.game.getHive().getPossibleGaps(bee, false);
        //PRINT GAPS
        Log.d("Gaps for",bee.tokenInfo());
        for(int a=0;a<beeMoves.size();a++)
            Log.d("gap...",beeMoves.get(a).toString());
        ////////////
        //Inspect the gaps found --> They have less than 3 neighbours?
        for(int i=0;i<beeMoves.size();i++){
            //Get number neighbours of that gap
            int gaps = this.game.getHive().numberOfNeighbours(beeMoves.get(i));
            //If they have less than 3 neighbours --> inspect those neighbours
            if(gaps<3){
                int points = evalPosition(bee,beeMoves.get(i));
                moves.add(new TokenMove(bee,beeMoves.get(i),points));
            }
        }
        if(!moves.isEmpty()){
            TokenMove bestmove = getBetterMove(moves);
            this.game.getHive().movetoken(bestmove.getToken(),bestmove.getHex(),false);
            this.move = true;
        }
    }

    /**
     * Attacks enemy following some rules order by priority of good moves.
     * The order of attack will be:
     * First: Move a token already in the game by besting its current score.
     * Second: If any token in the game can best its current position --> Add new token.
     * Third: If the AI can't take any of the two last decisions --> WHAT TO DO??? NEXT PLAYER
     */
    private void attackOpponent() {
        //Array to store the possible moves
        ArrayList<TokenMove> bestMoves = new ArrayList<>();
        //First: Check if a token already in the game can be place in a better position
        bestMoves = moveTokenScores();
        //Evaluate the points of the best move
        if(!bestMoves.isEmpty()){
            TokenMove bestmove = getBetterMove(bestMoves);
            this.game.getHive().movetoken(bestmove.getToken(), bestmove.getHex(),false);
            this.move = true;
        }
        //Second: If the tokens in the game can't be in a better position --> Add new ones
        if(!this.move && !this.game.getHive().getPlayerGapsAvailable(this.player).isEmpty()){
            Token newToken = new Token();
            //Takes a token from the box
            newToken = getToken();
            //Add token to the board
            this.game.getHive().addToken(newToken,newToken.getCoordinates(),true);
            this.move=true;
        }else{
            //Third: No tokens to add... NEXT PLAYER?
        }

    }

    /**
     * Determines the score for the possible moves that each token in the game can make.
     * If the token is already blocking the enemy's bee --> Do nothing.
     * If the token can't best its current score --> Do nothing.
     * @return list of possible moves and its score.
     */
    private ArrayList<TokenMove> moveTokenScores(){
        //Array to store the possible moves
        ArrayList<TokenMove> bestMoves = new ArrayList<>();
        boolean tokenBlocking = false;
        for(int i=0;i<player.getTokensInGame().size();i++){
            //Array to store the moves for the token
            ArrayList<TokenMove> moves = new ArrayList<>();
            //If the token can't be moved --> Do nothing
            ArrayList<Hex> tokenMoves = new ArrayList<>();
            tokenMoves = this.game.getHive().getPossibleGaps(player.getTokensInGame().get(i),false);
            //PRINT GAPS
            Log.d("Gaps for",player.getTokensInGame().get(i).tokenInfo());
            for(int a=0;a<tokenMoves.size();a++)
                Log.d("gap...",tokenMoves.get(a).toString());
            ////////////
            if(!tokenMoves.isEmpty()) {
                //If the token is already blocking the enemy's bee --> Do nothing
                Token[] n = new Token[6];
                n = game.getHive().tokenNeighbours(player.getTokensInGame().get(i).getCoordinates());
                for (int j = 0; j < n.length; j++) {
                    if (n[j] != null) {
                        if (n[j].getType().equals(TokenType.BEE) && n[j].getPlayer().getColor().equals("White")) {
                            if (checkIfUnblockingGap(player.getTokensInGame().get(i), n[j])) {
                                tokenBlocking = true;
                            }
                        }
                    }
                }
                if (!tokenBlocking) {
                    //Evaluate each move
                    for (int j = 0; j < tokenMoves.size(); j++) {
                        int points = evalPosition(player.getTokensInGame().get(i), tokenMoves.get(j));
                        moves.add(new TokenMove(player.getTokensInGame().get(i), tokenMoves.get(j), points));
                    }
                    //Check if it's current position is better than the new one
                    int current = evalPosition(player.getTokensInGame().get(i),player.getTokensInGame().get(i).getCoordinates());
                    TokenMove bestMoveToken = getBetterMove(moves);
                    int bestMove = bestMoveToken.getPoints();
                    if(bestMove>current) bestMoves.add(bestMoveToken);
                }
            }
        }
        return bestMoves;
    }

    /**
     * Evaluate a position that a token can make.
     * Points: If the neighbour is an friend: -1 (CHANGE)
     *         If the move could block the token: -5
     *         If the token is the AI bee: -10
     *         If its an enemy token: +1 (CHANGE)
     *         If the move could block the enemy token: +5
     *         If the token is the enemy's bee: +10
     * @param hex
     * @return
     */
    private int evalPosition(Token toMove,Hex hex) {

        Log.d("-----------------------","-----------------------");
        Log.d("Token", toMove.tokenInfo());
        Log.d("To move", hex.toString());

        //Save current token position
        Hex currentPos = new Hex(toMove.getCoordinates().getQ(),toMove.getCoordinates().getR(),toMove.getCoordinates().getD());
        //Move token to the position to evaluate
        this.game.getHive().movetoken(toMove,hex,true);

        int points = 0;
        Token[] n = new Token[6];
        n = this.game.getHive().tokenNeighbours(hex);
        for(int j=0;j<n.length;j++){
            if(n[j]!=null){
                if(n[j].getPlayer().getColor().equals(this.player.getColor())){
                    points -= evalToken(n[j]);
                    Log.d("for the friendly token",n[j].getType().toString());
                    Log.d("Points",String.valueOf(points));
                    if(game.getHive().checkIfTokenBlocked(n[j])){
                        points *= 2;
                        Log.d("Token blocked",String.valueOf(points));
                    }
                }else{
                    points += evalToken(n[j]);
                    Log.d("for the enemy token",n[j].getType().toString());
                    Log.d("Points",String.valueOf(points));
                    if(game.getHive().checkIfTokenBlocked(n[j])){
                        points *= 2;
                        Log.d("Token blocked",String.valueOf(points));
                    }
                }
            }
        }
        //Return token to its original position
        this.game.getHive().movetoken(toMove, currentPos, true);

        Log.d("Total Points", String.valueOf(points));
        Log.d("-----------------------","-----------------------");

        return points;
    }

    /**
     * Calculates the value of the given token.
     * @param token
     * @return
     */
    private int evalToken(Token token){
        switch (token.getType()){
            case BEE: return 20;
            case ANT: return 8;
            case SPIDER: return 6;
            case BEETLE: return 4;
            case GRASSHOPPER: return 2;
            default: return 0;
        }
    }

    /**
     * Returns friendly tokens from a given token.
     * @param token
     * @return
     */
    private ArrayList<Token> getFriends(Token token) {
        ArrayList<Token> friends = new ArrayList<>();
        Token[] neighbours = new Token[6];
        neighbours = this.game.getHive().tokenNeighbours(token.getCoordinates());
        for(int i=0;i<neighbours.length;i++){
            if(neighbours[i]!=null)
                if(neighbours[i].getPlayer().getColor().equals(this.player.getColor())) friends.add(neighbours[i]);
        }
        return friends;
    }

    /**
     * Checks, if the token in the given coordinates gaps is blocked,
     * and if moving the given token would unblock it.
     * @param token
     * @param toCheck
     * @return
     */
    private boolean checkIfUnblockingGap(Token token, Token toCheck){
        //Blocked?
        boolean unBlocked = false;
        //If the token in the given gap is already bloked
        if(game.getHive().checkIfTokenBlocked(toCheck)) {
            //Save actual token coordinates
            Hex currentPos = new Hex(token.getCoordinates().getQ(),token.getCoordinates().getR(),token.getCoordinates().getD());
            //Change coordinates of the token to move
            this.game.getHive().movetoken(token,new Hex(-100,-100,-100),true);
            //Check if the token is now blocked
            if (!game.getHive().checkIfTokenBlocked(toCheck)) unBlocked = true;
            //Return the token to the board
            this.game.getHive().movetoken(token,currentPos,true);
        }
        return unBlocked;
    }


    /**
     *
     *
     * @return Token to place in the game.
     */
    public Token getToken(){

        Log.d("Turn",String.valueOf(this.player.getTurn()));

        Token token = new Token();
        //Get tokens in the box
        ArrayList<Token> tokens = new ArrayList<>();
        tokens = this.player.getTokensInTheBox();

        //ArrayList to store Tokens and its moves
        ArrayList<TokenMove> moves = new ArrayList<>();

        //If the opening used was the first one, the AI has a spider in the box
        //Following a tip from the authors game we will bring the spiders early in the game: http://gen42.com/images/tipspage1.jpg
        if(this.randomOp==0){
            token = this.player.takeTokenFromTheBox(5);
            //Work out the best gap to place the spider
            Log.d("Token",token.tokenInfo());
            moves = addTokenScores(token);
        }else if(this.player.getTurn()<=12){
            //Take beetles or ants
            int type = getRandomPos(0,1);
            if(type==0){
                //Take beetle
                if(this.player.isTokenInBox(TokenType.BEETLE))
                    token = this.player.takeToken(TokenType.BEETLE.toString());
                else
                    token = this.player.takeToken(TokenType.ANT.toString());
            }else{
                //Take ant
                if(this.player.isTokenInBox(TokenType.ANT))
                    token = this.player.takeToken(TokenType.ANT.toString());
                else
                    token = this.player.takeToken(TokenType.BEETLE.toString());
            }
            Log.d("Token",token.tokenInfo());
            moves = addTokenScores(token);
        }else if(this.player.getTurn()>12){
            //Keep hoppers in reserve for end moves
            //http://www.gen42.com/images/tipspage1.jpg
            token = this.player.takeToken(TokenType.GRASSHOPPER.toString());
            Log.d("Token",token.tokenInfo());
            moves = addTokenScores(token);
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
     *
     * @param token
     * @return
     */
    private ArrayList<TokenMove> addTokenScores(Token token){

        //Get possible gaps for the player
        ArrayList<Hex> gaps = new ArrayList<>();
        gaps = this.game.getHive().getPlayerGapsAvailable(this.player);
        //ArrayList to store Tokens and its moves
        ArrayList<TokenMove> moves = new ArrayList<>();

        for(int i=0;i<gaps.size();i++){
            //Place token in that gap
            this.game.getHive().addToken(token,gaps.get(i),true);
            //Get possible moves from that position
            ArrayList<Hex> possibleMoves = new ArrayList<>();
            possibleMoves = this.game.getHive().getPossibleGaps(token,true);
            //PRINT GAPS
            Log.d("Gaps for",token.tokenInfo());
            for(int a=0;a<possibleMoves.size();a++)
                Log.d("gap...",possibleMoves.get(a).toString());
            ////////////
            //Evaluate those moves
            for(int j=0;j<possibleMoves.size();j++){
                int points = evalPosition(token,possibleMoves.get(j));
                moves.add(new TokenMove(token,gaps.get(i),points));
            }
            //Delete token from the board
            this.game.getHive().deteleToken(token);
        }

        return moves;
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
