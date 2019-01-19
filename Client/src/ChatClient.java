import java.io.*;
import java.net.*;
import java.util.Scanner;
 
public class ChatClient
{
    public static final String SERVER_HOSTNAME = "localhost";
    public static final int SERVER_PORT = 3002;
    public String name;
 
    public static void main(String[] args)
    {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
           // Connection au Serveur
           Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
           in = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
           out = new PrintWriter(
               new OutputStreamWriter(socket.getOutputStream()));
           System.out.println("Connecté au serveur " + SERVER_HOSTNAME + ":" + SERVER_PORT);
        } catch (IOException ioe) {
           System.err.println("Impossible de se connecter au serveur " + SERVER_HOSTNAME + ":" + SERVER_PORT);
           ioe.printStackTrace();
           System.exit(-1);
        }
 
        // Creation du thread 
        Sender sender = new Sender(out);
        sender.setDaemon(true);
        sender.start();
 
        try {
           // lis les messages du serveur et affiche
            String message;
           while ((message=in.readLine()) != null) {
               System.out.println(message);
           }
        } catch (IOException ioe) {
           System.err.println("Connection au serveur erreur.");
           ioe.printStackTrace();
        }
 
    }
}