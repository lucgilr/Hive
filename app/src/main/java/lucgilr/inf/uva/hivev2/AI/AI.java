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
 *
 * Created by Lucía Gil Román on 11/05/16.
 */
public class AI{

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
    //To check if second spider from opening one in game
    boolean spider;

    public AI(Player player){
        this.player=player;
        //Opening vars
        this.random=0;
        this.randomOp=0;
        this.opening = new Hexagon[2];
        this.hexagon =new Hexagon();
        this.move=false;
        this.spider=false;
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
            //t = player.inspectPieceByIdFromBox(4);
            t = player.inspectPieceFromBox(PieceType.SPIDER);
            this.hexagon = game.getHive().getAvailableHexagonsPlayer(player).get(random);
            game.getHive().addPiece(t, hexagon);
        }else if(player.getTurn()==2){
            this.random = rand.nextBoolean() ? 0:1;
            //Place Ant (id=8)
            Piece t = new Piece();
            //t = player.inspectPieceByIdFromBox(8);
            t = player.inspectPieceFromBox(PieceType.ANT);
            this.opening = game.getHive().vHexagons(hexagon);
            this.hexagon = opening[random];
            game.getHive().addPiece(t, hexagon);
        }else if(player.getTurn()==3){
            //Place Bee (id=0)
            Piece t = new Piece();
            //t = player.inspectPieceByIdFromBox(0);
            t = player.inspectPieceFromBox(PieceType.BEE);
            if(random==0){
                hexagon = opening[1];
            }else{
                hexagon = opening[0];
            }
            //first: check if there is already a piece in that gap
            if(!game.getHive().checkIfHexagonTaken(this.hexagon))
                game.getHive().addPiece(t, this.hexagon);
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
            //t = player.inspectPieceByIdFromBox(0);
            t = player.inspectPieceFromBox(PieceType.BEE);
            this.hexagon = game.getHive().getAvailableHexagonsPlayer(player).get(random);
            game.getHive().addPiece(t, hexagon);
        }else if(player.getTurn()==2){
            this.random = rand.nextBoolean() ? 0:1;
            //Place Spider (id=4)
            Piece t = new Piece();
            //t = player.inspectPieceByIdFromBox(4);
            t = player.inspectPieceFromBox(PieceType.SPIDER);
            this.opening = game.getHive().vHexagons(hexagon);
            this.hexagon = opening[random];
            game.getHive().addPiece(t, hexagon);
        }else if(player.getTurn()==3){
            //Place Spider (id=5)
            Piece t = new Piece();
            //t = player.inspectPieceByIdFromBox(5);
            t = player.inspectPieceFromBox(PieceType.SPIDER);
            if(random==0){
                hexagon = opening[1];
            }else{
                hexagon = opening[0];
            }
            //first: check if there is already a piece in that gap
            if(!game.getHive().checkIfHexagonTaken(this.hexagon))
                game.getHive().addPiece(t, this.hexagon);
            else{
                beeStatus();
                if(!this.move) attackOpponent();
            }
        }
    }

    /**
     * The first step the AI will take when deciding a move will be to check the bee status.
     * First: If the bee is blocked it will try to move one of his neighbours. If it can't --> bee not saved.
     * Second: If the bee is not blocked but surrounded by more than 2 pieces it will move the bee to a safest place if possible.
     * Third: If the bee is not blocked and surrounded by less than 3 pieces --> The bee is already save.
     */
    private void beeStatus(){
        Piece bee = new Piece();
        //bee = this.player.inspectPieceById(0);
        bee = this.player.inspectPiece(PieceType.BEE);
        //First: if the bee is blocked --> Try to move one of its friendly neighbours
        if(this.game.getHive().checkIfPieceBlocked(bee)){
            saveBee(bee);
        }else{
            //Second: If the bee is not blocked --> Get number of neighbours
            int n = game.getHive().numberOfNeighbours(bee.getHexagon());
            //Get enemy's bee
            Piece enemy = new Piece();
            //enemy = this.game.getPlayer1().inspectPieceById(0);
            enemy = this.game.getPlayer1().inspectPiece(PieceType.BEE);
            //If the enemy's bee is in game --> get neighbours
            int n2=0;
            if(enemy.isInGame()){ n2 = game.getHive().numberOfNeighbours(enemy.getHexagon());}
            //If enemy' bee in game
            if(n2!=0) {
                //If the bee has more than 2 neighbours and the enemy's bee has less than 4 --> Move to a safer place.
                if (n > 2 && n2<4) {
                    moveBee(bee);
                }
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
        Log.d("SAVING BEE!","YES");
        ArrayList<PieceMoveScore> moves = new ArrayList<>();
        //Get friendly pieces
        ArrayList<Piece> friends = new ArrayList<>();
        friends = getFriends(bee);
        if(!friends.isEmpty()){
            //Get possible moves for the friendly pieces
            for(int i=0;i<friends.size();i++){
                ArrayList<Hexagon> gaps = new ArrayList<>();
                gaps = this.game.getHive().getPossibleHexagons(friends.get(i), false);
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
        Log.d("MOVING BEE!","YES!");
        //Array to store the possible moves
        ArrayList<PieceMoveScore> moves = new ArrayList<>();
        //Take possible moves for the bee:
        ArrayList<Hexagon> beeMoves = new ArrayList<>();
        beeMoves = this.game.getHive().getPossibleHexagons(bee, false);
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
     * First: Move a piece already in the game by besting its current score.
     * Second: If any piece in the game can best its current position --> Add new piece.
     * Third: If the AI can't take any of the two last decisions --> WHAT TO DO??? NEXT PLAYER
     */
    private void attackOpponent() {
        //Array to store the possible moves
        ArrayList<PieceMoveScore> bestMoves = new ArrayList<>();
        //First: Check if a piece is already in the game can be place in a better position
        bestMoves = movePieceScores(true);
        //Evaluate the points of the best move
        if(!bestMoves.isEmpty()){
            PieceMoveScore bestmove = getBetterMove(bestMoves);
            this.game.getHive().movePiece(bestmove.getPiece(), bestmove.getHexagon(), false);
            this.move = true;
        }
        //Second: If the pieces in the game can't be in a better position --> Add new ones
        if(!this.move && !this.game.getHive().getAvailableHexagonsPlayer(this.player).isEmpty()){
            Piece newPiece = new Piece();
            //Takes a piece from the box
            newPiece = getPiece();
            //Add piece to the board
            this.game.getHive().addPiece(newPiece, newPiece.getHexagon());
            this.move=true;
        }else{
            //Third: No pieces to add...
            //If the player can move a piece --> move it
            bestMoves = movePieceScores(false);
            if(!bestMoves.isEmpty()){
                PieceMoveScore bestMove = getBetterMove(bestMoves);
                this.game.getHive().movePiece(bestMove.getPiece(), bestMove.getHexagon(), false);
                this.move = true;
            }else {
                //If not possible moves either --> next player
                Log.d("NEXT PLAYER!!!","NEXT PLAYER!!!");
            }

        }

    }

    /**
     * Determines the score for the possible moves that each piece in the game can make.
     * If the piece is already blocking the enemy's bee --> Do nothing.
     * If the piece can't best its current score --> Do nothing.
     * @return list of possible moves and its score.
     */
    private ArrayList<PieceMoveScore> movePieceScores(boolean evalCurrent){
        //Array to store the possible moves
        ArrayList<PieceMoveScore> bestMoves = new ArrayList<>();
        boolean pieceBlocking = false;
        for(int i=0;i<player.getPiecesInGame().size();i++){
            Log.d("EVALUATING PIECE",player.getPiecesInGame().get(i).pieceInfo());
            //Array to store the moves for the piece
            ArrayList<PieceMoveScore> moves = new ArrayList<>();
            //If the piece can't be moved --> Do nothing
            ArrayList<Hexagon> pieceMoves = new ArrayList<>();
            pieceMoves = this.game.getHive().getPossibleHexagons(player.getPiecesInGame().get(i), false);
            Log.d("MOVES FOR THE PIECE",String.valueOf(pieceMoves.size()));
            if(!pieceMoves.isEmpty()) {
                Piece[] n = new Piece[6];
                n = game.getHive().pieceNeighbours(player.getPiecesInGame().get(i).getHexagon());
                for (int j = 0; j < n.length; j++) {
                    if (n[j] != null) {
                        if (n[j].getType().equals(PieceType.BEE) && n[j].getPlayer().getColor().equals("White")) {
                            //If the piece is already blocking the enemy's bee --> Do nothing
                            /*if (checkIfUnblockingGap(player.getPiecesInGame().get(i), n[j])) {
                                pieceBlocking = true;
                            }*/
                            //If the piece is already touching enemy's bee
                            Log.d("IS TOUCHING ENEMY BEE","YES!");
                            pieceBlocking=true;
                        }
                    }
                }
                if (!pieceBlocking) {
                    //Evaluate each move
                    for (int j = 0; j < pieceMoves.size(); j++) {
                        int points = evalPosition(player.getPiecesInGame().get(i), pieceMoves.get(j));
                        Log.d("POINTS 1",String.valueOf(points));
                        moves.add(new PieceMoveScore(player.getPiecesInGame().get(i), pieceMoves.get(j), points));
                    }
                    Log.d("MOVES SIZE",String.valueOf(moves.size()));
                    PieceMoveScore bestMovePiece = getBetterMove(moves);
                    int bestMove = bestMovePiece.getScore();
                    Log.d("PIECE ATTACKING",player.getPiecesInGame().get(i).pieceInfo());
                    //Log.d("NEXT MOVE",bestMovePiece.getInfo());
                    Log.d("PIECE",bestMovePiece.getPiece().pieceInfo());
                    Log.d("MOVE",bestMovePiece.getHexagon().toString());
                    Log.d("SCORE", String.valueOf(bestMovePiece.getScore()));
                    //Check if it's current position is better than the new one
                    if(evalCurrent) {
                        int current = evalPosition(player.getPiecesInGame().get(i), player.getPiecesInGame().get(i).getHexagon());
                        Log.d("CURRENT POSITION: ",String.valueOf(current));
                        if(bestMove>current) bestMoves.add(bestMovePiece);
                    }
                }
                pieceBlocking=false;
            }
        }
        return bestMoves;
    }

    /**
     * Evaluate a position where a piece can be placed. The score is calculated watching its neighbours value.
     * Score: If the piece is friendly: -value and if it can be blocked 2*(-value)
     *        If its a enemy piece: +value and if it can be blocked 2*(+value)
     * @param hexagon
     * @return
     */
    private int evalPosition(Piece toMove,Hexagon hexagon) {

        //Exception #1: If the piece is a bee --> If the next move has more neighbours than the current position --> return -100
        if(toMove.getType().equals(PieceType.BEE)){
            int now = this.game.getHive().numberOfNeighbours(toMove.getHexagon());
            int next = this.game.getHive().numberOfNeighbours(hexagon);
            if(next>now) return -100;
        }

        //Exception #1: If the piece is a beetle and its moving on top of a piece of the same color --> return -100
        if(toMove.getType().equals(PieceType.BEETLE) && hexagon.getL()!=0){
            if(this.game.getHive().searchPiece(new Hexagon(hexagon.getQ(),hexagon.getR(),hexagon.getL()-1)).getPlayer().getColor().equals("Black")) {
                Log.d("Moving on top of black","Moving on top of a black!");
                Log.d("Piece",toMove.pieceInfo());
                Log.d("Position",hexagon.toString());
                return -100;
            }
        }

        //Save current piece position
        Hexagon currentPos = new Hexagon(toMove.getHexagon().getQ(),toMove.getHexagon().getR(),toMove.getHexagon().getL());
        //Move piece to the position to evaluate
        this.game.getHive().movePiece(toMove, hexagon, true);

        int points = 0;
        Piece[] n = new Piece[6];
        n = this.game.getHive().pieceNeighbours(hexagon);
        for(int j=0;j<n.length;j++){
            if(n[j]!=null){
                if(n[j].getPlayer().getColor().equals(this.player.getColor())){
                    points -= n[j].getValue();
                    if(game.getHive().checkIfPieceBlocked(n[j])){
                        points *= 2;
                    }
                }else{
                    points += n[j].getValue();
                    if(game.getHive().checkIfPieceBlocked(n[j])){
                        points *= 2;
                    }
                }
            }
        }
        //Return piece to its original position
        this.game.getHive().movePiece(toMove, currentPos, true);
        return points;
    }

    /**
     * Returns friendly piece from a given piece.
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
            Hexagon currentPos = new Hexagon(piece.getHexagon().getQ(), piece.getHexagon().getR(), piece.getHexagon().getL());
            //Change coordinates of the piece to move --> Hexagon outside the board
            this.game.getHive().movePiece(piece, new Hexagon(piece.getHexagon().getQ()+10, piece.getHexagon().getR()+10, 10), true);
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
    public Piece getPiece(){


        Piece piece = new Piece();
        //Get pieces in the box
        ArrayList<Piece> pieces = new ArrayList<>();
        pieces = this.player.getPiecesInTheBox();

        //ArrayList to store pieces and its moves
        ArrayList<PieceMoveScore> moves = new ArrayList<>();

        //If the opening used was the first one, the AI has a spider in the box
        //Following a tip from the authors game we will bring the spiders early in the game: http://gen42.com/images/tipspage1.jpg
        if(this.randomOp==0 && !spider){
            //piece = this.player.inspectPieceByIdFromBox(5);
            piece = this.player.inspectPieceFromBox(PieceType.SPIDER);
            //Work out the best gap to place the spider
            moves = addPieceScores(piece);
            if(!moves.isEmpty()) this.spider=true;
        }else{
            //First take beetles or ants
            int type = getRandomPos(0,2);
            if(type==0){
                //Take beetle
                if(this.player.inspectPieceFromBox(PieceType.BEETLE)!=null)
                    piece = this.player.inspectPieceFromBox(PieceType.BEETLE);
                else if(this.player.inspectPieceFromBox(PieceType.ANT)!=null)
                    piece = this.player.inspectPieceFromBox(PieceType.ANT);
                else if(this.player.inspectPieceFromBox(PieceType.GRASSHOPPER)!=null)
                    piece = this.player.inspectPieceFromBox(PieceType.GRASSHOPPER);
            }else if(type==1){
                //Take ant
                if(this.player.inspectPieceFromBox(PieceType.ANT)!=null)
                    piece = this.player.inspectPieceFromBox(PieceType.ANT);
                else if(this.player.inspectPieceFromBox(PieceType.GRASSHOPPER)!=null)
                    piece = this.player.inspectPieceFromBox(PieceType.GRASSHOPPER);
                else if (this.player.inspectPieceFromBox(PieceType.BEETLE)!=null)
                    piece = this.player.inspectPieceFromBox(PieceType.BEETLE);
            }else{
                //Take grasshopper
                if(this.player.inspectPieceFromBox(PieceType.GRASSHOPPER)!=null)
                    piece = this.player.inspectPieceFromBox(PieceType.GRASSHOPPER);
                else if(this.player.inspectPieceFromBox(PieceType.ANT)!=null)
                    piece = this.player.inspectPieceFromBox(PieceType.ANT);
                else if (this.player.inspectPieceFromBox(PieceType.BEETLE)!=null)
                    piece = this.player.inspectPieceFromBox(PieceType.BEETLE);
            }
            if(piece.getValue()!=0) moves = addPieceScores(piece);
            else{
                //If there aren't any beetles or ants to add --> add grasshoppers
                piece = this.player.inspectPieceFromBox(PieceType.GRASSHOPPER);
                moves = addPieceScores(piece);
            }
        }
        if(!moves.isEmpty()){
            //Get better move
            PieceMoveScore bestPiece = new PieceMoveScore();
            bestPiece = getBetterMove(moves);
            piece = bestPiece.getPiece();
            piece.setHexagon(bestPiece.getHexagon());
        }
        return piece;
    }

    /**
     *
     * @param piece
     * @return
     */
    private ArrayList<PieceMoveScore> addPieceScores(Piece piece){
        //Get possible gaps for the player
        ArrayList<Hexagon> gaps = new ArrayList<>();
        gaps = this.game.getHive().getAvailableHexagonsPlayer(this.player);
        //ArrayList to store pieces and its moves
        ArrayList<PieceMoveScore> moves = new ArrayList<>();

        for(int i=0;i<gaps.size();i++){
            //Place piece in that gap
            this.game.getHive().addPiece(piece, gaps.get(i));
            //Get possible moves from that position
            ArrayList<Hexagon> possibleMoves = new ArrayList<>();
            possibleMoves = this.game.getHive().getPossibleHexagons(piece, true);
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
     * Get opponents pieces in game.
     * @return
     */
    public ArrayList<Piece> getOpponentsPieces(){
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
    public boolean checksIfHexagonInGivenList(Hexagon hexagon, ArrayList<Hexagon> list){
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
        //bee = player.inspectPieceByIdFromBox(0);
        bee = player.inspectPieceFromBox(PieceType.BEE);
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
        int max = -1000;
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
