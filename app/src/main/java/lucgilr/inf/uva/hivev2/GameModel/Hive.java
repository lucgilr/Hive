package lucgilr.inf.uva.hivev2.GameModel;


import java.util.ArrayList;
import java.util.Iterator;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.alg.BlockCutpointGraph;

/**
 * @author Lucía Gil Román
 *
 * Representation of the Hive.
 * The Hive has a fictional board, it is made by putting together pieces.
 */
public final class Hive {

    private ArrayList<Hexagon> availableHexagons;
    private ArrayList<Piece> board;
    private UndirectedGraph<Integer,DefaultEdge> graph;
    private int vertex;

    /**
     *
     */
    public Hive(){
        this.board=new ArrayList<>();
        availableHexagons = new ArrayList<>();
        this.availableHexagons.add(new Hexagon(0, 0, 0));
        this.graph = new SimpleGraph<Integer,DefaultEdge>(DefaultEdge.class);
        this.vertex=0;
    }

    /**
     *
     * @return
     */
    public ArrayList<Piece> getBoard() {
        return board;
    }

    /**
     *
     * @param board
     */
    public void setBoard(ArrayList<Piece> board) {
        this.board = board;
    }

    /**
     *
     * @return
     */
    public ArrayList<Hexagon> getAvailableHexagons() {
        return availableHexagons;
    }

