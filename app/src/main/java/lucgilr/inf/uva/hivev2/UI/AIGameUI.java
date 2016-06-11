package lucgilr.inf.uva.hivev2.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import lucgilr.inf.uva.hivev2.Controller.GameController;
import lucgilr.inf.uva.hivev2.GameModel.Cube;
import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hexagon;
import lucgilr.inf.uva.hivev2.GameModel.Language;
import lucgilr.inf.uva.hivev2.GameModel.Piece;
import lucgilr.inf.uva.hivev2.GameModel.PieceType;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.R;

/**
 * The original code has been modified but it can be found in:
 * https://github.com/omplanet/android-hexagonal-grids/blob/master/HexagonalGrids/app/src/main/java/net/omplanet/hexagonalgrids/ui/MainActivity.java
 * When a player choose to play a game against other real player
 * in the same device it will load this Activity.
 */
public class AIGameUI extends AppCompatActivity {

    private Game game;
    private GameController controller;
    private Language language;

    private ArrayList<Hexagon> gaps;
    private Player player;
    private boolean movingToken;
    private boolean deselect;
    private ArrayList<Hexagon> possibleGaps;
    private Piece piece;
    private String displayLanguage;
    private boolean gameover;
    private ArrayList<Hexagon> gridBoard;
    private boolean boardReady;

    private RelativeLayout mRelativeLayout;
    private ScrollView vScrollView;
    private HorizontalScrollView hScrollView;
    private final int radius = 22;
    private final int scale = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        //RelativeLayout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.gridLayout);

        //Scrolls
        vScrollView = (ScrollView) findViewById(R.id.vertical_scroll);
        hScrollView = (HorizontalScrollView)findViewById(R.id.horizontal_scroll);

        //Language
        displayLanguage = Locale.getDefault().getDisplayLanguage();

        //Choose which player stars to play
        Random r = new Random();
        int p = r.nextInt(((1) + 1) + 0);

        //Create new Game
        game = new Game(p);
        controller = new GameController(game,this);
        language = new Language();

        this.movingToken =false;
        possibleGaps = new ArrayList<>();
        gaps = new ArrayList<>();
        piece = new Piece();
        this.gameover=false;
        this.gridBoard = new ArrayList<>();
        this.deselect=false;
        this.boardReady=false;

        //First player to play
        Player firstPlayer = controller.getPlayer();

        initGridView();

