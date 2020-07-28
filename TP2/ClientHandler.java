/**
 * Classe para a thread por parte do cliente
 * Fase I
 * Grupo 2
 * PL6
 * 2019/2020
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

public class ClientHandler extends Thread{

    /* Socket para o servidor */
    private Socket cs;

    /* IP do cliente */
    private String ip;

    /**
     * Construtor parameterizado
     * @param cs
     */
    public ClientHandler(Socket cs){
        this.cs = cs;
        this.ip = cs.getInetAddress().getHostAddress();
        System.err.println(ip + " connected!");
    }

    @Override
    public void run(){
        try{
            Socket newSocket = new Socket("10.3.3.1",80);

            /** usado para escrever */
            PrintWriter pw = new PrintWriter(cs.getOutputStream());
            /** usado para ler */
            BufferedReader br = new BufferedReader(new InputStreamReader((cs.getInputStream())));

            /** Escrever para o servidor */
            PrintWriter sPw = new PrintWriter(newSocket.getOutputStream());
            /** usado para ler do servidor */
            BufferedReader sBr = new BufferedReader((new InputStreamReader(newSocket.getInputStream())));

            new serverWriter(br,sPw).start();
            new serverReader(sBr,pw).start();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
