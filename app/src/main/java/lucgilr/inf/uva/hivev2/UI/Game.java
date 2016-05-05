package lucgilr.inf.uva.hivev2.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.ArrayList;

import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.GameModel.Token;
import lucgilr.inf.uva.hivev2.ModelUI.Cube;
import lucgilr.inf.uva.hivev2.ModelUI.Grid;
import lucgilr.inf.uva.hivev2.ModelUI.Hex;
import lucgilr.inf.uva.hivev2.R;

public class Game extends ActionBarActivity {

    ArrayList<Hex> gaps;
    private RelativeLayout mRelativeLayout;
    //private ArrayList<Prueba> solucion;
    private lucgilr.inf.uva.hivev2.GameModel.Game game;
    private Player player;
    private boolean movingToken;
    private ArrayList<Hex> possibleGaps;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create new Game
        game = new lucgilr.inf.uva.hivev2.GameModel.Game();
        this.movingToken =false;
        possibleGaps = new ArrayList<>();
        gaps = new ArrayList<>();
        token = new Token();

        mRelativeLayout = (RelativeLayout) findViewById(R.id.gridLayout);
        //this.solucion = new ArrayList<>();

        Grid.Shape shape = Grid.Shape.HEXAGON_POINTY_TOP;
        //Grid.Shape shape = Grid.Shape.RECTANGLE;
        //int radius = 3;
        int radius = 6;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //radius = extras.getInt("GRID_RADIUS", 3);
            radius = extras.getInt("GRID_RADIUS", 6);
            shape = Grid.Shape.valueOf(extras.getString("GRID_SHAPE"));
            if (shape == null) {
                //radius = 3;
                radius = 6;
                shape = Grid.Shape.HEXAGON_POINTY_TOP;
                //shape = Grid.Shape.RECTANGLE;
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
            player = game.playerTurn();

            //Print board
            /*for(int i=0;i<this.game.getHive().getBoard().size();i++){
                Log.d("type",game.getHive().getBoard().get(i).getType().toString());
                Log.d("player",game.getHive().getBoard().get(i).getPlayer().getColor());
                Log.d("coordinates",game.getHive().getBoard().get(i).getCoordinates().toString());
                Log.d("beetle",String.valueOf(game.getHive().getBoard().get(i).isBeetle()));
            }
            Log.d("----------","-------------");*/

            /**/
            if(!this.movingToken){
                gaps = game.getHive().getPlayerGapsAvailable(player);
                //If there are not gaps and the bee is not in game
                if(gaps.isEmpty() && !player.isBeeInGame()) nextPlayer();
            }
            Log.d("gaps size",String.valueOf(gaps.size()));

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


                //First check if is an available gap
                /*if(checkIfGapAvailable(view.getHex(), gaps)){
                    view.setBackgroundResource(R.drawable.greyhex);
                }else{
                    */view.setBackgroundResource(R.drawable.orangehex);
                /*}*/

                //int size = solucion.size();
                int size = game.getHive().getBoard().size();

                if(size!=0) {

                    for(int i=0;i<game.getHive().getBoard().size();i++){

                        //String hexView = view.getHex().toString();
                        String hexView = view.getHex().toString2D();
                        //String sol = solucion.get(i).getHex().toString();
                        //String sol = game.getHive().getBoard().get(i).getCoordinates().toString();
                        String sol = game.getHive().getBoard().get(i).getCoordinates().toString2D();
                        //String color = solucion.get(i).getColor();
                        String color = game.getHive().getBoard().get(i).getPlayer().getColor();
                        int id = game.getHive().getBoard().get(i).getId();

                        if (hexView.equals(sol)) {
                            Log.d("Type:",game.getHive().getBoard().get(i).getType().toString());
                            Log.d("coordinates",game.getHive().getBoard().get(i).getCoordinates().toString());
                            Log.d("beetle",String.valueOf(game.getHive().getBoard().get(i).isBeetle()));
                        }

                        if(hexView.equals(sol)) {
                            if (!game.getHive().getBoard().get(i).isBeetle()) {
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
                //PRUEBA --> CARGAR SOLO GAPSAVAILABLE --> MEJORAR RENDIMIENTO?
                //Add available gaps
                if(checkIfGapAvailable(view.getHex(), gaps)){
                    Log.d("gap",view.getHex().toString());
                    view.setBackgroundResource(R.drawable.greyhex);
                }

                view.setOnTouchListener(gridNodeTouchListener);
                addViewToLayout(view, hex, grid);

                //Check if bee fully surrounded
                int endgame = game.beeSurrounded();
                if(endgame!=0){
                    //GAME OVER
                    gameOver(endgame);
                }

            }
            return grid;


        } catch (Exception e) {
            Toast.makeText(Game.this, "Sorry, there was a problem initializing the application.", Toast.LENGTH_LONG).show();
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
        //Toast.makeText(Game.this, "OnGridHexClick: " + hex, Toast.LENGTH_SHORT).show();

        if(this.movingToken){
            Log.d("moviendo token", token.tokenInfo());
            Hex coords = getRealCoords(hex.getR(),hex.getQ());
            game.getHive().movetoken(token, coords);
            game.oneMoreRound();
            player.oneMoreTurn();
            this.movingToken=false;
            //initGridView(3, Grid.Shape.HEXAGON_POINTY_TOP);
            initGridView(6, Grid.Shape.HEXAGON_POINTY_TOP);
        }
        else if(!this.movingToken && checkIfGapAvailable(hex, gaps)) {
            Log.d("empty gap","0");
            ArrayList<Token> tokens = player.getTokensInTheBox();
            if(!tokens.isEmpty()) {
                final ArrayList<String> t = new ArrayList<>();
                if (this.game.playerTurn().getTurn() == 4 && !this.game.playerTurn().isBeeInGame()) {
                    for (int i = 0; i < tokens.size(); i++) {
                        if (tokens.get(i).getId() == 0)
                            t.add(new String(tokens.get(i).getType().toString()));
                    }
                } else {
                    for (int i = 0; i < tokens.size(); i++) {
                        t.add(new String(tokens.get(i).getType().toString()));
                    }
                }
                //Alert Dialog -> SI T NO ESTA VACÍO, SI NO OTRA VENTANA
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setItems(t.toArray(new String[t.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(Game.this, t.get(which),Toast.LENGTH_SHORT).show();
                        token = new Token();
                        //token = player.takeTokenFromTheBox(which);
                        token = player.takeToken(t.get(which));
                        Hex coords = getRealCoords(hex.getR(), hex.getQ());
                        game.getHive().addToken(token, coords);
                        game.oneMoreRound();
                        player.oneMoreTurn();
                        //initGridView(3, Grid.Shape.HEXAGON_POINTY_TOP);
                        initGridView(6, Grid.Shape.HEXAGON_POINTY_TOP);
                    }
                });
                alert.create();
                alert.show();
            }
        }else if(tokenTouched(hex)){
            token = new Token();
            token = getTokenFromBoard(hex);
            Log.d("Token touched",token.tokenInfo());
            possibleGaps = game.getHive().getPossibleGaps(token);
            //Log.d("possiblegaps size",String.valueOf(possibleGaps.size()));
            if(!possibleGaps.isEmpty()){
                movingToken = true;
                //GapsAvailable = possibleGaps
                this.gaps = new ArrayList<>(possibleGaps);
                //initGridView(3, Grid.Shape.HEXAGON_POINTY_TOP);
                initGridView(6, Grid.Shape.HEXAGON_POINTY_TOP);
            }/*else{
                //windows for not gaps available?
            }*/
        }else if(!checkIfGapAvailable(hex, gaps)) {
            Log.d("quitar seleccion","2");
            this.gaps = game.getHive().getPlayerGapsAvailable(player);
            this.movingToken=false;
            //initGridView(3, Grid.Shape.HEXAGON_POINTY_TOP);
            initGridView(6, Grid.Shape.HEXAGON_POINTY_TOP);
        }/*else if(this.movingToken){
            Log.d("moviendo token","3");
            Hex coords = getRealCoords(hex.getR(),hex.getQ());
            game.getHive().movetoken(token, coords);
            game.oneMoreRound();
            player.oneMoreTurn();
            this.movingToken=false;
            initGridView(3, Grid.Shape.HEXAGON_POINTY_TOP);
        }*/

    }

    /**
     *
     * @param hex
     * @return
     */
    private Token getTokenFromBoard(Hex hex){
        for(int i=0;i<game.getHive().getBoard().size();i++){
            if(game.getHive().getBoard().get(i).getCoordinates().getR()==hex.getR()
                    && game.getHive().getBoard().get(i).getCoordinates().getQ()==hex.getQ()
                    && !game.getHive().getBoard().get(i).isBeetle())
                return game.getHive().getBoard().get(i);
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
        //ArrayList<Hex> gapsAvailable = game.getHive().getPlayerGapsAvailable(player);
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
        for(int i=0;i<game.getHive().getBoard().size();i++){
            if(game.getHive().getBoard().get(i).getCoordinates().getR()==hex.getR()
                    && game.getHive().getBoard().get(i).getCoordinates().getQ()==hex.getQ()
                    && game.getHive().getBoard().get(i).getPlayer().getColor().equals(player.getColor()))
                return true;
        }
        return false;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}

