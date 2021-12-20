package com.lampmobile.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaveActivity extends Activity {
    private EditText editSave;
    private Button butConfirm,butCancel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        editSave = (EditText)findViewById(R.id.editSave);
        butConfirm = (Button)findViewById(R.id.butConfirm);
        butConfirm.setOnClickListener(confirmListener);
        butCancel = (Button)findViewById(R.id.butCancel);
        butCancel.setOnClickListener(cancelListener);

    }
    private int getNumSave(){
        return editSave.getText().toString().equals("") ? 0 : Integer.parseInt(editSave.getText().toString());
    }

    View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int numSave = getNumSave();
            if(numSave<1){
                Toast.makeText(getApplicationContext(),"incorrect value (1-160)\n please, try again",Toast.LENGTH_SHORT).show();
            }
            else{
                if(numSave>160){
                    Toast.makeText(getApplicationContext(),"incorrect value (1-160)\n please, try again",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("preset", numSave);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }
    };

    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };
}
