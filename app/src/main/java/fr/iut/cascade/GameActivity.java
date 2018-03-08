package fr.iut.cascade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import fr.iut.cascade.game.Grid;
import fr.iut.cascade.game.listeners.GridEventListener;

public class GameActivity extends AppCompatActivity {

    /**
     * Represents the difficulty, determined by the number of lines and columns.
     * From 1 to 4 (defined in MainActivity)
     */
    private int difficulty;
    private final String SCORE_PREFIX = "Score : ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
        //((TextView) findViewById(R.id.scoreValue)).setText(grid.getScore());

        grid.setGridEventListener(new GridEventListener() {
            @Override
            public void scoreChanged(Grid grid) {
                ((TextView) findViewById(R.id.scoreValue)).setText(grid.getScore());
            }
        });
    }
}
