/**
 * Classe que permite a escrita constante por parte do servidor
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

public class serverWriter extends Thread{

    /* Buffer de leitura */
    private BufferedReader br;

    /* Buffer de escrita */
    private PrintWriter sPw;

    /**
     * Construtor parameterizado
     * @param sPw
     * @param br
     */
    public serverWriter(BufferedReader br, PrintWriter sPw){
        this.sPw = sPw;
        this.br = br;
    }

    /**
     * Método Run
     */
    public void run(){
        try{
            while(true){
                String read;
                read = br.readLine();
                if(read != null){
                    System.out.println("Me " + " >> " + read);
                    sPw.println(read);
                    sPw.flush();
                }
            }
        } catch (IOException e){}
    }
}
