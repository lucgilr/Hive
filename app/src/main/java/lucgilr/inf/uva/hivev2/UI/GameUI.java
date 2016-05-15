package lucgilr.inf.uva.hivev2.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;

import lucgilr.inf.uva.hivev2.Controller.GameController;
import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Language;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.GameModel.Token;
import lucgilr.inf.uva.hivev2.GameModel.Cube;
import lucgilr.inf.uva.hivev2.GameModel.Grid;
import lucgilr.inf.uva.hivev2.GameModel.Hex;
import lucgilr.inf.uva.hivev2.R;

/**
 * The original code has been modified but it can be found in:
 * https://github.com/omplanet/android-hexagonal-grids/blob/master/HexagonalGrids/app/src/main/java/net/omplanet/hexagonalgrids/ui/MainActivity.java
 * When a player choose to play a game against other real player
 * in the same device it will load this Activity.
 */
public class GameUI extends AppCompatActivity {

    private Game game;
    private GameController controller;
    private Language language;

    ArrayList<Hex> gaps;
    private RelativeLayout mRelativeLayout;
    private ScrollView vScrollView;
    private HorizontalScrollView hScrollView;
    private Player player;
    private boolean movingToken;
    private ArrayList<Hex> possibleGaps;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

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

        //Create new GameUI
        game = new Game();
        controller = new GameController(game,this);
        language = new Language();

        this.movingToken =false;
        possibleGaps = new ArrayList<>();
        gaps = new ArrayList<>();
        token = new Token();

        mRelativeLayout = (RelativeLayout) findViewById(R.id.gridLayout);

