package lucgilr.inf.uva.hivev2.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import lucgilr.inf.uva.hivev2.R;

/**
 * Created by Lucía Gil Román on 07/06/16.
 */
public class HowToPlay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*this.setContentView(R.layout.how_to_play);

        WebView myWebView = (WebView) this.findViewById(R.id.webView);
        myWebView.setWebViewClient(new WebViewClient());

        //myWebView.loadUrl("file:///android_assets/HowToPlay.html");
        myWebView.loadUrl("https://www.google.es");*/

        this.setContentView(R.layout.how_to_play_v2);

        TextView first_paragraph = (TextView) this.findViewById(R.id.first_paragraph);
        first_paragraph.setText(R.string.paragraph_1);

        TextView second_paragraph = (TextView) this.findViewById(R.id.second_paragraph);
        second_paragraph.setText(R.string.paragraph_2);

        TextView third_paragraph = (TextView) this.findViewById(R.id.third_paragraph);
        third_paragraph.setText(R.string.paragraph_3);

        TextView fourth_paragraph = (TextView) this.findViewById(R.id.fourth_paragraph);
        fourth_paragraph.setText(R.string.paragraph_4);

        TextView fifth_paragraph = (TextView) this.findViewById(R.id.fifth_paragraph);
        fifth_paragraph.setText(R.string.paragraph_5);

        TextView sixth_paragraph = (TextView) this.findViewById(R.id.sixth_paragraph);
        sixth_paragraph.setText(R.string.paragraph_6);

        TextView seventh_paragraph = (TextView) this.findViewById(R.id.seventh_paragraph);
        seventh_paragraph.setText(R.string.paragraph_7);

        TextView eighth_paragraph = (TextView) this.findViewById(R.id.eighth_paragraph);
        eighth_paragraph.setText(R.string.paragraph_8);

        TextView ninth_paragraph = (TextView) this.findViewById(R.id.ninth_paragraph);
        ninth_paragraph.setText(R.string.paragraph_9);

        TextView tenth_paragraph = (TextView) this.findViewById(R.id.tenth_paragraph);
        tenth_paragraph.setText(R.string.paragraph_10);

        TextView eleventh_paragraph = (TextView) this.findViewById(R.id.eleventh_paragraph);
        eleventh_paragraph.setText(R.string.paragraph_11);

        TextView twelfth_paragraph = (TextView) this.findViewById(R.id.twelfth_paragraph);
        twelfth_paragraph.setText(R.string.paragraph_12);

        TextView thirtieth_paragraph = (TextView) this.findViewById(R.id.thirtieth_paragraph);
        thirtieth_paragraph.setText(R.string.paragraph_13);

        TextView fourteenth_paragraph = (TextView) this.findViewById(R.id.fourteenth_paragraph);
        fourteenth_paragraph.setText(R.string.paragraph_14);

    }

}
