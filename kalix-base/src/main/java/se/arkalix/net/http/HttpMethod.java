package se.arkalix.net.http;

import java.util.Objects;

/**
 * HTTP request methods.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-4">RFC 7231, Section 4</a>
 * @see <a href="https://tools.ietf.org/html/rfc5789">RFC 5789</a>
 */
@SuppressWarnings("unused")
public final class HttpMethod implements Comparable<HttpMethod> {
    private final String name;
    private final boolean isStandard;

    private HttpMethod(final String name, final boolean isStandard) {
        this.name = name;
        this.isStandard = isStandard;
    }

    /**
     * @return Method name.
     */
    public String name() {
        return name;
    }

    /**
     * @return Whether or not this method is "safe" as defined in RFC 7231,
     * Section 4.2.1.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.2.1">RFC 7231, Section 4.2.1</a>
     */
    public boolean isSafe() {
        return this == GET || this == HEAD || this == OPTIONS || this == TRACE;
    }

    /**
     * @return Whether or not this method is "idempotent" as defined in RFC
     * 7231, Section 4.2.2.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.2.2">RFC 7231, Section 4.2.2</a>
     */
    public boolean isIdempotent() {
        return this == PUT || this == DELETE || isSafe();
    }

    /**
     * @return Whether or not this method is part of either RFC 7231, Section 4
     * or RFC 5789.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4">RFC 7231, Section 4</a>
     * @see <a href="https://tools.ietf.org/html/rfc5789">RFC 5789</a>
     */
    public boolean isStandard() {
        return isStandard;
    }

    /**
     * Method {@code GET}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.1">RFC 7231, Section 4.3.1</a>
     */
    public static final HttpMethod GET = new HttpMethod("GET", true);

    /**
     * Method {@code POST}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.3">RFC 7231, Section 4.3.3</a>
     */
    public static final HttpMethod POST = new HttpMethod("POST", true);

    /**
     * Method {@code PUT}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.4">RFC 7231, Section 4.3.4</a>
     */
    public static final HttpMethod PUT = new HttpMethod("PUT", true);

    /**
     * Method {@code DELETE}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.5">RFC 7231, Section 4.3.5</a>
     */
    public static final HttpMethod DELETE = new HttpMethod("DELETE", true);

    /**
     * Method {@code HEAD}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.2">RFC 7231, Section 4.3.2</a>
     */
    public static final HttpMethod HEAD = new HttpMethod("HEAD", true);

    /**
     * Method {@code OPTIONS}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.7">RFC 7231, Section 4.3.7</a>
     */
    public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS", true);

    /**
     * Method {@code CONNECT}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.6">RFC 7231, Section 4.3.6</a>
     */
    public static final HttpMethod CONNECT = new HttpMethod("CONNECT", true);

    /**
     * Method {@code PATCH}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc5789">RFC 5789</a>
     */
    public static final HttpMethod PATCH = new HttpMethod("PATCH", true);

    /**
     * Method {@code TRACE}.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.8">RFC 7231, Section 4.3.8</a>
     */
    public static final HttpMethod TRACE = new HttpMethod("TRACE", true);

    /**
     * Resolves {@link HttpMethod} from given status code.
     * <p>
     * If given {@code name} is a standardized method, a cached
     * {@link HttpMethod} is returned. Otherwise, a new instance is provided.
     *
     * @param name Name to resolve. Case sensitive, as required by RFC 7230,
     *             Section 3.1.1.
     * @return Cached or new {@link HttpMethod}.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-3.1.1">RFC 7230, Section 3.1.1</a>
     */
    public static HttpMethod valueOf(final String name) {
        switch (name) {
        case "GET": return GET;
        case "POST": return POST;
        case "PUT": return PUT;
        case "DELETE": return DELETE;
        case "HEAD": return HEAD;
        case "OPTIONS": return OPTIONS;
        case "CONNECT": return CONNECT;
        case "PATCH": return PATCH;
        case "TRACE": return TRACE;
        }
        return new HttpMethod(name, false);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        HttpMethod that = (HttpMethod) o;
        if (that.name == null) { return false; }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(final HttpMethod other) {
        return name.compareTo(other.name);
    }
}
