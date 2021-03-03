package com.example.kettu.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends Activity {
    TextView text;
    EditText editText;
    Context context = null;
    TextView text2;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String value = s.toString();
            text.setText(value);
        }
        @Override
        public void afterTextChanged(Editable s) {
            String value = s.toString();
            text.setText(value);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.textView);
        text2 = findViewById(R.id.textView2);
        editText = findViewById(R.id.editText);
        editText.addTextChangedListener(textWatcher);
        context = MainActivity.this;

        System.out.println(context.getFilesDir());
    }

    public void printHello(View v) {
        System.out.println("Hello World!\n");
        text.setText("Hello World!");
    }

    public void copyText(View v) {
        text.setText(editText.getText());
        //text.setText("Hello World!");
    }

    public void loadFile(View v){
        try{
            InputStream ins = context.openFileInput("testi.txt");

            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String s = "";
            StringBuilder sbuild = new StringBuilder(100);

            while ((s = br.readLine()) != null){
                sbuild.append(s);
                System.out.println(s);
                sbuild.append("\n");
            }
            editText.setText(sbuild);
            ins.close();
            text2.setText("File loaded.");
        } catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public void saveFile(View v){

        try{
            String s;
            OutputStreamWriter out = new OutputStreamWriter(context.openFileOutput("testi.txt",
                                                            Context.MODE_PRIVATE));

            s = editText.getText().toString();

            out.write(s);
            out.flush();
            out.close();
            text2.setText("File saved.");
        } catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public void clearText(View v){
        text2.setText("");
        editText.getText().clear();
    }
}
