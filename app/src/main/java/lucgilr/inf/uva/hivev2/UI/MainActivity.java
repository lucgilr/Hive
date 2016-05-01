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

import lucgilr.inf.uva.hivev2.GameModel.Coords;
import lucgilr.inf.uva.hivev2.GameModel.Game;
import lucgilr.inf.uva.hivev2.GameModel.Player;
import lucgilr.inf.uva.hivev2.GameModel.Token;
import lucgilr.inf.uva.hivev2.ModelUI.Cube;
import lucgilr.inf.uva.hivev2.ModelUI.Grid;
import lucgilr.inf.uva.hivev2.ModelUI.Hex;
import lucgilr.inf.uva.hivev2.ModelUI.Prueba;
import lucgilr.inf.uva.hivev2.R;

public class MainActivity extends ActionBarActivity {

    ArrayList<Hex> gaps;
    private RelativeLayout mRelativeLayout;
    //private ArrayList<Prueba> solucion;
    private Game game;
    private Player player;
    private boolean movingToken;
    private ArrayList<Hex> possibleGaps;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create new Game
        game = new Game();
        this.movingToken =false;
        possibleGaps = new ArrayList<>();
        gaps = new ArrayList<>();
        token = new Token();

        mRelativeLayout = (RelativeLayout) findViewById(R.id.gridLayout);
        //this.solucion = new ArrayList<>();

        Grid.Shape shape = Grid.Shape.HEXAGON_POINTY_TOP;
        //Grid.Shape shape = Grid.Shape.RECTANGLE;
        int radius = 3;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            radius = extras.getInt("GRID_RADIUS", 3);
            shape = Grid.Shape.valueOf(extras.getString("GRID_SHAPE"));
            if (shape == null) {
                radius = 3;
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
        int scale = (int) (displayWidth / ((2*radius + 1) * (Math.sqrt(1))));

        // Changes the height and width of the grid to the specified *pixels*
        params.width = Grid.getGridWidth(radius, scale, shape);
        params.height = Grid.getGridHeight(radius, scale, shape);

        return scale;
    }

