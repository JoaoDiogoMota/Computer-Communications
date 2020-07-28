/**
 * Classe onde se encontram os métodos de encriptação dos dados dos pacotes
 * Fase II
 * Grupo 2
 * PL6
 * 2019/2020
 */

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    /**
     * Método de encriptação dos dados
     * @param Data - dados a encriptar
     * @return byte[] - dados encriptados
     * @throws Exception
     */
    public static byte[] encrypt(byte[] Data) throws Exception {

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data);

        return encVal;
    }

    /**
     * Método de desencriptação dos dados
     * @param encryptedData - dados encriptados
     * @return byte[] - dados desencriptados
     * @throws Exception
     */
    public static byte[] decrypt(byte[] encryptedData) throws Exception{

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decValue = c.doFinal(encryptedData);

        return decValue;
    }

    /**
     * Método que gera a chave de encriptação
     * @return Key - chave de encriptação
     * @throws Exception
     */
    private static Key generateKey() throws Exception{
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

}
