package com.lampmobile.app;

import java.util.Arrays;

/**
 * Created by root on 14.11.17.
 */
public class LampTimer {
    private int time1;
    private int state; // 0 - начало работы, 1 - переход1, 3 - значение установилось
    private byte[] start;
    private byte[] stop;
    private byte[] current;
    private long millisStartTime;

    public LampTimer(int time1, byte[] start, byte[] stop) {
        this.time1 = time1;
        this.stop = stop;
        this.start = start;
        state = 0;
        current = Arrays.copyOf(start, 512);
        millisStartTime = System.currentTimeMillis();
    }

    public void update(){
        if(time1 == 0){
            System.arraycopy(stop, 0, current, 0, 512);
            state = 3;
            return;
        }
        state = 1;
        long millisCurrentTime = System.currentTimeMillis();
        double persent = (millisCurrentTime - millisStartTime)/(time1*1000.0);
        if(persent >= 1){
            state = 3;
            persent = 1;
        }

        for(int i = 0; i < 512; i++){
            int s1 = start[i] >= 0 ? start[i] : start[i]+256;
            int s2 = stop[i] >= 0 ? stop[i] : stop[i]+256;
            if(s1 > s2)
                current[i] = (byte) ((s1 - s2)*(1-persent) + s2);
            else
                current[i] = (byte) ((s2 - s1)*(persent) + s1);
        }

    }

    public byte[] getCurrent(){
        return Arrays.copyOf(current, 512);
    }

    public boolean isNeedWrite(){
        return  state != 3;
    }
}
