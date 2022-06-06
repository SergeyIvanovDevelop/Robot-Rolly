package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Arrays;
import java.util.List;

public class Main4Activity extends AppCompatActivity {

    String[] days = {"01", "02", "03", "04", "05", "06", "07","08", "09", "10", "11", "12", "13","14", "15", "16", "17", "18", "19", "20", "21", "22", "23","24","25","26","27","28","29","30","31"};
    String[] monthes = {"01", "02", "03", "04", "05", "06", "07","08", "09","10", "11", "12"};
    String[] years = {"2020", "2021", "2022", "2023", "2025", "2026", "2027"};
    File f = new File( "my_deals.txt");
    String FILENAME = f.getPath().toString();
    List<String> list;
    TextView selection;
    EditText your_notice;
    String day = "01";
    String month = "01";
    String year = "2020";
    final int N = 1000; // max число заметок;
    String[] paramatrs = new String[N];  // строки, где будут храниться заметки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        selection = (TextView) findViewById(R.id.textView7);
        your_notice = (EditText) findViewById(R.id.editText6);
        readFile();
        for (int i=0; i<N; i++)
        {
            Log.d("read",String.valueOf(i) + " = " + paramatrs[i]);
        }
	String[] example = {"01.01.2020 kfdkf", "02.02.2020 lsdldl"};
        list = new ArrayList<>();
        for (int i=0; i < N; i++)
        {
            if (!paramatrs[i].equals("0"))
            {
                list.add(paramatrs[i]);
            }
        }
        ListView listView = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                selection.setText(((TextView) itemClicked).getText());
                Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button b1 = (Button) findViewById(R.id.button7);
        b1.setOnClickListener(ButtonClickListener);
        Button b2 = (Button) findViewById(R.id.button6);
        b2.setOnClickListener(ButtonClickListener);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner2);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner3);
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner1.setAdapter(adapter);
        ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthes);
        adapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter11);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter2);
        AdapterView.OnItemSelectedListener itemSelectedListener1 = new AdapterView.OnItemSelectedListener() {
        
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                day = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        AdapterView.OnItemSelectedListener itemSelectedListener2 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                month = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };


        AdapterView.OnItemSelectedListener itemSelectedListener3 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                year = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinner1.setOnItemSelectedListener(itemSelectedListener1);
        spinner2.setOnItemSelectedListener(itemSelectedListener2);
        spinner3.setOnItemSelectedListener(itemSelectedListener3);
    }

    View.OnClickListener ButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button7:
                    { 
			    //удалить заметку
			    for (int i = 0; i < N; i++) {
			        if (paramatrs[i].equals(selection.getText().toString())) {
			            paramatrs[i] = "z";
			        }
			    }
			    writeFile();
			    readFile();
			    ListView listView = (ListView) findViewById(R.id.listView1);
		            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		            @Override
		            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
		                                    long id) {
		                selection.setText(((TextView) itemClicked).getText());
		                Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
		                        Toast.LENGTH_SHORT).show();
		            }
		            });
		            list = new ArrayList<>();
		            for (int i=0; i < N; i++)
		            {
		                if (!paramatrs[i].equals("z"))
		                {
		                    list.add(paramatrs[i]);
		                }
		            }
		            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
		            listView.setAdapter(adapter1);
                }
                break;
                    case R.id.button6:
                    {
                        String string = day + "." + month + "." + year + " " + your_notice.getText().toString();
                        for (int i=0; i < N; i++)
                        {
                            if (paramatrs[i].equals("z"))
                            {
                                paramatrs[i] = string;
                                writeFile();
                                readFile();
                                ListView listView = (ListView) findViewById(R.id.listView1);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                                            long id) {
                                        selection.setText(((TextView) itemClicked).getText());
                                        Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                                list = new ArrayList<>();
                                for (int ii=0; ii < N; ii++)
                                {
                                    if (!paramatrs[ii].equals("z"))
                                    {
                                        list.add(paramatrs[ii]);
                                    }
                                }
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                                listView.setAdapter(adapter1);
                                break;
                            }
                        }
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
