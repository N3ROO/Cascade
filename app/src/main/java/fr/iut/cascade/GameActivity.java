package fr.iut.cascade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
                int score = grid.getScore();
                String score_str = Integer.toString(grid.getScore());
                SettingsUtil.saveScore(score, grid.getDifficulty(), SHARED_PREFERENCES_SCOREBOARD_NAME, getApplicationContext());
                Toast.makeText(getBaseContext(), getString(R.string.score_label) + " " + score_str, Toast.LENGTH_LONG).show();
                grid.reset();
                ((TextView) findViewById(R.id.scoreValue)).setText(score_str);
            }
        });
    }
}
