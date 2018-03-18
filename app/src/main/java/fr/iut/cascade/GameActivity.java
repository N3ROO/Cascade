package fr.iut.cascade;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
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
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;

import org.w3c.dom.Text;

import java.util.ArrayList;

import fr.iut.cascade.game.Cell;
import fr.iut.cascade.game.Grid;
import fr.iut.cascade.game.listeners.GridEventListener;
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

public class GameActivity extends AppCompatActivity {

    /**
     * Represents the difficulty, determined by the number of lines and columns.
     * From 1 to 4 (defined in MainActivity)
     */
    private int difficulty;

    public final static String LAST_SCORE = "last_score";
    public final static int FAST_SCORE_INFO_DISPLAY_DURATION = 2000;
    public final static int FAST_SCORE_INFO_FADE_DURATION = 250;

    public static final int GREEN = Color.rgb(135 , 245, 50);
    public static final int YELLOW = Color.rgb(245, 245, 50);
    public static final int BLUE = Color.rgb(50,50,245);
    public static final int ORANGE = Color.rgb(245, 190, 50);
    public static final int RED = Color.rgb(245, 70, 50);
    public static final int PURPLE = Color.rgb(230, 50, 245);


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        initButtons();
        findViewById(R.id.end_layout).setVisibility(View.INVISIBLE);

        difficulty = 1;
        // Recovery of the difficulty chosen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.difficulty = extras.getInt(MainActivity.DIFFICULTY);
        }else{
            throw new IllegalStateException("The user shouldn't be able to play without choosing the difficulty, has he cheated ?");
        }

        // Initialisation of the grid
        final Grid grid = findViewById(R.id.grid);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(BLUE);
        colors.add(RED);
        colors.add(ORANGE);
        colors.add(PURPLE);
        colors.add(GREEN);
        colors.add(YELLOW);
        grid.init(difficulty, colors, LocalSettingsUtil.color_intensity * 255 / 100, LocalSettingsUtil.animation);
        String score = Integer.toString(grid.getScore());
        ((TextView) findViewById(R.id.scoreValue)).setText(score);
        String difficulty_str = Integer.toString(difficulty);
        ((TextView) findViewById(R.id.difficultyValue)).setText(difficulty_str);

        grid.setGridEventListener(new GridEventListener() {
            @Override
            public void onScoreChanged(Grid grid, int score_increment) {
                String score = Integer.toString(grid.getScore());
                ((TextView) findViewById(R.id.scoreValue)).setText(score);
                showInstantScoreInformation(score_increment);
            }

            @Override
            public void onGameFinished(Grid grid) {
                // Get the final score
                int score = grid.getScore();
                // Save the score and get the place
                int place = SettingsUtil.saveScore(score, grid.getDifficulty(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, getApplicationContext());
                // Set up the end screen
                showEndScreen(grid, place);
            }

            @Override
            public void onTouchEvent(Cell clicked_cell, float cell_width, float cell_height, MotionEvent motionEvent) {
                int particle_resource = R.drawable.star_white;

                if(clicked_cell != null) {
                    int color = clicked_cell.getColor();
                    if (color == BLUE) particle_resource = R.drawable.star_blue;
                    if (color == ORANGE) particle_resource = R.drawable.star_orange;
                    if (color == YELLOW) particle_resource = R.drawable.star_yellow;
                    if (color == GREEN) particle_resource = R.drawable.star_green;
                    if (color == RED) particle_resource = R.drawable.star_red;
                    if (color == PURPLE) particle_resource = R.drawable.star_purple;
                }

                // Particle image shift
                final float x_shift =  20 / 2;
                final float y_shift = - 22 / 2;

                ParticleSystem particles = new ParticleSystem((Activity) grid.getContext(), 50 * LocalSettingsUtil.particles / 50, particle_resource, 100);
                particles.setSpeedRange(0.2f, 0.5f);
                particles.setAcceleration(0.0005f, 90);
                particles.emit((int)(motionEvent.getX() + x_shift) , (int)(motionEvent.getY() + cell_height + y_shift), 50, 100);
            }
        });

    }

    /**
     * Shows the text indicating the last score increment
     * @param score_increment score_increment
     */
    private void showInstantScoreInformation(int score_increment){
        final TextView text = findViewById(R.id.instant_score_info);

        // Animation of the text (fade in and then fade out)
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        in.setDuration(FAST_SCORE_INFO_FADE_DURATION);
        out.setDuration(FAST_SCORE_INFO_FADE_DURATION);

        // We say that the out animation will start later
        out.setStartOffset(FAST_SCORE_INFO_DISPLAY_DURATION);

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
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                text.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Now, we put particles according to the score increment
        int particle_number;
        int drawable_id;
        int text_color;

        if(score_increment < 100){
            drawable_id = R.drawable.star_red;
            particle_number = 5 * LocalSettingsUtil.particles / 50;
            text_color = RED;
        }else if(score_increment >= 100 && score_increment < 250){
            drawable_id = R.drawable.star_orange;
            particle_number = 10 * LocalSettingsUtil.particles / 50;
            text_color = ORANGE;
        }else if(score_increment >= 250 && score_increment < 700){
            drawable_id = R.drawable.star_yellow;
            particle_number = 15 * LocalSettingsUtil.particles / 50;
            text_color = YELLOW;
        }else if(score_increment >= 700 && score_increment < 1000){
            drawable_id = R.drawable.star_green;
            particle_number = 20 * LocalSettingsUtil.particles / 50;
            text_color = GREEN;
        }else if(score_increment >= 1000 && score_increment < 1600){
            drawable_id = R.drawable.star_dark_green;
            particle_number = 25 * LocalSettingsUtil.particles / 50;
            text_color = GREEN;
        }else{
            drawable_id = R.drawable.animated_particle;
            particle_number = 30 * LocalSettingsUtil.particles / 50;
            text_color = BLUE;
        }

        text.setTextColor(text_color);


        new ParticleSystem(this, 30 * LocalSettingsUtil.particles / 50, drawable_id, FAST_SCORE_INFO_DISPLAY_DURATION )
                .setSpeedRange(0.1f, 0.2f)
                .oneShot(text, particle_number);

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
        ParticleSystem ps = new ParticleSystem(this, 100 * LocalSettingsUtil.particles / 50, R.drawable.star_dark_green, 1000);
        ps.setScaleRange(0.7f, 1.3f);
        ps.setSpeedModuleAndAngleRange(0.07f, 0.16f, 0, 180);
        ps.setRotationSpeedRange(90, 180);
        ps.setAcceleration(0.00013f, 90);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.emit(end_score, 100 * LocalSettingsUtil.particles / 50, 1500);
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
