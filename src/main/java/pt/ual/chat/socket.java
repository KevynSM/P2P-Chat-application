package pt.ual.chat;

import java.io.*;
import java.net.*;
import java.awt.*;

public class socket extends Thread {
    InetAddress ER, IPr;
    DatagramSocket DS;
    String pin;
    byte bp[] = new byte[1024];
    TextArea ecran = new TextArea(10,30);

    socket(TextArea ta, String pin) {
        ecran = ta;
        this.pin = pin;
    }

    @Override
    public void run(){
        try {
            System.out.println(pin);
            DS = new DatagramSocket(Integer.parseInt(pin));
            System.out.println(DS);
        }
        catch(IOException e){

        }
        while(true) {
            receiveDP();
        }
    }

    public void receiveDP(){
        try {
            DatagramPacket DP = new DatagramPacket(bp,1024);
            DS.receive(DP);
            IPr = DP.getAddress();
            byte Payload[] = DP.getData();
            int len = DP.getLength();
            String res = new String(Payload,0,0,len);
            String tmp = IPr.toString();
            String temp = tmp.substring(1);
            ecran.appendText("\n"+temp+": "+res);
        }
        catch(IOException e){

        }
    }

    public void sendDP(int Pr,String msg,String end){
        int len = msg.length();
        byte b[] = new byte[len];
        msg.getBytes(0,len,b,0);
        try{
            ER = InetAddress.getByName("127.0.0.1");
            System.out.println(ER);
            DatagramPacket DP=new DatagramPacket(b,len,ER,Pr);
            DS.send(DP);
        }catch(IOException e){}
    }

}
