package com.lampmobile.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class PressetActivity extends Activity{



    private TextView txtPage;
    private ImageButton butPageUp,butPageDown;
    private PressetManipulator butPr160;
    private EditText etIn, etOut;
    private ProcessThread thread;
    private Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presset);

        txtPage = (TextView)findViewById(R.id.txtPage);
        etIn = (EditText)findViewById(R.id.etTimeIn);
        etOut = (EditText)findViewById(R.id.etTimeOut);

        butPageUp =(ImageButton)findViewById(R.id.butPageUp);
        butPageDown =(ImageButton)findViewById(R.id.butPageDown);
        Button but[] = new Button[16];
        but[0] = (Button)findViewById(R.id.butPr1);
        but[1] = (Button)findViewById(R.id.butPr2);
        but[2] = (Button)findViewById(R.id.butPr3);
        but[3] = (Button)findViewById(R.id.butPr4);
        but[4] = (Button)findViewById(R.id.butPr5);
        but[5] = (Button)findViewById(R.id.butPr6);
        but[6] = (Button)findViewById(R.id.butPr7);
        but[7] = (Button)findViewById(R.id.butPr8);
        but[8] = (Button)findViewById(R.id.butPr9);
        but[9] = (Button)findViewById(R.id.butPr10);
        but[10] = (Button)findViewById(R.id.butPr11);
        but[11] = (Button)findViewById(R.id.butPr12);
        but[12] = (Button)findViewById(R.id.butPr13);
        but[13] = (Button)findViewById(R.id.butPr14);
        but[14] = (Button)findViewById(R.id.butPr15);
        but[15] = (Button)findViewById(R.id.butPr16);
        for(int i=0;i<16;i++){
            but[i].setOnClickListener(prListener);
        }
        butPr160 = new PressetManipulator(but);
        butPageUp.setOnClickListener(pageListenerUp);
        butPageDown.setOnClickListener(pageListenerDown);
        butPr160.applyPage();

        thread = new ProcessThread(null);
        thread.start();
        thread.startSend();

        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textOut = etOut.getText().toString();
                int outTime = textOut.equals("")?0:Integer.parseInt(textOut);
                thread.clear(outTime);
                butPr160.clear();
            }
        });

    }

    View.OnClickListener prListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int num = Integer.parseInt(((Button)view).getText().toString());
            String textIn = etIn.getText().toString();
            String textOut = etOut.getText().toString();
            int inTime = textIn.equals("")?0:Integer.parseInt(textIn);
            int outTime = textOut.equals("")?0:Integer.parseInt(textOut);
            int state = butPr160.pressetClick(num);
            if(state == 1)
                thread.loadPresset(num, inTime);
            if(state == 2)
                thread.backPresset(outTime);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        thread.stopSend();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener pageListenerDown = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            txtPage.setText(""+butPr160.changeDown());
        }
    };

    View.OnClickListener pageListenerUp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            txtPage.setText(""+butPr160.changeUp());
        }
    };

}
