package lucgilr.inf.uva.hivev2.GameModel;


import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.alg.BlockCutpointGraph;

import lucgilr.inf.uva.hivev2.ModelUI.Hex;

/**
 *  Representation of the board.
 * @author Lucía Gil Román
 */
public final class Hive {

    //private ArrayList<Hex> availableGaps;
    private ArrayList<Hex> availableGaps;
    private ArrayList<Token> board;
    UndirectedGraph<Integer,DefaultEdge> graph;
    private int vertex;

    /**
     *
     * There is only one position available (0,0,0).
     */
    public Hive(){
        this.board=new ArrayList<Token>();
        availableGaps = new ArrayList<>();
        //this.availableGaps.add(new Hex(0,0,0));
        this.availableGaps.add(new Hex(0,0,0));
        this.graph = new SimpleGraph<Integer,DefaultEdge>(DefaultEdge.class);
        this.vertex=0;
    }

    /**
     *
     * @return
     */
    public ArrayList<Token> getBoard() {
        return board;
    }

    /**
     *
     * @param board
     */
    public void setBoard(ArrayList<Token> board) {
        this.board = board;
    }

    /**
     *
     * @return
     */
    /*public ArrayList<Hex> getAvailableGaps() {
        return availableGaps;
    }*/
    public ArrayList<Hex> getAvailableGaps() {
        return availableGaps;
    }

    /**
     *
     * @param availableGaps
     */
    /*public void setAvailableGaps(ArrayList<Hex> availableGaps) {
        this.availableGaps = availableGaps;
    }*/
    public void setAvailableGaps(ArrayList<Hex> availableGaps) {
        this.availableGaps = availableGaps;
    }

    /**
     *
     * @return
     */
    public UndirectedGraph<Integer, DefaultEdge> getGraph() {
        return graph;
    }

    /**
     *
     * @param graph
     */
    public void setGraph(UndirectedGraph<Integer, DefaultEdge> graph) {
        this.graph = graph;
    }

    /**
     *
     * @return
     */
    public int getVertex() {
        return vertex;
    }

    /**
     *
     * @param vertex
     */
    public void setVertex(int vertex) {
        this.vertex = vertex;
    }

    /**
     * Adds a token to a gap in the board.
     * Deletes de used gap from the list of gaps available for its use and adds the new ones from
     * the new token.
     * @param token
     * @param hex
     * CHANGES --> Hex to Hex
     */
    public void addToken(Token token, Hex hex){
        //If is a bee --> beeInGame
        if(token.getType()==TokenType.BEE) token.getPlayer().setBeeInGame(true);
        //Add Hex to token
        token.setCoordinates(hex);
        //Place token on the board
        this.board.add(token);
        //Get neighbours
        Token[] neighbours = new Token[6];
        neighbours = tokenNeighbours(token.getCoordinates());
        //Set token a graph id
        token.setGraphId(this.vertex);
        this.vertex=this.vertex+1;
        //Add token to graph
        this.graph.addVertex(token.getGraphId());
        //Add edges (neighbours) to the vertex
        for (Token neighbour : neighbours)
            if (neighbour != null){
                this.graph.addEdge(token.getGraphId(), neighbour.getGraphId());
            }
        //addEdges(neighbours,token);
        //Set token to inGame
        token.setInGame(true);
        //Delete gap from ArrayList of gaps avaliable
        removeHexFromAvaliable(token.getCoordinates());
        //add new neighbours if they are not already there and have no token
        refreshGapsAvailable(token.getCoordinates());
    }

    /**
     * Prints board situation.
     * @return
     */
    public String printBoard(){
        String board="\nBOARD SITUATION:\n";
        for(int i=0;i<this.board.size();i++){
            board+="Type: "+this.board.get(i).getType()
                    +" Player: "+this.board.get(i).getPlayer().getColor()
                    +" Coordinates: "+this.board.get(i).getCoordinates().toString()
                    +" Beetle: "+this.board.get(i).isBeetle()+"\n";
        }
        return board;
    }

    /**
     * Checks if a token is in the board
     * @param hex of the token to look for
     * @return true if found in the board
     */
    /*private boolean isInBoard(Hex Hex){
        for(int i=0;i<this.board.size();i++){
            if(this.board.get(i).getCoordinates().getR()==Hex.getR()
                    && this.board.get(i).getCoordinates().getQ()==Hex.getQ()
                    && this.board.get(i).getCoordinates().getD()==Hex.getD())
                return true;
        }
        return false;
    }*/
    //CHANGES --> Hex to Hex
    private boolean isInBoard(Hex hex){
        for(int i=0;i<this.board.size();i++){
            if(this.board.get(i).getCoordinates().getR()==hex.getR()
                    && this.board.get(i).getCoordinates().getQ()==hex.getQ()
                    && this.board.get(i).getCoordinates().getD()==hex.getD())
                return true;
        }
        return false;
    }

