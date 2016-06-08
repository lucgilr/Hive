package lucgilr.inf.uva.hivev2.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import lucgilr.inf.uva.hivev2.R;

/**
 * Created by Lucía Gil Román on 07/06/16.
 */
public class HowToPlay extends AppCompatActivity {

    private String displayLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.how_to_play);

        //Language
        displayLanguage = Locale.getDefault().getDisplayLanguage();

        ImageView image_1 = (ImageView) this.findViewById(R.id.image_1);
        ImageView image_2 = (ImageView) this.findViewById(R.id.image_2);
        ImageView image_3 = (ImageView) this.findViewById(R.id.image_3);
        ImageView image_4 = (ImageView) this.findViewById(R.id.image_4);
        ImageView image_5 = (ImageView) this.findViewById(R.id.image_5);
        ImageView image_6 = (ImageView) this.findViewById(R.id.image_6);
        ImageView image_7 = (ImageView) this.findViewById(R.id.image_7);
        ImageView image_8 = (ImageView) this.findViewById(R.id.image_8);
        ImageView image_9 = (ImageView) this.findViewById(R.id.image_9);
        ImageView image_10 = (ImageView) this.findViewById(R.id.image_10);
        ImageView image_11 = (ImageView) this.findViewById(R.id.image_11);
        ImageView image_12 = (ImageView) this.findViewById(R.id.image_12);
        ImageView image_13 = (ImageView) this.findViewById(R.id.image_13);

        image_1.setImageResource(R.drawable.image_1);
        if(displayLanguage.equals("English")) {
            image_2.setImageResource(R.drawable.image_2_english);
            image_3.setImageResource(R.drawable.image_3_english);
            image_4.setImageResource(R.drawable.image_4_english);
            image_5.setImageResource(R.drawable.image_5_english);
            image_6.setImageResource(R.drawable.image_6_english);
            image_7.setImageResource(R.drawable.image_7_english);
            image_8.setImageResource(R.drawable.image_8_english);
            image_9.setImageResource(R.drawable.image_9_english);
            image_10.setImageResource(R.drawable.image_10_english);
            image_11.setImageResource(R.drawable.image_11_english);
            image_12.setImageResource(R.drawable.image_12_english);
            image_13.setImageResource(R.drawable.image_13_english);
        }else{
            image_2.setImageResource(R.drawable.image_2);
            image_3.setImageResource(R.drawable.image_3);
            image_4.setImageResource(R.drawable.image_4);
            image_5.setImageResource(R.drawable.image_5);
            image_6.setImageResource(R.drawable.image_6);
            image_7.setImageResource(R.drawable.image_7);
            image_8.setImageResource(R.drawable.image_8);
            image_9.setImageResource(R.drawable.image_9);
            image_10.setImageResource(R.drawable.image_10);
            image_11.setImageResource(R.drawable.image_11);
            image_12.setImageResource(R.drawable.image_12);
            image_13.setImageResource(R.drawable.image_13);
        }

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
