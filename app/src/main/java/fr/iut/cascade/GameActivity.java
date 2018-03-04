package fr.iut.cascade;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import fr.iut.cascade.game.Cell;
import fr.iut.cascade.game.Grid;

public class GameActivity extends AppCompatActivity {

    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.difficulty = extras.getInt(MainActivity.DIFFICULTY);
        }

        Grid grid = findViewById(R.id.grid);
        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(new Cell(0,0, Color.RED));
        cells.add(new Cell(4,4, Color.GREEN));
        grid.init(5,5, cells);
    }
}
