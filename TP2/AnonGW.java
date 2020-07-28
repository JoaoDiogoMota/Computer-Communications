/**
 * Classe responsável por criar uma thread para o servidor e uma thread para cada cliente
 * Fase II
 * Grupo 2
 * PL6
 * 2019/2020
 */

import java.io.IOException;
import java.net.*;
import java.net.Socket;
import java.util.*;

public class AnonGW {

    /* porta TCP */
    private static final int PORT = 80;

    /* porta UDP */
    private static final int PORTUDP = 6666;

    /* Lista de gateways de transporte */
    private static List<String> peersIPs;

    /* IP do servidor */
    private static String serverIP;

    /**
     * Construtor parameterizado
     * @param peersIPs
     * @param serverIP
     */
    public AnonGW(List<String> peersIPs, String serverIP){
        this.peersIPs = peersIPs;
        this.serverIP = serverIP;
    }

    /**
     * Método Main
     */
    public static void main(String[] args) throws IOException{
        String ips = args[1];
        int port = Integer.parseInt(args[3]);

        List<String> peersIPs = new ArrayList<String>();
        for(int i = 5; i<args.length; i++){
            peersIPs.add(args[i]);
        }

        System.out.println("Target ip = " + ips);
        System.out.println("TargetServer port = " + port);
        System.out.println("Peers ips = " + peersIPs);

        ServerSocket ss = new ServerSocket(PORT);
        DatagramSocket ds = new DatagramSocket(PORTUDP);

        /* Criação de uma thread para tratar da componente que liga ao servidor */
        new Thread(new UDPserverworker(ds,ips)).start();
        while(true){
            /* Criação de threads para lidar com os clientes do AnonGW */
            Socket usableClient = ss.accept();
            Random rand = new Random();
            int randomNum = rand.nextInt(((peersIPs.size()-1)-0)+1)+0;
            String ip = peersIPs.get(randomNum);
            new Thread(new UDPclientworker(usableClient,ip)).start();
        }

    }
}
