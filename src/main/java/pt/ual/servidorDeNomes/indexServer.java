package pt.ual.servidorDeNomes;

import java.io.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class indexServer extends Frame {
    TextArea Server = new TextArea(12,40);
    socketIndexServer sock = new socketIndexServer(Server);



    public indexServer(String str) {
        super(str);
    }

    public static void main(String[] args) throws IOException {
        indexServer app = new indexServer("indexServer");
        app.resize(320,240);
        app.GUI();
        app.show();
        app.StartSocket();
    }


    public void GUI(){
        setBackground(Color.lightGray);
        Server.setEditable(false);
        GridBagLayout GBL = new GridBagLayout();
        setLayout(GBL);
        Panel P1 = new Panel();
        P1.setLayout(new BorderLayout(5,5));
        P1.add("Center",Server);
        GridBagConstraints P1C = new GridBagConstraints();
        P1C.gridwidth = GridBagConstraints.REMAINDER;
        GBL.setConstraints(P1,P1C);
        add(P1);
    }

    public void StartSocket(){
        sock.start();
    }

    public boolean handleEvent(Event i) {
        if(i.id == Event.WINDOW_DESTROY) {
            dispose();
            System.exit(0);
            return true;
        }
        return super.handleEvent(i);
    }
}
