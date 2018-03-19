package fr.iut.cascade;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;

import fr.iut.cascade.game.Cell;
import fr.iut.cascade.game.Grid;
import fr.iut.cascade.game.listeners.GridEventListener;
import fr.iut.cascade.utils.LocalSettingsUtil;
import fr.iut.cascade.utils.SettingsUtil;
import fr.iut.cascade.utils.SoundUtil;

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

        // Grid previewer
        final Grid grid = findViewById(R.id.viewerGrid);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(GameActivity.BLUE);
        colors.add(GameActivity.RED);
        colors.add(GameActivity.ORANGE);
        colors.add(GameActivity.PURPLE);
        colors.add(GameActivity.GREEN);
        colors.add(GameActivity.YELLOW);
        grid.init(5, colors, LocalSettingsUtil.color_intensity * 255 / 100, LocalSettingsUtil.animation);

        grid.setGridEventListener(new GridEventListener() {
            @Override
            public void onScoreChanged(Grid grid, int score_increment) {
                showInstantScoreInformation(score_increment);
            }

            @Override
            public void onGameFinished(Grid grid) {
                grid.reset();
            }

            @Override
            public void onTouchEvent(Cell clicked_cell, float cell_width, float cell_height, boolean has_destroyed_cells, MotionEvent motionEvent) {
                int particle_resource = R.drawable.star_white;

                if (clicked_cell != null) {
                    int color = clicked_cell.getColor();
                    if (color == GameActivity.BLUE) particle_resource = R.drawable.star_blue;
                    if (color == GameActivity.ORANGE) particle_resource = R.drawable.star_orange;
                    if (color == GameActivity.YELLOW) particle_resource = R.drawable.star_yellow;
                    if (color == GameActivity.GREEN) particle_resource = R.drawable.star_green;
                    if (color == GameActivity.RED) particle_resource = R.drawable.star_red;
                    if (color == GameActivity.PURPLE) particle_resource = R.drawable.star_purple;

                    if (has_destroyed_cells) {
                        if (Math.random() < 0.5) {
                            SoundUtil.playMusic(getApplicationContext(), R.raw.hit1, 1);
                        } else {
                            SoundUtil.playMusic(getApplicationContext(), R.raw.hit2, 0.8f);
                        }
                    } else {
                        SoundUtil.playMusic(getApplicationContext(), R.raw.hit_alone, 1);
                    }
                } else {
                    SoundUtil.playMusic(getApplicationContext(), R.raw.hit_failed, 1);
                }

                // Particle image shift
                final float x_shift = 20 / 2;
                final float y_shift = -100;

                ParticleSystem particles = new ParticleSystem((Activity) grid.getContext(), 50 * LocalSettingsUtil.particles / 50, particle_resource, 100);
                particles.setSpeedRange(0.2f, 0.5f);
                particles.setAcceleration(0.0005f, 90);
                particles.emit((int) (motionEvent.getX() + x_shift + findViewById(R.id.viewerGrid).getX()), (int) (motionEvent.getY() + cell_height + y_shift + findViewById(R.id.viewerGrid).getY()), 50, 100);
            }
        });
    }

    /**
     * Show the instant score information
     *
     * @param score_increment score increment
     */
    private void showInstantScoreInformation(int score_increment) {
        final TextView text = findViewById(R.id.instant_score_info_viewer);

        // Animation of the text (fade in and then fade out)
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        in.setDuration(GameActivity.FAST_SCORE_INFO_FADE_DURATION);
        out.setDuration(GameActivity.FAST_SCORE_INFO_FADE_DURATION);

        // We say that the out animation will start later
        out.setStartOffset(GameActivity.FAST_SCORE_INFO_DISPLAY_DURATION);

        // Then android gives us a nice animation set ;)
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(in);
        animationSet.addAnimation(out);

        // Let's apply the animation to our text
        String score_increment_str = Integer.toString(score_increment);
        text.setText(score_increment_str);
        text.setVisibility(View.VISIBLE);
        text.startAnimation(animationSet);

        // But, don't forget to turn off the text when the animation ends !
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                text.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        // Now, we put particles according to the score increment
        int particle_number;
        int drawable_id;
        int text_color;

        if (score_increment < 100) {
            drawable_id = R.drawable.star_red;
            particle_number = 5 * LocalSettingsUtil.particles / 50;
            text_color = GameActivity.RED;
        } else if (score_increment >= 100 && score_increment < 250) {
            drawable_id = R.drawable.star_orange;
            particle_number = 10 * LocalSettingsUtil.particles / 50;
            text_color = GameActivity.ORANGE;
        } else if (score_increment >= 250 && score_increment < 700) {
            drawable_id = R.drawable.star_yellow;
            particle_number = 15 * LocalSettingsUtil.particles / 50;
            text_color = GameActivity.YELLOW;
        } else if (score_increment >= 700 && score_increment < 1000) {
            drawable_id = R.drawable.star_green;
            particle_number = 20 * LocalSettingsUtil.particles / 50;
            text_color = GameActivity.GREEN;
        } else if (score_increment >= 1000 && score_increment < 1600) {
            drawable_id = R.drawable.star_dark_green;
            particle_number = 25 * LocalSettingsUtil.particles / 50;
            text_color = GameActivity.GREEN;
        } else {
            drawable_id = R.drawable.animated_particle;
            particle_number = 30 * LocalSettingsUtil.particles / 50;
            text_color = GameActivity.BLUE;
        }

        text.setTextColor(text_color);


        new ParticleSystem(this, 30 * LocalSettingsUtil.particles / 50, drawable_id, GameActivity.FAST_SCORE_INFO_DISPLAY_DURATION)
                .setSpeedRange(0.1f, 0.2f)
                .oneShot(text, particle_number);

    }

    /**
     * Used to init the spinners
     */
    private void initSpinners() {
        Spinner language_selector = findViewById(R.id.langSelector);

        // Set entries list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, LocalSettingsUtil.AVAILABLE_LANGUAGES);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        language_selector.setAdapter(adapter);

        String selected_language = (String) SettingsUtil.loadData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.LANG_KEY, String.class);

        // If it's null, we save the default value used for the language
        if (selected_language == null) {
            if (LocalSettingsUtil.language.equalsIgnoreCase(LocalSettingsUtil.AVAILABLE_LANGUAGES[0]))
                language_selector.setSelection(0);
            else
                language_selector.setSelection(1);
        } else if (selected_language.equalsIgnoreCase(LocalSettingsUtil.AVAILABLE_LANGUAGES[0])) {
            language_selector.setSelection(0);
        } else {
            language_selector.setSelection(1);
        }
        final Activity activity = this;
        language_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selected_item = ((TextView) parent.getSelectedView());
                if (selected_item != null) {
                    selected_item.setTextColor(getResources().getColor(R.color.white));
                    Typeface font = ResourcesCompat.getFont(getApplicationContext(), R.font.baloo);
                    selected_item.setTypeface(font);
                    selected_item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                }

                String lang = ((Spinner) findViewById(R.id.langSelector)).getSelectedItem().toString();
                SettingsUtil.saveData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.LANG_KEY, lang);

                if (!LocalSettingsUtil.language.equalsIgnoreCase(lang)) {
                    if (lang.equalsIgnoreCase(LocalSettingsUtil.AVAILABLE_LANGUAGES[1])) {
                        SettingsUtil.setLocale("fr", activity, true);
                    } else {
                        SettingsUtil.setLocale("en", activity, true);
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
     * Used to init the checkboxes
     */
    private void initCheckBoxes() {
        CheckBox checkBox = findViewById(R.id.animationSelector);
        boolean is_checked = (Boolean) SettingsUtil.loadData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.ANIMATION_KEY, Boolean.class);
        checkBox.setChecked(is_checked);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsUtil.saveData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.ANIMATION_KEY, isChecked);
                LocalSettingsUtil.animation = isChecked;
                ((Grid) findViewById(R.id.viewerGrid)).shouldAnimate(LocalSettingsUtil.animation);
            }
        });
    }


    /**
     * Used to init the style and the listeners of the selectors
     */
    private void initSelectors() {
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
        if (particles_progress == Integer.MAX_VALUE) {
            // In this case we found nothing so we need to set it to default
            particles_progress = LocalSettingsUtil.DEFAULT_PARTICLES;
        }
        particles_selector.setProgress(particles_progress);
        particles_selector.setMax(LocalSettingsUtil.MAX_PARTICLES);

        int color_intensity_progress = (Integer) SettingsUtil.loadData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.COLOR_INTENSITY_KEY, Integer.class);
        if (color_intensity_progress == Integer.MAX_VALUE) {
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
                if (seekBar.getId() == R.id.particlesSelector) {
                    String label = getString(R.string.particles_selector) + " : " + progress + "%";
                    ((TextView) findViewById(R.id.titleParticles)).setText(label);
                    SettingsUtil.saveData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.PARTICLES_KEY, progress);
                    LocalSettingsUtil.particles = progress;
                } else if (seekBar.getId() == R.id.colorIntensitySelector) {
                    if (progress < LocalSettingsUtil.MIN_COLOR_INTENSITY) {
                        progress = LocalSettingsUtil.MIN_COLOR_INTENSITY;
                        seekBar.setProgress(progress);
                    }
                    String label = getString(R.string.color_intensity_selector) + " : " + progress + "%";
                    ((TextView) findViewById(R.id.titleColors)).setText(label);
                    SettingsUtil.saveData(getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.COLOR_INTENSITY_KEY, progress);
                    LocalSettingsUtil.color_intensity = progress;
                    ((Grid) findViewById(R.id.viewerGrid)).setColorAlpha(LocalSettingsUtil.color_intensity * 255 / 100);
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
     *
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

    /**
     * Called when the user clicks on back
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SoundUtil.playMusic(getApplicationContext(), R.raw.back, 0.4f);
    }
}
