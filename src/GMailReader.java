import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Connects to gmail account and returns unread messages
 */
public class GMailReader {
    private Socket socket; //Communication socket
    private BufferedReader input;
    private BufferedWriter output;
    private static final int PORT = 995; //GMail port

    public boolean connect(String host, int port) {
        boolean res = false;

        //Getting socket factory to make new socket
        SSLSocketFactory socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();

        try {
            //Trying to create new socket
            socket = (SSLSocket)socketFactory.createSocket(host, port);

            //Setting up input handlers
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (isConnected()) {
            res = true;
            System.out.println(host + ": connected to host.");
            System.out.println(getResponse());
        }
        else System.out.println(host + ": error connecting...");

        return res;
    }

    public boolean connect (String host) {
        return connect(host, PORT);
    }
    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public void disconnect() {
        if (!isConnected()) {
            System.out.println("Not connected to disconnect.");
        } else {
            try {
                socket.close();
                input = null;
                output = null;
                System.out.println("Disconnected from the host.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getResponse() {
        String response = new String();
        try {
            response = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.startsWith("-ERR")) {
            response = "Error: " + response;
        }

        return response;
    }

    public String sendCommand(String command) {
        try {
            output.write(command + "\n");
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return getResponse();
    }

    public void login(String name, String pass) {
        String u = sendCommand("USER " + name);
        String p = sendCommand("PASS " + pass);

        if (u.startsWith("-ERR") || p.startsWith("-ERR")) {
            System.out.println("Bad account name or password.");
            disconnect();
            System.exit(0);
        }
    }

    public void logout() {
        System.out.println(sendCommand("QUIT"));
    }

    public int getNewMessageCount() {
        int count = 0;

        //Getting info about inbox
        String info = sendCommand("STAT");
        String[] data = info.split(" ");

        count = Integer.parseInt(data[1]); // data[1] message count

        return count;
    }
    public ArrayList<String> getMessage(int num) throws IOException {
        String response = sendCommand("RETR " + num);
        ArrayList<String> list = new ArrayList<String>();

        while ((response = getResponse()).length() != 0) {
        }

        while (!(response = getResponse()).equals(".")) {
            list.add(response);
        }

        return list;
    }

}