    /**
     *
     * @param availableHexagons
     */
    public void setAvailableHexagons(ArrayList<Hexagon> availableHexagons) {
        this.availableHexagons = availableHexagons;
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
     * Adds a piece to a hexagon on the board.
     * Deletes de used hexagon from the list of available hexagons for its use and adds the new ones from
     * the new piece.
     * @param piece
     * @param hexagon
     */
    public void addPiece(Piece piece, Hexagon hexagon, boolean graph){
        //If is a bee --> set the player's bee in game as true
        if(piece.getType()== PieceType.BEE) piece.getPlayer().setBeeInGame(true);
        //Add Hexagon position to the piece
        piece.setHexagon(hexagon);
        //Place piece on the board
        this.board.add(piece);
        //Get neighbours
        Piece[] neighbours = new Piece[6];
        neighbours = pieceNeighbours(piece.getHexagon());
        if(graph) {
            //Set piece a graph id
            piece.setGraphId(this.vertex);
            this.vertex = this.vertex + 1;
            //Add piece to graph
            this.graph.addVertex(piece.getGraphId());
            //Add edges (neighbours) to the vertex
            for (Piece neighbour : neighbours)
                if (neighbour != null) {
                    this.graph.addEdge(piece.getGraphId(), neighbour.getGraphId());
                }
        }
        //Delete hexagon from ArrayList of available hexagons
        removeHexFromAvailable(piece.getHexagon(), this.availableHexagons);
        //add new neighbours if they are not already there and have no piece
        refreshAvailableHexagons(piece.getHexagon());
    }

    /**
     * Search a piece in the board given its hexagon
     * @param hexagon --> position of the piece to look for
     * @return piece if found
     */
    public Piece searchPiece(Hexagon hexagon){
        for (Piece board1 : this.board) {
            if (board1.getHexagon().toString().equals(hexagon.toString())) {
                return board1;
            }
        }
        return null;
    }

    /**
     * Moves piece to a new hexagon
     * @param piece to update
     * @param hexagon new hexagon
     */
    public void updateHexagon(Piece piece, Hexagon hexagon) {
        for (Piece board1 : this.board) {
            if (board1.getHexagon().toString().equals(piece.getHexagon().toString())) {
                board1.setHexagon(hexagon);
            }
        }
    }

    /**
     * "Moves" the piece to the reserve hexagon. The reserve hexagon has the coordinates (-100,-100,-100)
     * HAY UNA PARECIDA --> LA ANTERIOR
     * @param piece
     */
    public void deleteHexagon(Piece piece){
        for (Piece board1 : this.board) {
            if (board1.getHexagon().toString().equals(piece.getHexagon().toString())) {
                board1.setHexagon(new Hexagon(-100, -100, -100));
            }
        }
    }

    /**
     * returns the neighbours of a piece from a given hexagon.
     * @param hexagon
     * @return
     */
    public Piece[] pieceNeighbours(Hexagon hexagon){

        int x = hexagon.getQ();
        int y = hexagon.getR();
        int z = 0;
        Piece[] n = new Piece[6];
        //
        while(searchPiece(new Hexagon(x+1,y-1,z))!=null) z=z+1;
        if(z==0) n[0]= searchPiece(new Hexagon(x + 1, y - 1, z));
        else n[0]= searchPiece(new Hexagon(x + 1, y - 1, z - 1));
        z=0;
        //
        while(searchPiece(new Hexagon(x+1,y,z))!=null) z=z+1;
        if(z==0) n[1]= searchPiece(new Hexagon(x + 1, y, z));
        else n[1]= searchPiece(new Hexagon(x + 1, y, z - 1));
        z=0;
        //
        while(searchPiece(new Hexagon(x,y+1,z))!=null) z=z+1;
        if(z==0) n[2]= searchPiece(new Hexagon(x, y + 1, z));
        else n[2]= searchPiece(new Hexagon(x, y + 1, z - 1));
        z=0;
        //
        while(searchPiece(new Hexagon(x-1,y+1,z))!=null) z=z+1;
        if(z==0) n[3]= searchPiece(new Hexagon(x - 1, y + 1, z));
        else n[3]= searchPiece(new Hexagon(x - 1, y + 1, z - 1));
        z=0;
        //
        while(searchPiece(new Hexagon(x-1,y,z))!=null) z=z+1;
        if(z==0) n[4]= searchPiece(new Hexagon(x - 1, y, z));
        else n[4]= searchPiece(new Hexagon(x - 1, y, z - 1));
        z=0;
        //
        while(searchPiece(new Hexagon(x,y-1,z))!=null) z=z+1;
        if(z==0) n[5]= searchPiece(new Hexagon(x, y - 1, z));
        else n[5]= searchPiece(new Hexagon(x, y - 1, z - 1));
        z=0;
        //
        return n;
    }

    /**
     * counts how many neighbors has a given hexagon.
     * @param hexagon
     * @return
     */
    public int numberOfNeighbours(Hexagon hexagon){
        int n = 0;
        Piece[] nb = new Piece[6];
        nb = pieceNeighbours(hexagon);
        for (int i=0;i<nb.length;i++){
            if(nb[i]!=null) n = n+1;
        }
        return n;
    }

    /**
     * Checks if all the neighbors of a piece for a given hexagon are from a specific player.
     * @param player
     * @param hexagon
     * @return
     */
    public boolean checkNeighboursPieceSamePlayer(Player player, Hexagon hexagon){
        Piece[] n = new Piece[6];
        n = pieceNeighbours(hexagon);
        for (Piece n1 : n) {
            if (n1 != null) {
                if (!n1.getPlayer().getColor().equals(player.getColor())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the hexagons where a player can place a piece.
     * @param player
     * @return
     */
    public ArrayList<Hexagon> getAvailableHexagonsPlayer(Player player){
        ArrayList<Hexagon> hexagons = new ArrayList<>();
        for(int i=0;i<this.availableHexagons.size();i++) {
            //A piece can only be placed for the first time in the lower level of the hive
            if (this.availableHexagons.get(i).getD() == 0) {

                if (player.getTurn() != 1) {
                    if (checkNeighboursPieceSamePlayer(player, this.getAvailableHexagons().get(i))) {
                        hexagons.add(this.availableHexagons.get(i));
                    }
                } else {
                    hexagons.add(this.availableHexagons.get(i));
                }
            }
        }
        return hexagons;
    }

    /**
     * Returns the list of hexagons surrounding an hexagon.
     * @param hexagon
     * @return
     */
    public ArrayList<Hexagon> getNeighbourHex(Hexagon hexagon){

        int x = hexagon.getQ();
        int y = hexagon.getR();
        int z = 0;
        ArrayList<Hexagon> neighbours = new ArrayList<>();

        while(searchPiece(new Hexagon(x+1,y-1,z))!=null)
            z=z+1;
        neighbours.add(new Hexagon(x+1,y-1,z));
        z=0;

        while(searchPiece(new Hexagon(x+1,y,z))!=null)
            z=z+1;
        neighbours.add(new Hexagon(x+1,y,z));
        z=0;

        while(searchPiece(new Hexagon(x,y+1,z))!=null)
            z=z+1;
        neighbours.add(new Hexagon(x,y+1,z));
        z=0;

        while(searchPiece(new Hexagon(x-1,y+1,z))!=null)
            z=z+1;
        neighbours.add(new Hexagon(x-1,y+1,z));
        z=0;

        while(searchPiece(new Hexagon(x-1,y,z))!=null)
            z=z+1;
        neighbours.add(new Hexagon(x-1,y,z));
        z=0;

        while(searchPiece(new Hexagon(x,y-1,z))!=null)
            z=z+1;
        neighbours.add(new Hexagon(x,y-1,z));

        return neighbours;
    }

    /**
     * Deletes a hexagon from a given list with hexagons.
     * @param hexagon
     */
    public void removeHexFromAvailable(Hexagon hexagon, ArrayList<Hexagon> list){
        Iterator<Hexagon> it = list.iterator();
        while (it.hasNext()) {
            Hexagon c = it.next();
            if (c.toString().equals(hexagon.toString())) {
                it.remove();
            }
        }
    }

    /**
     * Checks if a hexagon is already in the list of available hexagons.
     * @param hexagon
     * @return
     */
    private boolean checkIfDuplicate(Hexagon hexagon){
        for(int i=0;i < this.availableHexagons.size();i++){
            if(this.availableHexagons.get(i).toString().equals(hexagon.toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add new hexagon to the list of available hexagons.
     * Checks if the new hexagon is not already in the list.
     * @param hexagon
     */
    private void refreshAvailableHexagons(Hexagon hexagon) {
        ArrayList<Hexagon> newNeighbours = new ArrayList<>();
        newNeighbours = getNeighbourHex(hexagon);
        for(int i=0;i<newNeighbours.size();i++){
            if(searchPiece(newNeighbours.get(i))==null)
                if(checkIfDuplicate(newNeighbours.get(i)))
                    this.getAvailableHexagons().add(newNeighbours.get(i));

        }

    }

    /**
     * Deletes a hexagon from a given list with hexagons if they haven't any neighbour.
     * @param hexagon
     */
    private void deleteAvailableHexagons(Hexagon hexagon, ArrayList<Hexagon> list) {
        ArrayList<Hexagon> oldNeighbours = new ArrayList<>();
        oldNeighbours = getNeighbourHex(hexagon);
        for(int i=0; i<oldNeighbours.size();i++){
            //If this neighbours has no neighbours --> delete from AvailableHexagons
            if(numberOfNeighbours(oldNeighbours.get(i))==0) removeHexFromAvailable(oldNeighbours.get(i), list);
        }
    }

    /**
     * Returns a list of hexagons where a given piece can be moved.
     * the piece is already in game.
     * IA -->
     * @param piece
     * @return
     */
    public ArrayList<Hexagon> getPossibleHexagons(Piece piece, boolean ia){
        ArrayList<Hexagon> possibleHexagons = new ArrayList<>();
        boolean brokenHive = brokenHive(piece);
        if(ia) brokenHive = false;
        if(!piece.isBeetle() && piece.getPlayer().isBeeInGame() && !brokenHive){
            switch(piece.getType()){
                case BEE: possibleHexagons = beeMoves(piece);
                    break;
                case GRASSHOPPER: possibleHexagons = grasshopperMoves(piece.getHexagon());
                    break;
                case BEETLE: possibleHexagons = beetleMoves(piece);
                    break;
                case SPIDER: possibleHexagons = spiderMoves(piece);
                    break;
                case ANT: possibleHexagons = antMoves(piece);
                    break;
                default: break;
            }
        }
        return possibleHexagons;
    }

    /**
     * Moves a piece from its currently hexagon to a new one.
     * IA
     * @param piece to move
     * @param hexagon new position
     */
    public void movePiece(Piece piece, Hexagon hexagon, boolean ia){
        Hexagon c = new Hexagon(piece.getHexagon().getQ(), piece.getHexagon().getR(), piece.getHexagon().getD());
        if(!ia) {
            //Check, if the piece is a beetle, if its moving from the top of another piece --> unmark it
            if (piece.getType() == PieceType.BEETLE && piece.getHexagon().getD() != 0) {
                Piece t = searchPiece(new Hexagon(piece.getHexagon().getQ(), piece.getHexagon().getR(), piece.getHexagon().getD() - 1));
                t.setBeetle(false);
            }
            //And if its moving on top of another --> mark it
            if (piece.getType() == PieceType.BEETLE && hexagon.getD() != 0) {
                Piece t = searchPiece(new Hexagon(hexagon.getQ(), hexagon.getR(), hexagon.getD() - 1));
                t.setBeetle(true);
            }
        }
            //Add hexagon to available hexagons list
            this.availableHexagons.add(c);
            //Free hexagon in the board
            deleteHexagon(piece);
            //Delete hexagon neighbours if they haven't any neighbour
            deleteAvailableHexagons(c, this.availableHexagons);
            //Update hexagon of the piece of the board
            updateHexagon(piece, hexagon);
        //if(!ia) {
            refreshAvailableHexagons(hexagon);
            //Remove hexagon from available
            removeHexFromAvailable(hexagon, this.availableHexagons);//}

            //Update graph:
            //Delete vertex from the graph
            this.graph.removeVertex(piece.getGraphId());
            //If the destination hexagon is in level 0
            //Get new neighbours
            Piece[] newNeighbours = pieceNeighbours(piece.getHexagon());
            //Add piece again
            this.graph.addVertex(piece.getGraphId());
            //Add neighbours to graph
            //Log.d("Graph move piece","Graph move piece");
            //Log.d("piece",piece.pieceInfo());
            for (Piece newNeighbour : newNeighbours)
                if (newNeighbour != null){
                    //Log.d("neighbour",newNeighbour.pieceInfo());
                    this.graph.addEdge(piece.getGraphId(), newNeighbour.getGraphId());
                }
    }

    /**
     * Checks if a given piece is blocked.
     * @param piece
     * @return
     */
    public boolean checkIfPieceBlocked(Piece piece){
        //First: If moving the piece breaks the hive...
        if(brokenHive(piece)){
            return true;
        }
        if(piece.getType().equals(PieceType.BEE) || piece.getType().equals(PieceType.SPIDER) || piece.getType().equals(PieceType.ANT)) {
            //Second: If number of neighbours more than 4;
            if (numberOfNeighbours(piece.getHexagon()) > 4) return true;
            //Third: If it has at less 2 consecutive neighbours free --> Not blocked
            if (numberOfNeighbours(piece.getHexagon()) == 4)
                if (checkIfBlockedByFourNeighbours(piece.getHexagon())) return true;
            //Fourth: If there is only 3 neighbours check that they don't block the hexagon
            if (numberOfNeighbours(piece.getHexagon()) == 3)
                if (checkIfBlockedByThreeNeighbours(piece.getHexagon())) return true;
        }
        return false;
    }

    /**
     *
     * @param hexagon
     * @return
     */
    public boolean checkIfHexagonBlocked(Hexagon hexagon){
            //Second: If number of neighbours more than 4;
            if (numberOfNeighbours(hexagon) > 4) return true;
            //Third: If it has at less 2 consecutive neighbours free --> Not blocked
            if (numberOfNeighbours(hexagon) == 4)
                if (checkIfBlockedByFourNeighbours(hexagon)) return true;
            //Fourth: If there is only 3 neighbours check that they don't block the hexagon
            if (numberOfNeighbours(hexagon) == 3)
                if (checkIfBlockedByThreeNeighbours(hexagon)) return true;
        return false;
    }

    /**
     * Checks if a hexagon surrounded by 4 pieces is blocked.
     * @param hexagon
     * @return
     */
    private boolean checkIfBlockedByFourNeighbours(Hexagon hexagon){

        int x = hexagon.getQ();
        int y = hexagon.getR();

        if(searchPiece(new Hexagon(x+1,y-1,0))==null && searchPiece(new Hexagon(x+1,y,0))==null) return false;
        else if(searchPiece(new Hexagon(x+1,y,0))==null && searchPiece(new Hexagon(x,y+1,0))==null) return false;
        else if(searchPiece(new Hexagon(x,y+1,0))==null && searchPiece(new Hexagon(x-1,y+1,0))==null) return false;
        else if(searchPiece(new Hexagon(x-1,y+1,0))==null && searchPiece(new Hexagon(x-1,y,0))==null) return false;
        else if(searchPiece(new Hexagon(x-1,y,0))==null && searchPiece(new Hexagon(x,y-1,0))==null) return false;
        else if(searchPiece(new Hexagon(x,y-1,0))==null && searchPiece(new Hexagon(x+1,y-1,0))==null) return false;
        else return true;
    }

    /**
     * If a hexagon is surrounded by 3 separated pieces is blocked.
     * @param hexagon
     * @return
     */
    private boolean checkIfBlockedByThreeNeighbours(Hexagon hexagon){

        int x = hexagon.getQ();
        int y = hexagon.getR();

        if(searchPiece(new Hexagon(x,y-1,0))==null && searchPiece(new Hexagon(x+1,y,0))==null && searchPiece(new Hexagon(x-1,y+1,0))==null) return true;
        else if(searchPiece(new Hexagon(x+1,y-1,0))==null && searchPiece(new Hexagon(x,y+1,0))==null && searchPiece(new Hexagon(x-1,y,0))==null) return true;
        else return false;
    }

    /**
     * The bee can slide to an empty neighbour hexagon.
     * @param piece
     * @return
     */
    private ArrayList<Hexagon> beeMoves(Piece piece) {
        ArrayList<Hexagon> possibleHex = new ArrayList<>();
        ArrayList<Hexagon> realHex = new ArrayList<>();
        //First: Check if blocked
        if(!checkIfPieceBlocked(piece)){
            //Second: Get neighbours
            possibleHex = getNeighbourHex(piece.getHexagon());
            //Third : For each hexagon --> check if the piece can slide to it
            for(int i=0; i<possibleHex.size(); i++){
                if(possibleHex.get(i).getD()==0){
                    if(checkHexagon(piece.getHexagon(), possibleHex.get(i))==1)
                        realHex.add(possibleHex.get(i));
                }
            }
        }
        return realHex;
    }

    /**
     * The grasshopper can jump over other insects.
     * @param hexagon
     * @return
     */
    private ArrayList<Hexagon> grasshopperMoves(Hexagon hexagon) {

        ArrayList<Hexagon> possibleHexagons = new ArrayList<>();
        int x = hexagon.getQ();
        int y = hexagon.getR();

        //Check 6 sides: While there is a neighbour -> keep jumping
        //Face 1
        int i=1;
        int j=1;
        while(searchPiece(new Hexagon(x+i,y-j,0))!=null){

            i=i+1;
            j=j+1;
        }
        if(i!=1 && j!=1){
            Hexagon c = new Hexagon(hexagon.getQ()+i, hexagon.getR()-j,0);
            possibleHexagons.add(c);
        }
        //Face 2
        i=1;
        while(searchPiece(new Hexagon(x+i,y,0))!=null){
            i=i+1;
        }
        if(i!=1){
            Hexagon c = new Hexagon(hexagon.getQ()+i, hexagon.getR(),0);
            possibleHexagons.add(c);
        }
        //Face 3
        j=1;
        while(searchPiece(new Hexagon(x,y+j, 0))!=null){
            j=j+1;
        }
        if(j!=1){
            Hexagon c = new Hexagon(hexagon.getQ(), hexagon.getR()+j,0);
            possibleHexagons.add(c);
        }
        //Face 4
        i=1;
        j=1;
        while(searchPiece(new Hexagon(x-i,y+j,0))!=null){
            i=i+1;
            j= j +1;
        }
        if(i!=1 && j!=1){
            Hexagon c = new Hexagon(hexagon.getQ()-i, hexagon.getR()+j,0);
            possibleHexagons.add(c);
        }
        //Face 5
        i=1;
        while(searchPiece(new Hexagon(x-i,y, 0))!=null){
            i=i+1;
        }
        if(i!=1){
            Hexagon c = new Hexagon(hexagon.getQ()-i, hexagon.getR(),0);
            possibleHexagons.add(c);
        }
        //Face 6
        j=1;
        while(searchPiece(new Hexagon(x,y-j, 0))!=null){
            j=j+1;
        }
        if(j!=1){
            Hexagon c = new Hexagon(hexagon.getQ(), hexagon.getR()-j,0);
            possibleHexagons.add(c);
        }
        return possibleHexagons;
    }

    /**
     * The beetle can go on top of its neighbours or slide to an empty hexagon.
     * @param piece
     * @return
     */
    private ArrayList<Hexagon> beetleMoves(Piece piece) {
        ArrayList<Hexagon> possibleHexagons = new ArrayList<>();
        ArrayList<Hexagon> realHexagons = new ArrayList<>();
        int x,y,z;
        possibleHexagons = getNeighbourHex(piece.getHexagon());
        for(int i=0; i<possibleHexagons.size(); i++){
            x=possibleHexagons.get(i).getR();
            y=possibleHexagons.get(i).getQ();
            z=possibleHexagons.get(i).getD();
            if(z!=0){
                int n = checkHexagon(piece.getHexagon(), possibleHexagons.get(i));
                if(n==1 || n==0) realHexagons.add(possibleHexagons.get(i));
            }else{
                if(piece.getHexagon().getD()>z){
                    realHexagons.add(possibleHexagons.get(i));
                }else{
                    if(checkHexagon(piece.getHexagon(), possibleHexagons.get(i))==1) realHexagons.add(possibleHexagons.get(i));
                }
            }
        }
        System.out.println(realHexagons.size());
        return realHexagons;
    }

    /**
     * The spider can slide to a hexagon 3 hexagons away. Exactly 3, not less not more.
     * @param piece
     * @return
     */
    private ArrayList<Hexagon> spiderMoves(Piece piece) {
        ArrayList<Hexagon> realHexagons = new ArrayList<>();
        ArrayList<Hexagon> l1Piece = new ArrayList<>();
        ArrayList<Hexagon> l2Piece = new ArrayList<>();
        ArrayList<Hexagon> l3Piece = new ArrayList<>();

        //Save original Hexagon
        Hexagon c1 = new Hexagon(piece.getHexagon().getQ(), piece.getHexagon().getR(),0);

        //Level 1
        l1Piece=getNeighbourHex(piece.getHexagon());
        for(int i=0;i<l1Piece.size();i++){
            if(l1Piece.get(i).getD()==0){
                if (checkHexagon(c1, l1Piece.get(i))==1){
                    //Hexagon c2 = new Hexagon(l1Piece.get(i).getR(),l1Piece.get(i).getQ(), 0);
                    Hexagon c2 = new Hexagon(l1Piece.get(i).getQ(),l1Piece.get(i).getR(), 0);
                    //Move Spider to that hexagon
                    updateHexagon(piece, l1Piece.get(i));
                    //Take neighbours
                    l2Piece=getNeighbourHex(l1Piece.get(i));
                    //Level 2
                    for(int j=0;j<l2Piece.size();j++){
                        //if is not the hexagon from previous spider position
                        if(!(l2Piece.get(j).getR()== c2.getR() && l2Piece.get(j).getQ()==c2.getQ())
                                && !(l2Piece.get(j).getR()== c1.getR() && l2Piece.get(j).getQ()==c1.getQ())){
                            if(l2Piece.get(j).getD()==0){
                                if(checkHexagon(l1Piece.get(i), l2Piece.get(j))==1){
                                    //Save original Hexagon
                                    //Hexagon c3 = new Hexagon(l2Piece.get(j).getR(),l2Piece.get(j).getQ(),0);
                                    Hexagon c3 = new Hexagon(l2Piece.get(j).getQ(),l2Piece.get(j).getR(),0);
                                    //Move Spider to that hexagon
                                    updateHexagon(piece, l2Piece.get(j));
                                    //Take neighbours
                                    l3Piece=getNeighbourHex(l2Piece.get(j));
                                    //Level 3
                                    for(int k = 0;k<l3Piece.size();k++){
                                        if(!(l3Piece.get(k).getR()== c3.getR() && l3Piece.get(k).getQ()==c3.getQ())
                                                && !(l3Piece.get(k).getR()== c2.getR() && l3Piece.get(k).getQ()==c2.getQ())
                                                && !(l3Piece.get(k).getR()== c1.getR() && l3Piece.get(k).getQ()==c1.getQ())){
                                            if(l3Piece.get(k).getD()==0){
                                                if(checkHexagon(l2Piece.get(j), l3Piece.get(k))==1){
                                                    realHexagons.add(l3Piece.get(k));
                                                }
                                            }
                                        }
                                    }

                                }
                                updateHexagon(piece, c2);
                            }
                        }
                    }

                }
                updateHexagon(piece, c1);
            }

        }
        //leave spider in its original hexagon
        updateHexagon(piece, c1);
        return realHexagons;
    }

    /**
     * An ant could go in any hexagon that is not blocked.
     * @param piece
     * @return
     */
    private ArrayList<Hexagon> antMoves(Piece piece) {
        //First: Copy AvailableHexagons to new Array
        ArrayList<Hexagon> availableHexagonsClon = new ArrayList<>();
        for(int i=0;i<this.availableHexagons.size();i++)
            availableHexagonsClon.add(this.availableHexagons.get(i));
        Hexagon c = new Hexagon(piece.getHexagon().getQ(), piece.getHexagon().getR(), piece.getHexagon().getD());
        ArrayList<Hexagon> possibleHexagons = new ArrayList<>();
        //First: Check if blocked
        if(!checkIfPieceBlocked(piece)){
            //Second: take ant from the board
            //Free hexagon in the board
            deleteHexagon(piece);
            //Delete hexagon neighbours if they haven't any neighbour
            deleteAvailableHexagons(c, availableHexagonsClon);
            //Third: Get all available hexagons and check if d==0 and then if they are not blocked
            for(int i=0; i<availableHexagonsClon.size(); i++){
                if(availableHexagonsClon.get(i).getD()==0)
                    if(!checkIfHexagonBlocked(availableHexagonsClon.get(i)))
                        possibleHexagons.add(availableHexagonsClon.get(i));
            }
        }
        //Fourth: Return ant to its original hexagon
        updateHexagon(piece, c);
        return possibleHexagons;
    }

    /**
     * Checks if a piece could go in a hexagon
     * 1st checks that the hexagon is empty
     * 2nd checks if it has neighbours. If it has 2 the piece can't slide to it.
     * @param piece
     * @param hexagon
     * @return
     */
    private int checkHexagon(Hexagon piece, Hexagon hexagon){

        int n=0;

        int xt=piece.getQ();
        int yt=piece.getR();
        int xc= hexagon.getQ();
        int yc= hexagon.getR();
        int zc= hexagon.getD();


        if(xc==xt+1 && yc==yt-1){
            if(searchPiece(new Hexagon(xc,yc,zc))==null){
                if(searchPiece(new Hexagon(xc - 1, yc, zc))!=null) n=n+1;
                if(searchPiece(new Hexagon(xc, yc + 1, zc))!=null) n=n+1;
            }
        }else if(xc==xt+1 && yc==yt){
            if(searchPiece(new Hexagon(xc,yc,zc))==null){
                if(searchPiece(new Hexagon(xc, yc - 1, zc))!=null) n=n+1;
                if(searchPiece(new Hexagon(xc - 1, yc + 1, zc))!=null) n=n+1;
            }
        }else if(xc==xt && yc==yt+1){
            if(searchPiece(new Hexagon(xc,yc,zc))==null){
                if(searchPiece(new Hexagon(xc + 1, yc - 1, zc))!=null) n=n+1;
                if(searchPiece(new Hexagon(xc - 1, yc, zc))!=null) n=n+1;
            }
        }else if(xc==xt-1 && yc==yt+1){
            if(searchPiece(new Hexagon(xc,yc,zc))==null){
                if(searchPiece(new Hexagon(xc + 1, yc, zc))!=null) n=n+1;
                if(searchPiece(new Hexagon(xc, yc - 1, zc))!=null) n=n+1;
            }
        }else if(xc==xt-1 && yc==yt){
            if(searchPiece(new Hexagon(xc,yc,zc))==null){
                if(searchPiece(new Hexagon(xc, yc + 1, zc))!=null) n=n+1;
                if(searchPiece(new Hexagon(xc + 1, yc - 1, zc))!=null) n=n+1;
            }
        }else if(xc==xt && yc==yt-1){
            if(searchPiece(new Hexagon(xc,yc,zc))==null){
                if(searchPiece(new Hexagon(xc - 1, yc + 1, zc))!=null) n=n+1;
                if(searchPiece(new Hexagon(xc + 1, yc,zc))!=null) n=n+1;
            }
        }

        return n;
    }

    /**
     *
     * @param piece
     * @return
     */
    public boolean brokenHive(Piece piece){
        //If the destination hexagon is in level 0
        if(piece.getHexagon().getD()==0) {
            BlockCutpointGraph bcg = new BlockCutpointGraph(this.graph);
            return bcg.isCutpoint(piece.getGraphId());
        }else
            return false;
    }

    /**
     *
     * @param hexagon
     * @return
     */
    public Hexagon[] vHexagons(Hexagon hexagon){
        Hexagon[] hexagons = new Hexagon[2];

        int x = hexagon.getQ();
        int y = hexagon.getR();

        if(searchPiece(new Hexagon(x + 1, y - 1, 0))!=null){
            hexagons[0] = new Hexagon(x-1,y,0);
            hexagons[1] = new Hexagon(x,y+1,0);
        }else if(searchPiece(new Hexagon(x + 1, y, 0))!=null){
            hexagons[0] = new Hexagon(x,y-1,0);
            hexagons[1] = new Hexagon(x-1,y+1,0);
        }else if(searchPiece(new Hexagon(x, y + 1, 0))!=null){
            hexagons[0] = new Hexagon(x-1,y,0);
            hexagons[1] = new Hexagon(x+1,y-1,0);
        }else if(searchPiece(new Hexagon(x - 1, y + 1, 0))!=null){
            hexagons[0] = new Hexagon(x,y-1,0);
            hexagons[1] = new Hexagon(x+1,y,0);
        }else if(searchPiece(new Hexagon(x-1,y,0))!=null){
            hexagons[0] = new Hexagon(x+1,y-1,0);
            hexagons[1] = new Hexagon(x,y+1,0);
        }else{
            hexagons[0] = new Hexagon(x-1,y+1,0);
            hexagons[1] = new Hexagon(x+1,y,0);
        }

        return hexagons;
    }

    /**
     * Deletes a piece from the board game.
     * A piece can be remove if it was placed to test strategics for the AI.
     */
    public void detelePiece(Piece piece){
        //Save original hexagon
        Hexagon hex = new Hexagon(piece.getHexagon().getQ(),piece.getHexagon().getR(),piece.getHexagon().getD());
        for(int i=0;i<this.getBoard().size();i++){
            if(this.getBoard().get(i).getHexagon().toString().equals(piece.getHexagon().toString())) {
                //Add hexagon to available hexagons list
                this.availableHexagons.add(piece.getHexagon());
                //Free hexagon from the board
                deleteHexagon(piece);
                //Delete hexagon neighbours if they haven't any neighbour
                deleteAvailableHexagons(hex, this.availableHexagons);
                //Delete it from the graph
                this.graph.removeVertex(this.getBoard().get(i).getGraphId());
                this.vertex = this.vertex - 1;
                //Remove piece from the board
                this.getBoard().remove(this.getBoard().get(i));
            }
        }
    }

    /**
     * Checks if a given hexagon has a piece.
     * @param hexagon
     * @return
     */
    public boolean checkIfHexagonTaken(Hexagon hexagon){
        for(int i=0;i<this.getBoard().size();i++) {
            if (this.getBoard().get(i).getHexagon().toString().equals(hexagon.toString()))
                return true;
        }
        return false;
    }

}