    /**
     * Search a token in the board given its coordinates
     * @param hex of the token to look for
     * @return token if found
     */
    /*private Token searchToken(Hex Hex){
        for (Token board1 : this.board) {
            if (board1.getCoordinates().getR() == Hex.getR()
                    && board1.getCoordinates().getQ() == Hex.getQ()
                    && board1.getCoordinates().getD() == Hex.getD()) {
                return board1;
            }
        }
        return null;
    }*/
    //CHANGES --> Hex to Hex
    private Token searchToken(Hex hex){
        for (Token board1 : this.board) {
            if (board1.getCoordinates().getR() == hex.getR()
                    && board1.getCoordinates().getQ() == hex.getQ()
                    && board1.getCoordinates().getD() == hex.getD()) {
                return board1;
            }
        }
        return null;
    }

    /**
     * Updates token coordinates
     * @param token to update
     * @param Hex new coordinates
     */
    /*private void updateCoordinates(Token token, Hex Hex) {
        for (Token board1 : this.board) {
            if (board1.getCoordinates().getR() == token.getCoordinates().getR()
                    && board1.getCoordinates().getQ() == token.getCoordinates().getQ()
                    && board1.getCoordinates().getD() == token.getCoordinates().getD()) {
                board1.setCoordinates(Hex);
            }
        }
    }*/
    //CHANGES --> Hex to Hex
    private void updateCoordinates(Token token, Hex hex) {
        for (Token board1 : this.board) {
            if (board1.getCoordinates().getR() == token.getCoordinates().getR()
                    && board1.getCoordinates().getQ() == token.getCoordinates().getQ()
                    && board1.getCoordinates().getD() == token.getCoordinates().getD()) {
                board1.setCoordinates(hex);
            }
        }
    }

    /**
     * Deletes the coordinates of a token.
     * Updates it to an impossible position in the board: -100,-100,-100
     * @param token
     */
    /*private void deleteHex(Token token){
        for (Token board1 : this.board) {
            if (board1.getCoordinates().getR() == token.getCoordinates().getR()
                    && board1.getCoordinates().getQ() == token.getCoordinates().getQ()
                    && board1.getCoordinates().getD() == token.getCoordinates().getD()) {
                board1.setCoordinates(new Hex(-100,-100,-100));
            }
        }
    }*/
    private void deleteHex(Token token){
        for (Token board1 : this.board) {
            if (board1.getCoordinates().getR() == token.getCoordinates().getR()
                    && board1.getCoordinates().getQ() == token.getCoordinates().getQ()
                    && board1.getCoordinates().getD() == token.getCoordinates().getD()) {
                board1.setCoordinates(new Hex(-100,-100,-100));
            }
        }
    }

    /**
     * returns the neighbours Tokens from a given gap.
     * @param hex
     * @return
     */
    //CHANGES --> Hex to Hex
    private Token[] tokenNeighbours(Hex hex){
        //int x = Hex.getR();
        //int y = Hex.getQ();
        int x = hex.getQ();
        int y = hex.getR();
        int z = 0;
        Token[] n = new Token[6];
        //
        while(isInBoard(new Hex(x+1,y-1,z))) z=z+1;
        if(z==0) n[0]=searchToken(new Hex(x+1,y-1,z));
        else n[0]=searchToken(new Hex(x+1,y-1,z-1));
        z=0;
        //
        while(isInBoard(new Hex(x+1,y,z))) z=z+1;
        if(z==0) n[1]=searchToken(new Hex(x+1,y,z));
        else n[1]=searchToken(new Hex(x+1,y,z-1));
        z=0;
        //
        while(isInBoard(new Hex(x,y+1,z))) z=z+1;
        if(z==0) n[2]=searchToken(new Hex(x,y+1,z));
        else n[2]=searchToken(new Hex(x,y+1,z-1));
        z=0;
        //
        while(isInBoard(new Hex(x-1,y+1,z))) z=z+1;
        if(z==0) n[3]=searchToken(new Hex(x-1,y+1,z));
        else n[3]=searchToken(new Hex(x-1,y+1,z-1));
        z=0;
        //
        while(isInBoard(new Hex(x-1,y,z))) z=z+1;
        if(z==0) n[4]=searchToken(new Hex(x-1,y,z));
        else n[4]=searchToken(new Hex(x-1,y,z-1));
        z=0;
        //
        while(isInBoard(new Hex(x,y-1,z))) z=z+1;
        if(z==0) n[5]=searchToken(new Hex(x,y-1,z));
        else n[5]=searchToken(new Hex(x,y-1,z-1));
        z=0;
        //
        return n;
    }

