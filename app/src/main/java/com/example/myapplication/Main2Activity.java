package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main2Activity extends AppCompatActivity {

    private final int Pick_image = 1;
    Bitmap bmp;
    String history;
    String FILENAME;
    String name;
    int NN;
    final int N = 10; // max количество людей, которое может распознать робот
    String[] paramatrs = new String[N];  // строки, где будут храниться параметры

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button button = (Button)findViewById(R.id.button2_add_db);
        button.setOnClickListener(ButtonClickListener1);

        Button PickImage = (Button) findViewById(R.id.button_add_people);
        PickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, Pick_image);
            }
        });
    }

    View.OnClickListener ButtonClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_add_people:
                {}
                break;
                case R.id.button2_add_db: {
                    File file = new File( "count_people.txt");
                    FILENAME = file.getPath().toString();
                    readFile();
                    int count_peop = Integer.parseInt(paramatrs[0]);
                    Log.d("read","count_do = " + paramatrs[0]);
                    count_peop = count_peop + 1;
                    paramatrs[0] = String.valueOf(count_peop);
                    writeFile_count();
                    readFile();
                    Log.d("read","count_after = " + paramatrs[0]);
                    EditText tv = (EditText)findViewById(R.id.editText);
                    name = tv.getText().toString();
                    if (name.length()!=0) {
                        File file1 = new File( "list_people.txt");
                        FILENAME = file1.getPath().toString();
                        writeFile();
                    } else
                        {
                            Log.d("read","ERROR_Of_NAME_LENGTH");
                        }
                    readFile();
                    for (int i=0; i <count_peop; i++)
                    {
                        Log.d("read","[" + i + "] = " + paramatrs[i]);
                    }
                    try {
                        File count_people = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        File dest = new File(count_people, String.valueOf(count_peop) + ".jpg");
                        FileOutputStream out = new FileOutputStream(dest);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        Toast.makeText(getApplicationContext(), "Сохранил "+dest.getPath().toString(),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка при сохранении. Повторите попытку.",
                                Toast.LENGTH_SHORT).show();
                        Log.d("read", e.toString());
                    }
                    Toast.makeText(getApplicationContext(), "User: " + name + " successful added in DB",
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    Main2Activity.this.finish();
                }
                break;
                default:
                    break;
            }
        }
    };

    //Обрабатываем результат выбора в галерее:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Button Pick_image1 = (Button)findViewById(R.id.button_add_people);
        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ImageView imageView = (ImageView)findViewById(R.id.imageView);
                        imageView.setImageBitmap(selectedImage);
                        bmp = selectedImage;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_APPEND)));
                bw.append(String.valueOf(name) + '\n');
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFile_count() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
                bw.write(String.valueOf(Integer.parseInt(paramatrs[0])));
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
