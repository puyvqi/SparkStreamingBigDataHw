package Steamer;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] agrs){
        Socket socket=null;
        ServerSocket sv=null;
        try{
            sv=new ServerSocket(9999);



        }catch(Exception e){e.printStackTrace();}
        while(true){ try{
            socket=sv.accept();
            Writer wr=new PrintWriter(socket.getOutputStream());
            System.out.println("success!");
            while(true){
                Thread.sleep(1000);
                wr.write("2017-6-17 15:00:00,1,10,00,00,60,120,350\n");
                wr.write("2017-6-17 15:00:00,2,10,00,80,0,120,50\n");
                wr.write("2017-6-17 15:00:00,3,50,00,800,60,200,350\n");
                wr.write("2017-6-17 15:00:00,4,10,00,80,0,130,350\n");
                wr.write("2017-6-17 15:00:00,5,50,00,80,6,120,50\n");
                wr.write("2017-6-17 15:00:00,6,15,00,800,6,200,30\n");
                wr.write("2017-6-17 15:00:00,7,15,00,80,60,120,30\n");
                wr.write("2017-6-17 15:00:00,8,10,30,00,6,200,350\n");
                wr.write("2017-6-17 15:00:00,9,10,30,80,0,120,30\n");
                wr.write("2017-6-17 15:00:00,10,10,30,80,6,1300,35\n");
                wr.flush();
                System.out.println("xie na le");
            }

        }
        catch(Exception e){e .printStackTrace();}
        }
    }
}
