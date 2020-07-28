/**
 * Classe que recebe e envia informação por UDP para o Servidor
 * Fase II
 * Grupo 2
 * PL6
 *
 */

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;


public class UDPextraserverworker extends Thread {

    /* Socket para estabelecer ligação UDP */
    DatagramSocket serverSocket;

    /* Servidor de onde vai fazer o download */
    String serverTarget;

    /* Porta de ligação para o outro AnonGW */
    int port;

    /**
     * Construtor parameterizado
     * @param serverTarget
     * @param port
     */
    public UDPextraserverworker(String serverTarget, int port){
        this.serverTarget = serverTarget;
        this.port = port;
    }

    /**
     * Método Run
     */
    public void run(){
        byte[] buf = new byte[4215];
        byte[] bufS = new byte[4096];
        try{
            this.serverSocket = new DatagramSocket(this.port); /* Criação da conexão UDP para a porta this.port */
            DatagramPacket receivedPacket = new DatagramPacket(buf,buf.length);
            serverSocket.receive(receivedPacket); /* Recebe informação enviada por UDP */

            Pacote p = Pacote.converterParaPacote(receivedPacket.getData());

            if(p.getTipo() == 'T' && p.getAck() == 0){
                Socket targetSocket = new Socket(this.serverTarget,80); /* Criação da ligação com o servidor na porta 80 */
                while(true){
                    BufferedOutputStream os = new BufferedOutputStream(targetSocket.getOutputStream());
                    int n;
                    byte[] desencriptado;
                    byte[] buffer = new byte[4215];

                    try{
                        buffer = p.getDados();
                        if(buffer.length == 0) break;
                        byte[] separa = Arrays.copyOfRange(buffer,0,4);
                        int tamanho = ByteBuffer.wrap(separa).getInt();
                        byte[] correto = Arrays.copyOfRange(buffer,4,tamanho + 4);

                        desencriptado = AES.decrypt(correto); /* Desencriptação dos dados */
                        os.write(desencriptado);
                        os.flush(); /* Envio para o servidor */
                    } catch(Exception e){
                        System.out.println(e);
                    }
                    break;
                }
                while(true){
                    DataInputStream is = new DataInputStream(targetSocket.getInputStream()); /* Criação do canal de leitura do servidor */
                    int n2;
                    byte[] encriptado;
                    int nSeq = 0;

                    while((n2 = is.read(bufS)) > 0){
                        try{
                            byte[] aux = new byte[n2];
                            System.arraycopy(bufS,0,aux,0,n2); /* impede que envie sempre o mesmo package */
                            encriptado = AES.encrypt(aux); /* Encriptação dos dados */
                            byte[] size = ByteBuffer.allocate(4).putInt(encriptado.length).array();
                            byte[] correto = new byte[size.length + encriptado.length];
                            System.arraycopy(size,0,correto,0,size.length);
                            System.arraycopy(encriptado,0,correto,size.length,encriptado.length);
                            Pacote pEnviar = new Pacote(nSeq,correto,'T',1);
                            System.out.println("Envia pacote número:" + pEnviar.getNSeq());
                            byte[] bpEnviar = pEnviar.converterParaBytes();
                            DatagramPacket senderPacket = new DatagramPacket(bpEnviar,bpEnviar.length,receivedPacket.getAddress(),receivedPacket.getPort());
                            serverSocket.send(senderPacket); /* Envio da resposta do servidor UDP */
                            nSeq++;
                        } catch (Exception e){System.out.println(e);}
                    }
                    break;
                }
            }
        } catch(Exception e){}
    }
}
