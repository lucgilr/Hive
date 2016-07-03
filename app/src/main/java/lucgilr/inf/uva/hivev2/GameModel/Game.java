package lucgilr.inf.uva.hivev2.GameModel;

/**
 * @author Lucía Gil Román (https://github.com/lucgilr)
 *
 * The Game is form by two players and a Hive.
 * Both players have the same set of pieces, what makes them different its their name:
 * One will be "Black" and the other one "White", because of the color of their pieces.
 * The Game also has a round count, that increments when a player plays.
 *
 */
public class Game {

    private final Player player1;
    private final Player player2;
    private final Hive hive;
    private int round;
    private final int start;

    /**
     * A Game has 2 players (black & white) and "start" determines which one starts to play.
     */
    public Game(int start){
        player1 = new Player("White");
        player2 = new Player("Black");
        hive = new Hive();
        round = 1;
        this.start=start;
    }

    /**
     * @return the hive assigned to this game.
     */
    public Hive getHive() {
        return hive;
    }

    /**
     * @return player one ("White")
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * @return player two ("Black")
     */
    public Player getPlayer2() {
        return player2;
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
     */
    public int beeSurrounded(){
        Hexagon p1 = this.getPlayer1().inspectPiece(PieceType.BEE).getHexagon();
        Hexagon p2 = this.getPlayer2().inspectPiece(PieceType.BEE).getHexagon();
        if(this.getHive().numberOfNeighbours(p1)==6 && this.getHive().numberOfNeighbours(p2)==6)
            return 3;
        else if(this.getHive().numberOfNeighbours(p2)==6) return 2;
        else if(this.getHive().numberOfNeighbours(p1)==6) return 1;
        else return 0;

    }

}

