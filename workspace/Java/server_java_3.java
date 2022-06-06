import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;

public class server_java_3 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
	System.out.println("Server3 started ");
        try {
            serverSocket = new ServerSocket(8086);
        } catch (IOException ex) {
            System.out.println("Can't setup server on this port number. ");
        }

        
        while (true) {

		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;

		try {
		    socket = serverSocket.accept();
		} catch (IOException ex) {
		    System.out.println("Can't accept client connection. ");
		}
		
		try {
		    in = socket.getInputStream();
		} catch (IOException ex) {
		    System.out.println("Can't get socket input stream. ");
		}

		try {
		    out = new FileOutputStream("/home/sergey/ROS/Projects_ROS/workspace/RESURSES/count_people.txt");
		} catch (FileNotFoundException ex) {
		    System.out.println("File not found. ");
		}

		byte[] bytes = new byte[1024];

		int count;
		while ((count = in.read(bytes)) > 0) {
		    //System.out.println(count);
		    out.write(bytes, 0, count);
		}

		out.close();
		in.close();
		socket.close();

		
		int count_people = readFile("/home/sergey/ROS/Projects_ROS/workspace/RESURSES/count_people.txt");

		socket = null;
		in = null;
		out = null;

		try {
		    socket = serverSocket.accept();
		} catch (IOException ex) {
		    System.out.println("Can't accept client connection. ");
		}
		
		try {
		    in = socket.getInputStream();
		} catch (IOException ex) {
		    System.out.println("Can't get socket input stream. ");
		}
		try {
		    out = new FileOutputStream("/home/sergey/ROS/Projects_ROS/workspace/RESURSES/list_people.txt");
		} catch (FileNotFoundException ex) {
		    System.out.println("File not found. ");
		}

		byte[] bytes0 = new byte[1024];

		count = 0;
		while ((count = in.read(bytes0)) > 0) {
		    //System.out.println(count);
		    out.write(bytes0, 0, count);
		}

		out.close();
		in.close();
		socket.close();

		socket = null;
		in = null;
		out = null;

		try {
		    socket = serverSocket.accept();
		} catch (IOException ex) {
		    System.out.println("Can't accept client connection. ");
		}
		
		try {
		    in = socket.getInputStream();
		} catch (IOException ex) {
		    System.out.println("Can't get socket input stream. ");
		}
		try {
		    out = new FileOutputStream("/home/sergey/ROS/Projects_ROS/workspace/RESURSES/trening.txt");
		} catch (FileNotFoundException ex) {
		    System.out.println("File not found. ");
		}

		byte[] bytes2 = new byte[1024];

		count = 0;
		while ((count = in.read(bytes2)) > 0) {
		    //System.out.println(count);
		    out.write(bytes2, 0, count);
		}

		out.close();
		in.close();
		socket.close();

		socket = null;
		in = null;
		out = null;

		try {
		    socket = serverSocket.accept();
		} catch (IOException ex) {
		    System.out.println("Can't accept client connection. ");
		}
		
		try {
		    in = socket.getInputStream();
		} catch (IOException ex) {
		    System.out.println("Can't get socket input stream. ");
		}
		try {
		    out = new FileOutputStream("/home/sergey/ROS/Projects_ROS/workspace/RESURSES/deals.txt");
		} catch (FileNotFoundException ex) {
		    System.out.println("File not found. ");
		}

		byte[] bytes3 = new byte[1024];

		count = 0;
		while ((count = in.read(bytes3)) > 0) {
		    //System.out.println(count);
		    out.write(bytes3, 0, count);
		}

		out.close();
		in.close();
		socket.close();

		for (int i =0; i < count_people; i++)
		{
		socket = null;
		in = null;
		out = null;

		try {
		    socket = serverSocket.accept();
		} catch (IOException ex) {
		    System.out.println("Can't accept client connection. ");
		}
		
		try {
		    in = socket.getInputStream();
		} catch (IOException ex) {
		    System.out.println("Can't get socket input stream. ");
		}
		    try {
		    String current_number = "/home/sergey/ROS/Projects_ROS/workspace/RESURSES/faces/" + Integer.toString(i+1) + ".jpg";
		    out = new FileOutputStream(current_number);
		} catch (FileNotFoundException ex) {
		    System.out.println("File not found. ");
		}

		byte[] bytes1 = new byte[4096];

		count = 0;
		while ((count = in.read(bytes1)) > 0) {
		    //System.out.println(count);
		    out.write(bytes1, 0, count);
		}

		out.close();
		in.close();
		socket.close();
		}

		System.out.println("File has recieved!");

	}
        //serverSocket.close(); //(Т.к. никогда не достигнем этого кода)
    }


    static int readFile(String FILENAME) {
	int count_people = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILENAME));
            String str = "";
            int i = 0;
	    str = br.readLine();
	System.out.println(str);
	count_people = Integer.parseInt(str);
	return count_people;

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
	return count_people;
    }


}
  
