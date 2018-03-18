package fr.iut.cascade;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import java.lang.reflect.Type;
import java.util.Locale;

import fr.iut.cascade.utils.LocalSettingsUtil;
import fr.iut.cascade.utils.SettingsUtil;

public class SettingsActivity extends AppCompatActivity {

    private ParticleSystem particleSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);

        initSelectors();
        initCheckBoxes();
        initSpinners();
    }

    /**
     * Used to init the spinners
     */
    private void initSpinners(){
        Spinner language_selector = findViewById(R.id.langSelector);

        // Set entries list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, LocalSettingsUtil.AVAILABLE_LANGUAGES);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        language_selector.setAdapter(adapter);

        String selected_language = (String) SettingsUtil.loadData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.LANG_KEY, String.class);

        // If it's null, we save the default value used for the language
        if(selected_language == null) {
            if (LocalSettingsUtil.language.equalsIgnoreCase(LocalSettingsUtil.AVAILABLE_LANGUAGES[0]))
                language_selector.setSelection(0);
            else
                language_selector.setSelection(1);
        }else if(selected_language.equalsIgnoreCase(LocalSettingsUtil.AVAILABLE_LANGUAGES[0])){
            language_selector.setSelection(0);
        }else{
            language_selector.setSelection(1);
        }

        language_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selected_item = ((TextView) parent.getSelectedView());
                if(selected_item != null) {
                    selected_item.setTextColor(getResources().getColor(R.color.white));
                    Typeface font = ResourcesCompat.getFont(getApplicationContext(), R.font.baloo);
                    selected_item.setTypeface(font);
                    selected_item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                }

                String lang = ((Spinner) findViewById(R.id.langSelector)).getSelectedItem().toString();
                System.out.println("lang: " + lang);
                SettingsUtil.saveData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.LANG_KEY, lang);

                if(!LocalSettingsUtil.language.equalsIgnoreCase(lang)) {
                    if (lang.equalsIgnoreCase(LocalSettingsUtil.AVAILABLE_LANGUAGES[1])){
                        setLocale("fr");
                    }else {
                        setLocale("en");
                    }
                    LocalSettingsUtil.language = lang;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * Changes the app language
     * @param lang language code (fr, en)
     */
    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, SettingsActivity.class);
        startActivity(refresh);
        finish();
    }


    /**
     * Used to init the checkboxes
     */
    private void initCheckBoxes(){
        CheckBox checkBox = findViewById(R.id.animationSelector);
        boolean is_checked = (Boolean) SettingsUtil.loadData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.ANIMATION_KEY, Boolean.class);
        checkBox.setChecked(is_checked);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsUtil.saveData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.ANIMATION_KEY, isChecked);
                LocalSettingsUtil.animation = isChecked;
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
        int particles_progress = (Integer) SettingsUtil.loadData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.PARTICLES_KEY, Integer.class);
        if(particles_progress == Integer.MAX_VALUE){
            // In this case we found nothing so we need to set it to default
            particles_progress = LocalSettingsUtil.DEFAULT_PARTICLES;
        }
        particles_selector.setProgress(particles_progress);
        particles_selector.setMax(LocalSettingsUtil.MAX_PARTICLES);

        int color_intensity_progress = (Integer) SettingsUtil.loadData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.COLOR_INTENSITY_KEY, Integer.class);
        if(color_intensity_progress == Integer.MAX_VALUE){
            // In this case we found nothing so we need to set it to default
            color_intensity_progress = LocalSettingsUtil.DEFAULT_PARTICLES;
        }
        color_intensity_selector.setProgress(color_intensity_progress);
        color_intensity_selector.setMax(LocalSettingsUtil.MAX_COLOR_INTENSITY);

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
                    SettingsUtil.saveData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.PARTICLES_KEY, progress);
                    LocalSettingsUtil.particles = progress;
                }else if(seekBar.getId() == R.id.colorIntensitySelector){
                    if(progress < LocalSettingsUtil.MIN_COLOR_INTENSITY){
                        progress = LocalSettingsUtil.MIN_COLOR_INTENSITY;
                        seekBar.setProgress(progress);
                    }
                    String label = getString(R.string.color_intensity_selector) + " : " + progress + "%";
                    ((TextView) findViewById(R.id.titleColors)).setText(label);
                    SettingsUtil.saveData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.COLOR_INTENSITY_KEY, progress);
                    LocalSettingsUtil.color_intensity = progress;
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

    /**
     * Called when the user touches the screen
     * @param event event
     * @return true ;)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Create a particle system and start emitting
                particleSystem = new ParticleSystem(this, 100 * LocalSettingsUtil.particles / 50, R.drawable.star_dark_green, 800);
                particleSystem.setScaleRange(0.7f, 1.3f);
                particleSystem.setSpeedRange(0.05f, 0.1f);
                particleSystem.setRotationSpeedRange(90, 180);
                particleSystem.setFadeOut(200, new AccelerateInterpolator());
                particleSystem.emit((int) event.getX(), (int) event.getY(), 40 * LocalSettingsUtil.particles / 50);
                break;
            case MotionEvent.ACTION_MOVE:
                particleSystem.updateEmitPoint((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                particleSystem.stopEmitting();
                break;
        }
        return true;
    }
}