    /**
     * counts how many neighbors has a given position.
     * @param Hex
     * @return
     */
    //CHANGES --> Hex to Hex
    public int numberOfNeighbours(Hex hex){
        int n = 0;
        Token[] nb = new Token[6];
        nb = tokenNeighbours(hex);
        for (int i=0;i<nb.length;i++){
            if(nb[i]!=null) n = n+1;
        }
        return n;
    }

    /**
     * checks if all the neighbors tokens for a given gap are from a specific player.
     * @param player
     * @param Hex
     * @return
     */
    //CHANGES --> Hex to Hex
    public boolean checkNeighboursTokenSamePlayer(Player player, Hex hex){
        Token[] n = new Token[6];
        n = tokenNeighbours(hex);
        for (Token n1 : n) {
            if (n1 != null) {
                if (!n1.getPlayer().getColor().equals(player.getColor())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Prints in which gaps can a player place a token for the first time.
     * If it's the first turn of the player the token must touch a token
     * of the opposite player.
     * @param player
     * @return
     */
    public String printPlayerGapsAvailable(Player player){
        String gaps="\nGaps avaliable: \n";
        for(int i=0;i<this.availableGaps.size();i++)
            //A token can only be placed in the lower level of the hive
            if(this.availableGaps.get(i).getD()==0){
                if(player.getTurn()!=1){
                    if(checkNeighboursTokenSamePlayer(player,this.getAvailableGaps().get(i)))
                        gaps+=this.availableGaps.get(i).toString()+"\n";
                }else{
                    gaps+=this.availableGaps.get(i).toString()+"\n";
                }
            }
        return gaps;
    }

    /**
     *
     * @param player
     * @return
     */
    //CHANGES --> Hex to Hex
    public ArrayList<Hex> getPlayerGapsAvailable(Player player){
        ArrayList<Hex> gaps = new ArrayList<>();
        for(int i=0;i<this.availableGaps.size();i++)
            //A token can only be placed in the lower level of the hive
            if(this.availableGaps.get(i).getD()==0){
                if(player.getTurn()!=1){
                    if(checkNeighboursTokenSamePlayer(player,this.getAvailableGaps().get(i)))
                        gaps.add(this.availableGaps.get(i));
                }else{
                    gaps.add(this.availableGaps.get(i));
                }
            }
        return gaps;
    }

    /**
     * Checks if there are available gaps for a player to place a token
     * @param player
     * @return
     */
    //CHANGES --> Hex to Hex
    public boolean checkIfGapsAvailable(Player player){
        ArrayList<Hex> gaps = new ArrayList<Hex>();
        for(int i=0; i<this.getAvailableGaps().size();i++){
            if(this.availableGaps.get(i).getD()==0){
                if(player.getTurn()!=1){
                    if(checkNeighboursTokenSamePlayer(player,this.getAvailableGaps().get(i)))
                        gaps.add(this.availableGaps.get(i));
                }else{
                    gaps.add(this.availableGaps.get(i));
                }
            }
        }
        if(gaps.isEmpty()) return false;
        else return true;
    }

    /**
     * Prints neighbors tokens for a given token.
     * Creo que este método no se usa --> Mirarlo
     * @param token
     * @return
     */
    public String printNeighboursTokens(Token token){
        Token[] n = new Token[6];
        n = tokenNeighbours(token.getCoordinates());
        String ns="Neighbours: \n";
        for(int i=0;i<n.length;i++){
            if(n[i]!=null)
                ns+="At ("+n[i].getCoordinates().getR()+","+n[i].getCoordinates().getD()+") Type: "+n[i].getType()+" Player: "+n[i].getPlayer().getColor();
        }
        return ns;
    }

    /**
     * Returns the list of positions surrounding a gap.
     * @param Hex
     * @return
     */
    public ArrayList<Hex> getNeighbourHex(Hex hex){
        /*int x = Hex.getR();
        int y = Hex.getQ();*/
        int x = hex.getQ();
        int y = hex.getR();
        int z = 0;
        ArrayList<Hex> neighbours = new ArrayList<Hex>();

        while(isInBoard(new Hex(x+1,y-1,z)))
            z=z+1;
        neighbours.add(new Hex(x+1,y-1,z));
        z=0;

        while(isInBoard(new Hex(x+1,y,z)))
            z=z+1;
        neighbours.add(new Hex(x+1,y,z));
        z=0;

        while(isInBoard(new Hex(x,y+1,z)))
            z=z+1;
        neighbours.add(new Hex(x,y+1,z));
        z=0;

        while(isInBoard(new Hex(x-1,y+1,z)))
            z=z+1;
        neighbours.add(new Hex(x-1,y+1,z));
        z=0;

        while(isInBoard(new Hex(x-1,y,z)))
            z=z+1;
        neighbours.add(new Hex(x-1,y,z));
        z=0;

        while(isInBoard(new Hex(x,y-1,z)))
            z=z+1;
        neighbours.add(new Hex(x,y-1,z));

        return neighbours;
    }

    /**
     * Deletes a coordinate from the list of available gaps.
     * @param Hex
     */
    public void removeHexFromAvaliable(Hex hex){
        Iterator<Hex> it = this.availableGaps.iterator();
        while (it.hasNext()) {
            Hex c = it.next();
            if (c.getR()==hex.getR() && c.getQ()==hex.getQ() && c.getD()==hex.getD()) {
                it.remove();
            }
        }
    }

    /**
     * Checks if a positions is already in the list of available gaps.
     * @param Hex
     * @return
     */
    private boolean checkIfDuplicate(Hex hex){
        for(int i=0;i < this.availableGaps.size();i++){
            if(this.availableGaps.get(i).getR()==hex.getR()
                    && this.availableGaps.get(i).getQ()==hex.getQ()
                    && this.availableGaps.get(i).getD()==hex.getD())
                return false;
        }
        return true;
    }

    /**
     * Add new positions to the list of available gaps.
     * Checks if the new position is not already in the list.
     * @param Hex
     */
    private void refreshGapsAvailable(Hex hex) {
        ArrayList<Hex> newNeighbours = new ArrayList<>();
        newNeighbours = getNeighbourHex(hex);
        for(int i=0;i<newNeighbours.size();i++){
            if(!isInBoard(newNeighbours.get(i)))
                if(checkIfDuplicate(newNeighbours.get(i)))
                    this.getAvailableGaps().add(newNeighbours.get(i));

        }
    }

    /**
     * Deletes gaps from available if they haven't any neighbour.
     * @param Hex
     */
    private void deleteGapsAvailable(Hex hex) {
        ArrayList<Hex> oldNeighbours = new ArrayList<>();
        oldNeighbours = getNeighbourHex(hex);
        for(int i=0; i<oldNeighbours.size();i++){
            //If this neighbours gap has no neighbours --> delete from gapsAvailable
            if(numberOfNeighbours(oldNeighbours.get(i))==0) removeHexFromAvaliable(oldNeighbours.get(i));
        }
    }

    /**
     * Returns a list of coordinates where a given token can be moved.
     * the token is already in game.
     * @param token
     * @return
     */
    public ArrayList<Hex> getPossibleGaps(Token token){
        ArrayList<Hex> possibleGaps = new ArrayList<>();
        // And if it has a beetle on top
        Log.d("isbeetle",String.valueOf(!token.isBeetle()));
        Log.d("isbeeingame",String.valueOf(token.getPlayer().isBeeInGame()));
        Log.d("brokenhive",String.valueOf(!brokenHive(token)));
        if(!token.isBeetle() && token.getPlayer().isBeeInGame() && !brokenHive(token)){
            switch(token.getType()){
                case BEE: possibleGaps = beeMoves(token);
                    break;
                case GRASSHOPPER: possibleGaps = grasshopperMoves(token.getCoordinates());
                    break;
                case BEETLE: possibleGaps = beetleMoves(token);
                    break;
                case SPIDER: possibleGaps = spiderMoves(token);
                    break;
                case ANT: possibleGaps = antMoves(token);
                    break;
                default: break;
            }
        }
        return possibleGaps;
    }

    /**
     * Prints a list of coordinates where a given token can be moved.
     * @param token
     * @return
     */
    public String printPossibleGaps(Token token){
        String gaps ="\nPossible gaps for "+token.getType()+":\n";
        ArrayList<Hex> possibleGaps = new ArrayList<>();
        possibleGaps = getPossibleGaps(token);
        for(int i=0;i<possibleGaps.size(); i++){
            gaps+=" X: "+possibleGaps.get(i).getR()+" Y: "+possibleGaps.get(i).getQ()+" Z: "+possibleGaps.get(i).getD()+"\n";
        }
        return gaps;
    }

    /**
     * Moves a token from its currently position to a new one.
     * @param token to move
     * @param hex new position
     */
    public void movetoken(Token token, Hex hex){
        Hex c = new Hex(token.getCoordinates().getQ(),token.getCoordinates().getR(),token.getCoordinates().getD());
        //Check if players bee in game
        //if(token.getPlayer().isBeeInGame() && !token.isBeetle() && !brokenHive(token)){
            //Check, if the token is a beetle, if its moving from the top of another token --> unmark it
            if(token.getType()==TokenType.BEETLE && token.getCoordinates().getD()!=0){
                //Token t = searchToken(new Hex(token.getCoordinates().getR(),token.getCoordinates().getQ(),token.getCoordinates().getD()-1));
                Token t = searchToken(new Hex(token.getCoordinates().getQ(),token.getCoordinates().getR(),token.getCoordinates().getD()-1));
                t.setBeetle(false);
            }
            //And if its moving on top of another --> mark it
            if(token.getType()== TokenType.BEETLE && hex.getD()!=0){
                //Token t = searchToken(new Hex(hex.getR(),hex.getQ(),hex.getD()-1));
                Token t = searchToken(new Hex(hex.getQ(),hex.getR(),hex.getD()-1));
                t.setBeetle(true);
            }
            //Add gap to available gaps
            this.availableGaps.add(c);
            //Free gap in the board
            deleteHex(token);
            //Delete gap neighbours if they haven't any neighbour
            deleteGapsAvailable(c);
            //Update coordinates of the token of the board
            updateCoordinates(token,hex);
            //Add neighbours null coordinates to gapsAvailable
            refreshGapsAvailable(hex);
            //Remove gap from available
            removeHexFromAvaliable(hex);
            //Update graph:
            //Delete vertex from the graph
            this.graph.removeVertex(token.getGraphId());
            //Get new neighbours
            Token[] newNeighbours = tokenNeighbours(token.getCoordinates());
            //Add token again
            this.graph.addVertex(token.getGraphId());
            //Add neighbours to graph
            for (Token newNeighbour : newNeighbours)
                if (newNeighbour != null)  this.graph.addEdge(token.getGraphId(), newNeighbour.getGraphId());
        //}
    }

    /**
     * Checks if a given position is blocked.
     * @param hex
     * @return
     */
    public boolean checkIfGapBlocked(Hex hex){
        //First: If number of neighbours more than 4;
        if(numberOfNeighbours(hex)>4) return true;
        //Second: If it has at less 2 consecutive neighbours free --> Not blocked
        if(numberOfNeighbours(hex)==4)
            if(checkIfBlockedByFourNeighbours(hex)) return true;
        //Third: If there is only 3 neighbours check that they don't block the gap
        if(numberOfNeighbours(hex)==3)
            if(checkIfBlockedByThreeNeighbours(hex)) return true;
        return false;
    }

    /**
     * Checks if a position surrounded by 4 tokens is blocked.
     * @param Hex
     * @return
     */
    private boolean checkIfBlockedByFourNeighbours(Hex hex){
        /*int x = Hex.getR();
        int y = Hex.getQ();*/
        int x = hex.getQ();
        int y = hex.getR();
        if(!isInBoard(new Hex(x+1,y-1,0)) && !isInBoard(new Hex(x+1,y,0))) return false;
        else if(!isInBoard(new Hex(x+1,y,0)) && !isInBoard(new Hex(x,y+1,0))) return false;
        else if(!isInBoard(new Hex(x,y+1,0)) && !isInBoard(new Hex(x-1,y+1,0))) return false;
        else if(!isInBoard(new Hex(x-1,y+1,0)) && !isInBoard(new Hex(x-1,y,0))) return false;
        else if(!isInBoard(new Hex(x-1,y,0)) && !isInBoard(new Hex(x,y-1,0))) return false;
        else if(!isInBoard(new Hex(x,y-1,0)) && !isInBoard(new Hex(x+1,y-1,0))) return false;
        else return true;
    }

    /**
     * If a position is surrounded by 3 separated tokens is blocked.
     * @param Hex
     * @return
     */
    private boolean checkIfBlockedByThreeNeighbours(Hex hex){
        /*int x = Hex.getR();
        int y = Hex.getQ();*/
        int x = hex.getQ();
        int y = hex.getR();
        if(!isInBoard(new Hex(x,y-1,0)) && !isInBoard(new Hex(x+1,y,0)) && !isInBoard(new Hex(x-1,y+1,0))) return true;
        else if(!isInBoard(new Hex(x+1,y-1,0)) && !isInBoard(new Hex(x,y+1,0)) && !isInBoard(new Hex(x-1,y,0))) return true;
        else return false;
    }

    /**
     * The bee can slide to an empty neighbour gap.
     * @param token
     * @return
     */
    private ArrayList<Hex> beeMoves(Token token) {
        ArrayList<Hex> possibleGaps = new ArrayList<>();
        ArrayList<Hex> realGaps = new ArrayList<>();
        ArrayList<Hex> nToken = new ArrayList<>();
        //First: Check if blocked
        if(!checkIfGapBlocked(token.getCoordinates())){
            //Second: Get neighbours
            possibleGaps = getNeighbourHex(token.getCoordinates());
            //Third : For each coordinate --> check if its a gap and that the token can slide to it
            for(int i=0; i<possibleGaps.size(); i++){
                if(possibleGaps.get(i).getD()==0){
                    if(checkGap(token.getCoordinates(),possibleGaps.get(i))==1)
                        realGaps.add(possibleGaps.get(i));
                }
            }
        }
        return realGaps;
    }

    /**
     * The grasshopper can jump over other insects.
     * @param hex
     * @return
     */
    private ArrayList<Hex> grasshopperMoves(Hex hex) {
        ArrayList<Hex> possibleGaps = new ArrayList<>();
        /*int x = Hex.getR();
        int y = Hex.getQ();*/
        int x = hex.getQ();
        int y = hex.getR();
        //Check 6 sides: While there is a neighbour -> keep jumping
        //Face 1
        int i=1;
        int j=1;
        while(isInBoard(new Hex(x+i,y-j,0))){

            i=i+1;
            j=j+1;
        }
        if(i!=1 && j!=1){
            Hex c = new Hex(hex.getQ()+i,hex.getR()-j,0);
            //Hex c = new Hex(hex.getR(),hex.getQ(),0);

            possibleGaps.add(c);
        }
        //Face 2
        i=1;
        while(isInBoard(new Hex(x+i,y, 0))){

            i=i+1;
        }
        if(i!=1){
            //Hex c = new Hex(hex.getR()+i,hex.getQ(),0);
            Hex c = new Hex(hex.getQ()+i,hex.getR(),0);

            possibleGaps.add(c);
        }
        //Face 3
        j=1;
        while(isInBoard(new Hex(x,y+j, 0))){
            Log.d("HEX GRASS",hex.toString());
            j=j+1;
        }
        if(j!=1){
            Hex c = new Hex(hex.getQ(),hex.getR()+j,0);
            //Hex c = new Hex(hex.getR(),hex.getQ(),0);
            possibleGaps.add(c);
        }
        //Face 4
        i=1;
        j=1;
        while(isInBoard(new Hex(x-i,y+j,0))){
            i=i+1;
            j= j +1;
        }
        if(i!=1 && j!=1){
            Hex c = new Hex(hex.getQ()-i,hex.getR()+j,0);
            possibleGaps.add(c);
        }
        //Face 5
        i=1;
        while(isInBoard(new Hex(x-i,y, 0))){
            i=i+1;
        }
        if(i!=1){
            Hex c = new Hex(hex.getQ()-i,hex.getR(),0);
            possibleGaps.add(c);
        }
        //Face 6
        j=1;
        while(isInBoard(new Hex(x,y-j, 0))){
            j=j+1;
        }
        if(j!=1){
            Hex c = new Hex(hex.getQ(),hex.getR()-j,0);
            possibleGaps.add(c);
        }
        return possibleGaps;
    }

    /**
     * The beetle can go on top of its neighbours or slide to an empty gap.
     * @param token
     * @return
     */
    private ArrayList<Hex> beetleMoves(Token token) {
        ArrayList<Hex> possibleGaps = new ArrayList<>();
        ArrayList<Hex> realGaps = new ArrayList<>();
        int x,y,z;
        possibleGaps = getNeighbourHex(token.getCoordinates());
        for(int i=0; i<possibleGaps.size(); i++){
            x=possibleGaps.get(i).getR();
            y=possibleGaps.get(i).getQ();
            z=possibleGaps.get(i).getD();
            if(z!=0){
                int n = checkGap(token.getCoordinates(),possibleGaps.get(i));
                if(n==1 || n==0) realGaps.add(possibleGaps.get(i));
            }else{
                if(token.getCoordinates().getD()>z){
                    realGaps.add(possibleGaps.get(i));
                }else{
                    if(checkGap(token.getCoordinates(),possibleGaps.get(i))==1) realGaps.add(possibleGaps.get(i));
                }
            }
        }
        System.out.println(realGaps.size());
        return realGaps;
    }

    /**
     * The spider can slide to a gap 3 gaps away. Exactly 3, not less not more.
     * @param token
     * @return
     */
    private ArrayList<Hex> spiderMoves(Token token) {
        ArrayList<Hex> realGaps = new ArrayList<>();
        ArrayList<Hex> l1Token = new ArrayList<>();
        ArrayList<Hex> l2Token = new ArrayList<>();
        ArrayList<Hex> l3Token = new ArrayList<>();

        //Save original Hex
        //Hex c1 = new Hex(token.getCoordinates().getR(),token.getCoordinates().getQ(),0);
        Hex c1 = new Hex(token.getCoordinates().getQ(),token.getCoordinates().getR(),0);

        //Level 1
        l1Token=getNeighbourHex(token.getCoordinates());
        for(int i=0;i<l1Token.size();i++){
            if(l1Token.get(i).getD()==0){
                if (checkGap(c1, l1Token.get(i))==1){
                    //Hex c2 = new Hex(l1Token.get(i).getR(),l1Token.get(i).getQ(), 0);
                    Hex c2 = new Hex(l1Token.get(i).getQ(),l1Token.get(i).getR(), 0);
                    //Move Spider to that gap
                    updateCoordinates(token,l1Token.get(i));
                    //Take neighbours
                    l2Token=getNeighbourHex(l1Token.get(i));
                    //Level 2
                    for(int j=0;j<l2Token.size();j++){
                        //if is not Hex from previous spider position
                        if(!(l2Token.get(j).getR()== c2.getR() && l2Token.get(j).getQ()==c2.getQ())
                                && !(l2Token.get(j).getR()== c1.getR() && l2Token.get(j).getQ()==c1.getQ())){
                            if(l2Token.get(j).getD()==0){
                                if(checkGap(l1Token.get(i),l2Token.get(j))==1){
                                    //Save original Hex
                                    //Hex c3 = new Hex(l2Token.get(j).getR(),l2Token.get(j).getQ(),0);
                                    Hex c3 = new Hex(l2Token.get(j).getQ(),l2Token.get(j).getR(),0);
                                    //Move Spider to that gap
                                    updateCoordinates(token,l2Token.get(j));
                                    //Take neighbours
                                    l3Token=getNeighbourHex(l2Token.get(j));
                                    //Level 3
                                    for(int k = 0;k<l3Token.size();k++){
                                        if(!(l3Token.get(k).getR()== c3.getR() && l3Token.get(k).getQ()==c3.getQ())
                                                && !(l3Token.get(k).getR()== c2.getR() && l3Token.get(k).getQ()==c2.getQ())
                                                && !(l3Token.get(k).getR()== c1.getR() && l3Token.get(k).getQ()==c1.getQ())){
                                            if(l3Token.get(k).getD()==0){
                                                if(checkGap(l2Token.get(j),l3Token.get(k))==1){
                                                    realGaps.add(l3Token.get(k));
                                                }
                                            }
                                        }
                                    }

                                }
                                updateCoordinates(token,c2);
                            }
                        }
                    }

                }
                updateCoordinates(token,c1);
            }

        }
        //leave spider in its original position
        updateCoordinates(token, c1);
        return realGaps;
    }

    /**
     * An ant could go in any gap that is not blocked.
     * @param token
     * @return
     */
    private ArrayList<Hex> antMoves(Token token) {
        //Hex c = new Hex(token.getCoordinates().getR(),token.getCoordinates().getQ(),token.getCoordinates().getD());
        Hex c = new Hex(token.getCoordinates().getQ(),token.getCoordinates().getR(),token.getCoordinates().getD());
        ArrayList<Hex> possibleGaps = new ArrayList<>();
        //First: Check if blocked
        if(!checkIfGapBlocked(token.getCoordinates())){
            //Second: take ant from the board
            //Free gap in the board
            deleteHex(token);
            //Delete gap neighbours if they haven't any neighbour
            deleteGapsAvailable(c);
            //Third: Get all available gaps and check if z==0 and then if they are not blocked
            for(int i=0; i<this.availableGaps.size(); i++){
                if(this.availableGaps.get(i).getD()==0)
                    if(!checkIfGapBlocked(this.availableGaps.get(i))) possibleGaps.add(this.availableGaps.get(i));
            }
        }
        //Fourth: Return ant to its original position
        updateCoordinates(token,c);
        this.availableGaps.add(c);
        return possibleGaps;
    }

    /**
     * Checks if a token could go in a gap
     * 1st checks that the gap is empty
     * 2nd checks if it has neighbours. If it has 2 the token can't slide to it.
     * @param token
     * @param Hex
     * @return
     */
    private int checkGap(Hex token, Hex hex){

        int n=0;

        /*int xt=token.getR();
        int yt=token.getQ();
        int xc=Hex.getR();
        int yc=Hex.getQ();
        int zc=Hex.getD();*/
        int xt=token.getQ();
        int yt=token.getR();
        int xc=hex.getQ();
        int yc=hex.getR();
        int zc=hex.getD();


        if(xc==xt+1 && yc==yt-1){
            if(!isInBoard(new Hex(xc,yc,zc))){
                if(isInBoard(new Hex(xc-1,yc,zc))) n=n+1;
                if(isInBoard(new Hex(xc,yc+1,zc))) n=n+1;
            }
        }else if(xc==xt+1 && yc==yt){
            if(!isInBoard(new Hex(xc,yc,zc))){
                if(isInBoard(new Hex(xc,yc-1,zc))) n=n+1;
                if(isInBoard(new Hex(xc-1,yc+1,zc))) n=n+1;
            }
        }else if(xc==xt && yc==yt+1){
            if(!isInBoard(new Hex(xc,yc,zc))){
                if(isInBoard(new Hex(xc+1,yc-1,zc))) n=n+1;
                if(isInBoard(new Hex(xc-1,yc,zc))) n=n+1;
            }
        }else if(xc==xt-1 && yc==yt+1){
            if(!isInBoard(new Hex(xc,yc,zc))){
                if(isInBoard(new Hex(xc+1,yc,zc))) n=n+1;
                if(isInBoard(new Hex(xc,yc-1,zc))) n=n+1;
            }
        }else if(xc==xt-1 && yc==yt){
            if(!isInBoard(new Hex(xc,yc,zc))){
                if(isInBoard(new Hex(xc,yc+1,zc))) n=n+1;
                if(isInBoard(new Hex(xc+1,yc-1,zc))) n=n+1;
            }
        }else if(xc==xt && yc==yt-1){
            if(!isInBoard(new Hex(xc,yc,zc))){
                if(isInBoard(new Hex(xc-1,yc+1,zc))) n=n+1;
                if(isInBoard(new Hex(xc+1,yc,zc))) n=n+1;
            }
        }

        return n;
    }

    /**
     *
     * @param p
     * @param n
     * @return
     */
    /*public String playerSelection(Player p, String n) {
        String tok = "";
        ArrayList<Token> tokens = p.playerSelectionV2(n);
        if(n.equals("h")){
            ArrayList<Token> playeableTokens = new ArrayList<Token>();
            BlockCutpointGraph bcg = new BlockCutpointGraph(this.graph);
            //Check if moving that token will break the hive
            for(int i=0; i<tokens.size(); i++){
                if(!bcg.isCutpoint(tokens.get(i).getGraphId()))
                    playeableTokens.add(tokens.get(i));
            }
            //Copy solution in a String
            for(int i=0;i<playeableTokens.size();i++){
                tok += "\nType: "+playeableTokens.get(i).getType()
                        +" #"+playeableTokens.get(i).getId()
                        +" Position-->: "+playeableTokens.get(i).getCoordinates().getR()+"-"
                        +playeableTokens.get(i).getCoordinates().getQ()+"-"
                        +playeableTokens.get(i).getCoordinates().getD();
            }
        }else{
            for(int i=0;i<tokens.size();i++){
                tok+="\nType: "+tokens.get(i).getType()+" #"+tokens.get(i).getId();
            }
        }
        return tok;
    }*/

    /**
     *
     * @param token
     * @return
     */
    public boolean brokenHive(Token token){
        BlockCutpointGraph bcg = new BlockCutpointGraph(this.graph);
        return bcg.isCutpoint(token.getGraphId());
    }


}

