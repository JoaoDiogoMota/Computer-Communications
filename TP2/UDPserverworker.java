/**
 * Classe responsável por receber pedidos de conexão do outro AnonGW
 * Fase II
 * Grupo 2
 * PL6
 * 2019/2020
 */

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;

public class UDPserverworker extends Thread {

    /* Socket para estabelecer ligação UDP */
    DatagramSocket serverSocket;

    /* Servidor de onde vai fazer o download */
    String serverTarget;

    /* Flag que indica se a porta 6666 está ocupada */
    int working;

    /* Número de conexões realizadas */
    int nConnections;

    /**
     * Construtor parameterizado
     * @param socket
     * @param serverIP
     */
    public UDPserverworker(DatagramSocket socket, String serverIP){
        this.serverSocket = socket;
        this.working = 0;
        this.serverTarget = serverIP;
        this.nConnections = 0;
    }

    /**
     * Método run
     */
    public void run(){
        byte[] receiver = new byte[4215];
        while(true){
            try{
                while(this.working != 0){ /* Verifica se o servidor está ocupado na porta 6666 */
                    try{
                        wait();
                    } catch(Exception e){}
                }
                working ++;
                try{
                    DatagramPacket packet = new DatagramPacket(receiver, receiver.length);
                    serverSocket.receive(packet); /* Receção do pedido de conexão do outro AnonGW */
                    Pacote p = Pacote.converterParaPacote(packet.getData());
                    if(p.getTipo() == 'S'  && p.getAck() == 0);{
                        Pacote p2 = new Pacote(0,"".getBytes(), 'S',0);
                        byte[] bp2 = p2.converterParaBytes();
                        DatagramPacket packet2 = new DatagramPacket(bp2, bp2.length, packet.getAddress(),packet.getPort());
                        serverSocket.send(packet2); /* Envia resposta ao pedido de ligação */
                        working--; /* O servidor está livre na porta 6666 */
                    }
                        if(p.getTipo() == 'S' && p.getAck() == 1){
                            int newPort = 6667 + this.nConnections;
                            byte[] novaPort = (String.valueOf(newPort)).getBytes();
                            new Thread(new UDPextraserverworker(this.serverTarget,newPort)).start(); /* cria uma nova thread para comunicação entre o AnonGW e o servidor */
                            this.nConnections++;
                            working--; /* O servidor está livre na porta 6666 */
                            Pacote p3 = new Pacote(0,novaPort,'S',1);
                            byte[] bp3 = p3.converterParaBytes();
                            DatagramPacket packet3 = new DatagramPacket(bp3, bp3.length, packet.getAddress(),packet.getPort());
                            serverSocket.send(packet3); /* Envia para o AnonGW a nova porta atribuída */
                            notifyAll();
                        }
                } catch (Exception e){}
            } catch (Exception e){}
        }
    }
}
