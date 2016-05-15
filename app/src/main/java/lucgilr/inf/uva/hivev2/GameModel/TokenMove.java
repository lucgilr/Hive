package lucgilr.inf.uva.hivev2.GameModel;

/**
 * Created to combine Token and a possible gaps where it can be moved.
 * Used in the AI class.
 * Created by Lucía Gil Román on 15/05/16.
 */
public class TokenMove {

    private Token token;
    private Hex hex;

    public TokenMove(){
        this.token=null;
        this.hex=null;
    }

    public TokenMove(Token token, Hex hex){
        this.token=token;
        this.hex=hex;
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
}
