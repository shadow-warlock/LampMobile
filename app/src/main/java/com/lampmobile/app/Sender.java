package com.lampmobile.app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender {
    final static String host = "192.168.4.1";
    final static int port = 5060;
    public static void sendMess512(byte[] mas){
        try{

            InetAddress address = InetAddress.getByName(host);
            DatagramPacket pack = new DatagramPacket(mas, mas.length, address, port);
            DatagramSocket ds = new DatagramSocket();
            ds.send(pack);
            ds.close();
        }
        catch (IOException ex){
            System.err.println(ex.getMessage());
        }
    }
}
