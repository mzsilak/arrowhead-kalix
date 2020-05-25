package se.arkalix.net.http.service;

import se.arkalix.net.http.HttpStatus;
import se.arkalix.util.concurrent.Future;

/**
 * A route handler, meant to process incoming HTTP requests matching some
 * {@link HttpRoute set of preconditions}.
 */
@FunctionalInterface
public interface HttpRouteHandler {
    /**
     * Called with an incoming HTTP request and a response handler.
     *
     * @param request  Information about the incoming HTTP request, including
     *                 its header and body.
     * @param response An object useful for indicating how the request is to be
     *                 responded to. It is an error for a route handler not to
     *                 set a {@link HttpServiceResponse#status(HttpStatus)
     *                 status code}.
     * @return Future completed when handling is complete.
     * @throws Exception Whatever exception the handle may want to throw. If
     *                   the HTTP service owning this handle knows how to
     *                   translate the exception into a certain kind of HTTP
     *                   response, it should. Otherwise the requester should
     *                   receive a 500 Internal Server Error response without
     *                   any details and the exception be logged (if logging is
     *                   enabled).
     */
    Future<?> handle(HttpServiceRequest request, HttpServiceResponse response) throws Exception;
}
