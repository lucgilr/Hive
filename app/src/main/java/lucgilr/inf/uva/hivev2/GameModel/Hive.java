package lucgilr.inf.uva.hivev2.GameModel;


import java.util.ArrayList;
import java.util.Iterator;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.alg.BlockCutpointGraph;

/**
 *  Representation of the board.
 * @author Lucía Gil Román
 */
public final class Hive {

    private ArrayList<Coords> availableGaps;
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
        this.availableGaps.add(new Coords(0,0,0));
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
    public ArrayList<Coords> getAvailableGaps() {
        return availableGaps;
    }

    /**
     *
     * @param availableGaps
     */
    public void setAvailableGaps(ArrayList<Coords> availableGaps) {
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
     * @param coords
     */
    public void addToken(Token token, Coords coords){
        //If is a bee --> beeInGame
        if(token.getType()==TokenType.BEE) token.getPlayer().setBeeInGame(true);
        //Add coords to token
        token.setCoordinates(coords);
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
            if (neighbour != null) this.graph.addEdge(token.getGraphId(), neighbour.getGraphId());
        //Set token to inGame
        token.setInGame(true);
        //Delete gap from ArrayList of gaps avaliable
        removeCoordsFromAvaliable(token.getCoordinates());
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
                    +" Coordinates: "+this.board.get(i).getCoordinates().printCoords()+"\n";
        }
        return board;
    }

    /**
     * Checks if a token is in the board
     * @param coords of the token to look for
     * @return true if found in the board
     */
    private boolean isInBoard(Coords coords){
        for(int i=0;i<this.board.size();i++){
            if(this.board.get(i).getCoordinates().getX()==coords.getX()
                    && this.board.get(i).getCoordinates().getY()==coords.getY()
                    && this.board.get(i).getCoordinates().getZ()==coords.getZ())
                return true;
        }
        return false;
    }

    /**
     * Search a token in the board given its coordinates
     * @param coords of the token to look for
     * @return token if found
     */
    private Token searchToken(Coords coords){
        for (Token board1 : this.board) {
            if (board1.getCoordinates().getX() == coords.getX()
                    && board1.getCoordinates().getY() == coords.getY()
                    && board1.getCoordinates().getZ() == coords.getZ()) {
                return board1;
            }
        }
        return null;
    }

    /**
     * Updates token coordinates
     * @param token to update
     * @param coords new coordinates
     */
    private void updateCoordinates(Token token, Coords coords) {
        for (Token board1 : this.board) {
            if (board1.getCoordinates().getX() == token.getCoordinates().getX()
                    && board1.getCoordinates().getY() == token.getCoordinates().getY()
                    && board1.getCoordinates().getZ() == token.getCoordinates().getZ()) {
                board1.setCoordinates(coords);
            }
        }
    }

    /**
     * Deletes the coordinates of a token.
     * Updates it to an impossible position in the board: -100,-100,-100
     * @param token
     */
    private void deleteCoords(Token token){
        for (Token board1 : this.board) {
            if (board1.getCoordinates().getX() == token.getCoordinates().getX()
                    && board1.getCoordinates().getY() == token.getCoordinates().getY()
                    && board1.getCoordinates().getZ() == token.getCoordinates().getZ()) {
                board1.setCoordinates(new Coords(-100,-100,-100));
            }
        }
    }

