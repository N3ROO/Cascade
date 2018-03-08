package fr.iut.cascade.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;

import fr.iut.cascade.R;

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

public class SettingsUtil {

    /**
     * Saves the score if it is a new record
     * Important note : the score is stored using this template :
     * {difficulty} -> {1st score}:{2nd score}:{3rd score}:{4th score}:{5th score}
     * and so on (max of 10 for the moment)
     */
    public static void saveScore(int score_to_add, int difficulty,  String location, Context app_context){

        ArrayList<Integer> score_list = loadScore(difficulty, location, app_context);

        int index = 0;
        boolean place_found = false;
        while(!place_found && index < score_list.size()){
            int score = score_list.get(index);
            if(score_to_add > score){
                place_found = true;
            }else{
                ++ index;
            }
        }

        // It isn't a best score we don't save it obviously
        if(!place_found && score_list.size() > 10){
            Toast.makeText(app_context, R.string.not_record, Toast.LENGTH_SHORT).show();
            return;
        }

        // We add the nex record at its right place, and update the size of the array,
        // you have to note that we have set the max at 10 ! So, we need no more than 10 best scores.
        // And we have to think to the case when the score_list is empty, in this case, we put the score
        // directly in the array
        if(place_found){
            score_list.add(index, score_to_add);
        }else{
            score_list.add(score_to_add);
        }
        while (score_list.size() > 10)
            score_list.remove(score_list.size() - 1);

        // Now we format the result
        StringBuilder scoreboard_values = new StringBuilder();
        for(int score : score_list){
            scoreboard_values.append(Integer.toString(score)).append(":");
        }
        // We remove the last character that is a ":"
        scoreboard_values = new StringBuilder(scoreboard_values.substring(0, scoreboard_values.length() - 1));

        // Now we can save!
        SharedPreferences scoreboard = app_context.getSharedPreferences(location,0);
        SharedPreferences.Editor scoreboard_editor = scoreboard.edit();
        scoreboard_editor.putString(Integer.toString(difficulty), scoreboard_values.toString());

        // Apply better than commit since it stores the data in the background, so the application continues
        // its execution whereas commit does it directly
        scoreboard_editor.apply();
    }

    /**
     * Loads the score
     */
    public static ArrayList<Integer> loadScore(int difficulty, String location, Context app_context){
        ArrayList<Integer> score_list = new ArrayList<>();

        String value_if_nothing_is_found = "empty";
        SharedPreferences scoreboard = app_context.getSharedPreferences(location, 0);
        String scoreboard_value = scoreboard.getString(Integer.toString(difficulty), value_if_nothing_is_found);

        // If nothing is found, the second argument is returned, so here, "value_if_nothing_is_found"
        if(scoreboard_value.equalsIgnoreCase(value_if_nothing_is_found))
            return score_list;

        String[] scoreboard_values = scoreboard_value.split(":");
        for(String score : scoreboard_values){
            score_list.add(Integer.parseInt(score));
        }

        return score_list;
    }
}
