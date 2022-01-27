package pt.ual.chat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.Arrays;

public class socket extends Thread {
    private static final String password = "11111111";
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
        try{
            String msgToSend = myName + ": " + this.msg;

            byte key[] = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            byte[] clearSTR = msgToSend.getBytes();

            desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encodedSTR = desCipher.doFinal(clearSTR);

            int len = msgToSend.length();
            byte b[] = new byte[len];
            msgToSend.getBytes(0,len,b,0);

            ER = InetAddress.getByName("127.0.0.1");
            System.out.println("ER: " + ER);
            DatagramPacket DP = new DatagramPacket(encodedSTR,encodedSTR.length,ER,Pr);
            System.out.println("DP: " + DP);

            DS.send(DP);
        }catch(Exception e){}
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

            byte key[] = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            //byte[] encodedSTR = res.getBytes();
            byte[] encodedSTR = Arrays.copyOfRange(Payload, 0, len);

            //Decode message
            desCipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] clearSTR = desCipher.doFinal(encodedSTR);
            res = new String(clearSTR);

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
        catch(Exception e){

        }
    }

    public void sendDP(String Pr,String msg){
        try{
            this.msg = msg;
            Pr = "nam " + Pr;
            System.out.println("Pr: " + Pr);

            byte key[] = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            byte[] clearSTR = Pr.getBytes();
            desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encodedSTR = desCipher.doFinal(clearSTR);


           /* int len = Pr.length();
            byte b[] = new byte[len];
            Pr.getBytes(0,len,b,0);*/



            ER = InetAddress.getByName("127.0.0.1");
            System.out.println("ER: " + ER);
            int portIndexServer = 8081;
            DatagramPacket DP = new DatagramPacket(encodedSTR,encodedSTR.length,ER,portIndexServer);
            System.out.println("DP: " + DP);


            DS.send(DP);
        }catch(Exception e){}
    }

}
