package pt.ual.chat;


import java.awt.*;
import java.io.IOException;

public class chat3 extends Frame {
    private static chat3 app;
    TextArea ecran = new TextArea(10,30);
    TextField addr = new TextField(30);
    TextField text = new TextField(30);
    TextField code = new TextField(4);
    TextField myName = new TextField(30);
    Button Check = new Button("Check");
    Button Send = new Button("Send");
    socket sock;


    public chat3(String str){
        super(str);
    }

    public static void main(String[] args) throws IOException {
        app = new chat3("Chat 3");
        app.resize(700,290);
        app.GUI2();
        app.GUI();
        app.show();
//        app.StartSocket();
    }

    public void GUI(){
        setBackground(Color.lightGray);
        ecran.setEditable(false);
        GridBagLayout GBL = new GridBagLayout();
        setLayout(GBL);
        Panel P = new Panel();
        P.setLayout(new BorderLayout(5,5));
        P.add("North", text);
        P.add("West", addr);
        P.add("East", Send);
        P.add("South", ecran);
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
        P.add("North", myName);
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
            String myName = this.myName.getText();
            sock = new socket(ecran, pin, myName);
            app.StartSocket();
//            String msg = "pin " + code.getText();
//            String end = "127.0.0.1";
//            sock.sendDP(8081,msg,end);

            return true;
        }
        if(i.target == Send){
            String msg = text.getText();
            String[] endList = addr.getText().split(",");
//            int pr = Integer.parseInt(end);
            for(String end : endList) {
                sock.sendDP(end,msg);
            }

            text.setText("");
            return true;
        }
        return false;
    }


}
