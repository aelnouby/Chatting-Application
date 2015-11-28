package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class ClientReceiver extends Thread {
    Socket server;
    public ClientReceiver(Socket server){
        this.server=server;
    }
    
    @Override
    public void run(){
        try {
            DataInputStream din= new DataInputStream(server.getInputStream());
            while(true){
                String response;
                response= din.readUTF();
                System.out.println(response);
                if(response.equalsIgnoreCase("Bye"))
                    break;
            }
//            din.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

class ClientSender extends Thread {
    Socket server;
    public ClientSender(Socket server){
        this.server=server;
    }
    
    @Override
    public void run(){
        try {
            DataOutputStream dout= new DataOutputStream(server.getOutputStream());
            String userIn;
            Scanner sc= new Scanner(System.in);
            while(true){
                userIn=sc.nextLine();
                dout.writeUTF(userIn);
                if(userIn.equalsIgnoreCase("Bye"))
                    break;
            }
//            dout.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
public class Client {

    public static void main(String[] args) {
        try {
            Socket client = new Socket("localhost",1234);
            ClientReceiver cr=new ClientReceiver(client);
            ClientSender cs = new ClientSender(client);
            
            
                cs.start();
                cr.start();
            
   
          
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
