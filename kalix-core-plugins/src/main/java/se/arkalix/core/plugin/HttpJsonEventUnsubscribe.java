package se.arkalix.core.plugin;

import se.arkalix.ArConsumer;
import se.arkalix.ArConsumerFactory;
import se.arkalix.ArSystem;
import se.arkalix.description.ServiceDescription;
import se.arkalix.descriptor.EncodingDescriptor;
import se.arkalix.descriptor.TransportDescriptor;
import se.arkalix.net.http.client.HttpClient;
import se.arkalix.net.http.consumer.HttpConsumer;
import se.arkalix.net.http.consumer.HttpConsumerRequest;
import se.arkalix.util.concurrent.Future;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static se.arkalix.core.plugin.HttpJsonServices.unwrap;
import static se.arkalix.descriptor.EncodingDescriptor.JSON;
import static se.arkalix.descriptor.TransportDescriptor.HTTP;
import static se.arkalix.net.http.HttpMethod.DELETE;

/**
 * A remote {@link ArEventUnsubscribe} service that is communicated with via
 * HTTP/JSON in either secure or insecure mode.
 */
@SuppressWarnings("unused")
public class HttpJsonEventUnsubscribe implements ArConsumer, ArEventUnsubscribe {
    private static final Factory factory = new Factory();

    private final HttpConsumer consumer;

    HttpJsonEventUnsubscribe(final HttpConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * @return Consumer {@link ArConsumerFactory factory class}.
     */
    public static ArConsumerFactory<HttpJsonEventUnsubscribe> factory() {
        return factory;
    }

    @Override
    public Future<?> unsubscribe(final String eventType, final String subscriberName, final String hostname, final int port) {
        return consumer.send(new HttpConsumerRequest()
            .method(DELETE)
            .uri(service().uri())
            .queryParameter("event_type", eventType)
            .queryParameter("system_name", subscriberName)
            .queryParameter("address", hostname)
            .queryParameter("port", Integer.toString(port)))
            .flatMap(response -> unwrap(response, null));
    }

    @Override
    public ServiceDescription service() {
        return consumer.service();
    }

    private static class Factory implements ArConsumerFactory<HttpJsonEventUnsubscribe> {
        @Override
        public Optional<String> serviceName() {
            return Optional.of("event-unsubscribe");
        }

        @Override
        public Collection<TransportDescriptor> serviceTransports() {
            return Collections.singleton(HTTP);
        }

        @Override
        public Collection<EncodingDescriptor> serviceEncodings() {
            return Collections.singleton(JSON);
        }

        @Override
        public HttpJsonEventUnsubscribe create(
            final ArSystem system,
            final ServiceDescription service,
            final Collection<EncodingDescriptor> encodings) throws Exception
        {
            return new HttpJsonEventUnsubscribe(new HttpConsumer(HttpClient.from(system), service, encodings));
        }
    }
}

