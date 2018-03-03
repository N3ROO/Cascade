package fr.iut.cascade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RatingBar difficultyBar = findViewById(R.id.difficulty_bar);
        difficultyBar.setIsIndicator(true);
    }

    public void onButtonClick(View view) {
        switch (view.getId()){
            case R.id.play_button:
                Intent gameActivityIntent = new Intent(this, GameActivity.class);
                gameActivityIntent.putExtra("difficulty",((RatingBar) findViewById(R.id.difficulty_bar)).getNumStars());
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

    private void updateDifficultyBar(int increment){
        RatingBar difficultyBar = findViewById(R.id.difficulty_bar);
        difficultyBar.setIsIndicator(false);
        if(increment < 0 && difficultyBar.getNumStars() != 0){
            difficultyBar.setNumStars(difficultyBar.getNumStars() + increment);
        }else if(increment > 0 && difficultyBar.getMax() != difficultyBar.getNumStars()) {
            difficultyBar.setNumStars(difficultyBar.getNumStars() + increment);
        }
        difficultyBar.setIsIndicator(true);
    }
}
