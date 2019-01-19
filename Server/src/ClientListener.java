import java.io.*;
import java.util.*;
import java.net.*;
 
public class ClientListener extends Thread
{
    private ServerDispatcher mServerDispatcher;
    private ClientInfo mClientInfo;
    private BufferedReader mIn;
 
    public ClientListener(ClientInfo aClientInfo, ServerDispatcher aServerDispatcher)
    throws IOException
    {
        mClientInfo = aClientInfo;
        mServerDispatcher = aServerDispatcher;
        Socket socket = aClientInfo.mSocket;
        mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run()
    {
        try {
           while (!isInterrupted()) {
               String message = mIn.readLine();
               if (message == null)
                   break;
               mServerDispatcher.dispatchMessage(mClientInfo, message);
           }
        } catch (IOException ioex) {
          
        }
 
        mClientInfo.mClientSender.interrupt();
        mServerDispatcher.deleteClient(mClientInfo);
    }
 
}