    /**
     * returns the neighbours Tokens from a given gap.
     * @param coords
     * @return
     */
    private Token[] tokenNeighbours(Coords coords){
        int x = coords.getX();
        int y = coords.getY();
        int z = 0;
        Token[] n = new Token[6];
        //
        while(isInBoard(new Coords(x-2,y,z))) z=z+1;
        if(z==0) n[0]=searchToken(new Coords(x-2,y,z));
        else n[0]=searchToken(new Coords(x-2,y,z-1));
        z=0;
        //
        while(isInBoard(new Coords(x-1,y+1,z))) z=z+1;
        if(z==0) n[1]=searchToken(new Coords(x-1,y+1,z));
        else n[1]=searchToken(new Coords(x-1,y+1,z-1));
        z=0;
        //
        while(isInBoard(new Coords(x+1,y+1,z))) z=z+1;
        if(z==0) n[2]=searchToken(new Coords(x+1,y+1,z));
        else n[2]=searchToken(new Coords(x+1,y+1,z-1));
        z=0;
        //
        while(isInBoard(new Coords(x+2,y,z))) z=z+1;
        if(z==0) n[3]=searchToken(new Coords(x+2,y,z));
        else n[3]=searchToken(new Coords(x+2,y,z-1));
        z=0;
        //
        while(isInBoard(new Coords(x+1,y-1,z))) z=z+1;
        if(z==0) n[4]=searchToken(new Coords(x+1,y-1,z));
        else n[4]=searchToken(new Coords(x+1,y-1,z-1));
        z=0;
        //
        while(isInBoard(new Coords(x-1,y-1,z))) z=z+1;
        if(z==0) n[5]=searchToken(new Coords(x-1,y-1,z));
        else n[5]=searchToken(new Coords(x-1,y-1,z-1));
        z=0;
        //
        return n;
    }

    /**
     * counts how many neighbors has a given position.
     * @param coords
     * @return
     */
    private int numberOfNeighbours(Coords coords){
        int n = 0;
        Token[] nb = new Token[6];
        nb = tokenNeighbours(coords);
        for (int i=0;i<nb.length;i++){
            if(nb[i]!=null) n = n+1;
        }
        return n;
    }

    /**
     * checks if all the neighbors tokens for a given gap are from a specific player.
     * @param player
     * @param coords
     * @return
     */
    public boolean checkNeighboursTokenSamePlayer(Player player, Coords coords){
        Token[] n = new Token[6];
        n = tokenNeighbours(coords);
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
            if(this.availableGaps.get(i).getZ()==0){
                if(player.getTurn()!=1){
                    if(checkNeighboursTokenSamePlayer(player,this.getAvailableGaps().get(i)))
                        gaps+=this.availableGaps.get(i).printCoords()+"\n";
                }else{
                    gaps+=this.availableGaps.get(i).printCoords()+"\n";
                }
            }
        return gaps;
    }

