package pt.ual.registoDeUtilizadores;

import javax.crypto.*;
import javax.crypto.spec.*;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.security.InvalidKeyException;

public class socketRegistoDeUtilizadores extends Thread {
    private static final String password = "11111111";
    InetAddress ER;
    DatagramSocket DS;
    byte bp[]=new byte[1024];
    TextArea Server=new TextArea(10,30);

    socketRegistoDeUtilizadores(TextArea ta) {
        Server = ta;
    }


    @Override
    public void run(){
        try {
            DS = new DatagramSocket(8080);
            System.out.println("DS: " + DS);
        }
        catch(IOException e) {}
        while(true) {
            receiveDP();
        }
    }

    public void receiveDP(){
        try{
            DatagramPacket DP = new DatagramPacket(bp,1024);
            DS.receive(DP);
            byte Payload[] = DP.getData();
            int len = DP.getLength();
            String res = new String(Payload,0,0,len);

            byte key[] = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            byte[] encodedSTR = res.getBytes();

            //Decode message
            desCipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] clearSTR = desCipher.doFinal(encodedSTR);
            res = new String(clearSTR);


            Server.appendText("\n"+res);
        }
        catch(Exception e){System.out.println(e);}
    }

    public void sendDP(int Pr,String msg){
        try{
            byte key[] = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            byte[] clearSTR = msg.getBytes();
            System.out.println("(Y) ClearSTR Length:" + clearSTR.length);

            desCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encodedSTR = desCipher.doFinal(clearSTR);
            System.out.println("(X) encodedSTR: " + encodedSTR);
            System.out.println("encodedSTR lenght: " + encodedSTR.length);
            msg = new String(encodedSTR);
            System.out.println("(SEND) msg: " + msg);


            int len = msg.length();
            System.out.println("len: " + len);
            byte b[] = new byte[len];
            msg.getBytes(0,len,b,0);

            ER = InetAddress.getByName("127.0.0.1");
            DatagramPacket DP = new DatagramPacket(encodedSTR,encodedSTR.length,ER,Pr);
            System.out.println("DP: " + DP);
            DS.send(DP);

        }catch(Exception e){System.out.println(e);}


//        try{
//
//        }catch(Exception e){System.out.println(e);}
    }
}
