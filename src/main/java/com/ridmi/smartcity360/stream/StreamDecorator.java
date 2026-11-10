package com.ridmi.smartcity360.stream;

import java.util.Objects;

/** Base decorator: wraps another DataStream without changing its public contract. */
public abstract class StreamDecorator implements DataStream {
    protected final DataStream wrappedStream;

    protected StreamDecorator(DataStream wrappedStream) {
        this.wrappedStream = Objects.requireNonNull(wrappedStream);
    }

    @Override
    public byte[] encode(byte[] data) {
        return wrappedStream.encode(data);
    }

    @Override
    public byte[] decode(byte[] data) {
        return wrappedStream.decode(data);
    }
}
