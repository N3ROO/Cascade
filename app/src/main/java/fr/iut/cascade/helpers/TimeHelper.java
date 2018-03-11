package fr.iut.cascade.helpers;

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

public class TimeHelper {
    private long lastMS;
    private boolean stopped;

    public TimeHelper(){
        this.lastMS = 0L;
        stopped = true;
    }

    /**
     * Resets the timer and starts it
     */
    public void start(){
        reset();
        stopped = false;
    }

    /**
     * Returns true when the timer reaches the delay.
     * When the delay is reached, the timer is stopped.
     * @param delay delay to reach
     * @return true when the delay is reached
     */
    public boolean isDelayComplete(long delay) {
        if (stopped)
            return false;

        if(1000000L - lastMS != 0){
            if(System.nanoTime() / 1000000L - lastMS >= delay) {
                stopped = true;
                return true;
            }
        }else if(delay <= 0){
            stopped = true;
            return true;
        }

        return false;
    }

    /**
     * Gets the current system MS
     * @return the current system MS
     */
    private long getCurrentMS(){
        return System.nanoTime() / 1000000L;
    }

    /**
     * Resets the timer
     */
    private void reset(){
        this.lastMS = getCurrentMS();
    }

}