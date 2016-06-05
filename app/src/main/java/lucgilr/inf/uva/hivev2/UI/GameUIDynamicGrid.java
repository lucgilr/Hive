package lucgilr.inf.uva.hivev2.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import lucgilr.inf.uva.hivev2.Controller.GameController;
import lucgilr.inf.uva.hivev2.GameModel.Cube;
import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Hexagon;
import lucgilr.inf.uva.hivev2.GameModel.Language;
import lucgilr.inf.uva.hivev2.GameModel.Piece;
import lucgilr.inf.uva.hivev2.GameModel.PieceType;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.R;
import pl.polidea.view.ZoomView;

/**
 * The original code has been modified but it can be found in:
 * https://github.com/omplanet/android-hexagonal-grids/blob/master/HexagonalGrids/app/src/main/java/net/omplanet/hexagonalgrids/ui/MainActivity.java
 * When a player choose to play a game against other real player
 * in the same device it will load this Activity.
 */
public class GameUIDynamicGrid extends AppCompatActivity {

    private Game game;
    private GameController controller;
    private Language language;

    ArrayList<Hexagon> gaps;
    private Player player;
    private boolean movingToken;
    private ArrayList<Hexagon> possibleGaps;
    private Piece piece;
    private String displayLanguage;
    private boolean gameover;
    private ArrayList<Hexagon> gridBoard;

    private RelativeLayout mRelativeLayout;
    private RelativeLayout mainLayout;
    private LinearLayout linearLayout;
    private View view;
    private ZoomView zoomView;
    private ScrollView vScrollView;
    private HorizontalScrollView hScrollView;
    private int radius;
    private int zoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        //LinearLayout
        linearLayout = (LinearLayout) findViewById(R.id.container_layout);
        //View
        view = (View) findViewById(R.id.centerLayout);
        //
        mainLayout = (RelativeLayout) findViewById(R.id.main_Layout);

        //RelativeLayout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.gridLayout);
        //Adding zoom...
        /*View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.game_layout,null,false);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        zoomView = new ZoomView(this);
        zoomView.addView(v);
        mRelativeLayout.addView(zoomView);*/


        //Center scrolls
        vScrollView = (ScrollView) findViewById(R.id.vertical_scroll);
        hScrollView = (HorizontalScrollView)findViewById(R.id.horizontal_scroll);

