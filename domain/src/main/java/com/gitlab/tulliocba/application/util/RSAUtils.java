package com.gitlab.tulliocba.application.util;

import lombok.extern.log4j.Log4j2;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static javax.crypto.Cipher.ENCRYPT_MODE;

@Log4j2
public class RSAUtils {

    private RSAUtils() {
    }

    public static String encrypt(String data, String base64PublicKey) throws InvalidKeySpecException {
        try {
            PublicKey publicKey = getPublicKey(base64PublicKey);
            Cipher cipher = Cipher.getInstance(Algorithm.RSA.name());
            cipher.init(ENCRYPT_MODE, publicKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            log.error(e);
        }
        return null;
    }

    public static String decrypt(String data, String base64PrivateKey) {
        try {
            Cipher cipher = Cipher.getInstance(Algorithm.RSA.name());
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(base64PrivateKey));
            return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes())));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            log.error(e);
        }
        return null;
    }

    private static PublicKey getPublicKey(String base64PublicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        X509EncodedKeySpec x509EncodedKeySpec = null;
        try {
            x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
        } catch (Exception e) {
            throw new InvalidKeySpecException(e);
        }
        return KeyFactory.getInstance(Algorithm.RSA.name()).generatePublic(x509EncodedKeySpec);
    }

    private static PrivateKey getPrivateKey(String base64PrivateKey) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec =
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
            return KeyFactory.getInstance(Algorithm.RSA.name()).generatePrivate(pkcs8EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e);
        }
        return null;
    }

    private enum Algorithm {
        RSA;
    }
}
