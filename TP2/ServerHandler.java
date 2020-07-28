/**
 * Classe para a thread por parte do servidor
 * Fase I
 * Grupo 2
 * PL6
 * 2019/2020
 */

import java.io.*;
import java.net.Socket;

public class ServerHandler implements Runnable{

    /* Socket para o cliente */
    private Socket socket;

    /**
     * Construtor parameterizado
     * @param socket
     */
    public ServerHandler(Socket socket){
        this.socket=socket;
    }

    /**
     * Método run
     */
    public void run(){
        try{
            /* criação dos canais de leitura e escrita no socket */
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            String text = in.readLine();
            out.println(text);
            out.flush();
            out.close();
        } catch (IOException e){}
    }
}
