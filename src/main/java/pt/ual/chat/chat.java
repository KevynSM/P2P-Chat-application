package pt.ual.chat;


import java.awt.*;
import java.io.*;

public class chat extends Frame {
    private static chat app;
    TextArea ecran = new TextArea(10,30);
    TextField addr = new TextField(30);
    TextField text = new TextField(30);
    TextField code = new TextField(4);
    Button Check = new Button("Check");
    Button Send = new Button("Send");
    socket sock;

    public chat(String str){
        super(str);
    }

    public static void main(String[] args) throws IOException {
        app=new chat("Chat");
        app.resize(320,290);
        app.GUI2();
        app.show();
    }

    public void GUI(){
        setBackground(Color.lightGray);
        ecran.setEditable(false);
        GridBagLayout GBL = new GridBagLayout();
        setLayout(GBL);
        Panel P = new Panel();
        P.setLayout(new BorderLayout(5,5));
        P.add("North",text);
        P.add("West",addr);
        P.add("East",Send);
        P.add("South",ecran);
        GridBagConstraints PC = new GridBagConstraints();
        PC.gridwidth = GridBagConstraints.REMAINDER;
        GBL.setConstraints(P,PC);

        add(P);
    }

    public void GUI2(){
        setBackground(Color.lightGray);
        ecran.setEditable(false);
        GridBagLayout GBL = new GridBagLayout();
        setLayout(GBL);
        Panel P = new Panel();
        P.setLayout(new BorderLayout(5,5));
        P.add("West", code);
        P.add("East", Check);
        GridBagConstraints PC = new GridBagConstraints();
        PC.gridwidth = GridBagConstraints.REMAINDER;
        GBL.setConstraints(P,PC);

        add(P);
    }

    public void StartSocket() {
        this.sock.start();
    }

    public boolean handleEvent(Event i){
        if(i.id == Event.WINDOW_DESTROY){
            dispose();
            System.exit(0);
            return true;
        }
        return super.handleEvent(i);
    }

    public boolean action(Event i,Object o){
        if(i.target == Check){
            String pin = code.getText();
            sock = new socket(ecran, pin);
            StartSocket();
            String msg = "pin " + code.getText();
            String end = "127.0.0.1";
            sock.sendDP(8081,msg,end);
            return true;
        }
        if(i.target == Send){
            String msg = text.getText();
            String end = addr.getText();
            sock.sendDP(8080,msg,end);
            text.setText("");
            return true;
        }
        return false;
    }


}