        //Show dialog --> first player
        firstPlayer(firstPlayer);
    }

    /**
     * Scrolls to the middle of the RelativeLayout when rotating the screen
     * @param newConfig
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

    /**
     * Scrolls to the middle of the RelativeLayout when Device in Portrait mode
     */
    public void scrollWhenPortrait(){
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
    public void scrollWhenLandscape(){
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
        Grid grid = setGridNodes();
    }

    /**
     * Sets Grid dimensions
     * @return
     */
    private void setGridDimensions() {

        // Gets the layout params that will allow to resize the layout
        final ViewGroup.LayoutParams params = mRelativeLayout.getLayoutParams();

        //Get display metrics
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;

        //If in landscape mode, keep the width small as in portrait mode
        if(displayWidth > displayHeight) displayWidth = displayHeight;

        int horizontalPadding = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        displayWidth -= 2 * horizontalPadding;

        // Changes the height and width of the grid to the specified *pixels*
        params.width = Grid.getGridWidth(radius, scale);
        params.height = Grid.getGridHeight(radius, scale);

    }

    /**
     * Prepares which hexagons to draw
     * @return
     */
    private Grid setGridNodes() {
        try {

            //Clear View
            this.mRelativeLayout.removeAllViewsInLayout();

            //Check if a bee fully surrounded
            int endgame = controller.endGame();
            if(endgame!=0 && !this.gameover){
                //GAME OVER
                this.gameover=true;
                gameOver(endgame);
            }

            //My stuff
            player = controller.getPlayer();

            if(!this.movingToken && !isGameOver()){
                gaps = controller.getPlayerHexagons(player);
                //If there are not gaps and the bee is not in game
                if(gaps.isEmpty() && !controller.playerBeeInGame()) nextPlayer();
                if(player.getColor().equals("Black")){
                    if(player.getTurn()==1) controller.initIA(player);
                    controller.makeAChoice(game);
                    controller.oneMoreTurn();
                    controller.oneMoreRound();
                    initGridView();
                }
            }

            final Grid grid;
            if(!this.gameover) {
                grid = new Grid(radius, scale, gaps, this.game.getHive().getBoard());
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
                            if(!isGameOver()) {
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
                Hexagon hexagon = null;
                hexagon = cube.toHex();

                CircleImageView view = new CircleImageView(this);
                view.setHex(hexagon);

                int size = controller.getBoardSize();

                if(size!=0) {

                    for(int i=0;i<size;i++){

                        String hexView = view.getHex().toString2D();
                        String sol = controller.getBoard().get(i).getHexagon().toString2D();
                        String color = controller.getBoard().get(i).getPlayer().getColor();
                        int id = controller.getBoard().get(i).getId();

                        if(hexView.equals(sol)) {
                            if(!controller.getBoard().get(i).isBeetle()){
                                if(displayLanguage.equals("English")) {
                                    if (color.equals("White")) {
                                        if (id == 0)
                                            view.setBackgroundResource(R.drawable.whitebee);
                                        else if (id == 1 || id == 2 || id == 3)
                                            view.setBackgroundResource(R.drawable.whitegrass);
                                        else if (id == 4 || id == 5)
                                            view.setBackgroundResource(R.drawable.whitespider);
                                        else if (id == 6 || id == 7)
                                            view.setBackgroundResource(R.drawable.whitebeetle);
                                        else view.setBackgroundResource(R.drawable.whiteant);
                                    } else {
                                        if (id == 0)
                                            view.setBackgroundResource(R.drawable.blackbee);
                                        else if (id == 1 || id == 2 || id == 3)
                                            view.setBackgroundResource(R.drawable.blackgrass);
                                        else if (id == 4 || id == 5)
                                            view.setBackgroundResource(R.drawable.blackspider);
                                        else if (id == 6 || id == 7)
                                            view.setBackgroundResource(R.drawable.blackbeetle);
                                        else view.setBackgroundResource(R.drawable.blackant);
                                    }
                                }else{
                                    if (color.equals("White")) {
                                        if (id == 0)
                                            view.setBackgroundResource(R.drawable.abejablanca);
                                        else if (id == 1 || id == 2 || id == 3)
                                            view.setBackgroundResource(R.drawable.saltamblanco);
                                        else if (id == 4 || id == 5)
                                            view.setBackgroundResource(R.drawable.aranablanca);
                                        else if (id == 6 || id == 7)
                                            view.setBackgroundResource(R.drawable.escarblanco);
                                        else view.setBackgroundResource(R.drawable.hormigablanca);
                                    } else {
                                        if (id == 0)
                                            view.setBackgroundResource(R.drawable.abejanegra);
                                        else if (id == 1 || id == 2 || id == 3)
                                            view.setBackgroundResource(R.drawable.saltamnegro);
                                        else if (id == 4 || id == 5)
                                            view.setBackgroundResource(R.drawable.arananegra);
                                        else if (id == 6 || id == 7)
                                            view.setBackgroundResource(R.drawable.escarnegro);
                                        else view.setBackgroundResource(R.drawable.hormiganegra);
                                    }
                                }
                            }
                        }

                    }

                }

                if(!this.gameover) {
                    if (checkIfHexagonAvailable(view.getHex(), gaps)) {
                        view.setBackgroundResource(R.drawable.greyhex);
                    }
                }

                view.setOnTouchListener(gridNodeTouchListener);
                addViewToLayout(view, hexagon, grid);

            }

            return grid;


        } catch (Exception e) {
            Toast.makeText(AIGameUI.this, "Sorry, there was a problem initializing the application.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Add hexagon View to Layout
     * @param view
     * @param hexagon
     * @param grid
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
     * @param hexagon
     */
    private void OnGridHexClick(final Hexagon hexagon) {

        if(this.movingToken && checkIfHexagonAvailable(hexagon, gaps)){
            //Moving piece
            Hexagon hex = getRealHexagon(hexagon.getR(), hexagon.getQ());
            controller.movePiece(piece, hex);
            controller.oneMoreTurn();
            controller.oneMoreRound();
            this.movingToken=false;
            this.deselect=false;
            initGridView();
        }
        else if(!this.movingToken && checkIfHexagonAvailable(hexagon, gaps)) {
            //Adding a piece to the board
            this.deselect=false;
            ArrayList<Piece> pieces = controller.getPiecesFromBox();
            if(!pieces.isEmpty()) {
                final ArrayList<String> t = new ArrayList<>();
                int turn = controller.getPlayerTurn();
                boolean bee = controller.playerBeeInGame();
                String bug ="";
                if(turn == 4 && !bee){
                    for (int i = 0; i < pieces.size(); i++) {
                        if (pieces.get(i).getId() == 0) {
                            if (displayLanguage.equals("English")) {
                                bug = language.getEnglish(pieces.get(i).getType());
                            } else {
                                bug = language.getSpanish(pieces.get(i).getType());
                            }
                            t.add(bug);
                        }
                    }
                } else {
                    for (int i = 0; i < pieces.size(); i++) {
                        if (displayLanguage.equals("English")) {
                            bug = language.getEnglish(pieces.get(i).getType());
                        } else {
                            bug = language.getSpanish(pieces.get(i).getType());
                        }
                        t.add(bug);
                    }
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
                alert.setItems(t.toArray(new String[t.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        piece = new Piece();
                        PieceType bug = language.stringToPieceType(t.get(which));
                        piece = controller.takePieceByType(bug);
                        Hexagon hex = getRealHexagon(hexagon.getR(), hexagon.getQ());
                        controller.playPiece(piece, hex);
                        controller.oneMoreTurn();
                        controller.oneMoreRound();
                        initGridView();
                    }
                });
                alert.create();
                alert.show();
            }
        }else if(pieceTouched(hexagon)){
            //Piece touched by the correct player
            piece = new Piece();
            piece = getPieceFromBoard(hexagon);
            possibleGaps = controller.getPossibleMoves(piece);
            if (!possibleGaps.isEmpty()) {
                movingToken = true;
                this.gaps = new ArrayList<>(possibleGaps);
                this.deselect=false;
                initGridView();
            }
        }else if(!checkIfHexagonAvailable(hexagon, gaps)) {
            //Deselect pieces moves
            this.gaps = controller.getPlayerHexagons(player);
            this.movingToken=false;
            this.deselect=true;
            initGridView();
        }

    }

    /**
     * Returns true if the hexagon is free
     * @param hexagon
     * @param gaps
     * @return
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
     * Gets a piece from the board if it hasn't a beetle on top
     * @param hexagon
     * @return
     */
    private Piece getPieceFromBoard(Hexagon hexagon){
        ArrayList<Piece> board = controller.getBoard();
        for(int i=0;i<controller.getBoardSize();i++){
            if(board.get(i).getHexagon().toString2D().equals(hexagon.toString2D())
                    && !board.get(i).isBeetle())
            return board.get(i);
        }
        return null;
    }

    /**
     * Returns the real hexagon for a piece.
     * This method is necessary because the board is represented in 2D,
     * but the piece representation is in 3D because a piece can be on
     * top of another.
     * @param r
     * @param q
     * @return
     */
    private Hexagon getRealHexagon(int r, int q){
        for(int i=0;i<gaps.size();i++){
            if(gaps.get(i).getQ()==q && gaps.get(i).getR()==r) return gaps.get(i);
        }
        return null;
    }

    /**
     * Checks if the piece touched can be moved.
     * Reasons it can't be moved:
     * - The player color doesn't match the piece color
     * - The piece has a beetle on top
     * @param hexagon
     * @return
     */
    private boolean pieceTouched(Hexagon hexagon){
        ArrayList<Piece> board = controller.getBoard();
        for(int i=0;i<controller.getBoardSize();i++){
            if(board.get(i).getHexagon().toString2D().equals(hexagon.toString2D())
                    && board.get(i).getPlayer().getColor().equals(controller.getPlayer().getColor())
                    && !board.get(i).isBeetle())
                return true;
        }
        return false;
    }

    /**
     * Checks if the game is over
     * @return
     */
    private boolean isGameOver(){
        return this.gameover;
    }

    //DIALOGS

    /**
     * Shows dialog that explains to the player that he can't make a move in this turn
     */
    private void nextPlayer(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        alert.setMessage("You can't make any move or add any piece");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                game.oneMoreRound();
                player.oneMoreTurn();
                initGridView();
            }
        });
        alert.create();
        alert.show();
    }

    /**
     * Dialog showing which player has won
     * @param player
     */
    private void gameOver(int player){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
        gaps=null;
        if(player==1){
            alert.setMessage(R.string.whitePlayer);
            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initGridView();
                }
            });
            alert.create();
            alert.show();
        }else if(player==2){
            alert.setMessage(R.string.blackPlayer);
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
            alert.setMessage(R.string.aiTurn);
        else
            alert.setMessage(R.string.whiteStarts);
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

