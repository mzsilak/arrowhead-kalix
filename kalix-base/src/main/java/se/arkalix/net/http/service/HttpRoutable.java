package se.arkalix.net.http.service;

import se.arkalix.net.http.HttpMethod;

import java.util.Optional;

/**
 * Represents some handler that may be invoked with incoming HTTP request that
 * match its {@link #method() method} and {@link #pattern() path pattern}.
 * <p>
 * Please refer to the {@link se.arkalix.net.http.service package
 * documentation} for more details about {@link HttpRoutable routable classes}
 * and routing.
 */
public interface HttpRoutable {
    /**
     * @return {@link HttpMethod}, if any, that considered HTTP requests must
     * match if they are to be provided to this filter.
     */
    Optional<HttpMethod> method();

    /**
     * @return {@link HttpPattern}, if any, that the paths of considered HTTP
     * requests must match if they are to be provided to this filter.
     */
    Optional<HttpPattern> pattern();
}
