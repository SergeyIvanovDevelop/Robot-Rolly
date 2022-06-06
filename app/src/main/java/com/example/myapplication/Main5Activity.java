package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Main5Activity extends AppCompatActivity {

    File f = new File( "my_configures.txt");
    final int N = 7; 
    String[] paramatrs = new String[N];
    String FILENAME = f.getPath().toString();
    String[] voice = {"мужской", "женский"};
    String[] autopilot = { "выключен", "включен"};
    String[] auto_inet = {"выключен", "включен"};
    String voice_s;
    String autopilot_s;
    String auto_inet_s;
    EditText ed1;
    EditText ed2;
    EditText ed3;
    EditText ed4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        ed1 = (EditText) findViewById(R.id.editText5);
        ed2 = (EditText) findViewById(R.id.editText7);
        ed3 = (EditText) findViewById(R.id.editText8);
        ed4 = (EditText) findViewById(R.id.editText9);
        readFile();
        for (int i=0; i < N; i++)
            Log.d("read",String.valueOf(i) + " = " + paramatrs[i]);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner5);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner6);
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner7);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, voice);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner1.setAdapter(adapter);
        ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, autopilot);
        adapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter11);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, auto_inet);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter2);
        AdapterView.OnItemSelectedListener itemSelectedListener1 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                voice_s = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        AdapterView.OnItemSelectedListener itemSelectedListener2 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                autopilot_s = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        AdapterView.OnItemSelectedListener itemSelectedListener3 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                auto_inet_s = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner1.setOnItemSelectedListener(itemSelectedListener1);
        spinner2.setOnItemSelectedListener(itemSelectedListener2);
        spinner3.setOnItemSelectedListener(itemSelectedListener3);

        if (paramatrs[0].equals("0"))
        {
            spinner1.setSelection(0);
        }
        if (paramatrs[0].equals("1"))
        {
            spinner1.setSelection(1);
        }

        if (paramatrs[3].equals("0"))
        {
            spinner2.setSelection(0);
        }
        if (paramatrs[3].equals("1"))
        {
            spinner2.setSelection(1);
        }

        if (paramatrs[4].equals("0"))
        {
            spinner3.setSelection(0);
        }
        if (paramatrs[4].equals("1"))
        {
            spinner3.setSelection(1);
        }

        ed1.setText(paramatrs[1]);
        ed2.setText(paramatrs[2]);
        ed3.setText(paramatrs[5]);
        ed4.setText(paramatrs[6]);
        
        Button ow = (Button) findViewById(R.id.Owerwrite);
        ow.setOnClickListener(ButtonClickListener);
    }

    View.OnClickListener ButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.Owerwrite:
                {
                    if (voice_s.equals("мужской"))
                    {
                        paramatrs[0] = "0";
                    }
                    if (voice_s.equals("женский"))
                    {
                        paramatrs[0] = "1";
                    }

                    if (autopilot_s.equals("выключен"))
                    {
                        paramatrs[3] = "0";
                    }
                    if (autopilot_s.equals("включен"))
                    {
                        paramatrs[3] = "1";
                    }

                    if (auto_inet_s.equals("выключен"))
                    {
                        paramatrs[4] = "0";
                    }
                    if (auto_inet_s.equals("включен"))
                    {
                        paramatrs[4] = "1";
                    }

                    paramatrs[1] = ed1.getText().toString();
                    paramatrs[2] = ed2.getText().toString();
                    paramatrs[5] = ed3.getText().toString();
                    paramatrs[6] = ed4.getText().toString();

                    writeFile();
                    Toast.makeText(getApplicationContext(), "Конфигурационный файл успешно перезаписан",
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    Main5Activity.this.finish();
                }
                break;
                default:
                    break;
            }
        }
    };

    void readFile() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str = "";
            int i = 0;
            while ((str = br.readLine()) != null) {
                Log.d("1", str);
                paramatrs[i] = str;
                i++;
            }
            Log.d("read", "Файл прочитан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
            for (int i =0; i < N; i++)
                bw.write((paramatrs[i]+"\n"));
            bw.close();
            Log.d("read", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