    private Grid setGridNodes(int radius, int scale, Grid.Shape shape) {
        try {
            //StorageMap storageMap = new StorageMap(radius, shape, DemoObjects.squareMap);
            final Grid grid = new Grid(radius, scale, shape);

            //My stuff
            player = game.playerTurn();
            /**/
            if(!this.movingToken) gaps = game.getHive().getPlayerGapsAvailable(player);

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

                //int size = solucion.size();
                int size = game.getHive().getBoard().size();

                //First check if is an available gap
                if(checkIfGapAvailable(view.getHex(), gaps)){
                    view.setBackgroundResource(R.drawable.greyhex);
                }else{
                    view.setBackgroundResource(R.drawable.orangehex);
                }


                if(size!=0) {

                    for(int i=0;i<game.getHive().getBoard().size();i++){

                        String hexView = view.getHex().toString();
                        //String sol = solucion.get(i).getHex().toString();
                        String sol = game.getHive().getBoard().get(i).getCoordinates().toString();
                        //String color = solucion.get(i).getColor();
                        String color = game.getHive().getBoard().get(i).getPlayer().getColor();

                        if (hexView.equals(sol)) {
                            if(color.equals("White")) {
                                switch (game.getHive().getBoard().get(i).getId()) {
                                    case 0:
                                        view.setBackgroundResource(R.drawable.whitebee);
                                        break;
                                    case 1:
                                        view.setBackgroundResource(R.drawable.whitegrass);
                                        break;
                                    case 2:
                                        view.setBackgroundResource(R.drawable.whitegrass);
                                        break;
                                    case 3:
                                        view.setBackgroundResource(R.drawable.whitegrass);
                                        break;
                                    case 4:
                                        view.setBackgroundResource(R.drawable.whitespider);
                                        break;
                                    case 5:
                                        view.setBackgroundResource(R.drawable.whitespider);
                                        break;
                                    case 6:
                                        view.setBackgroundResource(R.drawable.whitebeetle);
                                        break;
                                    case 7:
                                        view.setBackgroundResource(R.drawable.whitebeetle);
                                        break;
                                    case 8:
                                        view.setBackgroundResource(R.drawable.whiteant);
                                        break;
                                    case 9:
                                        view.setBackgroundResource(R.drawable.whiteant);
                                        break;
                                    case 10:
                                        view.setBackgroundResource(R.drawable.whiteant);
                                        break;
                                }
                            }else{
                                switch (game.getHive().getBoard().get(i).getId()) {
                                    case 0:
                                        view.setBackgroundResource(R.drawable.blackbee);
                                        break;
                                    case 1:
                                        view.setBackgroundResource(R.drawable.blackgrass);
                                        break;
                                    case 2:
                                        view.setBackgroundResource(R.drawable.blackgrass);
                                        break;
                                    case 3:
                                        view.setBackgroundResource(R.drawable.blackgrass);
                                        break;
                                    case 4:
                                        view.setBackgroundResource(R.drawable.blackspider);
                                        break;
                                    case 5:
                                        view.setBackgroundResource(R.drawable.blackspider);
                                        break;
                                    case 6:
                                        view.setBackgroundResource(R.drawable.blackbeetle);
                                        break;
                                    case 7:
                                        view.setBackgroundResource(R.drawable.blackbeetle);
                                        break;
                                    case 8:
                                        view.setBackgroundResource(R.drawable.blackant);
                                        break;
                                    case 9:
                                        view.setBackgroundResource(R.drawable.blackant);
                                        break;
                                    case 10:
                                        view.setBackgroundResource(R.drawable.blackant);
                                        break;
                                }
                            }
                        }
                    }

                }
                view.setOnTouchListener(gridNodeTouchListener);
                addViewToLayout(view, hex, grid);

            }
            return grid;


        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Sorry, there was a problem initializing the application.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
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

    private void OnGridHexClick(final Hex hex) {
        //Toast.makeText(MainActivity.this, "OnGridHexClick: " + hex, Toast.LENGTH_SHORT).show();

        //ArrayList<Hex> gaps = new ArrayList<>();
        //gaps = game.getHive().getPlayerGapsAvailable(player);
        ArrayList<Token> tokens = player.getTokensInTheBox();

        //PRUEBA --> PASAR TOKEN A ARRAYLIST DE STRINGS
        final ArrayList<String> t = new ArrayList<>();
        for(int i=0; i<tokens.size();i++){
            t.add(new String(tokens.get(i).getType().toString()));
        }

        if(!this.movingToken && checkIfGapAvailable(hex, gaps)) {
            //Alert Dialog
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setItems(t.toArray(new String[t.size()]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(MainActivity.this, t.get(which),Toast.LENGTH_SHORT).show();
                    token = new Token();
                    //token = player.takeTokenFromTheBox(which);
                    token = player.takeToken(t.get(which));
                    Hex coords = getRealCoords(hex.getR(),hex.getQ());
                    game.getHive().addToken(token, coords);
                    game.oneMoreRound();
                    player.oneMoreTurn();
                    initGridView(3, Grid.Shape.HEXAGON_POINTY_TOP);
                }
            });
            alert.create();
            alert.show();
        }else if(tokenTouched(hex)){
            token = new Token();
            token = getTokenFromBoard(hex);
            possibleGaps = game.getHive().getPossibleGaps(token);
            if(!possibleGaps.isEmpty()){
                movingToken = true;
                //GapsAvailable = possibleGaps
                this.gaps = new ArrayList<>(possibleGaps);
                initGridView(3, Grid.Shape.HEXAGON_POINTY_TOP);
            }
        }else if(!checkIfGapAvailable(hex, gaps)) {
            this.gaps = game.getHive().getPlayerGapsAvailable(player);
            this.movingToken=false;
            initGridView(3, Grid.Shape.HEXAGON_POINTY_TOP);
        }else if(this.movingToken){
            Hex coords = getRealCoords(hex.getR(),hex.getQ());
            game.getHive().movetoken(token, coords);
            game.oneMoreRound();
            player.oneMoreTurn();
            this.movingToken=false;
            initGridView(3, Grid.Shape.HEXAGON_POINTY_TOP);
        }

    }

    /**
     *
     * @param hex
     * @return
     */
    private Token getTokenFromBoard(Hex hex){
        for(int i=0;i<game.getHive().getBoard().size();i++){
            if(game.getHive().getBoard().get(i).getCoordinates().getR()==hex.getR()
                    && game.getHive().getBoard().get(i).getCoordinates().getQ()==hex.getQ())
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

