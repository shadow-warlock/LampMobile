package com.lampmobile.app;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;


public class MainActivity extends Activity {


    private EditText editText;
    private TextView numView,valView,masView;
    private SeekBar seekBar;
    private ProcessThread thread;
    private Button butPresset,butSave;

    private static final int SAVE_CODE_RESULT = 0;
    private static final int OPEN_CODE_RESULT = 1;

    private long lastTime;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 2){
                masView.setText(fillStr((byte[]) msg.obj));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        numView = (TextView)findViewById(R.id.numView);
        valView = (TextView)findViewById(R.id.valView);
        masView = (TextView)findViewById(R.id.masView);
        editText = (EditText)findViewById(R.id.editText);
        butPresset = (Button)findViewById(R.id.butPresset);
        butSave = (Button)findViewById(R.id.butSave);
        final ColorPicker picker = (ColorPicker) findViewById(R.id.picker);
        SVBar svBar = (SVBar) findViewById(R.id.svbar);
        picker.addSVBar(svBar);
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener()
        {
            @Override
            public void onColorChanged(int color) {
                float hsv[] = new float[3];
                Color.colorToHSV(color, hsv);
                int[] rgb = ColorMatrix.hsvToRgb(hsv[0]/360, hsv[1], hsv[2]);
                System.out.println(rgb[0] + " " + rgb[1] + " " + rgb[2]);
                if(System.currentTimeMillis() - lastTime > 15){
                    lastTime = System.currentTimeMillis();
                    thread.setData(0, (byte)rgb[0]);
                    thread.setData(1, (byte)rgb[1]);
                    thread.setData(2, (byte)rgb[2]);
                    thread.send();
                }
            }
        });

        thread = new ProcessThread(handler);
        thread.start();
        thread.startSend();

        seekBar.setOnSeekBarChangeListener(seekBarListener);
        editText.setOnEditorActionListener(eTListener);
        butPresset.setOnClickListener(presetActivityListener);
        butSave.setOnClickListener(savePressetListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.stopSend();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread = null;
    }

    public String fillStr(byte mas[]){
        String str = " ";
        int c;
        for (int i=0;i<11;i++){
            c=(int)mas[i];
            if(c==0){
                str+=" 000";
            }
            else{
                if(c<0) {
                    c += 256;
                }
                if(c<100){
                    if(c<10){
                        str+=" 00"+c;
                    }
                    else{
                        str+=" 0"+c;
                    }
                }
                else{
                    str+=" "+c;
                }
            }
        }
        return str;
    }

    public SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(System.currentTimeMillis() - lastTime > 8){
                lastTime = System.currentTimeMillis();
                valView.setText("val = "+i);
                thread.setData(getChannel(), (byte)i);
                thread.send();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            valView.setText("val = " + seekBar.getProgress());
            thread.setData(getChannel(), (byte)seekBar.getProgress());
            thread.send();
        }
    };

    View.OnClickListener presetActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //переходим с первой на вторую активность
            Intent intent = new Intent(MainActivity.this, PressetActivity.class);
            startActivityForResult(intent, OPEN_CODE_RESULT);
        }
    };

    View.OnClickListener savePressetListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, SaveActivity.class);
            startActivityForResult(intent, SAVE_CODE_RESULT);
        }
    };

    TextView.OnEditorActionListener eTListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            int chanel = getChannel();
            numView.setText("channel №"+chanel);
            editText.setText(String.valueOf(chanel));
            return false;
        }
    };

    private int getChannel(){
        int ch = editText.getText().toString().equals("") ? 0 : Integer.parseInt(editText.getText().toString());
        if(ch < 0){
            ch = 0;
        }
        if(ch >= 512)
            ch = 511;
        editText.setText(String.valueOf(ch));
        return ch;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if(requestCode == SAVE_CODE_RESULT && resultCode == RESULT_OK){
            int presId = data.getIntExtra("preset", -1);

            if(presId != -1){
                thread.savePresset(presId);
                Toast.makeText(MainActivity.this, "Пресет сохранился", Toast.LENGTH_SHORT).show();
            }
        }
    }

}