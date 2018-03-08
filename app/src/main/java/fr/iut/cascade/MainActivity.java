package fr.iut.cascade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

public class MainActivity extends AppCompatActivity{

    public final static String DIFFICULTY = "difficulty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RatingBar difficultyBar = findViewById(R.id.difficulty_bar);
        difficultyBar.setIsIndicator(true);
    }

    /**
     * Called whenever a button is pressed
     * @param view view that sent the method
     */
    public void onButtonClick(View view) {
        switch (view.getId()){
            case R.id.play_button:
                Intent gameActivityIntent = new Intent(this, GameActivity.class);
                gameActivityIntent.putExtra(DIFFICULTY,((RatingBar) findViewById(R.id.difficulty_bar)).getNumStars());
                startActivity(gameActivityIntent);
                break;
            case R.id.plus_button:
                updateDifficultyBar(1);
                break;
            case R.id.minus_button:
                updateDifficultyBar(-1);
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
     * Update the difficulty bar (visual)
     * @param increment increment
     */
    private void updateDifficultyBar(int increment){
        RatingBar difficultyBar = findViewById(R.id.difficulty_bar);
        difficultyBar.setIsIndicator(false);
        if(increment < 0 && difficultyBar.getRating() != 0){
            difficultyBar.setRating(difficultyBar.getRating() + increment);
        }else if(increment > 0 && difficultyBar.getMax() != difficultyBar.getRating()) {
            difficultyBar.setRating(difficultyBar.getRating() + increment);
        }
        difficultyBar.setIsIndicator(true);
    }
}
