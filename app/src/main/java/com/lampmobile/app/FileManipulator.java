package com.lampmobile.app;

import android.content.Context;

import java.io.*;

/**
 * Created by root on 14.11.17.
 */
public class FileManipulator {
    private Context context;
    private String dir;
    private String fileName = "pressets.pidor";

    public FileManipulator(Context context) {
        this.context = context;
        dir = context.getFilesDir().getAbsolutePath();
        File pressets = new File(dir + "/" + fileName);
        if(!pressets.exists()){
            try {
                System.out.println(pressets.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        readFile();
    }

    void writeFile() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(fileName, context.MODE_PRIVATE)));
            // пишем данные
            for(int k = 1; k <= 160; k ++) {
                bw.write(k + "");
                byte[] data = MainApplication.getInstance().getPresset(k);
                for (int i = 0; i < 512; i++) {
                    bw.write(" " + data[i]);
                }
                bw.write("\n");
            }
            // закрываем поток
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFile() {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    context.openFileInput(fileName)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                String[] datas = str.split(" ");
                byte[] arr = new byte[512];
                for(int i = 1; i < 513; i++){
                    arr[i-1] = Byte.valueOf(datas[i]);
                }
                MainApplication.getInstance().setPresset(Integer.valueOf(datas[0]), arr);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
