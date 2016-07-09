package lucgilr.inf.uva.hivev2.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import lucgilr.inf.uva.hivev2.BoardSettings.CircleImageView;
import lucgilr.inf.uva.hivev2.BoardSettings.Grid;
import lucgilr.inf.uva.hivev2.Controller.GameController;
import lucgilr.inf.uva.hivev2.BoardSettings.Cube;
import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hexagon;
import lucgilr.inf.uva.hivev2.GameModel.Piece;
import lucgilr.inf.uva.hivev2.GameModel.PieceType;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.R;

/**
 * @author https://github.com/starwheel modified my Lucía Gil Román (https://github.com/lucgilr)
 *
 * The original code has been modified but it can be found in:
 * https://github.com/omplanet/android-hexagonal-grids/blob/master/HexagonalGrids/app/src/main/java/net/omplanet/hexagonalgrids/ui/MainActivity.java
 * When a player choose to play a game against other real player
 * in the same device it will load this Activity.
 *
 */
public class GameUI extends AppCompatActivity {

    private Game game;
    private GameController controller;

    private ArrayList<Hexagon> hexagons;
    private Player player;
    private ArrayList<Hexagon> possibleHexagons;
    private Piece piece;
    private String displayLanguage;
    private boolean movingPiece;
    private boolean gameOver;
    private boolean boardReady;
    private boolean ai;
    private int endGame;
    private PieceType bugType;
    private String bug;

