package se.arkalix.dto.json.value;

import se.arkalix.dto.DtoEncoding;
import se.arkalix.dto.DtoExclusive;
import se.arkalix.dto.DtoReadException;
import se.arkalix.dto.binary.BinaryReader;
import se.arkalix.dto.binary.BinaryWriter;
import se.arkalix.internal.dto.json.JsonTokenBuffer;
import se.arkalix.internal.dto.json.JsonTokenizer;
import se.arkalix.util.annotation.Internal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.Objects;

import static se.arkalix.dto.DtoEncoding.JSON;

/**
 * JSON number.
 *
 * @see <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>
 */
@DtoExclusive(JSON)
@SuppressWarnings("unused")
public class JsonNumber implements JsonValue {
    private final String number;

    private JsonNumber(final String number) {
        this.number = number;
    }

    /**
     * Creates new JSON number from given {@link BigDecimal}.
     *
     * @param number Number.
     */
    public JsonNumber(final BigDecimal number) {
        this.number = Objects.requireNonNull(number, "Expected number").toString();
    }

    /**
     * Creates new JSON number from given {@link BigInteger}.
     *
     * @param number Number.
     */
    public JsonNumber(final BigInteger number) {
        this.number = Objects.requireNonNull(number, "Expected number").toString();
    }

    /**
     * Creates new JSON number from given {@code byte}.
     *
     * @param number Number.
     */
    public JsonNumber(final byte number) {
        this.number = Byte.toString(number);
    }

    /**
     * Creates new JSON number from given {@code double}.
     *
     * @param number Number.
     */
    public JsonNumber(final double number) {
        this.number = Double.toString(number);
    }

    /**
     * Creates new JSON number from given {@link Duration}.
     *
     * @param number Number.
     */
    public JsonNumber(final Duration number) {
        Objects.requireNonNull(number, "Expected number");
        this.number = formatDecimal(number.getSeconds(), number.toNanosPart());
    }

    /**
     * Creates new JSON number from given {@code float}.
     *
     * @param number Number.
     */
    public JsonNumber(final float number) {
        this.number = Float.toString(number);
    }

    /**
     * Creates new JSON number from given {@code int}.
     *
     * @param number Number.
     */
    public JsonNumber(final int number) {
        this.number = Integer.toString(number);
    }

    /**
     * Creates new JSON number from given {@link Instant}.
     *
     * @param number Number.
     */
    public JsonNumber(final Instant number) {
        Objects.requireNonNull(number, "Expected number");
        this.number = formatDecimal(number.getEpochSecond(), number.getNano());
    }

    /**
     * Creates new JSON number from given {@code long}.
     *
     * @param number Number.
     */
    public JsonNumber(final long number) {
        this.number = Long.toString(number);
    }

    /**
     * Creates new JSON number from given {@link OffsetDateTime}.
     *
     * @param number Number.
     */
    public JsonNumber(final OffsetDateTime number) {
        final var instant = Objects.requireNonNull(number, "Expected number").toInstant();
        this.number = formatDecimal(instant.getEpochSecond(), instant.getNano());
    }

    /**
     * Creates new JSON number from given {@code short}.
     *
     * @param number Number.
     */
    public JsonNumber(final short number) {
        this.number = Short.toString(number);
    }

    private static String formatDecimal(final long seconds, final int nanos) {
        final var builder = new StringBuilder().append(seconds);
        if (nanos == 0) {
            return builder.toString();
        }
        builder.append('.');
        final var n = Integer.toString(nanos);
        var n1 = n.length();
        for (var padLeft = 9 - n1; padLeft-- != 0; ) {
            builder.append('0');
        }
        while (n1 > 0 && n.charAt(n1 - 1) == '0') {
            n1 -= 1;
        }
        for (var n0 = 0; n0 < n1; ++n0) {
            builder.append(n.charAt(n0));
        }
        return builder.toString();
    }

    @Override
    public JsonType type() {
        return JsonType.NUMBER;
    }

