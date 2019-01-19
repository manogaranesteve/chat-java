import java.net.*;
import java.io.*;
 
public class ChatServer
{
    public static final int LISTENING_PORT = 3002;
 
    public static void main(String[] args)
    {
        // On ouvre la socket
        ServerSocket serverSocket = null;
        try {
           serverSocket = new ServerSocket(LISTENING_PORT);
           System.out.println("Bienvenue sur le serveur");
           System.out.println("Le Serveur demmarre sur le port " + LISTENING_PORT);
        } catch (IOException se) {
           System.err.println("Erreur demarrage sur le port " + LISTENING_PORT);
           se.printStackTrace();
           System.exit(-1);
        }
 
        // Demmare le Dispatcher
        ServerDispatcher serverDispatcher = new ServerDispatcher();
        serverDispatcher.start();
 
        // Accept les connections clients
        while (true) {
           try {
               Socket socket = serverSocket.accept();
               ClientInfo clientInfo = new ClientInfo();
               String ip=(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
               
               if (ip.equals("10.3.0.241")) { 
            	   System.out.println("Votre ip est refusé");
            	   OutputStream os = socket.getOutputStream();
                   OutputStreamWriter osw = new OutputStreamWriter(os);
                   BufferedWriter bw = new BufferedWriter(osw);
                   bw.write("Votre ip est refusé");
                   socket.close();
            	} else {
            		clientInfo.mSocket = socket;
                    /*fonction ecoute*/
                    ClientListener clientListener =
                        new ClientListener(clientInfo, serverDispatcher);
                    /*fonction envoi*/
                    ClientSender clientSender =
                        new ClientSender(clientInfo, serverDispatcher);
                    clientInfo.mClientListener = clientListener;
                    clientInfo.mClientSender = clientSender;
                    clientListener.start();
                    clientSender.start();
                    String senderIP = socket.getInetAddress().getHostAddress();
                    //String senderPort = "" + socket.getPort();
                    String name = socket.getInetAddress().getHostName();
                    System.out.println(senderIP + ':' + name + " : connecté");
                    serverDispatcher.addClient(clientInfo);
            	}
               
               
           } catch (IOException ioe) {
               ioe.printStackTrace();
           }
        }
    }
 
}
 