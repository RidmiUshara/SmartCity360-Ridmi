package com.ridmi.smartcity360.stream;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Assembles independent decorators around the basic data stream. */
public final class SecurityPipeline {

    public DataStream build(boolean compress, boolean encrypt, boolean authenticate) {
        DataStream stream = new BasicDataStream();
        // Wrapping from inside out gives physical order: GZIP -> AES-GCM -> HMAC.
        if (compress) stream = new CompressionDecorator(stream);
        if (encrypt) stream = new EncryptionDecorator(stream);
        if (authenticate) stream = new AuthenticationDecorator(stream);
        return stream;
    }

    public Result demonstrate(String reading, boolean compress,
                              boolean encrypt, boolean authenticate) {
        if (reading == null || reading.isBlank() || reading.length() > 500) {
            throw new IllegalArgumentException("Enter a meter reading of 1–500 characters.");
        }
        DataStream pipeline = build(compress, encrypt, authenticate);
        byte[] original = reading.getBytes(StandardCharsets.UTF_8);
        byte[] encoded = pipeline.encode(original);
        String restored = new String(pipeline.decode(encoded), StandardCharsets.UTF_8);

        boolean tamperRejected = false;
        if (authenticate || encrypt) {
            byte[] tampered = encoded.clone();
            tampered[tampered.length - 1] ^= 1;
            try {
                pipeline.decode(tampered);
            } catch (IllegalArgumentException e) {
                tamperRejected = true;
            }
        }
        return new Result(Base64.getEncoder().encodeToString(encoded), restored,
                original.length, encoded.length, tamperRejected);
    }

    public record Result(String encodedBase64, String decoded, int inputBytes,
                         int outputBytes, boolean tamperRejected) { }
}