        Handler h = new Handler();

        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                vScrollView.scrollTo(0,530);
                hScrollView.scrollTo(1150,0);
            }
        }, 100);

        //Language
        displayLanguage = Locale.getDefault().getDisplayLanguage();

        //Create new GameUI
        game = new Game();
        controller = new GameController(game,this);
        language = new Language();

        this.movingToken =false;
        possibleGaps = new ArrayList<>();
        gaps = new ArrayList<>();
        piece = new Piece();
        this.gameover=false;
        this.gridBoard = new ArrayList<>();

        Grid.Shape shape = Grid.Shape.HEXAGON_POINTY_TOP;

        //Show dialog --> first player
        firstPlayer();

        //Init radius
        this.radius=1;
        this.zoom=6;
        Log.d("INIT GRID","INIT GRID");
        initGridView(radius, shape,zoom);
    }

    private void initGridView(int radius, Grid.Shape shape, int zoom) {
        int scale = setGridDimensions(radius, shape, zoom);
        Grid grid = setGridNodes(radius, scale, shape);
    }

    private int setGridDimensions(int radius, Grid.Shape shape, int zoom) {
        // Gets the layout params that will allow to resize the layout
        ViewGroup.LayoutParams params = mRelativeLayout.getLayoutParams();

        //Get display metrics
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;

        //If in landscape mode, keep the width small as in portrait mode
        if(displayWidth > displayHeight) displayWidth = displayHeight;

        int horizontalPadding = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        //int horizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalPaddingInDp, getResources().getDisplayMetrics());
        displayWidth -= 2 * horizontalPadding;

        // Calculate the scale: the radius of single node.
        //int scale = (int) (displayWidth / ((2*radius + 1) * (Math.sqrt(1))));
        //int scale = (int) (displayWidth / ((6*radius + 1) * (Math.sqrt(1))));
        //int scale = (int) (displayWidth / ((zoom *radius + 1) * (Math.sqrt(1))));
        int scale = 100;
        Log.d("SCALE",String.valueOf(scale));

        // Changes the height and width of the grid to the specified *pixels*
        params.width = Grid.getGridWidth(radius, scale, shape);
        params.height = Grid.getGridHeight(radius, scale, shape);

        return scale;
    }

    private void setBoard(){

        //My stuff
        player = controller.getPlayer();

        if(!this.movingToken){
            gaps = controller.getPlayerHexagons(player);
            //If there are not gaps and the bee is not in game
            if(gaps.isEmpty() && !controller.playerBeeInGame()) nextPlayer();
        }

        //Check if Gaps in board
        boolean hex=true;
        for(int i=0; i<gaps.size();i++){
            if(isInBoard(gaps.get(i))){
                Log.d("OK",gaps.get(i).toString());
            }else{
                Log.d("NOT OK",gaps.get(i).toString());
                hex=false;
                break;
            }
        }

        //PLAYERS TURN
        //Log.d("PLAYER",this.player.getColor());

        //PRINT BOARD
        //Log.d("BOARD PLAYER","BOARD PLAYER");
        /*for(int i=0;i<this.game.getHive().getBoard().size();i++)
            Log.d("piece...", this.game.getHive().getBoard().get(i).pieceInfo());*/

        //PRINT GAPS AVAILABLE
        /*if(!gaps.isEmpty()) {
            Log.d("GAPS SELECTED", "GAPS SELECTED");
            for (int i = 0; i < gaps.size(); i++)
                Log.d("Gap...", gaps.get(i).toString());
        }*/

        if(!hex){
            radius+=1;
            if(this.zoom!=2) this.zoom-=2;
            Log.d("INIT GRID","INIT GRID");
            initGridView(radius, Grid.Shape.HEXAGON_POINTY_TOP, zoom);
        }else {
            Log.d("INIT GRID","INIT GRID");
            initGridView(radius, Grid.Shape.HEXAGON_POINTY_TOP,zoom);
        }

    }

    private Grid setGridNodes(int radius, int scale, Grid.Shape shape) {
        try {
            //Clear View
            this.mRelativeLayout.removeAllViewsInLayout();
            Log.d("RADIUS", String.valueOf(this.radius));
            int height = mRelativeLayout.getMeasuredHeight();
            Log.d("HEIGHT",String.valueOf(height));
            int width = mRelativeLayout.getMeasuredWidth();
            Log.d("WIDTH",String.valueOf(width));
            this.mRelativeLayout.removeAllViews();
            if(radius==1) this.mRelativeLayout.scrollTo(-650,-300);
            if(radius==2) this.mRelativeLayout.scrollTo(-800,-400);
            if(radius==3) this.mRelativeLayout.scrollTo(-1000,-600);
            //ADD CENTER SCROLLS!!!

            //StorageMap storageMap = new StorageMap(radius, shape, DemoObjects.squareMap);
            //final Grid grid = new Grid(radius, scale, shape);

            //My stuff
            player = controller.getPlayer();

            if(!this.movingToken){
                gaps = controller.getPlayerHexagons(player);
                //If there are not gaps and the bee is not in game
                if(gaps.isEmpty() && !controller.playerBeeInGame()) nextPlayer();
            }

            final Grid grid = new Grid(radius, scale, shape, gaps,this.game.getHive().getBoard());

            //Check if a bee fully surrounded
            int endgame = controller.endGame();
            if(endgame!=0 && !this.gameover){
                //GAME OVER
                this.gameover=true;
                gameOver(endgame);
            }

            //Gird node listener restricted to the node's circular area.
            View.OnTouchListener gridNodeTouchListener = new View.OnTouchListener() {

                @Override
                public boolean onTouch(final View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            float xPoint = event.getX();
                            float yPoint = event.getY();
                            //Hexagon hex = grid.pixelToHex(event.getX(), event.getY()); //This can work on the RelativeLayout grid area
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
                            if(!isGameover()) {
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
                switch (shape) {
                    case HEXAGON_POINTY_TOP:
                        hexagon = cube.toHex();
                        break;
                    case RECTANGLE:
                        hexagon = cube.cubeToOddRHex();
                        break;
                }

                CircleImageView view = new CircleImageView(this);
                view.setHex(hexagon);

                view.setBackgroundResource(R.drawable.orangehex);
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
                    if (checkIfGapAvailable(view.getHex(), gaps)) {
                        Log.d("PAINTING",view.getHex().toString());
                        view.setBackgroundResource(R.drawable.greyhex);
                    }
                }

                view.setOnTouchListener(gridNodeTouchListener);
                addViewToLayout(view, hexagon, grid);

            }
            return grid;


        } catch (Exception e) {
            Toast.makeText(GameUIDynamicGrid.this, "Sorry, there was a problem initializing the application.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

    private void firstPlayer(){
        String player = controller.getPlayer().getColor();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.playerTurn);
        if(player.equals("Black"))
            alert.setMessage(R.string.blackStarts);
        else
            alert.setMessage(R.string.whiteStarts);
        alert.setCancelable(true);

        final AlertDialog dlg = alert.create();
        dlg.show();

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                dlg.dismiss();
                t.cancel();
            }
        },4000); //Shows message for 4 seconds
    }

    private void gameOver(int player){
        gaps=null;
        if(player==1){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(R.string.whitePlayer);
            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("INIT GRID","INIT GRID");
                    initGridView(radius, Grid.Shape.HEXAGON_POINTY_TOP,zoom);
                }
            });
            alert.create();
            alert.show();
        }else if(player==2){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(R.string.blackPlayer);
            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("INIT GRID","INIT GRID");
                    initGridView(radius, Grid.Shape.HEXAGON_POINTY_TOP,zoom);
                }
            });
            alert.create();
            alert.show();
        }else if(player==3){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(R.string.bothPlayers);
            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("INIT GRID","INIT GRID");
                    initGridView(radius, Grid.Shape.HEXAGON_POINTY_TOP,zoom);
                }
            });
            alert.create();
            alert.show();
        }
    }

    private boolean checkIfGapAvailable(Hexagon hexagon, ArrayList<Hexagon> gaps) {
        for(int i=0;i<gaps.size();i++){
            if(hexagon.getQ()==gaps.get(i).getQ() && hexagon.getR()==gaps.get(i).getR()){
                return true;
            }
        }
        return false;
    }

    private void addViewToLayout(View view, Hexagon hexagon, Grid grid) {
        //Add to view
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(grid.width, grid.height);
        Log.d("GRID WIDTH",String.valueOf(grid.width));
        Log.d("GRID HEIGHT",String.valueOf(grid.height));
        params.addRule(RelativeLayout.RIGHT_OF, R.id.centerLayout);
        params.addRule(RelativeLayout.BELOW, R.id.centerLayout);
        mRelativeLayout.addView(view, params);

        //Set coordinates
        Point p = grid.hexToPixel(hexagon);
        switch (grid.shape) {
            case HEXAGON_POINTY_TOP:
                params.leftMargin = -grid.centerOffsetX + p.x;
                params.topMargin = -grid.centerOffsetY + p.y;
                break;
            case RECTANGLE:
                params.leftMargin = -grid.width * grid.radius -grid.centerOffsetX + p.x;
                params.topMargin = (int) (-1.5 * grid.scale * grid.radius -grid.centerOffsetY + p.y);
                break;
        }
    }

    /**
     *
     */
    private void nextPlayer(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("You can't make any move or add any piece");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                game.oneMoreRound();
                player.oneMoreTurn();
                Log.d("INIT GRID", "INIT GRID");
                initGridView(radius, Grid.Shape.HEXAGON_POINTY_TOP,zoom);
            }
        });
        alert.create();
        alert.show();
    }

    private void OnGridHexClick(final Hexagon hexagon) {
        //Toast.makeText(GameUI.this, "OnGridHexClick: " + hexagon, Toast.LENGTH_SHORT).show();

        if(this.movingToken && checkIfGapAvailable(hexagon, gaps)){
            Hexagon coords = getRealCoords(hexagon.getR(), hexagon.getQ());
            controller.movePiece(piece, coords);
            controller.oneMoreTurn();
            controller.oneMoreRound();
            this.movingToken=false;
            setBoard();
            //initGridView(radius, Grid.Shape.HEXAGON_POINTY_TOP);
        }
        else if(!this.movingToken && checkIfGapAvailable(hexagon, gaps)) {
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
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setItems(t.toArray(new String[t.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        piece = new Piece();
                        PieceType bug = language.stringToPieceType(t.get(which));
                        piece = controller.takePieceByType(bug);
                        Hexagon coords = getRealCoords(hexagon.getR(), hexagon.getQ());
                        controller.playPiece(piece, coords);
                        controller.oneMoreTurn();
                        controller.oneMoreRound();
                        setBoard();
                        //initGridView(radius, Grid.Shape.HEXAGON_POINTY_TOP);
                    }
                });
                alert.create();
                alert.show();
            }
        }else if(tokenTouched(hexagon)){
            piece = new Piece();
            piece = getTokenFromBoard(hexagon);
            possibleGaps = controller.getPossibleMoves(piece);
            if (!possibleGaps.isEmpty()) {
                movingToken = true;
                this.gaps = new ArrayList<>(possibleGaps);
                setBoard();
                //initGridView(radius, Grid.Shape.HEXAGON_POINTY_TOP);
            }
        }else if(!checkIfGapAvailable(hexagon, gaps)) {
            this.gaps = controller.getPlayerHexagons(player);
            this.movingToken=false;
            setBoard();
            //initGridView(radius, Grid.Shape.HEXAGON_POINTY_TOP);
        }

    }

    /**
     *
     * @param hexagon
     * @return
     */
    private Piece getTokenFromBoard(Hexagon hexagon){
        ArrayList<Piece> board = controller.getBoard();
        for(int i=0;i<controller.getBoardSize();i++){
            if(board.get(i).getHexagon().getR()== hexagon.getR()
                    && board.get(i).getHexagon().getQ()== hexagon.getQ()
                    && !board.get(i).isBeetle())
            return board.get(i);
        }
        return null;
    }

    /**
     *
     * @param r
     * @param q
     * @return
     */
    private Hexagon getRealCoords(int r, int q){
        for(int i=0;i<gaps.size();i++){
            if(gaps.get(i).getQ()==q && gaps.get(i).getR()==r) return gaps.get(i);
        }
        return null;
    }

    /**
     *
     * @param hexagon
     * @return
     */
    private boolean tokenTouched(Hexagon hexagon){
        ArrayList<Piece> board = controller.getBoard();
        for(int i=0;i<controller.getBoardSize();i++){
            if(board.get(i).getHexagon().getR()== hexagon.getR()
                    && board.get(i).getHexagon().getQ()== hexagon.getQ()
                    && board.get(i).getPlayer().getColor().equals(controller.getPlayer().getColor())
                    && !board.get(i).isBeetle())
                return true;
        }
        return false;
    }

    private boolean isGameover(){
        return this.gameover;
    }

    public boolean isInBoard(Hexagon hex){
        for(int i=0;i<this.gridBoard.size();i++)
            if(this.gridBoard.get(i).toString().equals(hex.toString())) return true;
        return false;
    }

}

