package pt.ual.servidorDeNomes;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class socketIndexServer extends Thread {
    InetAddress ER;
    DatagramSocket DS;
    byte bp[] = new byte[1024];
    TextArea Server = new TextArea(12,40);

    Map<String, String> namesMap = new HashMap<String, String>();

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
            Server.appendText("\nCliente->" + msg);

            String action = msg.substring(0, 3);


            System.out.println(action);


            String res;

            if (action.equals("add")) {
                String newPin = msg.substring(6, 10);
                String newName = msg.substring(13);

                System.out.println(newPin);
                System.out.println(newName);

                namesMap.put(newName, newPin);
                res = "Usuario registado: " + newName + ": " + namesMap.get(newName).toString();
            }
            else if (action.equals("lis")) {
                res = "Lista de nomes: " + namesMap.toString();
            }
            else if (action.equals("nam")) {
                String name = msg.substring(6);
                System.out.println(name);
                res = namesMap.getOrDefault(name, "Nome inexistente");
            }
            else {
                res = "Metodo inexistente";

            }

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
