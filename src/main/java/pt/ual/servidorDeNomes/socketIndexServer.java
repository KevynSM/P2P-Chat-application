package pt.ual.servidorDeNomes;

import java.io.*;
import java.net.*;
import java.awt.*;

public class socketIndexServer extends Thread {
    InetAddress ER;
    DatagramSocket DS;
    byte bp[]=new byte[1024];
    TextArea Server=new TextArea(12,40);

    socketIndexServer(TextArea ta) {
        Server = ta;
    }

    @Override
    public void run() {
        try {
            DS = new DatagramSocket(8081);
        }
        catch(IOException e) {

        }
        while(true) {
            receiveDP();
        }
    }

    public void receiveDP(){
        try {
            DatagramPacket DP = new DatagramPacket(bp,1024);
            DS.receive(DP);
            byte Payload[] = DP.getData();
            int len=DP.getLength();
            String msg = new String(Payload,0,0,len);
            Server.appendText("\nCliente->"+msg);
            String res = "Servidor->Disse-me: "+msg;
            sendDP(8080,res);
        }
        catch (IOException e) {

        }
    }


    public void sendDP(int Pr,String msg){
        int len = msg.length();
        byte b[] = new byte[len];
        msg.getBytes(0,len,b,0);

        try {
            ER = InetAddress.getByName("127.0.0.1");
            DatagramPacket DP = new DatagramPacket(b,len,ER,Pr);
            DS.send(DP);
        }
        catch (IOException e) {

        }
    }


}
