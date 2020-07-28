/**
 * Classe que permite a leitura constante por parte do servidor
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

public class serverReader extends Thread {

    /* Buffer de leitura */
    private BufferedReader sBr;

    /* Buffer de escrita */
    private PrintWriter pw;

    /**
     * Construtor parameterizado
     * @param sBr
     * @param pw
     */
    public serverReader(BufferedReader sBr, PrintWriter pw){
        this.sBr = sBr;
        this.pw = pw;
    }

    /**
     * Método Run
     */
    public void run(){
        try{
            while(true){
                String read;
                while((read = sBr.readLine()) != null){
                    if(read == null) break;
                    pw.println(read);
                    pw.flush();
                }
            }
        } catch (IOException e){}
    }


}
