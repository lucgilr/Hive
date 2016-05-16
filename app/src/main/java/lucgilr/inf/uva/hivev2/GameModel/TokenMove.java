package lucgilr.inf.uva.hivev2.GameModel;

/**
 * Created to combine Token and a possible gaps where it can be moved.
 * Used in the AI class.
 * points will be used to choose the best move.
 * Created by Lucía Gil Román on 15/05/16.
 */
public class TokenMove {

    private Token token;
    private Hex hex;
    private int points;

    public TokenMove(){
        this.token=null;
        this.hex=null;
        this.points=0;
    }

    public TokenMove(Token token, Hex hex, int points){
        this.token=token;
        this.hex=hex;
        this.points=points;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Hex getHex() {
        return hex;
    }

    public void setHex(Hex hex) {
        this.hex = hex;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
