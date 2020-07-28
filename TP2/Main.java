/**
 * Classe Principal
 * Grupo 2
 * PL6
 * 2019/2020
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    //                0          1       2   3           4          5         6        7
    // anonGW  target-server  10.3.3.1  port 80  overlay-peers  10.1.1.2  10.4.4.2  10.4.4.3
    public static void main(String[] args) {

        String ip = args[1];
        int port = Integer.parseInt(args[3]);

        List<String> peersIPs = new ArrayList<String>();
        for(int i=5; i<args.length; i++){
            peersIPs.add(args[i]);
        }

        System.out.println("Server IP = " + ip);
        System.out.println("Server Port = " + port);
        System.out.println("Server IPs = " + peersIPs);

        new AnonGW(peersIPs,ip);

    }
}
