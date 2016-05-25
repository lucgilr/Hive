package lucgilr.inf.uva.hivev2.GameModel;

import java.util.ArrayList;


/**
 * A player is the person/AI who plays the GameUI.
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
     * @param id
     * @return
     */
    public Token getTokenById(int id){
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
     * Returns the Token given its type.
     * @param type
     * @return
     */
    public Token takeToken(String type){

        Token t = new Token();
        //Get token
        for(int i=0;i<this.tokensInTheBox.size();i++){
            if(this.tokensInTheBox.get(i).getType().equals(TokenType.valueOf(type)))
                t = this.tokensInTheBox.get(i);
        }
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
    public Token inspectTokenInGame(int id){
        Token token = new Token();
        for(int i=0;i<this.tokensInGame.size();i++){
            if(this.tokensInGame.get(i).getId()==id){
                token = this.tokensInGame.get(i);
            }
        }
        return token;
    }

    public boolean isTokenInBox(TokenType type){
        for(int i=0;i<this.getTokensInTheBox().size();i++){
            if(this.getTokensInTheBox().get(i).getType().equals(type)) return true;
        }
        return false;
    }

}