        Grid.Shape shape = Grid.Shape.HEXAGON_POINTY_TOP;
        int radius = 6;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            radius = extras.getInt("GRID_RADIUS", 6);
            shape = Grid.Shape.valueOf(extras.getString("GRID_SHAPE"));
            if (shape == null) {
                radius = 6;
                shape = Grid.Shape.HEXAGON_POINTY_TOP;
            }
        }

        initGridView(radius, shape);
    }

    private void initGridView(int radius, Grid.Shape shape) {
        int scale = setGridDimensions(radius, shape);
        Grid grid = setGridNodes(radius, scale, shape);

    }

    private int setGridDimensions(int radius, Grid.Shape shape) {
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
        int scale = (int) (displayWidth / ((1*radius + 1) * (Math.sqrt(1))));

        // Changes the height and width of the grid to the specified *pixels*
        params.width = Grid.getGridWidth(radius, scale, shape);
        params.height = Grid.getGridHeight(radius, scale, shape);

        return scale;
    }

    private Grid setGridNodes(int radius, int scale, Grid.Shape shape) {
        try {
            //StorageMap storageMap = new StorageMap(radius, shape, DemoObjects.squareMap);
            //final Grid grid = new Grid(radius, scale, shape);
            final Grid grid = new Grid(radius, scale, shape);

            //My stuff
            player = controller.getPlayer();

            if(!this.movingToken){
                gaps = controller.getPlayerGaps(player);
                //If there are not gaps and the bee is not in game
                if(gaps.isEmpty() && !controller.playerBeeInGame()) nextPlayer();
            }

            //Gird node listener restricted to the node's circular area.
            View.OnTouchListener gridNodeTouchListener = new View.OnTouchListener() {

                @Override
                public boolean onTouch(final View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            float xPoint = event.getX();
                            float yPoint = event.getY();
                            //Hex hex = grid.pixelToHex(event.getX(), event.getY()); //This can work on the RelativeLayout grid area
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
                            v.setSelected(false);
                            CircleImageView view = (CircleImageView) v;
                            OnGridHexClick(view.getHex());
                            break;
                    }
                    return true;
                }
            };

            for(Cube cube : grid.nodes) {
                Hex hex = null;
                switch (shape) {
                    case HEXAGON_POINTY_TOP:
                        hex = cube.toHex();
                        break;
                    case RECTANGLE:
                        hex = cube.cubeToOddRHex();
                        break;
                }

                CircleImageView view = new CircleImageView(this);
                view.setHex(hex);

                view.setBackgroundResource(R.drawable.orangehex);
                int size = controller.getBoardSize();

                if(size!=0) {

                    for(int i=0;i<size;i++){

                        String hexView = view.getHex().toString2D();
                        String sol = controller.getBoard().get(i).getCoordinates().toString2D();
                        String color = controller.getBoard().get(i).getPlayer().getColor();
                        int id = controller.getBoard().get(i).getId();

                        if(hexView.equals(sol)) {
                            if(!controller.getBoard().get(i).isBeetle()){
                                if (color.equals("White")) {
                                    if (id == 0) view.setBackgroundResource(R.drawable.whitebee);
                                    else if (id == 1 || id == 2 || id == 3)
                                        view.setBackgroundResource(R.drawable.whitegrass);
                                    else if (id == 4 || id == 5)
                                        view.setBackgroundResource(R.drawable.whitespider);
                                    else if (id == 6 || id == 7)
                                        view.setBackgroundResource(R.drawable.whitebeetle);
                                    else view.setBackgroundResource(R.drawable.whiteant);
                                } else {
                                    if (id == 0) view.setBackgroundResource(R.drawable.blackbee);
                                    else if (id == 1 || id == 2 || id == 3)
                                        view.setBackgroundResource(R.drawable.blackgrass);
                                    else if (id == 4 || id == 5)
                                        view.setBackgroundResource(R.drawable.blackspider);
                                    else if (id == 6 || id == 7)
                                        view.setBackgroundResource(R.drawable.blackbeetle);
                                    else view.setBackgroundResource(R.drawable.blackant);
                                }
                            }
                        }

                    }

                }

                if(checkIfGapAvailable(view.getHex(), gaps)){
                    view.setBackgroundResource(R.drawable.greyhex);
                }

                view.setOnTouchListener(gridNodeTouchListener);
                addViewToLayout(view, hex, grid);

                //Check if bee fully surrounded
                int endgame = controller.endGame();
                if(endgame!=0){
                    //GAME OVER
                    gameOver(endgame);
                }

            }
            return grid;


        } catch (Exception e) {
            Toast.makeText(GameUI.this, "Sorry, there was a problem initializing the application.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

    private void gameOver(int player){
        gaps=null;
        if(player==1){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("White player wins!");
            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //
                }
            });
            alert.create();
            alert.show();
        }else if(player==2){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Black player wins!");
            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //
                }
            });
            alert.create();
            alert.show();
        }else if(player==3){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("It's a draw...");
            alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //
                }
            });
            alert.create();
            alert.show();
        }
    }

    private boolean checkIfGapAvailable(Hex hex, ArrayList<Hex> gaps) {
        for(int i=0;i<gaps.size();i++){
            if(hex.getQ()==gaps.get(i).getQ() && hex.getR()==gaps.get(i).getR()){
                return true;
            }
        }
        return false;
    }

    private void addViewToLayout(View view, Hex hex, Grid grid) {
        //Add to view
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(grid.width, grid.height);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.centerLayout);
        params.addRule(RelativeLayout.BELOW, R.id.centerLayout);
        mRelativeLayout.addView(view, params);

        //Set coordinates
        Point p = grid.hexToPixel(hex);
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
        alert.setMessage("You can't make any move or add any token");
        alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               game.oneMoreRound();
                player.oneMoreTurn();
                initGridView(6, Grid.Shape.HEXAGON_POINTY_TOP);
            }
        });
        alert.create();
        alert.show();
    }

    private void OnGridHexClick(final Hex hex) {
        //Toast.makeText(GameUI.this, "OnGridHexClick: " + hex, Toast.LENGTH_SHORT).show();

        if(this.movingToken && checkIfGapAvailable(hex, gaps)){
            Log.d("1.moving token","...");
            Hex coords = getRealCoords(hex.getR(),hex.getQ());
            controller.movetoken(token, coords);
            controller.oneMoreTurn();
            controller.oneMoreRound();
            this.movingToken=false;
            initGridView(6, Grid.Shape.HEXAGON_POINTY_TOP);
        }
        else if(!this.movingToken && checkIfGapAvailable(hex, gaps)) {
            Log.d("2.select token","...");
            ArrayList<Token> tokens = controller.getTokensFromBox();
            if(!tokens.isEmpty()) {
                final ArrayList<String> t = new ArrayList<>();
                int turn = controller.getPlayerTurn();
                boolean bee = controller.playerBeeInGame();
                final String displayLanguage = Locale.getDefault().getDisplayLanguage();
                String bug ="";
                if(turn == 4 && !bee){
                    for (int i = 0; i < tokens.size(); i++) {
                        if (tokens.get(i).getId() == 0) {
                            if (displayLanguage.equals("English")) {
                                bug = language.getEnglish(new String(tokens.get(i).getType().toString()));
                            } else {
                                bug = language.getSpanish(new String(tokens.get(i).getType().toString()));
                            }
                            t.add(bug);
                        }
                    }
                } else {
                    for (int i = 0; i < tokens.size(); i++) {
                        if (displayLanguage.equals("English")) {
                            bug = language.getEnglish(new String(tokens.get(i).getType().toString()));
                        } else {
                            bug = language.getSpanish(new String(tokens.get(i).getType().toString()));
                        }
                        t.add(bug);
                    }
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setItems(t.toArray(new String[t.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        token = new Token();
                        String bug = language.StringToTokenString(t.get(which));
                        token = controller.takeTokenByType(bug);
                        Hex coords = getRealCoords(hex.getR(), hex.getQ());
                        controller.playToken(token,coords);
                        controller.oneMoreTurn();
                        controller.oneMoreRound();
                        initGridView(6, Grid.Shape.HEXAGON_POINTY_TOP);
                    }
                });
                alert.create();
                alert.show();
            }
        }else if(tokenTouched(hex)){
            Log.d("3.Token touched","...");
            token = new Token();
            token = getTokenFromBoard(hex);
            //If the token touched is one of yours
            if(token.getPlayer().getColor().equals(this.player.getColor())) {
                possibleGaps = controller.getPossibleMoves(token);
                if (!possibleGaps.isEmpty()) {
                    movingToken = true;
                    this.gaps = new ArrayList<>(possibleGaps);
                    initGridView(6, Grid.Shape.HEXAGON_POINTY_TOP);
                }
                //If its not --> deselect
            }else{
                Log.d("4. deselect","...");
                this.gaps = controller.getPlayerGaps(player);
                this.movingToken=false;
                initGridView(6, Grid.Shape.HEXAGON_POINTY_TOP);
            }
        }else if(!checkIfGapAvailable(hex, gaps)) {
            Log.d("4. deselect","...");
            this.gaps = controller.getPlayerGaps(player);
            this.movingToken=false;
            initGridView(6, Grid.Shape.HEXAGON_POINTY_TOP);
        }

    }

    /**
     *
     * @param hex
     * @return
     */
    private Token getTokenFromBoard(Hex hex){
        ArrayList<Token> board = controller.getBoard();
        for(int i=0;i<controller.getBoardSize();i++){
            if(board.get(i).getCoordinates().getR()==hex.getR()
                    && board.get(i).getCoordinates().getQ()==hex.getQ()
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
    private Hex getRealCoords(int r, int q){
        for(int i=0;i<gaps.size();i++){
            if(gaps.get(i).getQ()==q && gaps.get(i).getR()==r) return gaps.get(i);
        }
        return null;
    }

    /**
     *
     * @param hex
     * @return
     */
    private boolean tokenTouched(Hex hex){
        ArrayList<Token> board = controller.getBoard();
        for(int i=0;i<controller.getBoardSize();i++){
            if(board.get(i).getCoordinates().getR()==hex.getR()
                    && board.get(i).getCoordinates().getQ()==hex.getQ()
                    && board.get(i).getPlayer().getColor().equals(controller.getPlayer().getColor())
                    && !board.get(i).isBeetle())
                return true;
        }
        return false;
    }
}

