package com.ridmi.smartcity360.stream;

/** The component contract shared by the core stream and all its decorators. */
public interface DataStream {
    byte[] encode(byte[] data);
    byte[] decode(byte[] data);
}
