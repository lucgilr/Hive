package lucgilr.inf.uva.hivev2.UI;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lucgilr.inf.uva.hivev2.R;

/**
 * @author Lucía Gil Román (https://github.com/lucgilr)
 *
 *         Rules of the Game!
 */
public class HowToPlay extends FragmentActivity {

    private String displayLanguage;
    private final int numberImages = 16;
    private final int numberParagraphs = 16;
    private int countImages;
    private int countParagraphs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.how_to_play);

        this.countImages = 1;
        this.countParagraphs = 1;

        //Language
        displayLanguage = Locale.getDefault().getDisplayLanguage();

        //First image and paragraph
        final ImageView image = (ImageView) this.findViewById(R.id.image_1);
        final TextView paragraph = (TextView) this.findViewById(R.id.first_paragraph);
        image.setImageResource(R.drawable.image_1);
        paragraph.setText(R.string.paragraph_1);

        //Setting maps
        final Map<String, Integer> map = new HashMap<>();
        map.put("1", R.drawable.image_1);
        map.put("2", R.drawable.image_2);
        map.put("3", R.drawable.image_3);
        map.put("4", R.drawable.image_4);
        map.put("5", R.drawable.bee);
        map.put("6", R.drawable.bee);
        map.put("7", R.drawable.image_5);
        map.put("8", R.drawable.image_6);
        map.put("9", R.drawable.image_7);
        map.put("10", R.drawable.image_8);
        map.put("11", R.drawable.image_9);
        map.put("12", R.drawable.image_10);
        map.put("13", R.drawable.image_11);
        map.put("14", R.drawable.image_12);
        map.put("15", R.drawable.image_13);
        map.put("16", R.drawable.final_image);

        final Map<String, Integer> mapEnglish = new HashMap<>();
        mapEnglish.put("1", R.drawable.image_1);
        mapEnglish.put("2", R.drawable.image_2_english);
        mapEnglish.put("3", R.drawable.image_3_english);
        mapEnglish.put("4", R.drawable.image_4_english);
        mapEnglish.put("5", R.drawable.bee);
        mapEnglish.put("6", R.drawable.bee);
        mapEnglish.put("7", R.drawable.image_5_english);
        mapEnglish.put("8", R.drawable.image_6_english);
        mapEnglish.put("9", R.drawable.image_7_english);
        mapEnglish.put("10", R.drawable.image_8_english);
        mapEnglish.put("11", R.drawable.image_9_english);
        mapEnglish.put("12", R.drawable.image_10_english);
        mapEnglish.put("13", R.drawable.image_11_english);
        mapEnglish.put("14", R.drawable.image_12_english);
        mapEnglish.put("15", R.drawable.image_13_english);
        mapEnglish.put("16", R.drawable.final_image);

        final Map<String, Integer> mapParagraphs = new HashMap<>();
        mapParagraphs.put("1", R.string.paragraph_1);
        mapParagraphs.put("2", R.string.paragraph_2);
        mapParagraphs.put("3", R.string.paragraph_3);
        mapParagraphs.put("4", R.string.paragraph_4);
        mapParagraphs.put("5", R.string.paragraph_5);
        mapParagraphs.put("6", R.string.paragraph_6);
        mapParagraphs.put("7", R.string.paragraph_7);
        mapParagraphs.put("8", R.string.paragraph_8);
        mapParagraphs.put("9", R.string.paragraph_9);
        mapParagraphs.put("10", R.string.paragraph_10);
        mapParagraphs.put("11", R.string.paragraph_11);
        mapParagraphs.put("12", R.string.paragraph_12);
        mapParagraphs.put("13", R.string.paragraph_13);
        mapParagraphs.put("14", R.string.paragraph_14);
        mapParagraphs.put("15", R.string.paragraph_15);
        mapParagraphs.put("16", R.string.paragraph_16);

        paragraph.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countImages < numberImages + 1) {
                    nextImage();
                    String imageMsg = String.valueOf(countImages);
                    if (displayLanguage.equals("español")) {
                        image.setImageResource(map.get(imageMsg));
                    } else {
                        image.setImageResource(mapEnglish.get(imageMsg));
                    }
                }
                if (countParagraphs < numberParagraphs + 1) {
                    nextParagraph();
                    String paragraphMsg = String.valueOf(countParagraphs);
                    paragraph.setText(mapParagraphs.get(paragraphMsg));
                }
            }
        });

        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                previousImage();
                if (countImages > 0) {
                    String imageMsg = String.valueOf(countImages);
                    if (displayLanguage.equals("español")) {
                        image.setImageResource(map.get(imageMsg));
                    } else {
                        image.setImageResource(mapEnglish.get(imageMsg));
                    }
                }
                previousParagraph();
                if (countParagraphs > 0) {
                    String paragraphMsg = String.valueOf(countParagraphs);
                    paragraph.setText(mapParagraphs.get(paragraphMsg));
                }
            }
        });


    }

    private void nextImage() {
        if (this.countImages < numberImages) this.countImages += 1;
    }

    private void nextParagraph() {
        if (this.countParagraphs < numberParagraphs) this.countParagraphs += 1;
    }

    private void previousImage() {
        if (this.countImages > 1) this.countImages -= 1;
    }

    private void previousParagraph() {
        if (this.countParagraphs > 1) this.countParagraphs -= 1;
    }

}
