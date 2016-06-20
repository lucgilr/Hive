package lucgilr.inf.uva.hivev2.UI;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import lucgilr.inf.uva.hivev2.R;

/**
 * The first screen is a menu in which the user can choose
 * between a player vs player game or player vs AI game.
 * Created by Lucía Gil Román on 05/05/16.
 */
public class MainActivity extends ListActivity {

    private class Sample{
        private final CharSequence title;
        private final Class<? extends Activity> activityClass;

        public Sample(int titleResId, Class<? extends Activity> activityClass){
            this.activityClass = activityClass;
            this.title = getResources().getString(titleResId);
        }

        @Override
        public String toString(){
            return title.toString();
        }
    }

    private static Sample[] mSamples;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mSamples = new Sample[]{new Sample(R.string.game,GameUI.class),
                new Sample(R.string.aiGame,GameUI.class),
                new Sample(R.string.howTo,HowToPlay.class)};

        setListAdapter(new ArrayAdapter<Sample>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mSamples));
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        // Launch the sample associated with this list position.
        Intent intent = new Intent(MainActivity.this, mSamples[position].activityClass);
        Log.d("POSITION",String.valueOf(position));
        //If the user clicked the second option --> Game against the AI
        if(position==1) {
            Log.d("ADDING BUNDLE","TRUE");
            Bundle b = new Bundle();
            b.putBoolean("AI", true); //Your id
            intent.putExtras(b); //Put your id to your next Intent
        }
        startActivity(intent);
        //finish();
        //startActivity(new Intent(MainActivity.this, mSamples[position].activityClass));
    }

}
