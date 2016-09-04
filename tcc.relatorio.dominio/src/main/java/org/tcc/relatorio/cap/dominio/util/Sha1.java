/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.dominio.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author 140200
 */
public final class Sha1 {
    
    private static final int bitsToRotate   = 4;
    private static final int mash0F         = 0x0F;
    private static final int maxByte        = 10;
    private static final int minByte        = 9;
    //private static final int maxBuf         = 40;
    
    /**
     * Construtor private.
     */
    private Sha1() {
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> bitsToRotate) & mash0F;
            int twoHalfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= minByte)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - maxByte)));
                }
                halfbyte = data[i] & mash0F;
            } while (twoHalfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * Cria um hash para a string de entrada.
     * @param text Texto para o Hash    
     * @return String Texto do Hash.
     * @throws NoSuchAlgorithmException Algoritimo não disponivel.
     * @throws UnsupportedEncodingException Encode não suportado.
     */
    public static String digest(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        //byte[] sha1hash;// = new byte[maxBuf];

        md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
}
