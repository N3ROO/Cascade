package fr.iut.cascade;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;

import fr.iut.cascade.utils.LocalSettingsUtil;
import fr.iut.cascade.utils.SettingsUtil;
import fr.iut.cascade.utils.SoundUtil;

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

public class ScoreboardActivity extends AppCompatActivity {

    private int difficulty;
    private int[] last_score;
    private int[] last_combo;
    private ParticleSystem particleSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scoreboard);

        // Default values
        difficulty = 2;
        last_score = new int[]{-1, -1};
        last_combo = new int[]{-1, -1};

        initButtons();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.difficulty = extras.getInt(MainActivity.DIFFICULTY);

            if(getIntent().hasExtra(GameActivity.LAST_SCORE)){
                this.last_score[0] = this.difficulty;
                this.last_score[1] = extras.getInt(GameActivity.LAST_SCORE);
            }
            if(getIntent().hasExtra(GameActivity.LAST_COMBO)){
                this.last_combo[0] = this.difficulty;
                this.last_combo[1] = extras.getInt(GameActivity.LAST_COMBO);
            }
        }else{
            throw new IllegalStateException("The user shouldn't be able to play without choosing the difficulty, has he cheated ?");
        }

        setDifficultyStars(this.difficulty);
    }

    /**
     * It updates the scoreboard with the scores of the difficulty chosen
     */
    private void updateScoreboard(){

        LinearLayout layout =  findViewById(R.id.rankingLayout);
        layout.removeAllViews();
        Typeface font = ResourcesCompat.getFont(getApplicationContext(), R.font.rubik_mono_one);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f);

        // This boolean is used to prevent indicating in green multiple scores that have the same value (we only want to indicate the last score the user made)
        boolean last_score_was_indicated = false;
        ArrayList<Integer> score_list = SettingsUtil.loadScore(Integer.toString(this.difficulty), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, getApplicationContext());
        if(score_list.size() != 0){
            addText(getString(R.string.scoreboard_score_title), layoutParams, layout, R.color.white, font, 20, View.TEXT_ALIGNMENT_TEXT_START);
        }
        for(int score : score_list){
            if(this.last_score[0] == this.difficulty && this.last_score[1] == score && !last_score_was_indicated) {
                addText(Integer.toString(score), layoutParams, layout, R.color.greenTheme, font, 18, View.TEXT_ALIGNMENT_CENTER);
                last_score_was_indicated = true;
            }else {
                addText(Integer.toString(score), layoutParams, layout, R.color.white, font, 18, View.TEXT_ALIGNMENT_CENTER);
            }
        }

        boolean last_combo_was_indicated = false;
        ArrayList<Integer> combo_list = SettingsUtil.loadScore(Integer.toString(this.difficulty) + "c", LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, getApplicationContext());
        if(combo_list.size() != 0){
            LinearLayout.LayoutParams layoutParamsCombo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f);
            layoutParamsCombo.setMargins(0, 40, 0, 0);
            addText(getString(R.string.scoreboard_combo_title), layoutParamsCombo, layout, R.color.white, font, 20,  View.TEXT_ALIGNMENT_TEXT_START);
        }
        for(int combo : combo_list){
            if(this.last_combo[0] == this.difficulty && this.last_combo[1] == combo && !last_combo_was_indicated) {
                addText(Integer.toString(combo), layoutParams, layout, R.color.greenTheme, font, 18, View.TEXT_ALIGNMENT_CENTER);
                last_combo_was_indicated = true;
            }else {
                addText(Integer.toString(combo), layoutParams, layout, R.color.white, font, 18, View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

    /**
     * Adds a text
     * @param label text label
     * @param layoutParams layout params of the text
     * @param layout layout on which the text should be put
     * @param color_id color of the text
     * @param font of the text
     * @param size of the text
     * @param text_alignment of the text
     */
    private void addText(String label, LinearLayout.LayoutParams layoutParams, LinearLayout layout, int color_id, Typeface font, int size, int text_alignment){
        TextView textView = new TextView(this);
        textView.setText(label);
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(getResources().getColor(color_id));
        textView.setTextSize(size);
        textView.setTextAlignment(text_alignment);
        textView.setTypeface(font);
        layout.addView(textView);
    }

    /**
     * Called whenever a button is pressed
     * @param view view that sent the method
     */
    public void onButtonClick(View view) {
        switch (view.getId()){
            case R.id.plus_button:
                updateDifficultyStars(1);
                break;
            case R.id.minus_button:
                updateDifficultyStars(-1);
                break;
            default:
                break;
        }
        SoundUtil.playMusic(getApplicationContext(), R.raw.select, 1);
    }

    /**
     * Controller to update the difficulty bar (visual)
     * @param increment increment
     */
    private void updateDifficultyStars(int increment){
        if(increment < 0 && difficulty != MainActivity.DIFFICULTY_MIN){
            setDifficultyStars(this.difficulty + increment);
        }else if(increment > 0 && difficulty != MainActivity.DIFFICULTY_MAX) {
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
        updateScoreboard();
    }

    /**
     * Init the buttons dynamic (pushed / released)
     */
    private void initButtons(){
        ArrayList<int[]> buttons = new ArrayList<>();
        buttons.add(new int[]{R.id.plus_button, R.mipmap.plus_default, R.mipmap.plus_pushed});
        buttons.add(new int[]{R.id.minus_button, R.mipmap.minus_default, R.mipmap.minus_pushed});

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
                particleSystem = new ParticleSystem(this, 100, R.drawable.star_dark_green, 800);
                particleSystem.setScaleRange(0.7f, 1.3f);
                particleSystem.setSpeedRange(0.05f, 0.1f);
                particleSystem.setRotationSpeedRange(90, 180);
                particleSystem.setFadeOut(200, new AccelerateInterpolator());
                particleSystem.emit((int) event.getX(), (int) event.getY(), 40);
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
