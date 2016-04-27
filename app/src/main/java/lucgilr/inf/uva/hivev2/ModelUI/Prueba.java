package lucgilr.inf.uva.hivev2.ModelUI;

/**
 * Created by gil on 26/04/16.
 */
public class Prueba {

    private int insect;
    private Hex hex;
    private String color;

    public Prueba(int insect, Hex hex, String color){
        this.insect=insect;
        this.hex=hex;
        this.color=color;
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
}

