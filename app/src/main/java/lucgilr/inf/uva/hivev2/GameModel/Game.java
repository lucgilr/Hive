package lucgilr.inf.uva.hivev2.GameModel;

/**
 * @author Lucía Gil Román
 *
 * The Game is form by two players and a Hive.
 * Both players have the same set of pieces, what makes them different its their name:
 * One will be "Black" and the other one "White", because of the color of their pieces.
 * The Game also has a round count, that increments when a player plays.
 * When the Game ends it will be indicated by a boolean named "end".
 */
public class Game {

    private Player player1;
    private Player player2;
    private Hive hive;
    private int round;
    private boolean end;
    private int start;

    public Game(){
        player1 = new Player("White");
        player2 = new Player("Black");
        hive = new Hive();
        round = 1;
        end = false;
    }

    public Game(int start){
        player1 = new Player("White");
        player2 = new Player("Black");
        hive = new Hive();
        round = 1;
        end = false;
        this.start=start;
    }

    /**
     * @return the hive assigned to this game.
     */
    public Hive getHive() {
        return hive;
    }

    /**
     * Assigns new hive to the Game.
     * @param hive
     */
    public void setHive(Hive hive) {
        this.hive = hive;
    }

    /**
     * @return player one ("White")
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Assigns player 1 to the Game.
     * @param player1
     */
    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    /**
     * @return player two ("Black")
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Assigns player 2 to the Game.
     * @param player2
     */
    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    /**
     * @return current round of the Game.
     */
    public int getRound() {
        return round;
    }

    /**
     * Assigns a round to the Game.
     * @param round
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * @return true if the game is over, false otherwise.
     */
    public boolean isEnd() {
        return end;
    }

    /**
     * Assigns a state to the Game, false if its not over and true if it is.
     * @param end
     */
    public void setEnd(boolean end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Increments a round
     */
    public void oneMoreRound() { this.round+=1; }

    /**
     * @return player who has the turn to play.
     */
    public Player playerTurn(){
        if(start==0) {
            if (this.round % 2 == 0)
                return getPlayer2();
            else
                return getPlayer1();
        }else{
            if (this.round % 2 == 0)
                return getPlayer1();
            else
                return getPlayer2();
        }
    }

    /**
     * This method is used to resolve the state of both bees.
     * If both bees have 6 neighbours, then both are dead and the game ends in a draw.
     * If the bee of the player one is surrounded by 6 neighbours then the winner is
     * player 2, and vice versa.
     * @return
     */
    public int beeSurrounded(){
        Hexagon p1 = this.getPlayer1().inspectPiece(PieceType.BEE).getHexagon();
        Hexagon p2 = this.getPlayer2().inspectPiece(PieceType.BEE).getHexagon();
        if(this.getHive().numberOfNeighbours(p1)==6 && this.getHive().numberOfNeighbours(p2)==6)
            return 3;
        else if(this.getHive().numberOfNeighbours(p1)==6) return 2;
        else if(this.getHive().numberOfNeighbours(p2)==6) return 1;
        else return 0;

    }

}

