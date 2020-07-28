/**
 * Classe responsável por receber pacotes em TCP do cliente e enviá-los para outro AnonGW via UDP, e vice-versa
 * Fase II
 * Grupo 2
 * PL6
 * 2019/2020
 */

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class UDPclientworker extends Thread{

    /* Socket para estabelecer ligação UDP */
    DatagramSocket socket;

    /* Porta UDP onde se vai ligar */
    int port;

    /* Socket para estabelecer ligação TCP */
    private Socket clientSocket;

    /* IP do AnonGW */
    String peerIP;

    /* Porta para onde vai enviar os dados */
    int targetPort;

    /**
     * Construtor parameterizado
     * Cria um socket TCP com o AnonGW com o IP peerIP e inicializa a porta UDP a 6666.
     * @param clientSocket
     * @param peerIP
     */
    public UDPclientworker(Socket clientSocket, String peerIP){
        this.clientSocket = clientSocket;
        this.peerIP = peerIP;
        this.port = 6666;
    }

    /**
     * Método run
     */
    public void run(){
        byte[] buf = new byte[4096];
        try{
            DataInputStream clientInput = new DataInputStream(this.clientSocket.getInputStream()); /* Input do cliente */
            this.socket = new DatagramSocket();  /* Criação do socket UDP */
            InetAddress ip = InetAddress.getByName(this.peerIP);

            while(true){
                byte[] dados = "".getBytes();
                Pacote pc = new Pacote(0,dados,'S',0); /* Criação de um pacote para indicar que quer estabelecer ligação */
                byte[] packetToSend = pc.converterParaBytes();

                DatagramPacket firstPacket = new DatagramPacket(packetToSend, packetToSend.length, ip, this.port);
                socket.send(firstPacket); /* Envio para a porta 6666 o pedido para estabelecer ligação */

                DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
                socket.receive(receivedPacket); /* Receção de mensagem de resposta */
                byte[] connect = receivedPacket.getData();
                Pacote pc2 = Pacote.converterParaPacote(connect);

                if(pc2.getTipo() == 'S' && pc2.getAck() == 0){

                    Pacote pc3 = new Pacote(0,dados, 'S', 1); /* Pedido de porta para transferência de ficheiros */
                    byte[] packetToSend2 = pc3.converterParaBytes();

                    DatagramPacket firstPacket2 = new DatagramPacket(packetToSend2, packetToSend2.length, ip, this.port);
                    socket.send(firstPacket2);

                    byte[] portBytes = new byte[10];
                    DatagramPacket firstReceivedPacket = new DatagramPacket(buf, buf.length);
                    socket.receive(firstReceivedPacket);
                    portBytes = firstReceivedPacket.getData(); /* Receção de porta para transferência de ficheiros */

                    Pacote pcPorta = Pacote.converterParaPacote(portBytes);

                    if(pcPorta.getTipo() == 'S' && pcPorta.getAck() == 1){

                        String porta = new String(pcPorta.getDados(), 0, pcPorta.getDados().length);
                        this.targetPort = Integer.parseInt(porta);
                        break;
                    }
                }
            }

            /* Receção dos bytes do cliente por TCP e envio do pacote por UDP */
            int n;
            byte[] encriptado;
            while((n = clientInput.read(buf))>0){

                encriptado = AES.encrypt(buf); /* Encriptação dos bytes recebidos */
                byte[] size = ByteBuffer.allocate(4).putInt(encriptado.length).array();
                byte[] correto = new byte[size.length + encriptado.length];
                System.arraycopy(size, 0 ,correto, 0, size.length);
                System.arraycopy(encriptado, 0, correto, size.length, encriptado.length);
                Pacote pEnviar = new Pacote(-1,correto,'T',0);
                byte[] bpEnviar = pEnviar.converterParaBytes();

                DatagramPacket packet = new DatagramPacket(bpEnviar,bpEnviar.length, ip, this.targetPort);
                socket.send(packet); /* Envio do pacote por UDP para o próximo AnonGW */

                break;
            }

            BufferedOutputStream os = new BufferedOutputStream(this.clientSocket.getOutputStream()); /* extremo para enviar para o cliente */

            while(true){
                int n3;
                byte[] buf2 = new byte[4215];
                DatagramPacket receivedContentPacket = new DatagramPacket(buf2,buf2.length);
                socket.receive(receivedContentPacket); /* Receção do conteúdo do outro AnonGW */

                int n2;
                byte[] desencriptado;

                byte[] buf3 = new byte[4215];

                buf3 = receivedContentPacket.getData();
                if(buf3.length == 0) break;
                Pacote pRecebe = Pacote.converterParaPacote(buf3);
                if(pRecebe.getTipo() == 'T' && pRecebe.getAck() == 1){
                    byte[] dados = pRecebe.getDados();
                    byte[] separa = Arrays.copyOfRange(dados,0,4);
                    int tamanho = ByteBuffer.wrap(separa).getInt();
                    System.out.println("Cliente recebe pacote número: " +pRecebe.getNSeq());
                    byte[] conteudo = Arrays.copyOfRange(dados,4,tamanho + 4);
                    desencriptado = AES.decrypt(conteudo); /* Desencriptação dos dados */
                    os.write(desencriptado); /* Envio para o cliente por TCP */
                    os.flush();
                }

            }
        } catch(Exception e){}
    }

}
