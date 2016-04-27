package lucgilr.inf.uva.hivev2.GameModel;

import java.util.ArrayList;


/**
 * A player is the person/AI who plays the Game.  
 * @author Lucía Gil Román
 */
public class Player {

    private String color;
    private int turn;
    private int points;
    private ArrayList<Token> tokensInGame;
    private ArrayList<Token> tokensInTheBox;
    private int playedTokens;
    private boolean beeInGame;

    /**
     *
     * @param color
     */
    public Player(String color){
        this.color=color;
        this.turn=1;
        //this.turn=0;
        this.tokensInTheBox = new ArrayList<>();
        this.tokensInTheBox = setBoxTokens();
        this.tokensInGame = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public String getColor() {
        return color;
    }

    /**
     *
     * @pacolorname
     */
    public void setColor(String color) {
        this.color=color;
    }

    /**
     *
     * @return
     */
    public int getPlayedTokens() {
        return playedTokens;
    }

    /**
     *
     * @param playedTokens
     */
    public void setPlayedTokens(int playedTokens) {
        this.playedTokens = playedTokens;
    }

    /**
     *
     * @return
     */
    public int getTurn() {
        return turn;
    }

    /**
     *
     * @param turn
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }

    /**
     *
     * @return
     */
    public int getPoints() {
        return points;
    }

    /**
     *
     * @param points
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     *
     * @return
     */
    public ArrayList<Token> getTokensInGame() {
        return tokensInGame;
    }

    /**
     *
     * @param tokensInGame
     */
    public void setTokensInGame(ArrayList<Token> tokensInGame) {
        this.tokensInGame = tokensInGame;
    }

    /**
     *
     * @return
     */
    public ArrayList<Token> getTokensInTheBox() {
        return tokensInTheBox;
    }

    /**
     *
     * @param tokensInTheBox
     */
    public void setTokensInTheBox(ArrayList<Token> tokensInTheBox) {
        this.tokensInTheBox = tokensInTheBox;
    }

    /**
     *
     * @return
     */
    public boolean isBeeInGame() {
        return beeInGame;
    }

    /**
     *
     * @param beeInGame
     */
    public void setBeeInGame(boolean beeInGame) {
        this.beeInGame = beeInGame;
    }

    /**
     * Assign all possible tokens to the player 
     * @return
     */
    private ArrayList<Token> setBoxTokens() {
        this.tokensInTheBox.add(new Token(TokenType.BEE,0,this));
        this.tokensInTheBox.add(new Token(TokenType.GRASSHOPPER,1,this));
        this.tokensInTheBox.add(new Token(TokenType.GRASSHOPPER,2,this));
        this.tokensInTheBox.add(new Token(TokenType.GRASSHOPPER,3,this));
        this.tokensInTheBox.add(new Token(TokenType.SPIDER,4,this));
        this.tokensInTheBox.add(new Token(TokenType.SPIDER,5,this));
        this.tokensInTheBox.add(new Token(TokenType.BEETLE,6,this));
        this.tokensInTheBox.add(new Token(TokenType.BEETLE,7,this));
        this.tokensInTheBox.add(new Token(TokenType.ANT,8,this));
        this.tokensInTheBox.add(new Token(TokenType.ANT,9,this));
        this.tokensInTheBox.add(new Token(TokenType.ANT,10,this));
        return this.tokensInTheBox;
    }

    /**
     *
     */
    public void oneMoreTurn(){
        this.turn+=1;
    }

    /**
     *
     * @return
     */
    public String showTokensInGame(){
        String tokens="";
        if(!this.getTokensInGame().isEmpty()){
            for(int i=0;i<this.getTokensInGame().size();i++){
                tokens+="\nType: "+getTokensInGame().get(i).getType()
                        +" #"+getTokensInGame().get(i).getId()
                        +" Position-->: "+getTokensInGame().get(i).getCoordinates().getX()+"-"
                        +getTokensInGame().get(i).getCoordinates().getY()+"-"
                        +getTokensInGame().get(i).getCoordinates().getZ();
            }
        }else{
            tokens+="There aren't any token in the game";
        }
        return tokens;
    }

    /**
     *
     * @return
     */
    public String showTokensInTheBox(){
        String tokens="";
        if(!this.getTokensInTheBox().isEmpty()){
            for(int i=0;i<this.getTokensInTheBox().size();i++){
                tokens+="\nType: "+getTokensInTheBox().get(i).getType()
                        +" #"+getTokensInTheBox().get(i).getId();
            }
        }else{
            tokens+="Dock empty...";
        }
        return tokens;
    }

    /**
     *
     * @return
     */
    public int playerOptions(){
        if(this.turn==4 && !isBeeInGame()){
            return 0;
        }else if(getTokensInTheBox().isEmpty()){
            return 1;
        }else if(!isBeeInGame()){
            return 2;
        }else if(isBeeInGame()){
            return 3;
        }else{
            return -1;
        }
    }

    /**
     *
     * @return
     */
    public String chooseAction(){
        int opt = playerOptions();
        String outputInfo="";
        switch(opt){
            case 0: outputInfo+="Is your 4th turn and you haven't played the bee.\n"
                    + "You can only choose the bee... (press b): ";
                break;
            case 1: outputInfo+="You can only move a token of the hive (press h): ";
                break;
            case 2: outputInfo+="You can only choose a token from the box (press x): ";
                break;
            case 3: outputInfo+="You can choose a token from the box (press x) or move one of the hive (press h) ";
                break;
            default: outputInfo+="WTF!";
                break;
        }
        return outputInfo;
    }


    /**
     *
     * @param opt
     * @return
     */
    public String playerSelection(String opt) {
        String op="";
        switch(opt){
            case "h": op+=showTokensInGame();
                break;
            case "x": op+=showTokensInTheBox();
                break;
            case "b": op+="Type: BEE #0";
                break;
            default: break;
        }
        return op;
    }

    public ArrayList<Token> playerSelectionV2(String opt){
        ArrayList<Token> bee = new ArrayList<Token>();
        switch(opt){
            case "h": return this.tokensInGame;
            case "x": return this.tokensInTheBox;
            case "b": bee.add(this.tokensInTheBox.get(0));
            default: return null;
        }
    }

    /**
     *
     * @param id
     * @return
     */
    private Token getTokenById(int id){
        for(int i=0;i<this.tokensInTheBox.size();i++){
            if(this.tokensInTheBox.get(i).getId()==id)
                return this.tokensInTheBox.get(i);
        }
        for(int j=0;j<this.tokensInGame.size();j++){
            if(this.tokensInGame.get(j).getId()==id)
                return this.tokensInGame.get(j);
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public Token takeTokenFromTheBox(int id){
        //Get token
        Token t = getTokenById(id);
        //Set atribute inGame as true
        t.setInGame(true);
        //Set token in TokensInGame
        getTokensInGame().add(t);
        //Take token from TokensInTheBox
        getTokensInTheBox().remove(t);
        return t;
    }

    /**
     *
     * @param id
     * @return
     */
    public Token moveBoardToken(int id){
        //Get token
        return getTokenById(id);
    }

    /**
     * Returns the token choose by the player
     * @param op
     * @param opt
     * @return
     */
    public Token tokenAction(int op, String opt) {
        Token t = null;
        switch(opt){
            case "h": t = moveBoardToken(op);
                break;
            case "x": t = takeTokenFromTheBox(op);
                break;
            case "b": t = getTokenById(0);
                break;
        }
        return t;
    }

    /**
     *
     * @return
     */
    public String printTokensInBox(){
        String token="";
        for(int i=0;i<this.tokensInTheBox.size();i++){
            token += " Type: "+tokensInTheBox.get(i).getType()+"\n";
        }
        return token;
    }

}
