package fr.iut.cascade;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;

import org.w3c.dom.Text;

import java.util.ArrayList;

import fr.iut.cascade.game.Grid;
import fr.iut.cascade.game.listeners.GridEventListener;
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

public class GameActivity extends AppCompatActivity {

    /**
     * Represents the difficulty, determined by the number of lines and columns.
     * From 1 to 4 (defined in MainActivity)
     */
    private int difficulty;

    public final static String SHARED_PREFERENCES_SCOREBOARD_NAME = "scoreboard";
    public final static String LAST_SCORE = "last_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        initButtons();
        findViewById(R.id.end_layout).setVisibility(View.INVISIBLE
        );

        difficulty = 1;
        // Recovery of the difficulty chosen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.difficulty = extras.getInt(MainActivity.DIFFICULTY);
        }else{
            throw new IllegalStateException("The user shouldn't be able to play without choosing the difficulty, has he cheated ?");
        }

        // Initialisation of the grid
        Grid grid = findViewById(R.id.grid);
        grid.init(difficulty);
        String score = Integer.toString(grid.getScore());
        ((TextView) findViewById(R.id.scoreValue)).setText(score);
        String difficulty_str = Integer.toString(difficulty);
        ((TextView) findViewById(R.id.difficultyValue)).setText(difficulty_str);

        grid.setGridEventListener(new GridEventListener() {
            @Override
            public void onScoreChanged(Grid grid) {
                String score = Integer.toString(grid.getScore());
                ((TextView) findViewById(R.id.scoreValue)).setText(score);
            }

            @Override
            public void onGameFinished(Grid grid) {
                // Get the final score
                int score = grid.getScore();
                // Save the score and get the place
                int place = SettingsUtil.saveScore(score, grid.getDifficulty(), SHARED_PREFERENCES_SCOREBOARD_NAME, getApplicationContext());
                // Set up the end screen
                showEndScreen(grid, place);
            }
        });

    }

    /**
     * Shows the end screen with animation
     * @param grid the grid when the game has finished
     */
    private void showEndScreen(Grid grid, int place){
        final int score = grid.getScore();
       // String score_str = Integer.toString(score);
        String place_str = Integer.toString(place);

        // Get the views
        LinearLayout end_layout = findViewById(R.id.end_layout);
        final TextView end_score = findViewById(R.id.end_screen_score);
        final TextView end_place = findViewById(R.id.end_screen_place);
        final TextView end_combo = findViewById(R.id.end_screen_combo);
        final TextView end_clicks = findViewById(R.id.end_screen_clicks);
        final ImageView end_difficulty = findViewById(R.id.end_screen_difficulty);


        // Set up the views
        if(place == -1){
            end_place.setText(getString(R.string.not_record));
        } else if(place == 1){
            end_place.setText(getString(R.string.best_record));
        } else{
            String message = getString(R.string.record) + place_str;
            end_place.setText(message);
        }

        end_score.setText("0");


        String max_combo = getString(R.string.max_combo) + " " + Integer.toString(grid.getBestCombo()) + " (" + Integer.toString(grid.getBestComboScore()) + ")";
        end_combo.setText(max_combo);

        String total_clicks = getString(R.string.total_clicks) + " " +Integer.toString(grid.getTotalClicks());
        end_clicks.setText(total_clicks);

        switch (difficulty){
            case 1 :
                end_difficulty.setImageResource(R.mipmap.dif_1);
                break;
            case 2:
                end_difficulty.setImageResource(R.mipmap.dif_2);
                break;
            case 3:
                end_difficulty.setImageResource(R.mipmap.dif_3);
                break;
            case 4:
                end_difficulty.setImageResource(R.mipmap.dif_4);
                break;
            default:
        }

        // Animate the views and set their visibility according to their animation
        end_layout.setVisibility(View.VISIBLE);
        end_combo.setVisibility(View.INVISIBLE);
        end_clicks.setVisibility(View.INVISIBLE);
        end_place.setVisibility(View.INVISIBLE);

        // Particles
        ParticleSystem ps = new ParticleSystem(this, 100, R.drawable.star_dark_green, 1000);
        ps.setScaleRange(0.7f, 1.3f);
        ps.setSpeedModuleAndAngleRange(0.07f, 0.16f, 0, 180);
        ps.setRotationSpeedRange(90, 180);
        ps.setAcceleration(0.00013f, 90);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.emit(end_score, 100, 1500);
        // Animation
        ValueAnimator animator = ValueAnimator.ofInt(0, score);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                end_score.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ArrayList<View> views_to_animate = new ArrayList<>();
                views_to_animate.add(end_place);
                views_to_animate.add(end_clicks);
                views_to_animate.add(end_combo);
                slideViewsInEndLayout(views_to_animate, 0);
            }
        });
        animator.start();
    }

    /**
     * Recursive method that animate the views one by one
     * @param views_to_animate list of the views to animate in the end layout
     * @param index index to start with (should be 0)
     */
    private void slideViewsInEndLayout(final ArrayList<View> views_to_animate, final int index){
        if(index > views_to_animate.size() - 1) return;

        final View view_to_animate = views_to_animate.get(index);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                findViewById(R.id.end_layout).getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(1000);
        animate.setFillAfter(false);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view_to_animate.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // If its not the last view to animate, we will start the animation for the next one
                if(index != views_to_animate.size())
                    slideViewsInEndLayout(views_to_animate, index + 1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view_to_animate.startAnimation(animate);
    }



    /**
     * Called whenever a button is pressed
     * @param view view that sent the method
     */
    public void onButtonClick(View view) {
        switch (view.getId()){
            case R.id.scoreboard_button:
                Intent scoreboardIntent = new Intent(this, ScoreboardActivity.class);
                scoreboardIntent.putExtra(MainActivity.DIFFICULTY, this.difficulty);
                scoreboardIntent.putExtra(LAST_SCORE, ((Grid) findViewById(R.id.grid)).getScore());
                startActivity(scoreboardIntent);
                break;
            case R.id.restart_button:
                Grid grid = findViewById(R.id.grid);

                // Restart the game
                grid.reset();
                String score_str = Integer.toString(grid.getScore());
                ((TextView) findViewById(R.id.scoreValue)).setText(score_str);

                // Remove the end screen
                findViewById(R.id.end_layout).setVisibility(View.INVISIBLE);

                break;
            default:
                break;
        }
    }

    /**
     * Init the buttons dynamic (pushed / released)
     */
    private void initButtons(){
        ArrayList<int[]> buttons = new ArrayList<>();
        buttons.add(new int[]{R.id.restart_button, R.mipmap.restart_default, R.mipmap.restart_pushed});
        buttons.add(new int[]{R.id.scoreboard_button, R.mipmap.scoreboard_default, R.mipmap.scoreboard_pushed});

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
