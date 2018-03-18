package fr.iut.cascade.utils;

import android.content.Context;
import android.media.MediaPlayer;

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

/*
 * For the moment only one sound at once can be played
 */
public class SoundUtil {

    private static MediaPlayer mediaPlayer;

    /**
     * Plays a music (mediaPlayer is better for long sounds => for musics)
     * @param context context
     * @param id      sound id
     * @param volume volume (between 0 and 1)
     */
    public static void playMusic(Context context, int id, float volume){
        if(!LocalSettingsUtil.sound) return;
        try {
            if(mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    // *stops* the current ongoing music with that object.
                    mediaPlayer.stop();
                }
                // We need to release() the media players otherwise the resources are not released,
                // and we soon get out of memory (since we allocate them again next time).
                mediaPlayer.release();
            }
            // creates the new object of media player. this object is having music file from raw folder which is to be played when start() method is called
            mediaPlayer = MediaPlayer.create(context, id);
            // starts playing music* if object Media player is initialized. Otherwise gives an exception.
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.start();
            /* other fix for the memory
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // the music file path is no longer associated with Media player object. So we need to reallocate memory and all. Mind it media player would not be null.
                    mediaPlayer.release();
                }
            });
            */
        }catch (Exception e){
            LoggerUtil.log("SoundUtil/Playsound", "An error occurred while trying to play the sound " + id + ". Here is the detailed message : " + e.toString(), LoggerUtil.MESSAGE_TYPE.ERROR);
        }
    }

}

