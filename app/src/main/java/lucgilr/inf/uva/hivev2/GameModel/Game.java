package lucgilr.inf.uva.hivev2.GameModel;


import lucgilr.inf.uva.hivev2.ModelUI.Hex;

/**
 *
 * @author Lucía Gil Román
 */
public class Game {

    private Player player1;
    private Player player2;
    private Hive hive;
    private int round;
    private boolean end;

    public Game(){
        player1 = new Player("White");
        player2 = new Player("Black");
        hive = new Hive();
        round = 1;
        //round=0;
        end = false;
    }

    public Hive getHive() {
        return hive;
    }

    public void setHive(Hive hive) {
        this.hive = hive;
    }

    /**
     *
     * @return
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     *
     * @param player1
     */
    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    /**
     *
     * @return
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     *
     * @param player2
     */
    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    /**
     *
     * @return
     */
    public int getRound() {
        return round;
    }

    /**
     *
     * @param round
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     *
     * @return
     */
    public boolean isEnd() {
        return end;
    }

    /**
     *
     * @param end
     */
    public void setEnd(boolean end) {
        this.end = end;
    }

    /**
     * State of the game
     * @return
     */
    public String state(){
        String state = "Game status: \n"
                + "Round: "+getRound()+"\n\n"
                + "*"+getPlayer1().getColor()+"(Player1) Turns: "+getPlayer1().getTurn()+"\nTokens in the box:\n"+bugs(player1)+"\n"
                + "*"+getPlayer2().getColor()+"(Player2) Turns: "+getPlayer2().getTurn()+"\nTokens in the box:\n"+bugs(player2)+"\n";
        return state;
    }

    /**
     *
     * @param player
     * @return
     */
    public String bugs(Player player){
        String bugs="";
        for(int i=0;i<player.getTokensInTheBox().size();i++){
            bugs+="Token "+i+": "+player.getTokensInTheBox().get(i).getType()+"\n";
        }
        return bugs;
    }

    /**
     * Increments a round
     */
    public void oneMoreRound(){
        this.round+=1;
    }

    /**
     * Choose which player has the turn
     * @return
     */
    public Player playerTurn(){
        if(this.round%2==0){
            return getPlayer2();
        }
        else{
            return getPlayer1();
        }
    }

    /**
     *
     * @return
     */
    public int beeSurrounded(){
        Hex p1 = this.getPlayer1().getTokenById(0).getCoordinates();
        Hex p2 = this.getPlayer2().getTokenById(0).getCoordinates();
        if(this.getHive().numberOfNeighbours(p1)==6 && this.getHive().numberOfNeighbours(p2)==6){
            return 3;
        }else if(this.getHive().numberOfNeighbours(p1)==6){
            return 2;
        }else if(this.getHive().numberOfNeighbours(p2)==6){
            return 1;
        }else{
            return 0;
        }

    }


}

