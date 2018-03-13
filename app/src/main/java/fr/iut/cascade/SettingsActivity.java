package fr.iut.cascade;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ((SeekBar) findViewById(R.id.particlesSelector)).getProgressDrawable().setColorFilter(new PorterDuffColorFilter(0, PorterDuff.Mode.SRC_IN));
        ((SeekBar) findViewById(R.id.colorIntensitySelector)).getProgressDrawable().setColorFilter(new PorterDuffColorFilter(0, PorterDuff.Mode.SRC_IN));

        // TODO : handle listeners

        initSettings();
    }

    /**
     * Method used to load the current settings of the user, and to apply them to the components
     */
    private void initSettings() {
        // TODO load current settings and apply them
    }
}
