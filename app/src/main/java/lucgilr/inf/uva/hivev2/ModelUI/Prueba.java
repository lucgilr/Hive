package lucgilr.inf.uva.hivev2.ModelUI;

/**
 * Created by gil on 26/04/16.
 */
public class Prueba {

    private int insect;
    private Hex hex;

    public Prueba(int insect, Hex hex){
        this.insect=insect;
        this.hex=hex;
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
}

