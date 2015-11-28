/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author alaaelnouby
 */
public class Conversation {
    int count;
    int id;
    ArrayList<Socket> onlineUsers;   
    
    public Conversation(int id){
        this.id=id;
        onlineUsers= new ArrayList<Socket>();
    }
    
}
