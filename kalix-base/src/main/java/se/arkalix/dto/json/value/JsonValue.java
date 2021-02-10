package se.arkalix.dto.json.value;

import se.arkalix.dto.DtoReadException;
import se.arkalix.dto.binary.BinaryReader;
import se.arkalix.dto.json.JsonReadable;
import se.arkalix.dto.json.JsonWritable;
import se.arkalix.internal.dto.json.JsonTokenBuffer;
import se.arkalix.internal.dto.json.JsonTokenizer;
import se.arkalix.util.annotation.Internal;

import java.util.Optional;

/**
 * Any kind of JSON value.
 *
 * @see <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>
 */
public interface JsonValue extends JsonReadable, JsonWritable {
    JsonType type();

    /**
     * Attempts to use this JSON value into a Java boolean.
     * <p>
     * The attempt is successful only if the underlying JSON type is {@link
     * JsonBoolean}.
     *
     * @return This value as a Java boolean, if possible.
     */
    default Optional<Boolean> tryToBoolean() {
        return Optional.empty();
    }

    /**
     * Attempts to use this JSON value into a Java double.
     * <p>
     * The attempt is successful only if the underlying JSON type is {@link
     * JsonNumber}.
     *
     * @return This value as a Java double, if possible.
     */
    default Optional<Double> tryToDouble() {
        return Optional.empty();
    }

    /**
     * Attempts to use this JSON value into a Java long.
     * <p>
     * The attempt is successful only if the underlying JSON type is {@link
     * JsonNumber}.
     *
     * @return This value as a Java long, if possible.
     */
    default Optional<Long> tryToLong() {
        return Optional.empty();
    }

    /**
     * Attempts to use this JSON value into a Java String.
     * <p>
     * The attempt is successful only if the underlying JSON type is {@link
     * JsonString}.
     *
     * @return This value as a Java String, if possible.
     */
    default Optional<String> tryToString() {
        return Optional.empty();
    }

    /**
     * Reads JSON value from given {@code source}.
     *
     * @param source Source containing JSON value at the current read offset,
     *               ignoring any whitespace.
     * @return Decoded JSON value.
     * @throws DtoReadException If the source does not contain a valid JSON
     *                          value at the current read offset, or if the
     *                          source could not be read.
     */
    static JsonValue readJson(final BinaryReader source) throws DtoReadException {
        return readJson(JsonTokenizer.tokenize(source));
    }

    /**
     * <i>Internal API</i>. Might change in breaking ways between patch
     * versions of the Kalix library. Use is not advised.
     */
    @Internal
    static JsonValue readJson(final JsonTokenBuffer buffer) throws DtoReadException {
        var token = buffer.peek();
        switch (token.type()) {
        case OBJECT:
            return JsonObject.readJson(buffer);
        case ARRAY:
            return JsonArray.readJson(buffer);
        case STRING:
            return JsonString.readJson(buffer);
        case NUMBER:
            return JsonNumber.readJson(buffer);
        case TRUE:
        case FALSE:
            return JsonBoolean.readJson(buffer);
        case NULL:
            return JsonNull.readJson(buffer);
        default:
            throw new IllegalStateException("Illegal token type: " + token.type());
        }
    }
}
