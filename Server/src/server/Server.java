
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


 class ClientHandler extends Thread{
    
     private Socket client;
     private Vector<Socket> clients= new Vector<Socket>();
     private Vector<Conversation> convs= new Vector<Conversation>();

    public ClientHandler(Socket client,Vector<Socket> clients,Vector<Conversation> convs){
        this.client=client;
        this.clients=clients;
        this.convs=convs;
    }
    
    @Override
    public void run(){
         try {
            
            System.out.println("Client is here"+clients.size());
            int id=0;
            while(true){
                DataInputStream din= new DataInputStream(client.getInputStream());
                String msg= din.readUTF();
                System.out.println("recevied "+msg);
                if(msg.equalsIgnoreCase("create")){
                    Conversation c= new Conversation(id);
                    id++;
                    c.onlineUsers.add(client);
                    convs.add(c);
//                    System.out.println(convs.get(0).onlineUsers.toString()); 
                    System.out.println("Created Succesfully :D");
                }else if(msg.equalsIgnoreCase("enroll")){
                    System.out.println("Give me the conv code for the enrollement");
                    String no=din.readUTF();
                    int noI=Integer.parseInt(no);
                    System.out.println("code is Cool  :D");
                    convs.get(noI).onlineUsers.add(client);
//                    System.out.println(convs.get(no).onlineUsers.toString());

                }
                else if(msg.equalsIgnoreCase("msg")){
                    System.out.println("Give me the conv code for the msg");
                    String no=din.readUTF();
                    int noI=Integer.parseInt(no);
                    System.out.println("MSG code is Good :D");
//                    System.out.println(convs.get(no).onlineUsers.toString());
                    String txt=din.readUTF();
                    ArrayList<Socket> users= convs.get(noI).onlineUsers;
                    System.out.println(users.toString());
                    for (Socket u : users) {
                         DataOutputStream dout= new DataOutputStream(u.getOutputStream());
                         dout.writeUTF(txt);
                         dout.flush();
                    }
                    
                }
//                for (Socket c : clients) {
//                    DataOutputStream dout= new DataOutputStream(c.getOutputStream());
//                    dout.writeUTF(msg);
////                    dout.close();
//                }
//                if(msg.equalsIgnoreCase("Bye"))
//                    break;
            }
//            din.close();
//            dout.close();

            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

public class Server {

  
    public static void main(String[] args) {
          
        try {
             ServerSocket server= new ServerSocket(1234);
             Vector<Socket> clients= new Vector<Socket>();
             Vector<Conversation> activeConvs= new Vector<Conversation>();
             Socket c;

             while(true){
                c=server.accept();
                clients.add(c);
                ClientHandler ch = new ClientHandler(c,clients,activeConvs);
                ch.start();
                System.out.println("out of thread");
             }
                     
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
}