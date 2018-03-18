package fr.iut.cascade.utils;

import java.util.Locale;

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

public class LocalSettingsUtil {

    public final static String SHARED_PREFERENCES_SETTINGS_NAME = "settings";
    public final static String PARTICLES_KEY = "particles";
    public final static String COLOR_INTENSITY_KEY = "color_intensity";
    public final static String ANIMATION_KEY = "animation";
    public final static String LANG_KEY = "lang";

    public final static int MAX_PARTICLES = 100;
    public final static int DEFAULT_PARTICLES = 50;
    //public final static int MIN_PARTICLES = 0;

    public final static int MAX_COLOR_INTENSITY = 100;
    final static int DEFAULT_COLOR_INTENSITY = 82;
    public final static int MIN_COLOR_INTENSITY = 40;
    // NEEDS TO BE 10 MIN !! But to set a minimal value for sliders, we need
    // android api 26. The minimum is then forced when the user changes the value.

    final static boolean DEFAULT_ANIMATION = true;

    public final static String[] AVAILABLE_LANGUAGES = new String[]{"English", "Français"};

    public static String language;
    public static int particles;
    public static int color_intensity;
    public static boolean animation;
}
