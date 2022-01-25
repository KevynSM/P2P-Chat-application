package pt.ual.servidorDeNomes;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


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

            String[] commands = msg.split(" ");


            System.out.println(commands);


            String res;

            if (commands[0].equals("add")) {
                String newPin = commands[1];
                String newName = commands[2];

                System.out.println(newPin);
                System.out.println(newName);

                if(namesMap.containsKey(newName)) {
                    res = "Usuário existente!";
                } else if(namesMap.containsValue(newPin)) {
                    res = "Pin já utilizado!";
                } else if(Integer.valueOf(newPin) < 8000 && Integer.valueOf(newPin) > 8010){
                    res = "Pin têm de ser entre 8000 a 8010.";
                } else {
                    namesMap.put(newName, newPin);
                    res = "Usuário registado: " + newName + ": " + namesMap.get(newName).toString();
                }

            } else if (commands[0].equals("lis")) {
                res = "Lista de nomes: " + namesMap.toString();

            } else if (commands[0].equals("nam")) {
                String name = commands[1];
                System.out.println(name);
                res = namesMap.getOrDefault(name, "Nome inexistente");

            } else if (commands[0].equals("pin")) {
                String pin = commands[1];
                System.out.println(pin);
                if(getSingleKeyFromValue(namesMap, pin) != null){
                    res = getSingleKeyFromValue(namesMap, pin);
                } else {
                    res = "Pin não existente.";
                }

            } else {
                res = "Mêtodo inexistente";
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

    public static <K, V> K getSingleKeyFromValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
