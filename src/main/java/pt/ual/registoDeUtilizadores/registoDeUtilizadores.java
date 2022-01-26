package pt.ual.registoDeUtilizadores;

import java.io.*;
import java.awt.*;

public class registoDeUtilizadores extends Frame {
    TextArea Server = new TextArea(10,30);
    TextField Client = new TextField(30);
    Button Send  =new Button("Send");
    socketRegistoDeUtilizadores sock = new socketRegistoDeUtilizadores(Server);

    public registoDeUtilizadores(String str){
        super(str);
    }


    public static void main(String[] args)throws IOException{
        registoDeUtilizadores app = new registoDeUtilizadores("registoDeUtilizadores");
        app.resize(320,240);
        app.GUI();
        app.show();
        app.StartSocket();
    }

    public void GUI(){
        setBackground(Color.lightGray);
        Server.setEditable(false);
        GridBagLayout GBL=new GridBagLayout();
        setLayout(GBL);
        Panel P1 = new Panel();
        P1.setLayout(new BorderLayout(5,5));
        P1.add("West",Client);
        P1.add("East",Send);
        P1.add("South",Server);
        GridBagConstraints P1C=new GridBagConstraints();
        P1C.gridwidth=GridBagConstraints.REMAINDER;
        GBL.setConstraints(P1,P1C);
        add(P1);
    }

    public void StartSocket() {
        sock.start();
    }

    public boolean handleEvent(Event i) {
        if(i.id == Event.WINDOW_DESTROY){
            dispose();
            System.exit(0);
            return true;
        }
        return super.handleEvent(i);
    }

    public boolean action(Event i,Object o) {
        if(i.target == Send){
            String msg = Client.getText();
            sock.sendDP(8081,msg);
            Client.setText("");
            return true;
        }
        return false;
    }

}
