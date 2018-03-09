package fr.iut.cascade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;

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
    private static final int DIFFICULTY_MIN = 1;
    private static final int DIFFICULTY_MAX = 4;
    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Default difficulty
        difficulty = 2;
        setDifficultyStars(this.difficulty);

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
                startActivity(scoreboardIntent);
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
    
    private void setDifficultyStars(int difficulty){
        ImageView difficultyStars = findViewById(R.id.difficulty_stars);

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

    private void initButtons(){
        ArrayList<int[]> buttons = new ArrayList<>();
        buttons.add(new int[]{R.id.play_button, R.mipmap.play_default, R.mipmap.play_pushed});
        buttons.add(new int[]{R.id.plus_button, R.mipmap.plus_defaut, R.mipmap.plus_pushed});
        buttons.add(new int[]{R.id.minus_button, R.mipmap.minus_defaut, R.mipmap.minus_pushed});
        buttons.add(new int[]{R.id.scoreboard_button, R.mipmap.scoreboard_default, R.mipmap.scoreboard_pushed});
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
}
