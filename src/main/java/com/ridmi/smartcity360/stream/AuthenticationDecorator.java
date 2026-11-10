package com.ridmi.smartcity360.stream;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/** Optional HMAC-SHA256 authentication/integrity layer (demo only). */
public final class AuthenticationDecorator extends StreamDecorator {
    private static final int MAC_BYTES = 32;
    private final SecretKeySpec key;

    public AuthenticationDecorator(DataStream wrappedStream) {
        super(wrappedStream);
        byte[] secret = new byte[MAC_BYTES];
        new SecureRandom().nextBytes(secret);
        this.key = new SecretKeySpec(secret, "HmacSHA256");
    }

    @Override
    public byte[] encode(byte[] data) {
        byte[] payload = wrappedStream.encode(data);
        byte[] tag = mac(payload);
        byte[] result = Arrays.copyOf(tag, tag.length + payload.length);
        System.arraycopy(payload, 0, result, tag.length, payload.length);
        return result;
    }

    @Override
    public byte[] decode(byte[] data) {
        if (data == null || data.length < MAC_BYTES) {
            throw new IllegalArgumentException("Missing authentication tag");
        }
        byte[] receivedTag = Arrays.copyOfRange(data, 0, MAC_BYTES);
        byte[] payload = Arrays.copyOfRange(data, MAC_BYTES, data.length);
        if (!MessageDigest.isEqual(receivedTag, mac(payload))) {
            throw new IllegalArgumentException("Authentication failed: payload was changed");
        }
        return wrappedStream.decode(payload);
    }

    private byte[] mac(byte[] payload) {
        try {
            Mac signer = Mac.getInstance("HmacSHA256");
            signer.init(key);
            return signer.doFinal(payload);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Authentication unavailable", e);
        }
    }
}
