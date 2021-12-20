package com.lampmobile.app;


import android.os.Handler;
import android.os.Message;

import java.util.Arrays;

/**
 * Created by root on 29.10.17.
 */
public class ProcessThread extends Thread {

    private boolean isRun;
    private byte data[] = new byte[512];
    private byte lastBuffer[];
    private Handler h;
    private LampTimer timer;
    private long lastTime = 0;


    public ProcessThread(Handler h){
        this.h = h;
    }

    @Override
    public void run() {
        System.out.println("Thread open");
        while(isRun){
            if(timer != null && timer.isNeedWrite()){
                if(System.currentTimeMillis() - lastTime > 30) {
                    lastTime = System.currentTimeMillis();
                    timer.update();
                    data = timer.getCurrent();
                    if(h != null){
                        Message m = new Message();
                        m.what = 2;
                        m.obj = data;
                        h.sendMessage(m);
                    }
                    Sender.sendMess512(data);
                }
            }
        }
        System.out.println("Thread close");
    }

    public void startSend(){
        isRun = true;
    }
    public void stopSend(){
        isRun = false;
    }

    public void setData(int channel, byte value){
        data[channel] = value;
    }
    public void send(){
        timer = new LampTimer(0, data, data);
    }

    public byte[] getData() {
        return data;
    }

    public boolean isRun() {
        return isRun;
    }

    public void loadPresset(int pressId, int time1){
        isRun = true;
        byte[] arr = Arrays.copyOf(MainApplication.getInstance().getPresset(pressId), 512);
        lastBuffer = Arrays.copyOf(data, 512);
        timer = new LampTimer(time1, data, arr);
    }

    public void backPresset(int time1){
        isRun = true;
        timer = new LampTimer(time1, data, lastBuffer);
    }

    public void clear(int time1){
        isRun = true;
        byte[] cl = new byte[512];
        for(int i = 0; i < 512; i ++)
            cl[i] = 0;
        timer = new LampTimer(time1, data, cl);
    }

    public void savePresset(int pressId){
        MainApplication.getInstance().setPresset(pressId, data);
        MainApplication.getInstance().getManipulator().writeFile();
    }
}
