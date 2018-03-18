package fr.iut.cascade;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;

import fr.iut.cascade.utils.LocalSettingsUtil;
import fr.iut.cascade.utils.LoggerUtil;
import fr.iut.cascade.utils.SettingsUtil;

/**
 * This file is part of Cascade.
 *
 * Cascade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cascade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cascade. If not, see <http://www.gnu.org/licenses/>.
 * Author(s) : Lilian Gallon (N3RO)
 */

public class MainActivity extends AppCompatActivity{

    public final static String DIFFICULTY = "difficulty";
    public static final int DIFFICULTY_MIN = 1;
    public static final int DIFFICULTY_MAX = 4;
    private int difficulty;
    private ParticleSystem particleSystem;

    public static LocalSettingsUtil localSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Init the local settings at the application launch
        SettingsUtil.initLocalSettings(getApplicationContext());

        // Default difficulty
        setDifficultyStars(2);

        initButtons();
    }


    /**
     * Called whenever a button is pressed
     * @param view view that sent the method
     */
    public void onButtonClick(View view) {
        switch (view.getId()){
            case R.id.play_button:
                Intent gameActivityIntent = new Intent(this, GameActivity.class);
                gameActivityIntent.putExtra(DIFFICULTY, this.difficulty);
                startActivity(gameActivityIntent);
                break;
            case R.id.plus_button:
                updateDifficultyStars(1);
                break;
            case R.id.minus_button:
                updateDifficultyStars(-1);
                break;
            case R.id.scoreboard_button:
                Intent scoreboardIntent = new Intent(this, ScoreboardActivity.class);
                scoreboardIntent.putExtra(DIFFICULTY, this.difficulty);
                startActivity(scoreboardIntent);
                break;
            case R.id.settings_button:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            default:
                break;
        }
    }

    /**
     * Controller to update the difficulty bar (visual)
     * @param increment increment
     */
    private void updateDifficultyStars(int increment){
        if(increment < 0 && difficulty != DIFFICULTY_MIN){
            setDifficultyStars(this.difficulty + increment);
        }else if(increment > 0 && difficulty != DIFFICULTY_MAX) {
            setDifficultyStars(this.difficulty + increment);
        }
    }

    /**
     * Changes the displayed image according to the selected difficulty
     * @param difficulty new difficulty
     */
    private void setDifficultyStars(int difficulty){
        ImageView difficultyStars = findViewById(R.id.end_screen_difficulty);

        switch (difficulty){
            case 1 :
                difficultyStars.setImageResource(R.mipmap.dif_1);
                break;
            case 2:
                difficultyStars.setImageResource(R.mipmap.dif_2);
                break;
            case 3:
                difficultyStars.setImageResource(R.mipmap.dif_3);
                break;
            case 4:
                difficultyStars.setImageResource(R.mipmap.dif_4);
                break;
            default:
        }
        this.difficulty = difficulty;
    }

    /**
     * Init the buttons dynamic (pushed / released)
     */
    private void initButtons(){
        ArrayList<int[]> buttons = new ArrayList<>();
        buttons.add(new int[]{R.id.play_button, R.mipmap.play_default, R.mipmap.play_pushed});
        buttons.add(new int[]{R.id.plus_button, R.mipmap.plus_default, R.mipmap.plus_pushed});
        buttons.add(new int[]{R.id.minus_button, R.mipmap.minus_default, R.mipmap.minus_pushed});
        buttons.add(new int[]{R.id.scoreboard_button, R.mipmap.scoreboard_default, R.mipmap.scoreboard_pushed});
        buttons.add(new int[]{R.id.settings_button, R.mipmap.settings_default, R.mipmap.settings_pushed});
        // Actually there are 4 states for the sound button : enabled & pushed, enabled & default, disabled & pushed, disabled & default
        buttons.add(new int[]{R.id.sound_button, R.mipmap.sound_enabled_default, R.mipmap.sound_disabled_default});

        for (int[] values : buttons) {
            int button_id = values[0];
            final int button_res_default = values[1];
            final int button_res_pushed = values[2];

            // Animate the buttons
            findViewById(button_id).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    // Pressed
                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                        ((ImageView) view).setImageResource(button_res_pushed);
                    }
                    // Released
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        ((ImageView) view).setImageResource(button_res_default);
                        view.performClick();
                    }
                    return true;
                }
            });
        }
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
