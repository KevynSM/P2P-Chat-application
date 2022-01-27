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


    String myName;
    String msg = "";

    socket(TextArea ta, String pin, String myName) {
        ecran = ta;
        this.pin = pin;
        this.myName = myName;
    }

    @Override
    public void run(){
        try {
            int pinInt = Integer.parseInt(pin);
            System.out.println("pin: " + pinInt);
            DS = new DatagramSocket(pinInt);
            System.out.println("DS: " + DS);
        }
        catch(IOException e){

        }
        while(true) {
            receiveDP();
        }
    }

    private void sendDPtoChat(int Pr) {
        String msgToSend = myName + ": " + this.msg;
        int len = msgToSend.length();
        byte b[] = new byte[len];
        msgToSend.getBytes(0,len,b,0);
        try{
            ER = InetAddress.getByName("127.0.0.1");
            System.out.println("ER: " + ER);
            DatagramPacket DP = new DatagramPacket(b,len,ER,Pr);
            System.out.println("DP: " + DP);

            DS.send(DP);
        }catch(IOException e){}
//        this.msg = "";
    }

    public void receiveDP(){
        try {
            DatagramPacket DP = new DatagramPacket(bp,1024);
            DS.receive(DP);
            IPr = DP.getAddress();
            byte Payload[] = DP.getData();
            int len = DP.getLength();
            String res = new String(Payload,0,0,len);

            System.out.println(res.split(" ")[0]);

            if(res.split(" ")[0].equals("pin")) {
                System.out.println("Estou aqui");
                int port = Integer.parseInt(res.split(" ")[1]);
                sendDPtoChat(port);
            }
            else {
                String tmp = IPr.toString();
                String temp = tmp.substring(1);
//                ecran.appendText("\n"+temp+": "+res);
                ecran.appendText("\n"+res);
            }


        }
        catch(IOException e){

        }
    }

    public void sendDP(String Pr,String msg){
        this.msg = msg;
        Pr = "nam " + Pr;
        System.out.println("Pr: " + Pr);
        int len = Pr.length();
        byte b[] = new byte[len];
        Pr.getBytes(0,len,b,0);
        try{
            ER = InetAddress.getByName("127.0.0.1");
            System.out.println("ER: " + ER);
            int portIndexServer = 8081;
            DatagramPacket DP = new DatagramPacket(b,len,ER,portIndexServer);
            System.out.println("DP: " + DP);




            DS.send(DP);
        }catch(IOException e){}
    }

}
