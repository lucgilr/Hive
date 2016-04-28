package lucgilr.inf.uva.hivev2.ModelUI;

import lucgilr.inf.uva.hivev2.GameModel.Token;

/**
 * Created by gil on 26/04/16.
 */
public class Prueba {

    private int insect;
    private Hex hex;
    private String color;
    private Token token;

    public Prueba(int insect, Hex hex, String color, Token token){
        this.insect=insect;
        this.hex=hex;
        this.color=color;
        this.token=token;
    }

    public int getInsect() {
        return insect;
    }

    public void setInsect(int insect) {
        this.insect = insect;
    }

    public Hex getHex() {
        return hex;
    }

    public void setHex(Hex hex) {
        this.hex = hex;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}

