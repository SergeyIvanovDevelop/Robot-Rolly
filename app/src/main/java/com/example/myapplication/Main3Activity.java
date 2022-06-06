package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothDevice;
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
import java.util.Arrays;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    String[] cities = {"Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};
    TextView selection;
    File f = new File( "my_trains.txt");
    final int N = 8; 
    String[] paramatrs = new String[N]; 
    String FILENAME = f.getPath().toString();
    String[][] exercise = new String[N-1][1000];
    String[][] number_of_povtorov = new String[N-1][1000];
    String[][] number_of_podhodov = new String[N-1][1000];
    int [] number_of_exercise = new int[N-1];
    String names = "";
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        readFile();
        for (int i=0;i<N-1;i++)
            Log.d("read", String.valueOf(i) + ": " + paramatrs[i]);
            Log.d("read", "hi");

                for (int j=0; j< N-1; j++) { //пн, вт и т.п.
                    int m = 0;
                    int n =0; //количество упражнений
                    while (m < paramatrs[j].length() - 1)
                    {
                        String s = "";
                        while (paramatrs[j].charAt(m) != ';')
                        {
                            s = s + paramatrs[j].charAt(m);
                            m = m+1;
                        }
                        exercise[j][n]= s;
                        s= "";
                        m=m+1;
                        while (paramatrs[j].charAt(m) != '/')
                        {
                            s = s + paramatrs[j].charAt(m);
                            m=m+1;
                        }
                        number_of_povtorov[j][n] = s;
                        s="";
                        m=m+1;
                        while(paramatrs[j].charAt(m) != '&')
                        {
                            s = s + paramatrs[j].charAt(m);
                            m=m+1;
                        }
                        number_of_podhodov[j][n] = s;
                        s="";
                        m=m+1;

                        n=n+1;
                    }
                    number_of_exercise[j] = n;
                }

                for (int i =0; i < N-1; i ++)
                {
                    for (int j=0; j <= number_of_exercise[j]; j++)
                    {
                        Log.d("read", "exercise = " + exercise[i][j]);
                        Log.d("read", "number_of_povtorov = " + number_of_povtorov[i][j]);
                        Log.d("read", "number_of_podhodov = " + number_of_podhodov[i][j]);
                    }
                }

        selection = (TextView) findViewById(R.id.selection);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);
                selection.setText(item);
                int number = -1;
                if (item.equals("Пн"))
                {
                    number =0;
                }
                if (item.equals("Вт"))
                {
                    number =1;
                }
                if (item.equals("Ср"))
                {
                    number =2;
                }
                if (item.equals("Чт"))
                {
                    number =3;
                }
                if (item.equals("Пт"))
                {
                    number =4;
                }
                if (item.equals("Сб"))
                {
                    number =5;
                }
                if (item.equals("Вс"))
                {
                    number =6;
                }
                ListView listView = (ListView) findViewById(R.id.listView);
                list = new ArrayList<>(Arrays.asList(names));
                String[] String_for_line_view = new String[number_of_exercise[number]-1 ];
                for (int i=0; i < number_of_exercise[number]-1; i++)
                {
                    String_for_line_view[i] = number_of_podhodov[number][i+1] + "  " + number_of_povtorov[number][i+1] + "  " + exercise[number][i+1];
                    list.add(String_for_line_view[i]);
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, String_for_line_view);
                listView.setAdapter(adapter1);
                Button b = (Button) findViewById(R.id.button10);
                b.setOnClickListener(ButtonClickListener);
                Button b1 = (Button) findViewById(R.id.button4);
                b1.setOnClickListener(ButtonClickListener);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

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
            for (int i =0; i < N-1; i++)
                bw.write((paramatrs[i]+"\n"));
            bw.close();
            Log.d("read", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFile_empty_trening() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
            for (int i =0; i< 8; i++)
                bw.write(String.valueOf("---;---/---&\n"));
            bw.close();
            Log.d("read", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener ButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.button10:
                { 
                    //добавить упражнение в выбранный день
                    String string = "";
                    EditText ed1 = (EditText) findViewById(R.id.editText2);
                    EditText ed2 = (EditText) findViewById(R.id.editText3);
                    EditText ed3 = (EditText) findViewById(R.id.editText4);
                    string = ed1.getText().toString() + ";" + ed2.getText().toString() + "/" + ed3.getText().toString() + "&";
                    String item = selection.getText().toString();
                    int number = -1;

                    if (item.equals("Пн"))
                    {
                        number =0;
                    }
                    if (item.equals("Вт"))
                    {
                        number =1;
                    }
                    if (item.equals("Ср"))
                    {
                        number =2;
                    }
                    if (item.equals("Чт"))
                    {
                        number =3;
                    }
                    if (item.equals("Пт"))
                    {
                        number =4;
                    }
                    if (item.equals("Сб"))
                    {
                        number =5;
                    }
                    if (item.equals("Вс"))
                    {
                        number =6;
                    }
                    
                    paramatrs[number] = paramatrs[number] + string;
                    writeFile();
                    readFile();
                    if (paramatrs[0].equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), "У вас пока нет тренировок: ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for (int j=0; j< N-1; j++) { //пн, вт и т.п.
                            int m = 0;
                            int n =0; //количество упражнений
                            while (m < paramatrs[j].length() - 1)
                            {
                                String s = "";
                                while (paramatrs[j].charAt(m) != ';')
                                {
                                    s = s + paramatrs[j].charAt(m);
                                    m = m+1;
                                }
                                exercise[j][n]= s;
                                s= "";
                                m=m+1;
                                while (paramatrs[j].charAt(m) != '/')
                                {
                                    s = s + paramatrs[j].charAt(m);
                                    m=m+1;
                                }
                                number_of_povtorov[j][n] = s;
                                s="";
                                m=m+1;
                                while(paramatrs[j].charAt(m) != '&')
                                {
                                    s = s + paramatrs[j].charAt(m);
                                    m=m+1;
                                }
                                number_of_podhodov[j][n] = s;
                                s="";
                                m=m+1;

                                n=n+1;
                            }
                            number_of_exercise[j] = n;
                        }
                        ListView listView = (ListView) findViewById(R.id.listView);
                        String[] String_for_line_view = new String[number_of_exercise[number]-1 ];
                        for (int i=0; i < number_of_exercise[number]-1; i++)
                        {
                            String_for_line_view[i] = number_of_podhodov[number][i+1] + "  " + number_of_povtorov[number][i+1] + "  " + exercise[number][i+1];
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, String_for_line_view);
                        listView.setAdapter(adapter);
                        for (int i =0; i < N-1; i ++)
                        {
                            for (int j=0; j <= number_of_exercise[j]; j++)
                            {
                                Log.d("read", "exercise = " + exercise[i][j]);
                                Log.d("read", "number_of_povtorov = " + number_of_povtorov[i][j]);
                                Log.d("read", "number_of_podhodov = " + number_of_podhodov[i][j]);
                            }
                        }
                    }
                }
                break;
                case R.id.button4:
                {
                    String string = "";
                    EditText ed1 = (EditText) findViewById(R.id.editText2);
                    EditText ed2 = (EditText) findViewById(R.id.editText3);
                    EditText ed3 = (EditText) findViewById(R.id.editText4);
                    String item = selection.getText().toString();
                    int number = -1;
                    if (item.equals("Пн"))
                    {
                        number =0;
                    }
                    if (item.equals("Вт"))
                    {
                        number =1;
                    }
                    if (item.equals("Ср"))
                    {
                        number =2;
                    }
                    if (item.equals("Чт"))
                    {
                        number =3;
                    }
                    if (item.equals("Пт"))
                    {
                        number =4;
                    }
                    if (item.equals("Сб"))
                    {
                        number =5;
                    }
                    if (item.equals("Вс"))
                    {
                        number =6;
                    }
                    paramatrs[number] = "---;---/---&";
                    writeFile();
                    readFile();
                    if (paramatrs[0].equals("0"))
                    {
                        Toast.makeText(getApplicationContext(), "У вас пока нет тренировок: ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for (int j=0; j< N-1; j++) { //пн, вт и т.п.
                            int m = 0;
                            int n =0; //количество упражнений
                            while (m < paramatrs[j].length() - 1)
                            {
                                String s = "";
                                while (paramatrs[j].charAt(m) != ';')
                                {
                                    s = s + paramatrs[j].charAt(m);
                                    m = m+1;
                                }
                                exercise[j][n]= s;
                                s= "";
                                m=m+1;
                                while (paramatrs[j].charAt(m) != '/')
                                {
                                    s = s + paramatrs[j].charAt(m);
                                    m=m+1;
                                }
                                number_of_povtorov[j][n] = s;
                                s="";
                                m=m+1;
                                while(paramatrs[j].charAt(m) != '&')
                                {
                                    s = s + paramatrs[j].charAt(m);
                                    m=m+1;
                                }
                                number_of_podhodov[j][n] = s;
                                s="";
                                m=m+1;

                                n=n+1;
                            }
                            number_of_exercise[j] = n;
                        }
                        ListView listView = (ListView) findViewById(R.id.listView);
                        String[] String_for_line_view = new String[number_of_exercise[number]-1 ];
                        for (int i=0; i < number_of_exercise[number]-1; i++)
                        {
                            String_for_line_view[i] = number_of_podhodov[number][i+1] + "  " + number_of_povtorov[number][i+1] + "  " + exercise[number][i+1];
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, String_for_line_view);
                        listView.setAdapter(adapter);
                        for (int i =0; i < N-1; i ++)
                        {
                            for (int j=0; j <= number_of_exercise[j]; j++)
                            {
                                Log.d("read", "exercise = " + exercise[i][j]);
                                Log.d("read", "number_of_povtorov = " + number_of_povtorov[i][j]);
                                Log.d("read", "number_of_podhodov = " + number_of_podhodov[i][j]);
                            }
                        }
                    }
                }
                    break;
                default:
                    break;
            }
        }
    };
    
}
