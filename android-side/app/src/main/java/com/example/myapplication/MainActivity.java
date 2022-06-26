package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import android.os.ParcelUuid;

//---------- for Bluetooth--------------------
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;
import java.util.Set;
//--------------------------------------------

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainActivity<serverDevice> extends AppCompatActivity  {
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    ConnectedThread mConnectedThread;
    String TAG = "read";
    private UUID deviceUUID;
    String FILENAME;
    private BluetoothDevice mmDevice;

    File picture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File f = new File( "count_people.txt");
    File f1 = new File( "list_people.txt");

    File file_m = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File f_m = new File( "map_of_area.jpg");
    File count_vi = new File("video_count.txt");
    File count_ph = new File( "photo_count.txt");
    File count_a = new File( "audio_count.txt");

    Switch sw;
    private BluetoothSocket bs;
    private DataInputStream dis;
    private DataOutputStream dos;
    String  serverName = "raspberrypi";
    private static final int REQUEST_ENABLE_BT = 1;
    private Handler handler;

    EditText send_data;
    TextView view_data;
    StringBuilder messages;

    final int N = 10; // max количество людей, которое может распознать робот
    String[] paramatrs = new String[N];  // строки, где будут храниться параметры

    ConnectThread connect = null;
    int wasPress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BluetoothDevice serverDevice = searchDevice(serverName);
                if(sw.isChecked())
                {
                    Button b = (Button) findViewById(R.id.Bluetooth);
                    b.setEnabled(false);
                    if (serverDevice !=  null) {
                        connect = new ConnectThread(serverDevice, MY_UUID_INSECURE);
                        connect.start();
                    }
                    if (serverDevice !=  null) {
                        Button b1 = (Button) findViewById(R.id.forward);
                        b1.setVisibility(View.VISIBLE);
                        Button b2 = (Button) findViewById(R.id.back);
                        b2.setVisibility(View.VISIBLE);
                        Button b3 = (Button) findViewById(R.id.left);
                        b3.setVisibility(View.VISIBLE);
                        Button b4 = (Button) findViewById(R.id.rigth);
                        b4.setVisibility(View.VISIBLE);
                        Button b5 = (Button) findViewById(R.id.reset);
                        b5.setVisibility(View.VISIBLE);
                        TextView tv = (TextView) findViewById(R.id.manipulation);
                        tv.setText("maniputation ON");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Робот не подключен по Bluetooth каналу",
                                Toast.LENGTH_SHORT).show();
                        sw.setChecked(false);
                    }
                }
                else
                {
                    SendCommand("quit");
                    connect.cancel();
                    Button b1 = (Button) findViewById(R.id.forward);
                    b1.setVisibility(View.INVISIBLE);
                    Button b2 = (Button) findViewById(R.id.back);
                    b2.setVisibility(View.INVISIBLE);
                    Button b3 = (Button) findViewById(R.id.left);
                    b3.setVisibility(View.INVISIBLE);
                    Button b4 = (Button) findViewById(R.id.rigth);
                    b4.setVisibility(View.INVISIBLE);
                    TextView tv = (TextView) findViewById(R.id.manipulation);
                    tv.setText("maniputation OFF");
                    Button b = (Button) findViewById(R.id.Bluetooth);
                    b.setEnabled(true);
                }
            }
        });
        
        send_data =(EditText) findViewById(R.id.editText);
        view_data = (TextView) findViewById(R.id.textView);
        Button sd = (Button) findViewById(R.id.send_data);
        sd.setOnClickListener(ButtonClickListener);
        Button ss = (Button) findViewById(R.id.start);
        ss.setOnClickListener(ButtonClickListener);
        Button my_trains = (Button) findViewById(R.id.my_trains);
        my_trains.setOnClickListener(ButtonClickListener);
        Button b1 = (Button) findViewById(R.id.forward);
        b1.setOnClickListener(ButtonClickListener);
        Button b2 = (Button) findViewById(R.id.back);
        b2.setOnClickListener(ButtonClickListener);
        Button b3 = (Button) findViewById(R.id.left);
        b3.setOnClickListener(ButtonClickListener);
        Button b4 = (Button) findViewById(R.id.rigth);
        b4.setOnClickListener(ButtonClickListener);
        Button b5 = (Button) findViewById(R.id.reset);
        b5.setOnClickListener(ButtonClickListener);
        Button send_command = (Button) findViewById(R.id.button);
        send_command.setOnClickListener(ButtonClickListener);
        Button send_file = (Button) findViewById(R.id.button2);
        send_file.setOnClickListener(ButtonClickListener);
        Button my_deals = (Button) findViewById(R.id.button5);
        my_deals.setOnClickListener(ButtonClickListener);
        Button config = (Button) findViewById(R.id.config);
        config.setOnClickListener(ButtonClickListener);

	FILENAME = f.getPath().toString();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            writeFile_empty();
            Log.d("read","count_people not exist");
        } catch (IOException e) {
            writeFile_empty();
            Log.d("read","count_people not exist");
            e.printStackTrace();
        }
        FILENAME = "my_trains.txt";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            writeFile_empty_trening();
            Log.d("read","my_trains not exist");
        } catch (IOException e) {
            writeFile_empty();
            Log.d("read","my_trains not exist");
            e.printStackTrace();
        }

        FILENAME = "my_deals.txt";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            writeFile_empty_deals();
            Log.d("read","my_deals not exist");
        } catch (IOException e) {
            writeFile_empty_deals();
            Log.d("read","my_deals not exist");
            e.printStackTrace();
        }

        FILENAME = "my_configures.txt";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            writeFile_empty_configure();
            Log.d("read","my_configures not exist");
        } catch (IOException e) {
            writeFile_empty_configure();
            Log.d("read","my_configures not exist");
            e.printStackTrace();
        }

        FILENAME = f1.getPath().toString();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            writeFile_empty_list();
            Log.d("read","list_people not exist");
        } catch (IOException e) {
            writeFile_empty_list();
            Log.d("read","list_people not exist");
            e.printStackTrace();
        }

        Button button1 = (Button)findViewById(R.id.button_add);
        button1.setOnClickListener(ButtonClickListener);
        Button bluetooth = (Button)findViewById(R.id.Bluetooth);
        bluetooth.setOnClickListener(ButtonClickListener);
        Button forward_wifi = (Button)findViewById(R.id.forward_wifi);
        forward_wifi.setOnClickListener(ButtonClickListener);
        Button left_wifi = (Button)findViewById(R.id.left_wifi);
        left_wifi.setOnClickListener(ButtonClickListener);
        Button rigth_wifi = (Button)findViewById(R.id.rigth_wifi);
        rigth_wifi.setOnClickListener(ButtonClickListener);
        Button back_wifi = (Button)findViewById(R.id.back_wifi);
        back_wifi.setOnClickListener(ButtonClickListener);
        Button config_wifi = (Button)findViewById(R.id.config_wifi);
        config_wifi.setOnClickListener(ButtonClickListener);
        Button reset_wifi = (Button)findViewById(R.id.reset_wifi);
        reset_wifi.setOnClickListener(ButtonClickListener);

        File dir = new File (file_m, "/media_robot");
        if (!dir.exists())
        {
            dir.mkdir();
        }

        // Включить server
        TT TT1 = new TT();
        TT1.start();

    }

    // Передача файла при подключении
    class TT extends Thread {
        public void run() {
            super.run();
            try {
                Log.d("read","Server start !");
                ServerSocket ss = new ServerSocket(6666);
                Socket socket = ss.accept();
                //Считать количество фотографий в папке
                Log.d("read","Client connected !");
                int count_images = 0;
                for (int i =0; i <count_images; i++)
                {
                    File picture = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File file = new File(picture, "2.jpg");
                    FileOutputStream fos = new FileOutputStream(file);
                    @SuppressWarnings("resource")
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    InputStream is = socket.getInputStream();
                    byte[] aByte = new byte[1024 * 1024];
                    int bytesRead;
                    while ((bytesRead = is.read(aByte)) != -1) {
                        	bos.write(aByte, 0, bytesRead);
                        }
                }
                ss.close();
            } catch (IOException e) {
            }
        }
    }

    public void Connect(String Message){
        try {
            Socket SOCK = new Socket("192.168.0.15", 8083);
            OutputStream OS = SOCK.getOutputStream();
            byte[] bytes = Message.getBytes(Charset.defaultCharset());
            OS.write(bytes);
            OS.close();
            SOCK.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Message have sent",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Connect_file(String filename, int i){
        try {
            Socket SOCK = new Socket("192.168.0.15", 8086);
            byte[] bytes = new byte[1024];
            FileInputStream in = null;
            if (i == 1) {
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File fin_0 = new File(file, filename);
                in = new FileInputStream(fin_0);
            }
            if (i == 0) {
                in = openFileInput(filename);
            }
            OutputStream out = SOCK.getOutputStream();
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }

            out.close();
            in.close();
            SOCK.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener ButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_add:
                    { 
                        Intent i = new Intent(MainActivity.this, Main2Activity.class);
                        startActivity(i);
                    }
                break;
                case R.id.forward_wifi:
                {
                    Thread tthread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Connect("forward");
                        }
                    });
                    tthread.start();
                    Toast.makeText(getApplicationContext(), "forward_wifi",
                            Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.config_wifi:
                {
                    Thread tthread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String string_for_send = "";
                            FILENAME = "my_configures.txt";
                            try {
                                BufferedReader br = new BufferedReader(new InputStreamReader(
                                        openFileInput(FILENAME)));
                                String str = "";
                                int i = 0;
                                while ((str = br.readLine()) != null) {
                                    Log.d("string... : ", str);
                                    string_for_send = string_for_send + str + '\n';
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Connect(string_for_send);
                        }
                    });
                    tthread.start();
                    Toast.makeText(getApplicationContext(), "forward_wifi",
                            Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.rigth_wifi:
                {
                    Thread tthread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Connect("rigth");
                        }
                    });
                    tthread.start();
                    Toast.makeText(getApplicationContext(), "rigth_wifi",
                            Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.left_wifi:
                {
                    Thread tthread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Connect("left");
                        }

                    });
                    tthread.start();
                    Toast.makeText(getApplicationContext(), "left_wifi",
                            Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.back_wifi:
                {
                    Thread tthread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Connect("back");
                        }

                    });
                    tthread.start();
                    Toast.makeText(getApplicationContext(), "back_wifi",
                            Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.reset_wifi:
                {
                    Thread tthread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Connect("reset");
                        }
                    });
                    tthread.start();
                    Toast.makeText(getApplicationContext(), "reset_wifi",
                            Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.Bluetooth: {
                    Thread tthread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            File file = new File("count_people.txt");
                            FILENAME = file.getPath().toString();
                            readFile();
                            int count_peop = Integer.parseInt(paramatrs[0]);
                            Connect_file(FILENAME,0);
                            File file1 = new File("list_people.txt");
                            FILENAME = file1.getPath().toString();
                            Connect_file(FILENAME, 0);
                            File file2 = new File("my_trains.txt");
                            FILENAME = file2.getPath().toString();
                            Connect_file(FILENAME, 0);
                            File file3 = new File("my_deals.txt");
                            FILENAME = file3.getPath().toString();
                            Connect_file(FILENAME, 0);
                            for (int i = 0; i < count_peop; i++) {
                                File dest = new File( String.valueOf(i+1) + ".jpg");
                                FILENAME = dest.getPath().toString();
                                Connect_file(FILENAME,1);
                            }
                        }
                    });
                    tthread.start();
                    Toast.makeText(getApplicationContext(), "Робот обновлен по Wi-Fi",
                            Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.send_data:
                    {
                        View vv = (View)findViewById(R.id.send_data);
                        SendMessage(vv);
                    }
                break;
                case R.id.start:
                {
                    View vv = (View)findViewById(R.id.send_data);
                    Start_Server(vv);
                }
                break;
                case R.id.button:
                {
                    SendCommand("01");
                    SendCommand("02");
                    SendCommand("03");
                    SendCommand("04");
                }
                break;
                case R.id.button2:
                {}
                break;
                case R.id.forward:
                {
                    SendCommand("forward");
                }
                break;

                case R.id.back:
                {
                    SendCommand("back");
                }
                break;
                case R.id.left:
                {
                    SendCommand("left");
                }
                break;
                case R.id.rigth:
                {
                    SendCommand("rigth");
                }
                break;

                case R.id.reset:
                {
                    SendCommand("reset");
                }
                break;
                case R.id.my_trains:
                {
                    Intent i = new Intent(MainActivity.this, Main3Activity.class);
                    startActivity(i);
                }
                break;
                case R.id.button5:
                {
                    Intent i = new Intent(MainActivity.this, Main4Activity.class);
                    startActivity(i);
                }
                break;
                case R.id.config:
                {
                    Intent i = new Intent(MainActivity.this, Main5Activity.class);
                    startActivity(i);
                }
                break;
                default:
                    break;
            }
        }
    };

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started.");
            mmDevice = device;
            deviceUUID = uuid;
        }
        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread ");
            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                        +MY_UUID_INSECURE );
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }
            mmSocket = tmp;
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE );
            }
            connected(mmSocket);
        }
        public void cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
            }
        }
    }

    private void connected(BluetoothSocket mmSocket) {
        Log.d(TAG, "connected: Starting.");
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){
            byte[] buffer = new byte[1024]; 

            int bytes; 

            // Keep listening to the InputStream until an exception occurs
            while (true) {}
        }

        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
            }
        }

        public void read(FileOutputStream fos) {
            try {
                byte[] bytes = new byte[4];
                int len = mmInStream.read(bytes,0,4);
                Log.d("read","01");
                try {
                    Thread.sleep(3000);
                    // Do some stuff
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
                Log.d("read", "Length_recieved = " + String.valueOf(len));
                int length_file = ByteArrayToint (bytes);
                Log.d("read","Length_file = " + String.valueOf(length_file));
                int length_recive_iter;
                byte[] bytes_f = new byte[length_file];
                int a=0;
                while (((length_recive_iter = mmInStream.read(bytes_f,0,length_file)) != a) || length_file == a) //-1
                {
                   fos.write(bytes_f,0,length_recive_iter);
                        Log.d ("read", String.valueOf(length_recive_iter));
                        a=a+length_recive_iter;
                        Log.d ("read", "total = " + String.valueOf(a));
                    Log.d ("read", "length_of_recieved_bytes = " + String.valueOf(bytes_f.length));
                    if (length_file == a)
                    {
                        Log.d("read", "trying end!");
                        break;
                    }
                }
                fos.close();
                try {
                    Thread.sleep(3000);
                    // Do some stuff
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
                try{
                    Log.d("read", "File MAP recieved !");
                }
                catch (Exception e) {
                    Log.d("read", "File MAP recieved NOOOOO !");
                }
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public static void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public void SendMessage(View v) {
        byte[] bytes = send_data.getText().toString().getBytes(Charset.defaultCharset());
        mConnectedThread.write(bytes);
    }

    public void SendCommand(String command) {
        String ss = command;
        mConnectedThread.write(ss.getBytes(Charset.defaultCharset())); //отправляем размер
    }

    public static byte[] intToByteArray(int a)
    {
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }


    public static int ByteArrayToint (byte[] bytes)
    {
        int i = (bytes[0]<<24)&0xff000000 |
                (bytes[1]<<16)&0x00ff0000|
                (bytes[2]<<8)&0x0000ff00|
                (bytes[3]<<0)&0x000000ff;
        return i;
    }

    public void SendFile(String filename, int i) {
        try {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
            FileInputStream fin = null;
            if (i == 1) {
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File fin_0 = new File(file, filename);
                fin = new FileInputStream(fin_0);
            }
            if (i == 0) {
                fin = openFileInput(filename);
            }
            Log.d("read", "File size: bytes" + String.valueOf(fin.available()));
            int file_size = fin.available();
            if (file_size > 0) {
                byte[] buffer = new byte[file_size];
                fin.read(buffer, 0, file_size);
                String ss = String.valueOf(file_size);
                byte [] size = intToByteArray(file_size);
                Log.d("read", "file_size = " + ss);
                byte[] bytes = ss.getBytes(Charset.defaultCharset());
                mConnectedThread.write(size);
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
                mConnectedThread.write(buffer);
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            } else {Log.d("read","No this file");}
        }
        catch(IOException ex){
            Log.d("read", "Sending file failed !");
        }
    }

    public void RecieveFile(String filename, int i) {
        try {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.getLocalizedMessage();
            }

            FileOutputStream fin = null;
            if (i == 1) {
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File fin_1 = new File(file, "media_robot");
                File fin_0 = new File(fin_1, filename);
                fin = new FileOutputStream(fin_0);
            }
            if (i == 0) {
                fin = openFileOutput(filename, Context.MODE_PRIVATE);
            }
            mConnectedThread.read(fin);
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
        }
        catch(Exception ex){
            Log.d("read", "Recieving file failed !");
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

    void writeFile_empty_deals() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
            for (int i=0; i <1000; i++)
                bw.write("z\n");
            bw.close();
            Log.d("read", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFile_empty() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
                bw.write(String.valueOf(0));
            bw.close();
            Log.d("read", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFile_empty_configure() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
            bw.write("1\n"); //Пол голоса (0 - мужской, 1 - женский)
            bw.write("Loderun\n"); //Логин от Wi-Fi
            bw.write("12348765\n"); //Пароль от Wi-fi
            bw.write("0\n"); //Автопилот при простое больше 5 мин (0 - нет, 1 - да)
            bw.write("0\n"); //Автоматическое подключение к интернету (0 - нет, 1 - да)
            bw.write("0\n"); //Время записи видео
            bw.write("0\n"); //Время записи аудио
            bw.close();
            Log.d("read", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFile_empty_list() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
            bw.write("");
            bw.close();
            Log.d("read", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BluetoothDevice searchDevice(String btName)
    {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null)
        {
            return null;
        }

        if (adapter != null && !adapter.isEnabled()) {
            Intent enableBtIntent = new
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if (!adapter.isEnabled())
        {
            Log.d("read", "Unable Bluetooth !!!");
            return null;
        }

        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        if (pairedDevices.size() >0 )
        {
            Object[] devices = pairedDevices.toArray();
            for (int i =0; i < pairedDevices.size(); i++) {
                BluetoothDevice device = (BluetoothDevice) devices[i];
                Log.d("read", device.getName().toString());
                if (device.getName().toString().equals(serverName)) {
                    return device;
                }
            }
            Log.d("read", "Don't found Bluetooth server: " + serverName);
        }
        else {Log.d("read", "no devices");}
        return null;
    }

    public void Start_Server(View view) {
        AcceptThread accept = new AcceptThread();
        accept.start();
    }

    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        public AcceptThread(){
            BluetoothServerSocket tmp = null ;
            // Create a new listening server socket
            try{
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("appname", MY_UUID_INSECURE);
                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }
            mmServerSocket = tmp;
        }

        public void run(){
            Log.d(TAG, "run: AcceptThread Running.");
            BluetoothSocket socket = null;
            try{
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket start.....");
                socket = mmServerSocket.accept();
                Log.d(TAG, "run: RFCOM server socket accepted connection.");
            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }
            //talk about this is in the 3rd
            if(socket != null){
                connected(socket);
            }
            Log.i(TAG, "END mAcceptThread ");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
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

  Integer read_count(String file_name)
    {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(file_name)));
            String str = "";
            str = br.readLine();
            return Integer.parseInt(str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
