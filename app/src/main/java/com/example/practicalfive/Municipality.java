package com.example.practicalfive;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * Second screen. Displays the name, image, summary and official website
 * link of the municipality selected on the main screen. The colours of the
 * name TextView, the information text and the Visit site button change
 * according to the selected municipality.
 */
public class Municipality extends AppCompatActivity {

    /** Intent extra key used by both activities (same key on both sides). */
    public static final String EXTRA_MUNICIPALITY =
            "com.example.practicalfive.EXTRA_MUNICIPALITY";

    /** Official English websites of the four municipalities. */
    private static final String URL_BEIJING = "https://english.beijing.gov.cn/";
    private static final String URL_SHANGHAI = "https://english.shanghai.gov.cn/";
    private static final String URL_TIANJIN = "https://en.tj.gov.cn/";
    private static final String URL_CHONGQING = "https://english.cq.gov.cn/";

    private TextView textViewName;
    private TextView textViewInfo;
    private ImageView imageViewMunicipality;
    private Button buttonVisitSite;

    private String websiteUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality);

        textViewName = findViewById(R.id.textViewName);
        textViewInfo = findViewById(R.id.textViewInfo);
        imageViewMunicipality = findViewById(R.id.imageViewMunicipality);
        buttonVisitSite = findViewById(R.id.buttonVisitSite);

        // Retrieve the name passed from MainActivity, then validate it again
        // with TextProcessor so missing or invalid data is handled safely.
        String received = getIntent() != null
                ? getIntent().getStringExtra(EXTRA_MUNICIPALITY) : null;
        String municipality = new TextProcessor().processMunicipality(received);

        if (municipality == null) {
            // Nothing valid to display - return to the main screen.
            finish();
            return;
        }

        // Look up the summary. The array order is Beijing, Shanghai,
        // Chongqing, Tianjin (matching municipalities_array).
        String[] summaries =
                getResources().getStringArray(R.array.municipalities_array);

        int position;
        int colorRes;
        int imageRes;
        switch (municipality) {
            case "Shanghai":
                position = 1;
                colorRes = R.color.municipality_shanghai;
                imageRes = R.drawable.shanghai;
                websiteUrl = URL_SHANGHAI;
                break;
            case "Chongqing":
                position = 2;
                colorRes = R.color.municipality_chongqing;
                imageRes = R.drawable.chongqing;
                websiteUrl = URL_CHONGQING;
                break;
            case "Tianjin":
                position = 3;
                colorRes = R.color.municipality_tianjin;
                imageRes = R.drawable.tianjin;
                websiteUrl = URL_TIANJIN;
                break;
            case "Beijing":
            default:
                position = 0;
                colorRes = R.color.municipality_beijing;
                imageRes = R.drawable.beijing;
                websiteUrl = URL_BEIJING;
                break;
        }

        // Municipality name on a coloured background with light text.
        textViewName.setText(municipality);
        textViewName.setBackgroundColor(
                ContextCompat.getColor(this, colorRes));
        textViewName.setTextColor(
                ContextCompat.getColor(this, R.color.color_text_light));

        // Corresponding photo.
        imageViewMunicipality.setImageResource(imageRes);

        // Summary text in the municipality colour, scrollable in the XML.
        textViewInfo.setText(summaries[position]);
        textViewInfo.setTextColor(ContextCompat.getColor(this, colorRes));

        // Visit site button tinted in the municipality colour.
        buttonVisitSite.setBackgroundTintList(
                ColorStateList.valueOf(
                        ContextCompat.getColor(this, colorRes)));

        // Implicit Intent: open the official website in a browser app.
        buttonVisitSite.setOnClickListener(v -> openWebsite());
    }

    /** Opens the currently displayed municipality's official website. */
    private void openWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // No browser (or other suitable app) is installed.
            Toast.makeText(this, R.string.error_no_browser,
                    Toast.LENGTH_LONG).show();
        }
    }
}
