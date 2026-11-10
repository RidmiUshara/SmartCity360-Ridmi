package com.ridmi.smartcity360.stream;

import java.util.Objects;

/** Concrete component: performs no transformation. */
public final class BasicDataStream implements DataStream {
    @Override
    public byte[] encode(byte[] data) {
        return Objects.requireNonNull(data).clone();
    }

    @Override
    public byte[] decode(byte[] data) {
        return Objects.requireNonNull(data).clone();
    }
}
