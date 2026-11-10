package com.ridmi.smartcity360.stream;

import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class SecurityPipelineTest {
    @Test
    void allEightCombinationsRoundTrip() {
        SecurityPipeline pipeline = new SecurityPipeline();
        String text = "Meter 101: 250 kWh - ".repeat(20);
        for (boolean compress : new boolean[]{false, true}) {
            for (boolean encrypt : new boolean[]{false, true}) {
                for (boolean authenticate : new boolean[]{false, true}) {
                    SecurityPipeline.Result result = pipeline.demonstrate(text, compress, encrypt, authenticate);
                    assertEquals(text, result.decoded());
                    if (encrypt || authenticate) assertTrue(result.tamperRejected());
                }
            }
        }
    }

    @Test
    void compressionReducesHighlyRepetitiveText() {
        DataStream stream = new CompressionDecorator(new BasicDataStream());
        byte[] original = "Meter 101: 250 kWh ".repeat(100).getBytes(StandardCharsets.UTF_8);
        byte[] compressed = stream.encode(original);
        assertTrue(compressed.length < original.length);
        assertArrayEquals(original, stream.decode(compressed));
    }

    @Test
    void authenticationDetectsModifiedData() {
        DataStream stream = new AuthenticationDecorator(new BasicDataStream());
        byte[] bytes = stream.encode("meter data".getBytes(StandardCharsets.UTF_8));
        bytes[bytes.length - 1] ^= 1;
        assertThrows(IllegalArgumentException.class, () -> stream.decode(bytes));
    }

    @Test
    void aesEncryptionDetectsModifiedData() {
        DataStream stream = new EncryptionDecorator(new BasicDataStream());
        byte[] bytes = stream.encode("meter data".getBytes(StandardCharsets.UTF_8));
        bytes[bytes.length - 1] ^= 1;
        assertThrows(IllegalArgumentException.class, () -> stream.decode(bytes));
    }

    @Test
    void invalidInputIsRejected() {
        SecurityPipeline pipeline = new SecurityPipeline();
        assertThrows(IllegalArgumentException.class,
                () -> pipeline.demonstrate(" ", true, true, true));
        assertThrows(IllegalArgumentException.class,
                () -> pipeline.demonstrate("x".repeat(501), true, true, true));
    }
}
