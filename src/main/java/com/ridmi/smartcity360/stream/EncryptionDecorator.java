package com.ridmi.smartcity360.stream;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/** AES-256-GCM layer. Demo keys are generated per instance and are not persisted. */
public final class EncryptionDecorator extends StreamDecorator {
    private static final int NONCE_BYTES = 12;
    private static final int TAG_BITS = 128;
    private final SecretKey key;
    private final SecureRandom random = new SecureRandom();

    public EncryptionDecorator(DataStream wrappedStream) {
        super(wrappedStream);
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256, random);
            this.key = generator.generateKey();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("AES encryption is unavailable", e);
        }
    }

    @Override
    public byte[] encode(byte[] data) {
        byte[] payload = wrappedStream.encode(data);
        byte[] nonce = new byte[NONCE_BYTES];
        random.nextBytes(nonce);
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, nonce));
            byte[] ciphertext = cipher.doFinal(payload);
            byte[] result = Arrays.copyOf(nonce, nonce.length + ciphertext.length);
            System.arraycopy(ciphertext, 0, result, nonce.length, ciphertext.length);
            return result;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Encryption failed", e);
        }
    }

    @Override
    public byte[] decode(byte[] data) {
        if (data == null || data.length < NONCE_BYTES + TAG_BITS / 8) {
            throw new IllegalArgumentException("Invalid encrypted meter data");
        }
        byte[] nonce = Arrays.copyOfRange(data, 0, NONCE_BYTES);
        byte[] ciphertext = Arrays.copyOfRange(data, NONCE_BYTES, data.length);
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, nonce));
            return wrappedStream.decode(cipher.doFinal(ciphertext));
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Encrypted meter data failed verification", e);
        }
    }
}
