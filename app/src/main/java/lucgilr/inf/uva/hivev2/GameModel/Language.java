package lucgilr.inf.uva.hivev2.GameModel;

/**
 * Created by Lucía Gil Román on 14/05/16.
 */
public class Language {

    /**
     *
     * @param bug
     * @return
     */
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

    /**
     *
     * @param bug
     * @return
     */
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

    /**
     *
     * @param bug
     * @return
     */
    public String StringToTokenString(String bug){
        if(bug.equals("Bee") || bug.equals("Abeja")){
            return TokenType.BEE.toString();
        }else if(bug.equals("Ant") || bug.equals("Hormiga")){
            return TokenType.ANT.toString();
        }else if(bug.equals("Beetle") || bug.equals("Escarabajo")){
            return TokenType.BEETLE.toString();
        }else if(bug.equals("Grasshopper") || bug.equals("Saltamontes")){
            return TokenType.GRASSHOPPER.toString();
        }else{
            return TokenType.SPIDER.toString();
        }
    }

}
