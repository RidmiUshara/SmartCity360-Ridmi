package com.ridmi.smartcity360.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/** GZIP compression layer. Short strings can become longer after compression. */
public final class CompressionDecorator extends StreamDecorator {
    public CompressionDecorator(DataStream wrappedStream) {
        super(wrappedStream);
    }

    @Override
    public byte[] encode(byte[] data) {
        byte[] payload = wrappedStream.encode(data);
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try (GZIPOutputStream gzip = new GZIPOutputStream(output)) {
                gzip.write(payload);
            }
            return output.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Could not compress meter data", e);
        }
    }

    @Override
    public byte[] decode(byte[] data) {
        try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(data))) {
            byte[] expanded = gzip.readAllBytes();
            return wrappedStream.decode(expanded);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid compressed meter data", e);
        }
    }
}
