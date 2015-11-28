
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


 class User {
     public String loginName;
     public Socket userSocket;
     public String role;
     
     public User(String name, Socket s, String r){
         loginName=name;
         userSocket=s;
         role=r;
     }
 }


 class ClientHandler extends Thread{
    
     private Socket client;
     private Vector<User> clients= new Vector<User>();
     private Vector<Conversation> convs= new Vector<Conversation>();
     private int convId;

    public ClientHandler(Socket client,Vector<User> clients,Vector<Conversation> convs){
        this.client=client;
        this.clients=clients;
        this.convs=convs;
        convId=0;
    }
    
    private String clientAuth(){
        String name="";
        try{
            DataInputStream dis= new DataInputStream(client.getInputStream());
            DataOutputStream dos= new DataOutputStream(client.getOutputStream());

            dos.writeUTF("Insert Login name");
            name=dis.readUTF();
            dos.writeUTF("Insert Role");
            String role=dis.readUTF();
            
            User u= new User(name, client, role);
            
            clients.add(u);
            
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return name;

    }
    
    @Override
    public void run(){
         try {
            
           String loginName= clientAuth();
            
            while(true){
                DataInputStream din= new DataInputStream(client.getInputStream());
                String msg= din.readUTF();
                if(msg.equalsIgnoreCase("create")){
                    Conversation c= new Conversation(convId);
                    convId++;
                    c.onlineUsers.add(client);
                    convs.add(c);
                    System.out.println("Created Succesfully");
                }else if(msg.equalsIgnoreCase("enroll")){
                    String no=din.readUTF();
                    int noI=Integer.parseInt(no);
                    convs.get(noI).onlineUsers.add(client);

                }
                else if(msg.equalsIgnoreCase("msg")){
                    String no=din.readUTF();
                    int noI=Integer.parseInt(no);
                    String txt=din.readUTF();
                    ArrayList<Socket> users= convs.get(noI).onlineUsers;
                    System.out.println(users.toString());
                    for (Socket u : users) {
                         DataOutputStream dout= new DataOutputStream(u.getOutputStream());
                         dout.writeUTF(txt);
                    }
                    
                }

            }
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

public class Server {

  
    public static void main(String[] args) {
          
        try {
             ServerSocket server= new ServerSocket(1234);
             Vector<User> clients= new Vector<User>();
             Vector<Conversation> activeConvs= new Vector<Conversation>();
             Socket c;

             while(true){
                c=server.accept();
//                clients.add(c);
                ClientHandler ch = new ClientHandler(c,clients,activeConvs);
                ch.start();
             }
                     
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
}