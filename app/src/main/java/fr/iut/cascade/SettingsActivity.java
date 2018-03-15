package fr.iut.cascade;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import fr.iut.cascade.utils.SettingsUtil;

public class SettingsActivity extends AppCompatActivity {


    public final static String SHARED_PREFERENCES_SETTINGS_NAME = "settings";
    private final static String PARTICLES_KEY = "particles";
    private final static String COLOR_INTENSITY_KEY = "color_intensity";
    private final static String ANIMATION_KEY = "color_intensity";
    private final static int DEFAULT_PARTICLE_PROGRESS = 50;
    private final static int DEFAULT_COLOR_PROGRESS = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);

        initSelectors();
        initCheckBoxes();
    }


    /**
     * Used to init the checkboxes
     */
    private void initCheckBoxes(){
        CheckBox checkBox = findViewById(R.id.animationSelector);
        boolean is_checked = (Boolean) SettingsUtil.loadData(getApplicationContext(), SHARED_PREFERENCES_SETTINGS_NAME, ANIMATION_KEY, Boolean.class);
        checkBox.setChecked(is_checked);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsUtil.saveData(getApplicationContext(), SHARED_PREFERENCES_SETTINGS_NAME, ANIMATION_KEY, isChecked);
            }
        });
    }


    /**
     * Used to init the style and the listeners of the selectors
     */
    private void initSelectors(){
        // Get the selectors
        SeekBar particles_selector = findViewById(R.id.particlesSelector);

        SeekBar color_intensity_selector = findViewById(R.id.colorIntensitySelector);

        // Change the color style
        particles_selector.getThumb().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
        particles_selector.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));

        color_intensity_selector.getThumb().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
        color_intensity_selector.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));

        // Load the data
        int particles_progress = (Integer) SettingsUtil.loadData(getApplicationContext(), SHARED_PREFERENCES_SETTINGS_NAME, PARTICLES_KEY, Integer.class);
        if(particles_progress == Integer.MAX_VALUE){
            // In this case we found nothing so we need to set it to default
            particles_progress = DEFAULT_PARTICLE_PROGRESS;
        }
        particles_selector.setProgress(particles_progress);

        int color_intensity_progress = (Integer) SettingsUtil.loadData(getApplicationContext(), SHARED_PREFERENCES_SETTINGS_NAME, COLOR_INTENSITY_KEY, Integer.class);
        if(color_intensity_progress == Integer.MAX_VALUE){
            // In this case we found nothing so we need to set it to default
            color_intensity_progress = DEFAULT_COLOR_PROGRESS;
        }
        color_intensity_selector.setProgress(color_intensity_progress);

        // Set the text
        String label_particles = getString(R.string.particles_selector) + " : " + particles_selector.getProgress() + "%";
        ((TextView) findViewById(R.id.titleParticles)).setText(label_particles);

        String label_color = getString(R.string.color_intensity_selector) + " : " + color_intensity_selector.getProgress() + "%";
        ((TextView) findViewById(R.id.titleColors)).setText(label_color);


        // Set the listener
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(seekBar.getId() == R.id.particlesSelector){
                    String label = getString(R.string.particles_selector) + " : " + progress + "%";
                    ((TextView) findViewById(R.id.titleParticles)).setText(label);
                    SettingsUtil.saveData(getApplicationContext(), SHARED_PREFERENCES_SETTINGS_NAME,PARTICLES_KEY, progress);
                }else if(seekBar.getId() == R.id.colorIntensitySelector){
                    String label = getString(R.string.color_intensity_selector) + " : " + progress + "%";
                    ((TextView) findViewById(R.id.titleColors)).setText(label);
                    SettingsUtil.saveData(getApplicationContext(), SHARED_PREFERENCES_SETTINGS_NAME,COLOR_INTENSITY_KEY, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        color_intensity_selector.setOnSeekBarChangeListener(listener);
        particles_selector.setOnSeekBarChangeListener(listener);
    }
}
