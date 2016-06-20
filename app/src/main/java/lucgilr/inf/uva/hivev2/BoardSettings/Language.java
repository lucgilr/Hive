package lucgilr.inf.uva.hivev2.BoardSettings;

import lucgilr.inf.uva.hivev2.GameModel.PieceType;

/**
 * @author Lucía Gil Román
 *
 * The reason this class was created is to translate the pieces type from the class enum PieceType
 * to spanish and english.
 */
public class Language {

    /**
     * Gets the english translation of the given PieceType bug
     * @param bug
     * @return
     */
    public String getEnglish(PieceType bug){
        if(bug.equals(PieceType.BEE)) return "Bee";
        else if(bug.equals(PieceType.ANT)) return "Ant";
        else if(bug.equals(PieceType.BEETLE)) return "Beetle";
        else if(bug.equals(PieceType.GRASSHOPPER)) return "Grasshopper";
        else return "Spider";
    }

    /**
     * Gets the spanish translation of the given PieceType bug
     * @param bug
     * @return
     */
    public String getSpanish(PieceType bug){
        if(bug.equals(PieceType.BEE)) return "Abeja";
        else if(bug.equals(PieceType.ANT)) return "Hormiga";
        else if(bug.equals(PieceType.BEETLE)) return "Escarabajo";
        else if(bug.equals(PieceType.GRASSHOPPER)) return "Saltamontes";
        else return "Araña";
    }

    /**
     * Gets the PieceType equivalent to a given String bug from spanish or english.
     * @param bug
     * @return
     */
    public PieceType stringToPieceType(String bug){
        if(bug.equals("Bee") || bug.equals("Abeja")) return PieceType.BEE;
        else if(bug.equals("Ant") || bug.equals("Hormiga")) return PieceType.ANT;
        else if(bug.equals("Beetle") || bug.equals("Escarabajo")) return PieceType.BEETLE;
        else if(bug.equals("Grasshopper") || bug.equals("Saltamontes")) return PieceType.GRASSHOPPER;
        else return PieceType.SPIDER;
    }

}
