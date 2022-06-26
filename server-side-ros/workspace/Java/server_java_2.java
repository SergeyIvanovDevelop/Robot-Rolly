import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

public class server_java_2 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8083);
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
		    out = new FileOutputStream("output_file.jpg");
		} catch (FileNotFoundException ex) {
		    System.out.println("File not found. ");
		}

		byte[] bytes = new byte[1024];

		int count;
		while ((count = in.read(bytes)) > 0) {
		    out.write(bytes, 0, count);
		}

		out.close();
		in.close();
		socket.close();

		System.out.println("File has recieved!");

	}
        //serverSocket.close(); //(Т.к. никогда не достигнем этого кода)
    }
}
  
