package fr.iut.cascade.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import fr.iut.cascade.R;
import fr.iut.cascade.SettingsActivity;

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
     * Basic saver which saves the value "value" under the key "key", in the location "location"
     * @param app_context application context
     * @param location location
     * @param key key of the setting
     * @param value value of the setting
     * @throws IllegalArgumentException if the value type is invalid
     */
    public static void saveData(Context app_context, String location, String key, Object value) throws IllegalArgumentException{

        SharedPreferences settings = app_context.getSharedPreferences(location,0);
        SharedPreferences.Editor settings_editor = settings.edit();

        if(value.getClass() == Boolean.class){
            settings_editor.putBoolean(key, (Boolean) value);
        }else if(value.getClass() == Integer.class){
            settings_editor.putInt(key, (Integer) value);
        }else if(value.getClass() == Float.class){
            settings_editor.putFloat(key, (Float) value);
        }else if(value.getClass() == Long.class){
            settings_editor.putLong(key, (Long) value);
        }else if(value.getClass() == String.class){
            settings_editor.putString(key, (String) value);
        }else{
           throw new IllegalArgumentException("The value object is invalid (" + value.getClass().getName() + "). Expected Boolean, Integer, Float, Long or String.");
        }

        settings_editor.apply();
    }

    /**
     * Basic loader which loads an object under the key "key", in the location "location"
     * If the value was not found, these values are returned :
     * String -> null
     * Boolean -> true (lol)
     * Float -> max float
     * Int -> max int
     * Long -> max long
     * @param app_context application context
     * @param location location
     * @param key key
     * @param type type of the data loaded
     * @return data
     */
    public static Object loadData(Context app_context, String location, String key, Class<?> type) throws IllegalArgumentException{

        Object loaded_data;
        SharedPreferences settings = app_context.getSharedPreferences(location, 0);

        if(type == Boolean.class){
            loaded_data = settings.getBoolean(key, true);
        }else if(type == Integer.class){
            loaded_data = settings.getInt(key, Integer.MAX_VALUE);
        }else if(type == Float.class){
            loaded_data = settings.getFloat(key, Float.MAX_VALUE);
        }else if(type == Long.class){
            loaded_data = settings.getLong(key, Long.MAX_VALUE);
        }else if(type == String.class){
            loaded_data = settings.getString(key, null);
        }else{
            throw new IllegalArgumentException("The value object is invalid (" + type.getName() + "). Expected Boolean, Integer, Float, Long or String.");
        }

        return loaded_data;
    }

    /**
     * Saves the score if it is a new record
     * Important note : the score is stored using this template :
     * {difficulty} -> {1st score}:{2nd score}:{3rd score}:{4th score}:{5th score}
     * and so on (max of 10 for the moment)
     *
     * @param score_to_add new score
     * @param difficulty difficulty of the score
     * @param location location of the scoreboard
     * @param app_context context of the app
     * @return the place of the score in the scoreboard (-1 if not found)
     */
    public static int saveScore(int score_to_add, int difficulty,  String location, Context app_context){

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
        if(!place_found && score_list.size() >= 10){
            return -1;
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

        return index + 1;
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

    /**
     * Used to initialize the LocalSettingsUtil class with the settings
     */
    public static void initLocalSettings(Activity activity) {
        // Load particles
        try{
            LocalSettingsUtil.particles = (Integer) SettingsUtil.loadData(activity.getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.PARTICLES_KEY, Integer.class);
        }catch (Exception e){
            LocalSettingsUtil.particles = LocalSettingsUtil.DEFAULT_PARTICLES;
            LoggerUtil.log("SettingsUtils/initLocalSettings", "Couldn't load particles setting, the value has been set to the default value. Error message : " + e.toString(), LoggerUtil.MESSAGE_TYPE.ERROR);
        }

        // Load color intensity
        try{
            LocalSettingsUtil.color_intensity = (Integer) SettingsUtil.loadData(activity.getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.COLOR_INTENSITY_KEY, Integer.class);
        }catch (Exception e){
            LocalSettingsUtil.color_intensity = LocalSettingsUtil.DEFAULT_COLOR_INTENSITY;
            LoggerUtil.log("SettingsUtils/initLocalSettings", "Couldn't load color intensity setting, the value has been set to the default value. Error message : " + e.toString(), LoggerUtil.MESSAGE_TYPE.ERROR);
        }

        // Load animation
        try{
            LocalSettingsUtil.animation = (Boolean) SettingsUtil.loadData(activity.getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.ANIMATION_KEY, Boolean.class);
        }catch (Exception e){
            LocalSettingsUtil.animation = LocalSettingsUtil.DEFAULT_ANIMATION;
            LoggerUtil.log("SettingsUtils/initLocalSettings", "Couldn't load animation setting, the value has been set to the default value. Error message : " + e.toString(), LoggerUtil.MESSAGE_TYPE.ERROR);
        }

        // Init language
        String selected_language = (String) loadData(activity.getApplicationContext(), LocalSettingsUtil.SHARED_PREFERENCES_SETTINGS_NAME, LocalSettingsUtil.LANG_KEY, String.class);

        // If it's null, we save the default value used for the language
        if(selected_language == null) {
            if(!Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("français")){
                LocalSettingsUtil.language = LocalSettingsUtil.AVAILABLE_LANGUAGES[0];
            }else{
                LocalSettingsUtil.language = LocalSettingsUtil.AVAILABLE_LANGUAGES[1];
            }
        }else if(selected_language.equalsIgnoreCase(LocalSettingsUtil.AVAILABLE_LANGUAGES[0])){
            LocalSettingsUtil.language = LocalSettingsUtil.AVAILABLE_LANGUAGES[0];
        }else{
            LocalSettingsUtil.language = LocalSettingsUtil.AVAILABLE_LANGUAGES[1];
        }

        if (LocalSettingsUtil.language.equalsIgnoreCase(LocalSettingsUtil.AVAILABLE_LANGUAGES[0])) {
            setLocale("en", activity, false);
        } else {
            setLocale("fr", activity, false);
        }

    }

    /**
     * Changes the app language
     * @param lang language code (fr, en)
     */
    public static void setLocale(String lang, Activity activity, boolean should_refresh) {
        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        if(should_refresh) {
            Intent refresh = new Intent(activity, activity.getClass());
            activity.startActivity(refresh);
            activity.finish();
        }
    }
}
