package lucgilr.inf.uva.hivev2.AI;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hexagon;
import lucgilr.inf.uva.hivev2.GameModel.Piece;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.GameModel.PieceType;

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
    Hexagon[] opening;
    Hexagon hexagon;
    //To check if the AI has already move
    boolean move;

    public AI(Player player){
        this.player=player;
        //Opening vars
        this.random=0;
        this.randomOp=0;
        this.opening = new Hexagon[2];
        this.hexagon =new Hexagon();
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
            int size = game.getHive().getAvailableHexagonsPlayer(player).size();
            //Generate a random number (To choose a random position)
            this.random = rand.nextInt((size - 1) + 1);
            //Place spider (id=4)
            Piece t = new Piece();
            t = player.takePieceFromTheBox(4);
            this.hexagon = game.getHive().getAvailableHexagonsPlayer(player).get(random);
            game.getHive().addPiece(t, hexagon, true);
        }else if(player.getTurn()==2){
            this.random = rand.nextBoolean() ? 0:1;
            //Place Ant (id=8)
            Piece t = new Piece();
            t = player.takePieceFromTheBox(8);
            this.opening = game.getHive().vHexagons(hexagon);
            this.hexagon = opening[random];
            game.getHive().addPiece(t, hexagon, true);
        }else if(player.getTurn()==3){
            //Place Bee (id=0)
            Piece t = new Piece();
            t = player.takePieceFromTheBox(0);
            if(random==0){
                hexagon = opening[1];
            }else{
                hexagon = opening[0];
            }
            //first: check if there is already a token in that gap
            if(!game.getHive().checkIfHexagonTaken(this.hexagon))
                game.getHive().addPiece(t, this.hexagon, true);
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
            int size = game.getHive().getAvailableHexagonsPlayer(player).size();
            //Generate a random number (To choose a random position)
            this.random = rand.nextInt((size - 1) + 1);
            //Place Bee (id=0)
            Piece t = new Piece();
            t = player.takePieceFromTheBox(0);
            this.hexagon = game.getHive().getAvailableHexagonsPlayer(player).get(random);
            game.getHive().addPiece(t, hexagon, true);
        }else if(player.getTurn()==2){
            this.random = rand.nextBoolean() ? 0:1;
            //Place Spider (id=4)
            Piece t = new Piece();
            t = player.takePieceFromTheBox(4);
            this.opening = game.getHive().vHexagons(hexagon);
            this.hexagon = opening[random];
            game.getHive().addPiece(t, hexagon, true);
        }else if(player.getTurn()==3){
            //Place Spider (id=5)
            Piece t = new Piece();
            t = player.takePieceFromTheBox(5);
            if(random==0){
                hexagon = opening[1];
            }else{
                hexagon = opening[0];
            }
            //first: check if there is already a token in that gap
            if(!game.getHive().checkIfHexagonTaken(this.hexagon))
                game.getHive().addPiece(t, this.hexagon, true);
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
        Piece bee = new Piece();
        bee = this.player.inspectPieceInGame(0);
        //First: if the bee is blocked --> Try to move one of its friendly neighbours
        if(this.game.getHive().checkIfPieceBlocked(bee)){
            saveBee(bee);
        }else{
            //Second: If the bee is not blocked --> Get number of neighbours
            int n = game.getHive().numberOfNeighbours(bee.getHexagon());
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
    private void saveBee(Piece bee){
        ArrayList<PieceMoveScore> moves = new ArrayList<>();
        //Get friendly tokens
        ArrayList<Piece> friends = new ArrayList<>();
        friends = getFriends(bee);
        if(!friends.isEmpty()){
            //Get possible moves for the friendly token
            for(int i=0;i<friends.size();i++){
                ArrayList<Hexagon> gaps = new ArrayList<>();
                gaps = this.game.getHive().getPossibleHexagon(friends.get(i), false);
                //PRINT GAPS
                Log.d("Gaps for",friends.get(i).pieceInfo());
                for(int a=0;a<gaps.size();a++)
                    Log.d("gap...",gaps.get(a).toString());
                ////////////
                if(!gaps.isEmpty()){
                    //If gaps is not empty --> evaluate the move
                    for(int j=0;j<gaps.size();j++){
                        int points = evalPosition(friends.get(i),gaps.get(j));
                        moves.add(new PieceMoveScore(friends.get(i), gaps.get(j), points));
                    }
                }
            }
        }
        if(!moves.isEmpty()){
            PieceMoveScore bestmove = new PieceMoveScore();
            bestmove = getBetterMove(moves);
            this.game.getHive().movePiece(bestmove.getPiece(), bestmove.getHexagon(), false);
            this.move = true;
        }
    }

    /**
     * Tries to find a better position for the bee.
     * @param bee
     */
    private void moveBee(Piece bee){
        //Array to store the possible moves
        ArrayList<PieceMoveScore> moves = new ArrayList<>();
        //Take possible moves for the bee:
        ArrayList<Hexagon> beeMoves = new ArrayList<>();
        beeMoves = this.game.getHive().getPossibleHexagon(bee, false);
        //PRINT GAPS
        Log.d("Gaps for",bee.pieceInfo());
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
                moves.add(new PieceMoveScore(bee,beeMoves.get(i),points));
            }
        }
        if(!moves.isEmpty()){
            PieceMoveScore bestmove = getBetterMove(moves);
            this.game.getHive().movePiece(bestmove.getPiece(), bestmove.getHexagon(), false);
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
        ArrayList<PieceMoveScore> bestMoves = new ArrayList<>();
        //First: Check if a token already in the game can be place in a better position
        bestMoves = moveTokenScores();
        //Evaluate the points of the best move
        if(!bestMoves.isEmpty()){
            PieceMoveScore bestmove = getBetterMove(bestMoves);
            this.game.getHive().movePiece(bestmove.getPiece(), bestmove.getHexagon(), false);
            this.move = true;
        }
        //Second: If the tokens in the game can't be in a better position --> Add new ones
        if(!this.move && !this.game.getHive().getAvailableHexagonsPlayer(this.player).isEmpty()){
            Piece newPiece = new Piece();
            //Takes a token from the box
            newPiece = getToken();
            //Add token to the board
            this.game.getHive().addPiece(newPiece, newPiece.getHexagon(), true);
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
    private ArrayList<PieceMoveScore> moveTokenScores(){
        //Array to store the possible moves
        ArrayList<PieceMoveScore> bestMoves = new ArrayList<>();
        boolean tokenBlocking = false;
        for(int i=0;i<player.getPiecesInGame().size();i++){
            //Array to store the moves for the token
            ArrayList<PieceMoveScore> moves = new ArrayList<>();
            //If the token can't be moved --> Do nothing
            ArrayList<Hexagon> tokenMoves = new ArrayList<>();
            tokenMoves = this.game.getHive().getPossibleHexagon(player.getPiecesInGame().get(i), false);
            //PRINT GAPS
            Log.d("Gaps for",player.getPiecesInGame().get(i).pieceInfo());
            for(int a=0;a<tokenMoves.size();a++)
                Log.d("gap...",tokenMoves.get(a).toString());
            ////////////
            if(!tokenMoves.isEmpty()) {
                //If the token is already blocking the enemy's bee --> Do nothing
                Piece[] n = new Piece[6];
                n = game.getHive().pieceNeighbours(player.getPiecesInGame().get(i).getHexagon());
                for (int j = 0; j < n.length; j++) {
                    if (n[j] != null) {
                        if (n[j].getType().equals(PieceType.BEE) && n[j].getPlayer().getColor().equals("White")) {
                            if (checkIfUnblockingGap(player.getPiecesInGame().get(i), n[j])) {
                                tokenBlocking = true;
                            }
                        }
                    }
                }
                if (!tokenBlocking) {
                    //Evaluate each move
                    for (int j = 0; j < tokenMoves.size(); j++) {
                        int points = evalPosition(player.getPiecesInGame().get(i), tokenMoves.get(j));
                        moves.add(new PieceMoveScore(player.getPiecesInGame().get(i), tokenMoves.get(j), points));
                    }
                    //Check if it's current position is better than the new one
                    int current = evalPosition(player.getPiecesInGame().get(i),player.getPiecesInGame().get(i).getHexagon());
                    PieceMoveScore bestMoveToken = getBetterMove(moves);
                    int bestMove = bestMoveToken.getScore();
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
     * @param hexagon
     * @return
     */
    private int evalPosition(Piece toMove,Hexagon hexagon) {

        Log.d("-----------------------","-----------------------");
        Log.d("Piece", toMove.pieceInfo());
        Log.d("To move", hexagon.toString());

        //Save current token position
        Hexagon currentPos = new Hexagon(toMove.getHexagon().getQ(),toMove.getHexagon().getR(),toMove.getHexagon().getD());
        //Move token to the position to evaluate
        this.game.getHive().movePiece(toMove, hexagon, true);

        int points = 0;
        Piece[] n = new Piece[6];
        n = this.game.getHive().pieceNeighbours(hexagon);
        for(int j=0;j<n.length;j++){
            if(n[j]!=null){
                if(n[j].getPlayer().getColor().equals(this.player.getColor())){
                    points -= evalToken(n[j]);
                    Log.d("for the friendly token",n[j].getType().toString());
                    Log.d("Points",String.valueOf(points));
                    if(game.getHive().checkIfPieceBlocked(n[j])){
                        points *= 2;
                        Log.d("Piece blocked",String.valueOf(points));
                    }
                }else{
                    points += evalToken(n[j]);
                    Log.d("for the enemy token",n[j].getType().toString());
                    Log.d("Points",String.valueOf(points));
                    if(game.getHive().checkIfPieceBlocked(n[j])){
                        points *= 2;
                        Log.d("Piece blocked",String.valueOf(points));
                    }
                }
            }
        }
        //Return token to its original position
        this.game.getHive().movePiece(toMove, currentPos, true);

        Log.d("Total Points", String.valueOf(points));
        Log.d("-----------------------","-----------------------");

        return points;
    }

    /**
     * Calculates the value of the given piece.
     * @param piece
     * @return
     */
    private int evalToken(Piece piece){
        switch (piece.getType()){
            case BEE: return 20;
            case ANT: return 8;
            case SPIDER: return 6;
            case BEETLE: return 4;
            case GRASSHOPPER: return 2;
            default: return 0;
        }
    }

    /**
     * Returns friendly tokens from a given piece.
     * @param piece
     * @return
     */
    private ArrayList<Piece> getFriends(Piece piece) {
        ArrayList<Piece> friends = new ArrayList<>();
        Piece[] neighbours = new Piece[6];
        neighbours = this.game.getHive().pieceNeighbours(piece.getHexagon());
        for(int i=0;i<neighbours.length;i++){
            if(neighbours[i]!=null)
                if(neighbours[i].getPlayer().getColor().equals(this.player.getColor())) friends.add(neighbours[i]);
        }
        return friends;
    }

    /**
     * Checks, if the piece in the given coordinates gaps is blocked,
     * and if moving the given piece would unblock it.
     * @param piece
     * @param toCheck
     * @return
     */
    private boolean checkIfUnblockingGap(Piece piece, Piece toCheck){
        //Blocked?
        boolean unBlocked = false;
        //If the piece in the given gap is already bloked
        if(game.getHive().checkIfPieceBlocked(toCheck)) {
            //Save actual piece coordinates
            Hexagon currentPos = new Hexagon(piece.getHexagon().getQ(), piece.getHexagon().getR(), piece.getHexagon().getD());
            //Change coordinates of the piece to move
            this.game.getHive().movePiece(piece, new Hexagon(-100, -100, -100), true);
            //Check if the piece is now blocked
            if (!game.getHive().checkIfPieceBlocked(toCheck)) unBlocked = true;
            //Return the piece to the board
            this.game.getHive().movePiece(piece, currentPos, true);
        }
        return unBlocked;
    }


    /**
     *
     *
     * @return Piece to place in the game.
     */
    public Piece getToken(){

        Log.d("Turn",String.valueOf(this.player.getTurn()));

        Piece piece = new Piece();
        //Get pieces in the box
        ArrayList<Piece> pieces = new ArrayList<>();
        pieces = this.player.getPiecesInTheBox();

        //ArrayList to store Tokens and its moves
        ArrayList<PieceMoveScore> moves = new ArrayList<>();

        //If the opening used was the first one, the AI has a spider in the box
        //Following a tip from the authors game we will bring the spiders early in the game: http://gen42.com/images/tipspage1.jpg
        if(this.randomOp==0){
            piece = this.player.takePieceFromTheBox(5);
            //Work out the best gap to place the spider
            Log.d("Piece", piece.pieceInfo());
            moves = addTokenScores(piece);
        }else if(this.player.getTurn()<=12){
            //Take beetles or ants
            int type = getRandomPos(0,1);
            if(type==0){
                //Take beetle
                if(this.player.isPieceInBox(PieceType.BEETLE))
                    piece = this.player.takePieceByType(PieceType.BEETLE);
                else
                    piece = this.player.takePieceByType(PieceType.ANT);
            }else{
                //Take ant
                if(this.player.isPieceInBox(PieceType.ANT))
                    piece = this.player.takePieceByType(PieceType.ANT);
                else
                    piece = this.player.takePieceByType(PieceType.BEETLE);
            }
            Log.d("Piece", piece.pieceInfo());
            moves = addTokenScores(piece);
        }else if(this.player.getTurn()>12){
            //Keep hoppers in reserve for end moves
            //http://www.gen42.com/images/tipspage1.jpg
            piece = this.player.takePieceByType(PieceType.GRASSHOPPER);
            Log.d("Piece", piece.pieceInfo());
            moves = addTokenScores(piece);
        }
        if(!moves.isEmpty()){
            //Get better move
            PieceMoveScore bestToken = new PieceMoveScore();
            bestToken = getBetterMove(moves);
            piece = bestToken.getPiece();
            piece.setHexagon(bestToken.getHexagon());
        }

        return piece;
    }

    /**
     *
     * @param piece
     * @return
     */
    private ArrayList<PieceMoveScore> addTokenScores(Piece piece){

        //Get possible gaps for the player
        ArrayList<Hexagon> gaps = new ArrayList<>();
        gaps = this.game.getHive().getAvailableHexagonsPlayer(this.player);
        //ArrayList to store Tokens and its moves
        ArrayList<PieceMoveScore> moves = new ArrayList<>();

        for(int i=0;i<gaps.size();i++){
            //Place piece in that gap
            this.game.getHive().addPiece(piece, gaps.get(i), true);
            //Get possible moves from that position
            ArrayList<Hexagon> possibleMoves = new ArrayList<>();
            possibleMoves = this.game.getHive().getPossibleHexagon(piece, true);
            //PRINT GAPS
            Log.d("Gaps for", piece.pieceInfo());
            for(int a=0;a<possibleMoves.size();a++)
                Log.d("gap...",possibleMoves.get(a).toString());
            ////////////
            //Evaluate those moves
            for(int j=0;j<possibleMoves.size();j++){
                int points = evalPosition(piece,possibleMoves.get(j));
                moves.add(new PieceMoveScore(piece,gaps.get(i),points));
            }
            //Delete piece from the board
            this.game.getHive().detelePiece(piece);
        }

        return moves;
    }

    /**
     * Get opponents Tokens in game.
     * @return
     */
    public ArrayList<Piece> getOpponentsTokens(){
        ArrayList<Piece> pieces = new ArrayList<>();
        for(int i=0;i<game.getHive().getBoard().size();i++){
            if(game.getHive().getBoard().get(i).getPlayer().getColor().equals("White"))
                pieces.add(game.getHive().getBoard().get(i));
        }
        return pieces;
    }

    /**
     *
     * @param hexagon
     * @param list
     * @return
     */
    public boolean checksIfCoordinateInGivenList(Hexagon hexagon, ArrayList<Hexagon> list){
        for(int i=0;i<list.size();i++){
            if(list.get(i).toString().equals(hexagon.toString())) return true;
        }
        return false;
    }

    /**
     * Checks if two given lists has at least one element in common.
     * @param list1
     * @param list2
     * @return
     */
    public boolean getCommonFromLists(ArrayList<Hexagon> list1,ArrayList<Hexagon> list2){
        ArrayList<Hexagon> common = new ArrayList<>();
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
    public ArrayList<Hexagon> getBeeEmptyNeighbours(Player player){
        Piece bee = new Piece();
        bee = player.inspectPieceInGame(0);
        ArrayList<Hexagon> n = new ArrayList<>();
        Piece[] nb = new Piece[6];
        nb = game.getHive().pieceNeighbours(bee.getHexagon());
        for(int i=0;i<nb.length;i++){
            if(nb[i]==null) n.add(nb[i].getHexagon());
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
    public PieceMoveScore getBetterMove(ArrayList<PieceMoveScore> moves){
        int max = -100;
        PieceMoveScore chosenOne = new PieceMoveScore();
        for(int i=0;i<moves.size();i++){
            if(moves.get(i).getScore()>max){
                max = moves.get(i).getScore();
                chosenOne = moves.get(i);
            }
        }
        return chosenOne;
    }

}
