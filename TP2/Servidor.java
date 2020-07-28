import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Implementação da classe Servidor.
 * Classe de Teste
 * Local da execução do sistema e criação dos sockets onde é estabelecida a conexão com os clientes
 * Fase I
 * Grupo 2
 * PL6
 * 2019/2020
 */

public class Servidor implements Serializable {
    private ServerSocket sSock;
    private int port;
    private static final long serialVersionUID = 1L;

    /**
     * cria servidor com a porta recebida como parâmetro
     * @param port
     */
    public Servidor(int port){
        this.port = port;
    }

    /**
     * inicia o servidor, permitindo o estabelecimento de conexões com os clientes
     */
    public void startServer(){
        try{
            this.sSock = new ServerSocket(this.port);

            while (true){
                System.out.println("ServerMain > Server is running waiting for a new connection");
                Socket socket = sSock.accept();
                System.out.println("ServerMain > Connection received: Create worker thread to handle connection");

                ServerHandler sw = new ServerHandler(socket);
                new Thread(sw).start();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * método main
     * @param args
     */
    public static void main(String[] args){
        Servidor s = new Servidor(81);
        s.startServer();
    }
}
