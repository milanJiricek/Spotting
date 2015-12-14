package app.majlant.spotting.managers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by majlant on 15.11.15.
 *
 * @author Milan Jiricek <milan.jiricekpv@gmail.com></milan.jiricekpv@gmail.com>
 */
public class HashManager {
    private static final String TAG = "HashManager";

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("UTF-8"), 0, text.getBytes("UTF-8").length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
}
