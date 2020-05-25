package se.arkalix.net.http.client;

import se.arkalix.dto.DtoEncoding;
import se.arkalix.dto.DtoWritable;
import se.arkalix.net.http.HttpBodySender;
import se.arkalix.net.http.HttpHeaders;
import se.arkalix.net.http.HttpMethod;
import se.arkalix.net.http.HttpVersion;

import java.net.URI;
import java.nio.file.Path;
import java.util.*;

/**
 * An outgoing HTTP request.
 */
@SuppressWarnings("UnusedReturnValue")
public class HttpClientRequest implements HttpBodySender<HttpClientRequest> {
    private final HttpHeaders headers = new HttpHeaders();
    private final Map<String, List<String>> queryParameters = new HashMap<>();

    private Object body = null;
    private DtoEncoding encoding = null;
    private HttpMethod method = null;
    private String uri = null;
    private HttpVersion version = null;

    @Override
    public Optional<Object> body() {
        return Optional.ofNullable(body);
    }

    @Override
    public HttpClientRequest body(final byte[] byteArray) {
        encoding = null;
        body = byteArray;
        return this;
    }

    @Override
    public HttpClientRequest body(final DtoEncoding encoding, final DtoWritable data) {
        this.encoding = encoding;
        body = data;
        return this;
    }

    @Override
    public HttpClientRequest body(final DtoEncoding encoding, final List<DtoWritable> data) {
        this.encoding = encoding;
        body = data;
        return this;
    }

    @Override
    public HttpClientRequest body(final Path path) {
        encoding = null;
        body = path;
        return this;
    }

    @Override
    public HttpClientRequest body(final String string) {
        encoding = null;
        body = string;
        return this;
    }

    @Override
    public HttpClientRequest clearBody() {
        encoding = null;
        body = null;
        return this;
    }

    /**
     * @return Encoding set with the most recent call to {@link
     * #body(DtoEncoding, DtoWritable)}, if any.
     */
    public Optional<DtoEncoding> encoding() {
        return Optional.ofNullable(encoding);
    }

    /**
     * Gets value of first header with given {@code name}, if any such.
     *
     * @param name Name of header. Case is ignored. Prefer lowercase.
     * @return Header value, or {@code null}.
     */
    public Optional<String> header(final CharSequence name) {
        return headers.get(name);
    }

    /**
     * Sets header with {@code name} to given value.
     *
     * @param name  Name of header. Case is ignored. Prefer lowercase.
     * @param value Desired header value.
     * @return This request.
     */
    public HttpClientRequest header(final CharSequence name, final CharSequence value) {
        headers.set(name, value);
        return this;
    }

    /**
     * Gets all header values associated with given {@code name}, if any.
     *
     * @param name Name of header. Case is ignored. Prefer lowercase.
     * @return Header values. May be an empty list.
     */
    public List<String> headers(final CharSequence name) {
        return headers.getAll(name);
    }

    /**
     * @return <i>Modifiable</i> map of all request headers.
     */
    public HttpHeaders headers() {
        return headers;
    }

    /**
     * @return Currently set HTTP method, if any.
     */
    public Optional<HttpMethod> method() {
        return Optional.ofNullable(method);
    }

    /**
     * Sets HTTP method. <b>Must be specified.</b>
     *
     * @param method Desired method.
     * @return This request.
     */
    public HttpClientRequest method(final HttpMethod method) {
        this.method = method;
        return this;
    }

    /**
     * Gets first query parameter with given name, if any such.
     *
     * @param name Name of query parameter. Case sensitive.
     * @return Query parameter value, if a corresponding parameter name exists.
     */
    public Optional<String> queryParameter(final String name) {
        final var values = queryParameters.get(name);
        return Optional.ofNullable(values != null && values.size() > 0 ? values.get(0) : null);
    }

    /**
     * Sets query parameter pair, replacing all previous such with the same
     * name.
     *
     * @param name  Name of query parameter. Case sensitive.
     * @param value Desired parameter value.
     * @return This request.
     */
    public HttpClientRequest queryParameter(final String name, final Object value) {
        final var list = new ArrayList<String>(1);
        list.add(value.toString());
        queryParameters.put(name, list);
        return this;
    }

    /**
     * @return Modifiable map of query parameters.
     */
    public Map<String, List<String>> queryParameters() {
        return queryParameters;
    }

    /**
     * @return Currently set request URI, if any.
     */
    public Optional<String> uri() {
        return Optional.ofNullable(uri);
    }

    /**
     * Sets request URI. <b>Must be specified.</b>
     *
     * @param uri Desired URI.
     * @return This request.
     */
    public HttpClientRequest uri(final String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * Sets request URI. <b>Must be specified.</b>
     *
     * @param uri Desired URI.
     * @return This request.
     */
    public HttpClientRequest uri(final URI uri) {
        return uri(uri.toString());
    }

    /**
     * @return Currently set HTTP version, if any.
     */
    public Optional<HttpVersion> version() {
        return Optional.ofNullable(version);
    }

    /**
     * Sets HTTP version.
     * <p>
     * Note that only HTTP/1.0 and HTTP/1.1 are supported by this version of
     * Kalix.
     *
     * @param version Desired HTTP version.
     * @return This request.
     * @throws IllegalArgumentException If any other HTTP version than HTTP/1.0
     *                                  or HTTP/1.1 is provided.
     */
    public HttpClientRequest version(final HttpVersion version) {
        if (version.major() != 1) {
            throw new IllegalArgumentException(version + " not supported");
        }
        this.version = version;
        return this;
    }
}
