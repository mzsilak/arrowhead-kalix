package se.arkalix.internal.security.identity;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TestX509Keys {
    private static final byte[] PUBLIC_KEY_EC = new byte[]{
        (byte) 0x30, (byte) 0x49, (byte) 0x30, (byte) 0x13, (byte) 0x06,
        (byte) 0x07, (byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0xCE,
        (byte) 0x3D, (byte) 0x02, (byte) 0x01, (byte) 0x06, (byte) 0x08,
        (byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0xCE, (byte) 0x3D,
        (byte) 0x03, (byte) 0x00, (byte) 0x05, (byte) 0x03, (byte) 0x32,
        (byte) 0x00, (byte) 0x04, (byte) 0x4F, (byte) 0xAC, (byte) 0xF2,
        (byte) 0x9D, (byte) 0x3E, (byte) 0x53, (byte) 0xB9, (byte) 0xAA,
        (byte) 0x1D, (byte) 0x14, (byte) 0x77, (byte) 0xC3, (byte) 0x3C,
        (byte) 0x84, (byte) 0x90, (byte) 0x10, (byte) 0x80, (byte) 0x2D,
        (byte) 0x90, (byte) 0xDF, (byte) 0xC5, (byte) 0x37, (byte) 0xAD,
        (byte) 0xDD, (byte) 0x07, (byte) 0x98, (byte) 0x53, (byte) 0x9F,
        (byte) 0x56, (byte) 0x3F, (byte) 0x85, (byte) 0x04, (byte) 0x04,
        (byte) 0xAB, (byte) 0xD4, (byte) 0xB4, (byte) 0x2A, (byte) 0xFF,
        (byte) 0xA7, (byte) 0xF7, (byte) 0x2A, (byte) 0x42, (byte) 0x63,
        (byte) 0x77, (byte) 0x35, (byte) 0x86, (byte) 0xF5, (byte) 0x11};

    private static final byte[] PUBLIC_KEY_RSA = new byte[]{
        (byte) 0x30, (byte) 0x5C, (byte) 0x30, (byte) 0x0D, (byte) 0x06,
        (byte) 0x09, (byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86,
        (byte) 0xF7, (byte) 0x0D, (byte) 0x01, (byte) 0x01, (byte) 0x01,
        (byte) 0x05, (byte) 0x00, (byte) 0x03, (byte) 0x4B, (byte) 0x00,
        (byte) 0x30, (byte) 0x48, (byte) 0x02, (byte) 0x41, (byte) 0x00,
        (byte) 0xFA, (byte) 0x5E, (byte) 0x09, (byte) 0xBD, (byte) 0xB1,
        (byte) 0xD2, (byte) 0xC6, (byte) 0xCC, (byte) 0xCB, (byte) 0x09,
        (byte) 0xED, (byte) 0xAB, (byte) 0xC2, (byte) 0x5A, (byte) 0x37,
        (byte) 0x49, (byte) 0x2E, (byte) 0x05, (byte) 0x93, (byte) 0x59,
        (byte) 0x95, (byte) 0x7B, (byte) 0x80, (byte) 0x73, (byte) 0xAC,
        (byte) 0xFF, (byte) 0x59, (byte) 0x84, (byte) 0x84, (byte) 0xCD,
        (byte) 0x3C, (byte) 0x4D, (byte) 0x51, (byte) 0x7E, (byte) 0x87,
        (byte) 0xC1, (byte) 0xE2, (byte) 0xCA, (byte) 0x6E, (byte) 0x0C,
        (byte) 0xC3, (byte) 0xAC, (byte) 0x6E, (byte) 0xC3, (byte) 0x09,
        (byte) 0xC9, (byte) 0x07, (byte) 0x0A, (byte) 0x40, (byte) 0x2A,
        (byte) 0x68, (byte) 0x3E, (byte) 0x39, (byte) 0x1A, (byte) 0xF0,
        (byte) 0x8A, (byte) 0xF0, (byte) 0x7C, (byte) 0x73, (byte) 0x9C,
        (byte) 0x38, (byte) 0x78, (byte) 0x90, (byte) 0x0D, (byte) 0x02,
        (byte) 0x03, (byte) 0x01, (byte) 0x00, (byte) 0x01};

    private static final byte[] PUBLIC_KEY_DSA = new byte[]{
        (byte) 0x30, (byte) 0x81, (byte) 0xF1, (byte) 0x30, (byte) 0x81,
        (byte) 0xA8, (byte) 0x06, (byte) 0x07, (byte) 0x2A, (byte) 0x86,
        (byte) 0x48, (byte) 0xCE, (byte) 0x38, (byte) 0x04, (byte) 0x01,
        (byte) 0x30, (byte) 0x81, (byte) 0x9C, (byte) 0x02, (byte) 0x41,
        (byte) 0x00, (byte) 0xFC, (byte) 0xA6, (byte) 0x82, (byte) 0xCE,
        (byte) 0x8E, (byte) 0x12, (byte) 0xCA, (byte) 0xBA, (byte) 0x26,
        (byte) 0xEF, (byte) 0xCC, (byte) 0xF7, (byte) 0x11, (byte) 0x0E,
        (byte) 0x52, (byte) 0x6D, (byte) 0xB0, (byte) 0x78, (byte) 0xB0,
        (byte) 0x5E, (byte) 0xDE, (byte) 0xCB, (byte) 0xCD, (byte) 0x1E,
        (byte) 0xB4, (byte) 0xA2, (byte) 0x08, (byte) 0xF3, (byte) 0xAE,
        (byte) 0x16, (byte) 0x17, (byte) 0xAE, (byte) 0x01, (byte) 0xF3,
        (byte) 0x5B, (byte) 0x91, (byte) 0xA4, (byte) 0x7E, (byte) 0x6D,
        (byte) 0xF6, (byte) 0x34, (byte) 0x13, (byte) 0xC5, (byte) 0xE1,
        (byte) 0x2E, (byte) 0xD0, (byte) 0x89, (byte) 0x9B, (byte) 0xCD,
        (byte) 0x13, (byte) 0x2A, (byte) 0xCD, (byte) 0x50, (byte) 0xD9,
        (byte) 0x91, (byte) 0x51, (byte) 0xBD, (byte) 0xC4, (byte) 0x3E,
        (byte) 0xE7, (byte) 0x37, (byte) 0x59, (byte) 0x2E, (byte) 0x17,
        (byte) 0x02, (byte) 0x15, (byte) 0x00, (byte) 0x96, (byte) 0x2E,
        (byte) 0xDD, (byte) 0xCC, (byte) 0x36, (byte) 0x9C, (byte) 0xBA,
        (byte) 0x8E, (byte) 0xBB, (byte) 0x26, (byte) 0x0E, (byte) 0xE6,
        (byte) 0xB6, (byte) 0xA1, (byte) 0x26, (byte) 0xD9, (byte) 0x34,
        (byte) 0x6E, (byte) 0x38, (byte) 0xC5, (byte) 0x02, (byte) 0x40,
        (byte) 0x67, (byte) 0x84, (byte) 0x71, (byte) 0xB2, (byte) 0x7A,
        (byte) 0x9C, (byte) 0xF4, (byte) 0x4E, (byte) 0xE9, (byte) 0x1A,
        (byte) 0x49, (byte) 0xC5, (byte) 0x14, (byte) 0x7D, (byte) 0xB1,
        (byte) 0xA9, (byte) 0xAA, (byte) 0xF2, (byte) 0x44, (byte) 0xF0,
        (byte) 0x5A, (byte) 0x43, (byte) 0x4D, (byte) 0x64, (byte) 0x86,
        (byte) 0x93, (byte) 0x1D, (byte) 0x2D, (byte) 0x14, (byte) 0x27,
        (byte) 0x1B, (byte) 0x9E, (byte) 0x35, (byte) 0x03, (byte) 0x0B,
        (byte) 0x71, (byte) 0xFD, (byte) 0x73, (byte) 0xDA, (byte) 0x17,
        (byte) 0x90, (byte) 0x69, (byte) 0xB3, (byte) 0x2E, (byte) 0x29,
        (byte) 0x35, (byte) 0x63, (byte) 0x0E, (byte) 0x1C, (byte) 0x20,
        (byte) 0x62, (byte) 0x35, (byte) 0x4D, (byte) 0x0D, (byte) 0xA2,
        (byte) 0x0A, (byte) 0x6C, (byte) 0x41, (byte) 0x6E, (byte) 0x50,
        (byte) 0xBE, (byte) 0x79, (byte) 0x4C, (byte) 0xA4, (byte) 0x03,
        (byte) 0x44, (byte) 0x00, (byte) 0x02, (byte) 0x41, (byte) 0x00,
        (byte) 0xA3, (byte) 0x5A, (byte) 0xC8, (byte) 0x68, (byte) 0x52,
        (byte) 0x87, (byte) 0x18, (byte) 0xC7, (byte) 0x1F, (byte) 0x2D,
        (byte) 0x69, (byte) 0x12, (byte) 0x99, (byte) 0x82, (byte) 0x73,
        (byte) 0x09, (byte) 0x53, (byte) 0x12, (byte) 0x46, (byte) 0xED,
        (byte) 0x89, (byte) 0x19, (byte) 0xFF, (byte) 0x10, (byte) 0x2F,
        (byte) 0xD4, (byte) 0x9A, (byte) 0x7C, (byte) 0x9A, (byte) 0xE0,
        (byte) 0xCD, (byte) 0x82, (byte) 0xEA, (byte) 0xFE, (byte) 0xB3,
        (byte) 0xAD, (byte) 0xC8, (byte) 0xAB, (byte) 0x24, (byte) 0x44,
        (byte) 0x3F, (byte) 0xB1, (byte) 0x43, (byte) 0x8B, (byte) 0xD3,
        (byte) 0xEE, (byte) 0x65, (byte) 0x43, (byte) 0x65, (byte) 0x8D,
        (byte) 0x19, (byte) 0x67, (byte) 0x04, (byte) 0x61, (byte) 0x03,
        (byte) 0x3E, (byte) 0xA2, (byte) 0x2E, (byte) 0x49, (byte) 0x32,
        (byte) 0x94, (byte) 0xFD, (byte) 0x5E, (byte) 0x90};

    @ParameterizedTest
    @MethodSource("publicKeysAndAlgorithmNames")
    void shouldIdentifyPublicKeyAlgorithm(final byte[] publicKeyDer, final String expected) {
        final var actual = X509Keys.identifyPublicKeyAlgorithm(publicKeyDer);
        assertEquals(expected, actual.orElse(""));
    }

    static Stream<Arguments> publicKeysAndAlgorithmNames() {
        return Stream.of(
            arguments(PUBLIC_KEY_EC, "EC"),
            arguments(PUBLIC_KEY_RSA, "RSA"),
            arguments(PUBLIC_KEY_DSA, "DSA")
        );
    }
}
