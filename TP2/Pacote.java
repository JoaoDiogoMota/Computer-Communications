/**
 * Classe Protocolo PDU - Pacote
 * Fase II
 * Grupo 2
 * PL6
 * 2019/2020
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Pacote {
    /* Identifica o número do pacote a transferir */
    int nSeq;

    /* Dados transferidos */
    byte[] dados;

    /* S para estabelecer conexão, T para transferir */
    char tipo;

    /* S 0 - para pedir ligação
     * S 1 - para pedir porta
     * T 1 - ao transferir
     */
    int ack;

    /**
     * Construtor parameterizado
     * @param nSeq
     * @param dados
     * @param tipo
     * @param ack
     */
    public Pacote(int nSeq, byte[] dados, char tipo, int ack){
        this.nSeq = nSeq;
        this.dados = dados;
        this.tipo = tipo;
        this.ack = ack;
    }

    /**
     * Método que devolve os dados do pacote
     * @return
     */
    public byte[] getDados(){
        return this.dados;
    }

    /**
     * Método que devolve o tipo do pacote
     * @return
     */
    public char getTipo(){
        return this.tipo;
    }

    /**
     * Método que devolve o Ack do pacote
     * @return
     */
    public int getAck(){
        return this.ack;
    }

    /**
     * Método que devolve o número de sequência do pacote
     * @return
     */
    public int getNSeq(){
        return this.nSeq;
    }

    /**
     * Método que converte um pacote para um array de bytes
     * @return byte[] - pacote convertido em array de bytes
     * @throws IOException
     */
    public byte[] converterParaBytes() throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        oos.flush();
        return bos.toByteArray();
    }

    /**
     * Método que converte um array de bytes para um pacote
     * @param buf
     * @return Pacote
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Pacote converterParaPacote(byte[] buf) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(buf);
        ObjectInputStream oos = new ObjectInputStream(bis);
        Pacote pacote = (Pacote) oos.readObject();
        oos.close();
        return pacote;
    }
}
