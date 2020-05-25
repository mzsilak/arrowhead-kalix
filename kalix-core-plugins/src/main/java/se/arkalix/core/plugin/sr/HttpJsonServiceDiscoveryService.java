package se.arkalix.core.plugin.sr;

import se.arkalix.description.ServiceDescription;
import se.arkalix.descriptor.EncodingDescriptor;
import se.arkalix.internal.core.plugin.HttpJsonServices;
import se.arkalix.internal.core.plugin.Paths;
import se.arkalix.net.http.client.HttpClient;
import se.arkalix.net.http.consumer.HttpConsumer;
import se.arkalix.net.http.consumer.HttpConsumerRequest;
import se.arkalix.util.concurrent.Future;

import java.util.Collections;
import java.util.Objects;

import static se.arkalix.internal.core.plugin.HttpJsonServices.unwrap;
import static se.arkalix.net.http.HttpMethod.DELETE;
import static se.arkalix.net.http.HttpMethod.POST;

/**
 * A remote {@link ArServiceDiscoveryService} service that is communicated with via
 * HTTP/JSON in either secure or insecure mode.
 */
public class HttpJsonServiceDiscoveryService implements ArServiceDiscoveryService {
    private final HttpConsumer consumer;

    private final String uriQuery;
    private final String uriRegister;
    private final String uriUnregister;

    public HttpJsonServiceDiscoveryService(final HttpClient client, final ServiceDescription service) {
        Objects.requireNonNull(client, "Expected client");
        Objects.requireNonNull(service, "Expected service");

        consumer = new HttpConsumer(client, service, Collections.singleton(EncodingDescriptor.JSON));

        final var basePath = service.uri();
        uriQuery = Paths.combine(basePath, "query");
        uriRegister = Paths.combine(basePath, "register");
        uriUnregister = Paths.combine(basePath, "unregister");
    }

    @Override
    public ServiceDescription service() {
        return consumer.service();
    }

    @Override
    public Future<ServiceQueryResultDto> query(final ServiceQueryDto query) {
        return consumer
            .send(new HttpConsumerRequest()
                .method(POST)
                .uri(uriQuery)
                .body(query))
            .flatMap(response -> unwrap(response, ServiceQueryResultDto.class));
    }

    @Override
    public Future<?> register(final ServiceRegistrationDto registration) {
        return consumer
            .send(new HttpConsumerRequest()
                .method(POST)
                .uri(uriRegister)
                .body(registration))
            .flatMap(HttpJsonServices::unwrap);
    }

    @Override
    public Future<?> unregister(
        final String serviceName,
        final String systemName,
        final String hostname,
        final int port)
    {
        return consumer
            .send(new HttpConsumerRequest()
                .method(DELETE)
                .uri(uriUnregister)
                .queryParameter("service_definition", serviceName)
                .queryParameter("system_name", systemName)
                .queryParameter("address", hostname)
                .queryParameter("port", Integer.toString(port)))
            .flatMap(HttpJsonServices::unwrap);
    }
}
