package lucgilr.inf.uva.hivev2.GameModel;

/**
 * Created by Lucía Gil Román on 14/05/16.
 */
public class Language {

    public String getEnglish(String bug){
        if(bug.equals(TokenType.BEE.toString())){
            return "Bee";
        }else if(bug.equals(TokenType.ANT.toString())){
            return "Ant";
        }else if(bug.equals(TokenType.BEETLE.toString())){
            return "Beetle";
        }else if(bug.equals(TokenType.GRASSHOPPER.toString())){
            return "Grasshopper";
        }else{
            return "Spider";
        }
    }

    public String getSpanish(String bug){
        if(bug.equals(TokenType.BEE.toString())){
            return "Abeja";
        }else if(bug.equals(TokenType.ANT.toString())){
            return "Hormiga";
        }else if(bug.equals(TokenType.BEETLE.toString())){
            return "Escarabajo";
        }else if(bug.equals(TokenType.GRASSHOPPER.toString())){
            return "Saltamontes";
        }else{
            return "Araña";
        }
    }

}
