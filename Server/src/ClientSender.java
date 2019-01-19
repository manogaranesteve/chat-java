import java.io.*;
import java.net.*;
import java.util.*;
 
public class ClientSender extends Thread
{
    private Vector mMessageQueue = new Vector();
 
    private ServerDispatcher mServerDispatcher;
    private ClientInfo mClientInfo;
    private PrintWriter mOut;
 
    public ClientSender(ClientInfo aClientInfo, ServerDispatcher aServerDispatcher)
    throws IOException
    {
        mClientInfo = aClientInfo;
        mServerDispatcher = aServerDispatcher;
        Socket socket = aClientInfo.mSocket;
        mOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
 
    public synchronized void sendMessage(String aMessage)
    {
        mMessageQueue.add(aMessage);
        notify();
    }
 
    private synchronized String getNextMessageFromQueue() throws InterruptedException
    {
        while (mMessageQueue.size()==0)
           wait();
        String message = (String) mMessageQueue.get(0);
        mMessageQueue.removeElementAt(0);
        return message;
    }
 
    private void sendMessageToClient(String aMessage)
    {
        mOut.println(aMessage);
        mOut.flush();
    }
 
    public void run()
    {
        try {
           while (!isInterrupted()) {
               String message = getNextMessageFromQueue();
               sendMessageToClient(message);
           }
        } catch (Exception e) {
        }
 
        mClientInfo.mClientListener.interrupt();
        mServerDispatcher.deleteClient(mClientInfo);
    }
 
}