package me.offeex.bloomware.api.util;

public class TimerUtil {
    private long time = -1L;

     public void reset() {
         time = System.currentTimeMillis();
     }

     public boolean passed(long time) {
         return System.currentTimeMillis() - this.time >= time;
     }

}