    /**
     *
     * @param player
     * @return
     */
    public ArrayList<Coords> getPlayerGapsAvailable(Player player){
        ArrayList<Coords> gaps = new ArrayList<>();
        for(int i=0;i<this.availableGaps.size();i++)
            //A token can only be placed in the lower level of the hive
            if(this.availableGaps.get(i).getZ()==0){
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
    public boolean checkIfGapsAvailable(Player player){
        ArrayList<Coords> gaps = new ArrayList<Coords>();
        for(int i=0; i<this.getAvailableGaps().size();i++){
            if(this.availableGaps.get(i).getZ()==0){
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
                ns+="At ("+n[i].getCoordinates().getX()+","+n[i].getCoordinates().getY()+") Type: "+n[i].getType()+" Player: "+n[i].getPlayer().getColor();
        }
        return ns;
    }

    /**
     * Returns the list of positions surrounding a gap.
     * @param coords
     * @return
     */
    public ArrayList<Coords> getNeighbourCoords(Coords coords){
        int x = coords.getX();
        int y = coords.getY();
        int z = 0;
        ArrayList<Coords> neighbours = new ArrayList<Coords>();

        while(isInBoard(new Coords(x-2,y,z)))
            z=z+1;
        neighbours.add(new Coords(x-2,y,z));
        z=0;

        while(isInBoard(new Coords(x-1,y+1,z)))
            z=z+1;
        neighbours.add(new Coords(x-1,y+1,z));
        z=0;

        while(isInBoard(new Coords(x+1,y+1,z)))
            z=z+1;
        neighbours.add(new Coords(x+1,y+1,z));
        z=0;

        while(isInBoard(new Coords(x+2,y,z)))
            z=z+1;
        neighbours.add(new Coords(x+2,y,z));
        z=0;

        while(isInBoard(new Coords(x+1,y-1,z)))
            z=z+1;
        neighbours.add(new Coords(x+1,y-1,z));
        z=0;

        while(isInBoard(new Coords(x-1,y-1,z)))
            z=z+1;
        neighbours.add(new Coords(x-1,y-1,z));

        return neighbours;
    }

    /**
     * Deletes a coordinate from the list of available gaps.
     * @param coords
     */
    public void removeCoordsFromAvaliable(Coords coords){
        Iterator<Coords> it = this.availableGaps.iterator();
        while (it.hasNext()) {
            Coords c = it.next();
            if (c.getX()==coords.getX() && c.getY()==coords.getY() && c.getZ()==coords.getZ()) {
                it.remove();
            }
        }
    }

    /**
     * Checks if a positions is already in the list of available gaps.
     * @param coords
     * @return
     */
    private boolean checkIfDuplicate(Coords coords){
        for(int i=0;i<this.availableGaps.size();i++){
            if(this.availableGaps.get(i).getX()==coords.getX()
                    && this.availableGaps.get(i).getY()==coords.getY()
                    && this.availableGaps.get(i).getZ()==coords.getZ())
                return false;
        }
        return true;
    }

    /**
     * Add new positions to the list of available gaps.
     * Checks if the new position is not already in the list.
     * @param coords
     */
    private void refreshGapsAvailable(Coords coords) {
        ArrayList<Coords> newNeighbours = new ArrayList<>();
        newNeighbours = getNeighbourCoords(coords);
        for(int i=0;i<newNeighbours.size();i++){
            if(!isInBoard(newNeighbours.get(i)))
                if(checkIfDuplicate(newNeighbours.get(i)))
                    this.getAvailableGaps().add(newNeighbours.get(i));

        }
    }

    /**
     * Deletes gaps from available if they haven't any neighbour.
     * @param coords
     */
    private void deleteGapsAvailable(Coords coords) {
        ArrayList<Coords> oldNeighbours = new ArrayList<>();
        oldNeighbours = getNeighbourCoords(coords);
        for(int i=0; i<oldNeighbours.size();i++){
            //If this neighbours gap has no neighbours --> delete from gapsAvailable
            if(numberOfNeighbours(oldNeighbours.get(i))==0) removeCoordsFromAvaliable(oldNeighbours.get(i));
        }
    }

    /**
     * Returns a list of coordinates where a given token can be moved.
     * the token is already in game.
     * @param token
     * @return
     */
    public ArrayList<Coords> getPossibleGaps(Token token){
        ArrayList<Coords> possibleGaps = new ArrayList<>();
        // And if it has a beetle on top
        if(!token.isBeetle() && token.getPlayer().isBeeInGame()){
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
        ArrayList<Coords> possibleGaps = new ArrayList<>();
        possibleGaps = getPossibleGaps(token);
        for(int i=0;i<possibleGaps.size();i++){
            gaps+=" X: "+possibleGaps.get(i).getX()+" Y: "+possibleGaps.get(i).getY()+" Z: "+possibleGaps.get(i).getZ()+"\n";
        }
        return gaps;
    }

    /**
     * Moves a token from its currently position to a new one.
     * @param token to move
     * @param coords new position
     */
    public void movetoken(Token token, Coords coords){
        Coords c = new Coords(token.getCoordinates().getX(),token.getCoordinates().getY(),token.getCoordinates().getZ());
        //Check if players bee in game
        if(token.getPlayer().isBeeInGame() && !token.isBeetle()){
            //Check, if the token is a beetle, if its moving from the top of another token --> unmark it
            if(token.getType()==TokenType.BEETLE && token.getCoordinates().getZ()!=0){
                Token t = searchToken(new Coords(token.getCoordinates().getX(),token.getCoordinates().getY(),token.getCoordinates().getZ()-1));
                t.setBeetle(false);
            }
            //And if its moving on top of another --> mark it
            if(token.getType()==TokenType.BEETLE && coords.getZ()!=0){
                Token t = searchToken(new Coords(coords.getX(),coords.getY(),coords.getZ()-1));
                t.setBeetle(true);
            }
            //Add gap to available gaps
            this.availableGaps.add(c);
            //Free gap in the board
            deleteCoords(token);
            //Delete gap neighbours if they haven't any neighbour
            deleteGapsAvailable(c);
            //Update coordinates of the token of the board
            updateCoordinates(token,coords);
            //Add neighbours null coordinates to gapsAvailable
            refreshGapsAvailable(coords);
            //Remove gap from available
            removeCoordsFromAvaliable(coords);
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
        }
    }

    /**
     * Checks if a given position is blocked.
     * @param coords
     * @return
     */
    public boolean checkIfGapBlocked(Coords coords){
        //First: If number of neighbours more than 4;
        if(numberOfNeighbours(coords)>4) return true;
        //Second: If it has at less 2 consecutive neighbours free --> Not blocked
        if(numberOfNeighbours(coords)==4)
            if(checkIfBlockedByFourNeighbours(coords)) return true;
        //Third: If there is only 3 neighbours check that they don't block the gap
        if(numberOfNeighbours(coords)==3)
            if(checkIfBlockedByThreeNeighbours(coords)) return true;
        return false;
    }

    /**
     * Checks if a position surrounded by 4 tokens is blocked.
     * @param coords
     * @return
     */
    private boolean checkIfBlockedByFourNeighbours(Coords coords){
        int x = coords.getX();
        int y = coords.getY();
        if(!isInBoard(new Coords(x-2,y,0)) && !isInBoard(new Coords(x-1,y+1,0))) return false;
        else if(!isInBoard(new Coords(x-1,y+1,0)) && !isInBoard(new Coords(x+1,y+1,0))) return false;
        else if(!isInBoard(new Coords(x+1,y+1,0)) && !isInBoard(new Coords(x+2,y,0))) return false;
        else if(!isInBoard(new Coords(x+2,y,0)) && !isInBoard(new Coords(x+1,y-1,0))) return false;
        else if(!isInBoard(new Coords(x+1,y-1,0)) && !isInBoard(new Coords(x-1,y-1,0))) return false;
        else if(!isInBoard(new Coords(x-1,y-1,0)) && !isInBoard(new Coords(x-2,y,0))) return false;
        else return true;
    }

    /**
     * If a position is surrounded by 3 separated tokens is blocked.
     * @param coords
     * @return
     */
    private boolean checkIfBlockedByThreeNeighbours(Coords coords){
        int x = coords.getX();
        int y = coords.getY();
        if(!isInBoard(new Coords(x-2,y,0)) && !isInBoard(new Coords(x+1,y+1,0)) && !isInBoard(new Coords(x+1,y-1,0))) return true;
        else if(!isInBoard(new Coords(x-1,y+1,0)) && !isInBoard(new Coords(x+2,y,0)) && !isInBoard(new Coords(x-1,y-1,0))) return true;
        else return false;
    }

    /**
     * The bee can slide to an empty neighbour gap.
     * @param token
     * @return
     */
    private ArrayList<Coords> beeMoves(Token token) {
        ArrayList<Coords> possibleGaps = new ArrayList<>();
        ArrayList<Coords> realGaps = new ArrayList<>();
        ArrayList<Coords> nToken = new ArrayList<>();
        //First: Check if blocked
        if(!checkIfGapBlocked(token.getCoordinates())){
            //Second: Get neighbours
            possibleGaps = getNeighbourCoords(token.getCoordinates());
            //Third : For each coordinate --> check if its a gap and that the token can slide to it
            for(int i=0; i<possibleGaps.size(); i++){
                if(possibleGaps.get(i).getZ()==0){
                    if(checkGap(token.getCoordinates(),possibleGaps.get(i))==1)
                        realGaps.add(possibleGaps.get(i));
                }
            }
        }
        return realGaps;
    }

    /**
     * The grasshopper can jump over other insects.
     * @param coords
     * @return
     */
    private ArrayList<Coords> grasshopperMoves(Coords coords) {
        ArrayList<Coords> possibleGaps = new ArrayList<>();
        int x = coords.getX();
        int y = coords.getY();
        //Check 6 sides: While there is a neighbour -> keep jumping
        //Face 1
        int i=2;
        while(isInBoard(new Coords(x-i,y,0)))i=i+2;
        if(i!=2){
            Coords c = new Coords(coords.getX()-i,coords.getY(),0);
            possibleGaps.add(c);
        }
        //Face 2
        i=1;
        int j=1;
        while(isInBoard(new Coords(x-i,y+j,0))){
            i=i+1;
            j=j+1;
        }
        if(i!=1 && j!=1){
            Coords c = new Coords(coords.getX()-i,coords.getY()+j,0);
            possibleGaps.add(c);
        }
        //Face 3
        i=1;
        j=1;
        while(isInBoard(new Coords(x+i,y+j,0))){
            i=i+1;
            j=j+1;
        }
        if(i!=1 && j!=1){
            Coords c = new Coords(coords.getX()+i,coords.getY()+j,0);
            possibleGaps.add(c);
        }
        //Face 4
        i=2;
        while(isInBoard(new Coords(x+i,y,0))){
            i=i+2;
        }
        if(i!=2){
            Coords c = new Coords(coords.getX()+i,coords.getY(),0);
            possibleGaps.add(c);
        }
        //Face 5
        i=1;
        j=1;
        while(isInBoard(new Coords(x+i,y-j,0))){
            i=i+1;
            j=j+1;
        }
        if(i!=1 && j!=1){
            Coords c = new Coords(coords.getX()+i,coords.getY()-j,0);
            possibleGaps.add(c);
        }
        //Face 6
        i=1;
        j=1;
        while(isInBoard(new Coords(x-i,y-j,0))){
            i=i+1;
            j=j+1;
        }
        if(i!=1 && j!=1){
            Coords c = new Coords(coords.getX()-i,coords.getY()-j,0);
            possibleGaps.add(c);
        }
        return possibleGaps;
    }

    /**
     * The beetle can go on top of its neighbours or slide to an empty gap.
     * @param token
     * @return
     */
    private ArrayList<Coords> beetleMoves(Token token) {
        ArrayList<Coords> possibleGaps = new ArrayList<>();
        ArrayList<Coords> realGaps = new ArrayList<>();
        int x,y,z;
        possibleGaps = getNeighbourCoords(token.getCoordinates());
        for(int i=0; i<possibleGaps.size(); i++){
            x=possibleGaps.get(i).getX();
            y=possibleGaps.get(i).getY();
            z=possibleGaps.get(i).getZ();
            if(z!=0){
                int n = checkGap(token.getCoordinates(),possibleGaps.get(i));
                if(n==1 || n==0) realGaps.add(possibleGaps.get(i));
            }else{
                if(token.getCoordinates().getZ()>z){
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
    private ArrayList<Coords> spiderMoves(Token token) {
        ArrayList<Coords> realGaps = new ArrayList<>();
        ArrayList<Coords> l1Token = new ArrayList<>();
        ArrayList<Coords> l2Token = new ArrayList<>();
        ArrayList<Coords> l3Token = new ArrayList<>();

        //Save original coords
        Coords c1 = new Coords(token.getCoordinates().getX(),token.getCoordinates().getY(),0);

        //Level 1
        l1Token=getNeighbourCoords(token.getCoordinates());
        for(int i=0;i<l1Token.size();i++){
            if(l1Token.get(i).getZ()==0){
                if(checkGap(c1,l1Token.get(i))==1){
                    Coords c2 = new Coords(l1Token.get(i).getX(),l1Token.get(i).getY(),0);
                    //Move Spider to that gap
                    updateCoordinates(token,l1Token.get(i));
                    System.out.println("First move: "+l1Token.get(i).printCoords());
                    //Take neighbours
                    l2Token=getNeighbourCoords(l1Token.get(i));
                    //Level 2
                    for(int j=0;j<l2Token.size();j++){
                        //if is not coords from previous spider position
                        if(!(l2Token.get(j).getX()== c2.getX() && l2Token.get(j).getY()==c2.getY())
                                && !(l2Token.get(j).getX()== c1.getX() && l2Token.get(j).getY()==c1.getY())){
                            if(l2Token.get(j).getZ()==0){
                                if(checkGap(l1Token.get(i),l2Token.get(j))==1){
                                    //Save original coords
                                    Coords c3 = new Coords(l2Token.get(j).getX(),l2Token.get(j).getY(),0);
                                    //Move Spider to that gap
                                    updateCoordinates(token,l2Token.get(j));
                                    System.out.println("Second move: "+l2Token.get(j).printCoords());
                                    //Take neighbours
                                    l3Token=getNeighbourCoords(l2Token.get(j));
                                    //Level 3
                                    for(int k=0;k<l3Token.size();k++){
                                        if(!(l3Token.get(k).getX()== c3.getX() && l3Token.get(k).getY()==c3.getY())
                                                && !(l3Token.get(k).getX()== c2.getX() && l3Token.get(k).getY()==c2.getY())
                                                && !(l3Token.get(k).getX()== c1.getX() && l3Token.get(k).getY()==c1.getY())){
                                            if(l3Token.get(k).getZ()==0){
                                                if(checkGap(l2Token.get(j),l3Token.get(k))==1){
                                                    System.out.println("Third move: "+l3Token.get(k).printCoords());
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
        updateCoordinates(token,c1);
        return realGaps;
    }

    /**
     * An ant could go in any gap that is not blocked.
     * @param token
     * @return
     */
    private ArrayList<Coords> antMoves(Token token) {
        Coords c = new Coords(token.getCoordinates().getX(),token.getCoordinates().getY(),token.getCoordinates().getZ());
        ArrayList<Coords> possibleGaps = new ArrayList<>();
        //First: Check if blocked
        if(!checkIfGapBlocked(token.getCoordinates())){
            //Second: take ant from the board
            //Free gap in the board
            deleteCoords(token);
            //Delete gap neighbours if they haven't any neighbour
            deleteGapsAvailable(c);
            //Third: Get all available gaps and check if z==0 and then if they are not blocked
            for(int i=0; i<this.availableGaps.size(); i++){
                if(this.availableGaps.get(i).getZ()==0)
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
     * @param coords
     * @return
     */
    private int checkGap(Coords token, Coords coords){

        int n=0;

        int xt=token.getX();
        int yt=token.getY();
        int xc=coords.getX();
        int yc=coords.getY();
        int zc=coords.getZ();


        if(xc==xt-2 && yc==yt){
            if(!isInBoard(new Coords(xc,yc,zc))){
                if(isInBoard(new Coords(xc+1,yc-1,zc))) n=n+1;
                if(isInBoard(new Coords(xc+1,yc+1,zc))) n=n+1;
            }
        }else if(xc==xt-1 && yc==yt+1){
            if(!isInBoard(new Coords(xc,yc,zc))){
                if(isInBoard(new Coords(xc-1,yc-1,zc))) n=n+1;
                if(isInBoard(new Coords(xc+2,yc,zc))) n=n+1;
            }
        }else if(xc==xt+1 && yc==yt+1){
            if(!isInBoard(new Coords(xc,yc,zc))){
                if(isInBoard(new Coords(xc-2,yc,zc))) n=n+1;
                if(isInBoard(new Coords(xc+1,yc-1,zc))) n=n+1;
            }
        }else if(xc==xt+2 && yc==yt){
            if(!isInBoard(new Coords(xc,yc,zc))){
                if(isInBoard(new Coords(xc-1,yc+1,zc))) n=n+1;
                if(isInBoard(new Coords(xc-1,yc-1,zc))) n=n+1;
            }
        }else if(xc==xt+1 && yc==yt-1){
            if(!isInBoard(new Coords(xc,yc,zc))){
                if(isInBoard(new Coords(xc+1,yc+1,zc))) n=n+1;
                if(isInBoard(new Coords(xc-2,yc,zc))) n=n+1;
            }
        }else if(xc==xt-1 && yc==yt-1){
            if(!isInBoard(new Coords(xc,yc,zc))){
                if(isInBoard(new Coords(xc+2,yc,zc))) n=n+1;
                if(isInBoard(new Coords(xc-1,yc+1,zc))) n=n+1;
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
    public String playerSelection(Player p, String n) {
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
                        +" Position-->: "+playeableTokens.get(i).getCoordinates().getX()+"-"
                        +playeableTokens.get(i).getCoordinates().getY()+"-"
                        +playeableTokens.get(i).getCoordinates().getZ();
            }
        }else{
            for(int i=0;i<tokens.size();i++){
                tok+="\nType: "+tokens.get(i).getType()+" #"+tokens.get(i).getId();
            }
        }
        return tok;
    }

}

