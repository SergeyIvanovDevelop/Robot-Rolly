import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileWriter;

public class server_java {

public static void main(String[] args) {
	System.out.println("Server Started");
	start();
}

public static void start(){
    try {
    ServerSocket SRVSOCK = new ServerSocket(8083);
	while (true){
		try {
			Socket SOCK = SRVSOCK.accept();
			System.out.println("Connected!!!!");
			InputStream IS = SOCK.getInputStream();
			byte[] bytes = new byte[1024];
			int size_recieved = IS.read(bytes);
			System.out.println(size_recieved);
			String value = new String(bytes, "UTF-8");
			String norm_value = value.substring(0, size_recieved);
			System.out.println(value);

			// Запись в файл и отправка на RPi3B+
			BufferedWriter writer = new BufferedWriter(new FileWriter("/home/sergey/ROS/Projects_ROS/workspace/RESURSES/command.txt"));
			writer.write(norm_value);
			writer.close();
			String[] arguments = new String[] {"xterm", "-e", "cd /home/sergey/ROS/Projects_ROS/workspace/ && source devel/setup.bash && rosrun WiFi publisher.py command /home/sergey/ROS/Projects_ROS/workspace/RESURSES/command.txt"};
			Process proc = new ProcessBuilder(arguments).start();

    		} catch (IOException e) {
        		e.printStackTrace();    
    		}
	}
	}
  catch (IOException e) {
        e.printStackTrace();    
    }
}
}
  