    private RelativeLayout mRelativeLayout;
    private ScrollView vScrollView;
    private HorizontalScrollView hScrollView;
    private final int radius = 22;
    private int scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        //Setting scale value
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            //Tablet 8'
            //Tablet 7'
            this.scale=100;

        }
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            //Smartphone 5'
            this.scale=180;

        }

        //Get the type of game we are going to play
        Bundle b = getIntent().getExtras();
        ai = false; // or other values
        /*if(b != null)
            ai = b.getBoolean("AI");*/
        if(b !=null)
            setAi(b.getBoolean("AI"));


        //RelativeLayout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.gridLayout);

        //Scrolls
        vScrollView = (ScrollView) findViewById(R.id.vertical_scroll);
        hScrollView = (HorizontalScrollView)findViewById(R.id.horizontal_scroll);

        //Language
        displayLanguage = Locale.getDefault().getDisplayLanguage();

        //Choose which player stars to play
        Random r = new Random();
        int p = r.nextInt(((1) + 1));

        //Create new Game
        game = new Game(p);
        controller = new GameController(game,this);

        this.movingPiece =false;
        possibleHexagons = new ArrayList<>();
        hexagons = new ArrayList<>();
        piece = new Piece();
        this.gameOver =false;
        this.boardReady=false;

        //First player to play
        controller.getPlayer();
        Player firstPlayer = getPlayer();

        initGridView();

        //Show dialog --> first player
        firstPlayer(firstPlayer);

    }

    /**
     * Scrolls to the middle of the RelativeLayout when rotating the screen
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            scrollWhenLandscape();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            scrollWhenPortrait();

        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        if(isNotGameOver()) {
            alert.setMessage(R.string.leavingActivity);
            alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }

            });
            alert.setNegativeButton(R.string.no, null);
            alert.show();
        }else{
            alert.setMessage(R.string.playAgain);
            alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }

            });
            alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }

            });
            alert.show();
        }
    }

    /**
     * Scrolls to the middle of the RelativeLayout when Device in Portrait mode
     */
    private void scrollWhenPortrait(){
        Handler h = new Handler();

        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                vScrollView.scrollTo(0, mRelativeLayout.getHeight() / 2 - 600);
                hScrollView.scrollTo(mRelativeLayout.getWidth() / 2 - 400, 0);
            }
        }, 100);
    }

    /**
     * Scrolls to the middle of the RelativeLayout when Device in Landscape mode
     */
    private void scrollWhenLandscape(){
        Handler h = new Handler();

        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                vScrollView.scrollTo(0, mRelativeLayout.getHeight() / 2 - 350);
                hScrollView.scrollTo(mRelativeLayout.getWidth() / 2 - 600, 0);
            }
        }, 100);
    }

    /**
     * Init the Grid
     */
    private void initGridView() {

        if(this.boardReady){
            //Center Scrolls
            Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            int orientation = display.getRotation();
            if(orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270){
                //Landscape
                scrollWhenLandscape();
            }else if(orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180){
                //Portrait
                scrollWhenPortrait();
            }
        }
        setGridDimensions();
        setGridNodes();
    }

    /**
     * Sets Grid dimensions
     */
    private void setGridDimensions() {

        // Gets the layout params that will allow to resize the layout
        final ViewGroup.LayoutParams params = mRelativeLayout.getLayoutParams();

        //Get display metrics
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Changes the height and width of the grid to the specified *pixels*
        params.width = Grid.getGridWidth(radius, scale);
        params.height = Grid.getGridHeight(radius, scale);

    }

    /**
     * Prepares which hexagons to draw
     */
    private void setGridNodes() {
        try {

            //Clear View
            this.mRelativeLayout.removeAllViewsInLayout();

            //Check if a bee fully surrounded
            controller.endGame();
            if(getEndGame()!=0 && !this.gameOver){
                //GAME OVER
                this.gameOver =true;
                gameOver(getEndGame());
            }

            //My stuff
            controller.getPlayer();

            if(!this.movingPiece && isNotGameOver()){
                //Get possible hexagons to place a piece for the first time is the pieces box is not empty
                if(getPlayer().getPiecesInTheBox().size() != 0) controller.getPlayerHexagons(getPlayer());
                else getHexagons().clear();
                //If there are no hexagons and the bee is not in game --> next player
                if(getHexagons().isEmpty() && !controller.playerBeeInGame()) nextPlayer();
                //If all the players pieces are in the game and the player can't move any piece --> next player
                if(getPlayer().getPiecesInTheBox().size()==0 && game.getHive().noMoves(player)) nextPlayer();
                //If there are not hexagons to place the pieces and the player can't move any of the ones in the game
                if(getHexagons().isEmpty() && controller.getHive().noMoves(getPlayer())) nextPlayer();
                if(ai){
                    if(player.getColor().equals("Black")){

                        if(player.getTurn()==1) controller.initIA(player);
                        controller.makeAChoice(game);
                        controller.oneMoreTurn();
                        controller.oneMoreRound();
                        initGridView();

                    }
                }
            }

            final Grid grid;
            if(!this.gameOver) {
                grid = new Grid(radius, scale, hexagons, this.game.getHive().getBoard());
            }else{
                grid = new Grid(radius, scale,this.game.getHive().getBoard());
            }

            //Gird node listener restricted to the node's circular area.
            View.OnTouchListener gridNodeTouchListener = new View.OnTouchListener() {

                @Override
                public boolean onTouch(final View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            float xPoint = event.getX();
                            float yPoint = event.getY();
                            boolean isPointOutOfCircle = (grid.centerOffsetX -xPoint)*(grid.centerOffsetX -xPoint) + (grid.centerOffsetY -yPoint)*(grid.centerOffsetY -yPoint) > grid.width * grid.width / 4;
                            if (isPointOutOfCircle) return false;
                            else v.setSelected(true);
                            break;
                        case MotionEvent.ACTION_OUTSIDE:
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_SCROLL:
                            break;
                        case MotionEvent.ACTION_UP:
                            if(isNotGameOver()) {
                                v.setSelected(false);
                                CircleImageView view = (CircleImageView) v;
                                OnGridHexClick(view.getHex());
                            }
                            break;
                    }
                    return true;
                }
            };

            for(Cube cube : grid.nodes) {
                Hexagon hexagon = cube.toHex();

                CircleImageView view = new CircleImageView(this);
                view.setHex(hexagon);

                int size = controller.getBoardSize();

                if(size!=0) {

                    for(int i=0;i<size;i++){

                        String hexView = view.getHex().toString2D();
                        String sol = controller.getBoard().get(i).getHexagon().toString2D();
                        String color = controller.getBoard().get(i).getPlayer().getColor();
                        PieceType type = controller.getBoard().get(i).getType();

                        if(hexView.equals(sol)) {
                            if(controller.getBoard().get(i).isBeetle()){
                                view.setBackgroundResource(getPiece(displayLanguage,color,type));
                            }
                        }

                    }

                }

                if(!this.gameOver) {
                    if (checkIfHexagonAvailable(view.getHex(), hexagons)) {
                        view.setBackgroundResource(R.drawable.greyhex);
                    }
                }

                view.setOnTouchListener(gridNodeTouchListener);
                addViewToLayout(view, hexagon, grid);

            }


        } catch (Exception e) {
            Toast.makeText(GameUI.this, "Sorry, there was a problem initializing the application.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private int getPiece(String language, String color, PieceType type){
        int pieceToPlace;
        if(language.equals("español")) {
            if (color.equals("White")) {
                if (type.equals(PieceType.BEE))
                    pieceToPlace=R.drawable.abejablanca;
                else if (type.equals(PieceType.GRASSHOPPER))
                    pieceToPlace=R.drawable.saltamblanco;
                else if (type.equals(PieceType.SPIDER))
                    pieceToPlace=R.drawable.aranablanca;
                else if (type.equals(PieceType.BEETLE))
                    pieceToPlace=R.drawable.escarblanco;
                else pieceToPlace=R.drawable.hormigablanca;
            } else {
                if (type.equals(PieceType.BEE))
                    pieceToPlace=R.drawable.abejanegra;
                else if (type.equals(PieceType.GRASSHOPPER))
                    pieceToPlace=R.drawable.saltamnegro;
                else if (type.equals(PieceType.SPIDER))
                    pieceToPlace=R.drawable.arananegra;
                else if (type.equals(PieceType.BEETLE))
                    pieceToPlace=R.drawable.escarnegro;
                else pieceToPlace=R.drawable.hormiganegra;
            }
        }else{
            if (color.equals("White")) {
                if (type.equals(PieceType.BEE))
                    pieceToPlace=R.drawable.whitebee;
                else if (type.equals(PieceType.GRASSHOPPER))
                    pieceToPlace=R.drawable.whitegrass;
                else if (type.equals(PieceType.SPIDER))
                    pieceToPlace=R.drawable.whitespider;
                else if (type.equals(PieceType.BEETLE))
                    pieceToPlace=(R.drawable.whitebeetle);
                else pieceToPlace=R.drawable.whiteant;
            } else {
                if (type.equals(PieceType.BEE))
                    pieceToPlace=R.drawable.blackbee;
                else if (type.equals(PieceType.GRASSHOPPER))
                    pieceToPlace=R.drawable.blackgrass;
                else if (type.equals(PieceType.SPIDER))
                    pieceToPlace=R.drawable.blackspider;
                else if (type.equals(PieceType.BEETLE))
                    pieceToPlace=R.drawable.blackbeetle;
                else pieceToPlace=R.drawable.blackant;
            }
        }
        return pieceToPlace;
    }

    /**
     * Add hexagon View to Layout
     */
    private void addViewToLayout(View view, Hexagon hexagon, Grid grid) {

        //Add to view
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(grid.width, grid.height);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.centerLayout);
        params.addRule(RelativeLayout.BELOW, R.id.centerLayout);

        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        if(mRelativeLayout.getWidth()==0){
            mRelativeLayout.setScrollX(-size.x/2);
        }else {
            mRelativeLayout.setScrollX(-mRelativeLayout.getWidth()/2);
        }

        if(mRelativeLayout.getHeight()==0){
            mRelativeLayout.setScrollY(-size.y/2);
        }else {
            mRelativeLayout.setScrollY(-mRelativeLayout.getHeight()/2);
        }

        mRelativeLayout.addView(view, params);

        //Set coordinates
        Point p = grid.hexToPixel(hexagon);
        params.leftMargin = -grid.centerOffsetX + p.x;
        params.topMargin = -grid.centerOffsetY + p.y;
    }

    /**
     * Choose which action to execute when board touched
     */
    private void OnGridHexClick(final Hexagon hexagon) {

        if(this.movingPiece && checkIfHexagonAvailable(hexagon, getHexagons())){
            //Moving piece
            Hexagon hex = getRealHexagon(hexagon.getR(), hexagon.getQ());
            controller.movePiece(getPiece(), hex);
            controller.oneMoreTurn();
            controller.oneMoreRound();
            this.movingPiece =false;
            initGridView();
        }
        else if(!this.movingPiece && checkIfHexagonAvailable(hexagon, getHexagons())) {
            //Adding a piece to the board
            ArrayList<Piece> pieces = controller.getPiecesFromBox();
                final ArrayList<String> t = new ArrayList<>();
                boolean bee = controller.playerBeeInGame();

                if(controller.getPlayerTurn() == 4 && !bee){
                    for (int i = 0; i < pieces.size(); i++) {
                        if (pieces.get(i).getType().equals(PieceType.BEE)) {
                            if (displayLanguage.equals("español")) {
                                controller.setSpanish(pieces.get(i).getType());
                            } else {
                                controller.setEnglish(pieces.get(i).getType());
                            }
                            t.add(bug);
                        }
                    }
                } else {
                    for (int i = 0; i < pieces.size(); i++) {
                        if (displayLanguage.equals("español")) {
                            controller.setSpanish(pieces.get(i).getType());
                        } else {
                            controller.setEnglish(pieces.get(i).getType());
                        }
                        t.add(bug);
                    }
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
                alert.setItems(t.toArray(new String[t.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPiece(new Piece());
                        controller.stringToPieceType(t.get(which));
                        controller.takePieceByType(getBugType());
                        Hexagon hex = getRealHexagon(hexagon.getR(), hexagon.getQ());
                        controller.playPiece(getPiece(), hex);
                        controller.oneMoreTurn();
                        controller.oneMoreRound();
                        initGridView();
                    }
                });
                alert.create();
                alert.show();
        }else if(pieceTouched(hexagon)){
            //Piece touched by the correct player
            setPiece(getPieceFromBoard(hexagon));
            controller.getPossibleMoves(getPiece());
            if (!getPossibleHexagons().isEmpty()) {
                movingPiece = true;
                this.hexagons = new ArrayList<>(getPossibleHexagons());
                initGridView();
            }
        }else if(this.movingPiece && !checkIfHexagonAvailable(hexagon, getHexagons())) {
            //Deselect pieces moves
            controller.getPlayerHexagons(getPlayer());
            this.movingPiece =false;
            initGridView();
        }
    }

    /**
     * @return true if the hexagon is free
     */
    private boolean checkIfHexagonAvailable(Hexagon hexagon, ArrayList<Hexagon> gaps) {
        for(int i=0;i<gaps.size();i++){
            if(hexagon.toString2D().equals(gaps.get(i).toString2D())){
                return true;
            }
        }
        return false;
    }

    /**
     * @return a piece from the board to move it if it hasn't a beetle on top
     */
    private Piece getPieceFromBoard(Hexagon hexagon){
        ArrayList<Piece> board = controller.getBoard();
        for(int i=0;i<controller.getBoardSize();i++){
            if(board.get(i).getHexagon().toString2D().equals(hexagon.toString2D())
                    && board.get(i).isBeetle())
            return board.get(i);
        }
        return null;
    }

    /**
     * @return the real hexagon for a piece.
     * This method is necessary because the board is represented in 2D,
     * but the piece representation is in 3D because a piece can be on
     * top of another.
     */
    private Hexagon getRealHexagon(int r, int q){
        for(int i=0;i< hexagons.size();i++){
            if(hexagons.get(i).getQ()==q && hexagons.get(i).getR()==r) return hexagons.get(i);
        }
        return null;
    }

    /**
     * Checks if the piece touched can be moved.
     * Reasons it can't be moved:
     * - The player color doesn't match the piece color
     * - The piece has a beetle on top
     */
    private boolean pieceTouched(Hexagon hexagon){
        ArrayList<Piece> board = controller.getBoard();
        for(int i=0;i<controller.getBoardSize();i++){
            if(board.get(i).getHexagon().toString2D().equals(hexagon.toString2D())
                    && board.get(i).getPlayer().getColor().equals(getPlayer().getColor())
                    && board.get(i).isBeetle())
                return true;
        }
        return false;
    }

    /**
     * Checks if the game is over
     */
    private boolean isNotGameOver(){
        return !this.gameOver;
    }

    //DIALOGS

    /**
     * Shows dialog that explains to the player that he can't make a move in this turn
     */
    private void nextPlayer(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        alert.setMessage(R.string.nextPlayerMessage);
        alert.setPositiveButton(R.string.nextPlayer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.oneMoreRound();
                controller.oneMoreTurn();
                initGridView();
            }
        });
        alert.setCancelable(false);
        alert.create();
        alert.show();
    }

    /**
     * Dialog showing which player has won
     */
    private void gameOver(int player){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        hexagons =null;
        if(player==1){
            if(this.ai)alert.setMessage(R.string.aiWins);
            else alert.setMessage(R.string.blackPlayer);
            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initGridView();
                }
            });
            alert.create();
            alert.show();
        }else if(player==2){
            alert.setMessage(R.string.whitePlayer);
            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initGridView();
                }
            });
            alert.create();
            alert.show();
        }else if(player==3){
            alert.setMessage(R.string.bothPlayers);
            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initGridView();
                }
            });
            alert.create();
            alert.show();
        }
    }

    /**
     * Shows dialog saying which player starts to play
     */
    private void firstPlayer(Player player){
        //Init board ok
        this.boardReady=true;
        String color = player.getColor();
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        alert.setTitle(R.string.playerTurn);

        if(color.equals("Black"))
            if(this.ai) alert.setMessage(R.string.aiTurn);
            else alert.setMessage(R.string.blackStarts);
        else
            if(this.ai) alert.setMessage(R.string.playerStarts);
            else alert.setMessage(R.string.whiteStarts);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initGridView();
            }
        });
        alert.setCancelable(false);
        alert.create();
        alert.show();

    }

    //Getters and Setters

    public void setPlayer(Player player){
        this.player=player;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void setHexagons(ArrayList<Hexagon> hexagons){
        this.hexagons=hexagons;
    }

    private ArrayList<Hexagon> getHexagons(){
        return this.hexagons;
    }

    public void setEndGame(int endGame){
        this.endGame=endGame;
    }

    private int getEndGame(){
        return this.endGame;
    }

    public void setBugType(PieceType bugType){
        this.bugType =bugType;
    }

    private PieceType getBugType(){
        return this.bugType;
    }

    public void setPiece(Piece piece){
        this.piece=piece;
    }

    private Piece getPiece(){
        return this.piece;
    }

    public void setPossibleHexagons(ArrayList<Hexagon> possibleHexagons){
        this.possibleHexagons=possibleHexagons;
    }

    private ArrayList<Hexagon> getPossibleHexagons(){
        return this.possibleHexagons;
    }

    public void setBug(String bug){
        this.bug=bug;
    }

    public void setAi(boolean ai) {
        this.ai = ai;
    }
}

