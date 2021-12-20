package com.lampmobile.app;


import android.graphics.Color;
import android.widget.Button;

public class PressetManipulator{
    private int page=1;
    private Button but[] = new Button[16];
    private int presetIdState = -1;
    private boolean btnState;
    public PressetManipulator(Button[] buts){
        this.but = buts;
        for(int i = 0; i <but.length; i++){
            but[i].setBackgroundColor(Color.DKGRAY);
        }
    }

    //не реализовано пока
//        public void deletePresset(int index){
//            this.data[index] = new byte[512];
//            but[index].setEnabled(false);
//        }
//        public void changePresset(int index,byte mas512[]){
//            this.data[index] = mas512;
//            but[index].setEnabled(true);
//        }
    public void applyPage(){
        for(int i=0;i<16;i++){
            but[i].setText(""+(16*(page-1)+i+1));
            but[i].setEnabled(MainApplication.getInstance().isHasPresset(16*(page-1)+i+1));
        }
        for(int i = 0; i <but.length; i++){
            if(i + (page-1)*16 == presetIdState-1) {
                if (!btnState)
                    but[i].setBackgroundColor(Color.GREEN);
                else
                    but[i].setBackgroundColor(Color.RED);
            }
            else {
                but[i].setBackgroundColor(Color.DKGRAY);
            }
        }
    }

    public void clear(){
        for(int i = 0; i <but.length; i++){
            but[i].setBackgroundColor(Color.DKGRAY);
        }
        presetIdState = -1;
        btnState = false;
    }

    public int changeDown(){
        if (page>1){
            page--;
            applyPage();

        }
        return page;
    }

    public int changeUp(){
        if(page<10){
            page++;
            applyPage();
        }
        return page;
    }

    public int pressetClick(int id){
        if(presetIdState != id){
            presetIdState = id;
            btnState = false;
            for(int i = 0; i <but.length; i++){
                if(i+1 + (page-1)*16 != id){
                    but[i].setBackgroundColor(Color.DKGRAY);
                }else{
                    but[i].setBackgroundColor(Color.GREEN);
                }
            }
            return 1;
        }
        if(!btnState) {
            btnState = true;
            //presetIdState = -1;
            for (int i = 0; i < but.length; i++) {
                if (i + 1 + (page - 1) * 16 != id) {
                    but[i].setBackgroundColor(Color.DKGRAY);
                } else {
                    but[i].setBackgroundColor(Color.RED);
                }
            }
            return 2;
        }else{
            presetIdState = id;
            btnState = false;
            for(int i = 0; i <but.length; i++){
                if(i+1 + (page-1)*16 != id){
                    but[i].setBackgroundColor(Color.DKGRAY);
                }else{
                    but[i].setBackgroundColor(Color.GREEN);
                }
            }
            return 1;
        }
    }
}