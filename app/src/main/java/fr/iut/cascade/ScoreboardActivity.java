package fr.iut.cascade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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

public class ScoreboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        ArrayList<LinearLayout> layouts = new ArrayList<>();
        layouts.add((LinearLayout) findViewById(R.id.layoutDifficulty1));
        layouts.add((LinearLayout) findViewById(R.id.layoutDifficulty2));
        layouts.add((LinearLayout) findViewById(R.id.layoutDifficulty3));
        layouts.add((LinearLayout) findViewById(R.id.layoutDifficulty4));

        updateScoreboards(layouts);
    }

    /**
     * It updates all the scoreboards with the scores
     * @param layouts layouts
     */
    private void updateScoreboards(ArrayList<LinearLayout> layouts){
        for(int index = 0 ; index < layouts.size() ; ++ index){
            LinearLayout layout = layouts.get(index);

            ArrayList<Integer> score_list = SettingsUtil.loadScore(index + 1, GameActivity.SHARED_PREFERENCES_SCOREBOARD_NAME, getApplicationContext());
            for(int score : score_list){
                TextView textView = new TextView(this);
                String score_str = Integer.toString(score);
                textView.setText(score_str);
                textView.setWidth(88);
                layout.addView(textView);
            }
        }

    }
}
