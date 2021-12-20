package com.lampmobile.app;

import android.app.Application;

import java.util.Arrays;

public class MainApplication extends Application {
    private static MainApplication instance;
    private byte[][] masPresset = new byte[160][512];
    private boolean[] hasPresset = new boolean[160];
    private FileManipulator manipulator;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        manipulator = new FileManipulator(this.getApplicationContext());
    }

    public static MainApplication getInstance(){
        return instance;
    }

    public boolean isHasPresset(int i) {
        return hasPresset[i-1];
    }

    public byte[] getPresset(int i){
        return masPresset[i-1];
    }
    public void setPresset(int i, byte mas[]){
        masPresset[i-1] = Arrays.copyOf(mas, 512);
        hasPresset[i-1] = true;
    }

    public FileManipulator getManipulator() {
        return manipulator;
    }
}