    /**
     * @return This number converted to a {@link BigDecimal}.
     */
    public BigDecimal toBigDecimal() {
        return new BigDecimal(number);
    }

    /**
     * @return This number converted to a {@link BigInteger}.
     * @throws NumberFormatException If this number contains decimals or uses
     *                               exponent notation.
     */
    public BigInteger toBigInteger() {
        return new BigInteger(number);
    }

    /**
     * @return This number converted to a {@code byte}.
     * @throws NumberFormatException If this number contains decimals, uses
     *                               exponent notation or is too large.
     */
    public byte toByte() {
        return Byte.parseByte(number);
    }

    /**
     * @return This number converted to a {@code double}.
     */
    public double toDouble() {
        return Double.parseDouble(number);
    }

    /**
     * @return This number converted to a {@link Duration}.
     * @throws ArithmeticException If this number is too large.
     */
    public Duration toDuration() {
        final var number0 = Double.parseDouble(number);
        final long integer = (long) number0;
        return Duration.ofSeconds(integer, (long) ((number0 - integer) * 1e9));
    }

    /**
     * @return This number converted to a {@code float}.
     */
    public float toFloat() {
        return Float.parseFloat(number);
    }

    /**
     * @return This number converted to a {@code int}.
     * @throws NumberFormatException If this number contains decimals, uses
     *                               exponent notation or is too large.
     */
    public int toInteger() {
        return Integer.parseInt(number);
    }

    /**
     * @return This number converted to a {@link Instant}.
     * @throws DateTimeException   If this number is too large to be represented
     *                             by an {@link Instant}.
     * @throws ArithmeticException If this number is too large.
     */
    public Instant toInstant() {
        final var number0 = Double.parseDouble(number);
        final long integer = (long) number0;
        return Instant.ofEpochSecond(integer, (long) ((number0 - integer) * 1e9));
    }

    /**
     * @return This number converted to a {@code long}.
     * @throws NumberFormatException If this number contains decimals, uses
     *                               exponent notation or is too large.
     */
    public long toLong() {
        return Long.parseLong(number);
    }

    /**
     * @return This number converted to an {@link OffsetDateTime}.
     * @throws DateTimeException   If this number is too large to be represented
     *                             by an {@link OffsetDateTime}.
     * @throws ArithmeticException If this number is too large.
     */
    public OffsetDateTime toOffsetDateTime() {
        return OffsetDateTime.ofInstant(toInstant(), ZoneId.systemDefault());
    }

    /**
     * @return This number converted to a {@code short}.
     * @throws NumberFormatException If this number contains decimals, uses
     *                               exponent notation or is too large.
     */
    public short toShort() {
        return Short.parseShort(number);
    }

    /**
     * Reads JSON number from given {@code source}.
     *
     * @param source Source containing JSON number at the current read offset,
     *               ignoring any whitespace.
     * @return Decoded JSON number.
     * @throws DtoReadException If the source does not contain a valid JSON
     *                          number at the current read offset, or if the
     *                          source could not be read.
     */
    public static JsonNumber readJson(final BinaryReader source) throws DtoReadException {
        return readJson(JsonTokenizer.tokenize(source));
    }

    /**
     * <i>Internal API</i>. Might change in breaking ways between patch
     * versions of the Kalix library. Use is not advised.
     */
    @Internal
    public static JsonNumber readJson(final JsonTokenBuffer buffer) throws DtoReadException {
        final var source = buffer.source();
        var token = buffer.next();
        if (token.type() != JsonType.NUMBER) {
            throw new DtoReadException(DtoEncoding.JSON, "Expected number",
                token.readStringRaw(source), token.begin());
        }
        return new JsonNumber(token.readStringRaw(source));
    }

    @Override
    public void writeJson(final BinaryWriter writer) {
        writer.write(number.getBytes(StandardCharsets.ISO_8859_1));
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) { return true; }
        if (other == null || getClass() != other.getClass()) { return false; }
        final JsonNumber that = (JsonNumber) other;
        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return number;
    }
}
