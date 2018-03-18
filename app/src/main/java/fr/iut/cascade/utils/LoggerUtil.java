package fr.iut.cascade.utils;

import android.util.Log;

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

public class LoggerUtil {

    public enum MESSAGE_TYPE{
        VERBOSE, DEBUG, INFO, WARNING, ERROR, WTF
    }

    public static void log(String tag, String message, MESSAGE_TYPE message_type){
        String time = Long.toString(System.currentTimeMillis());
        switch (message_type){
            case VERBOSE:
                Log.v("[" + time + "]: " + tag, message);
                break;
            case INFO:
                Log.i("[" + time + "]: " + tag, message);
                break;
            case DEBUG:
                Log.d("[" + time + "]: " + tag, message);
                break;
            case ERROR:
                Log.e("[" + time + "]: " + tag, message);
                break;
            case WARNING:
                Log.w("[" + time + "]: " + tag, message);
                break;
            case WTF:
                Log.wtf("[" + time + "]: " + tag, message);
                break;
            default:
                Log.wtf("[" + time + "]: " + tag, "wtf in wtf! " + message);
                break;
        }
    }
}
