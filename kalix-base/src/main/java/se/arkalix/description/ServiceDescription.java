package se.arkalix.description;

import se.arkalix.descriptor.SecurityDescriptor;
import se.arkalix.descriptor.InterfaceDescriptor;
import se.arkalix.internal.net.dns.DnsNames;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Describes an Arrowhead Framework service.
 */
public class ServiceDescription {
    private final String name;
    private final SystemDescription provider;
    private final String uri;
    private final SecurityDescriptor security;
    private final Map<String, String> metadata;
    private final int version;
    private final List<InterfaceDescriptor> interfaces;

    private ServiceDescription(final Builder builder) {
        name = Objects.requireNonNull(builder.name, "Expected name");
        if (DnsNames.isLabel(name)) {
            throw new IllegalArgumentException("Name \"" + name + "\" is " +
                "not a valid DNS label; such a label may only contain the " +
                "characters `0-9 A-Z a-z -`, must not start with a digit or " +
                "a dash, and must not end with a dash");
        }
        provider = Objects.requireNonNull(builder.provider, "Expected provider");
        uri = Objects.requireNonNull(builder.uri, "Expected uri");
        if (uri.isBlank()) {
            throw new IllegalArgumentException("Blank or null qualifiers " +
                "are not permitted");
        }
        security = Objects.requireNonNull(builder.security, "Expected security");
        metadata = builder.metadata == null
            ? Collections.emptyMap()
            : Collections.unmodifiableMap(builder.metadata);
        version = builder.version;
        interfaces = Collections.unmodifiableList(
            Objects.requireNonNull(builder.interfaces, "Expected interfaces"));
        if (interfaces.size() == 0) {
            throw new IllegalArgumentException("At least one supported interface must be specified");
        }
    }

    /**
     * @return Name, or <i>service definition</i>, of this service.
     */
    public String name() {
        return name;
    }

    /**
     * @return Description of system providing this service.
     */
    public SystemDescription provider() {
        return provider;
    }

    /**
     * Qualifier that distinguishes this service from other such provided by
     * the same system. How it is used depends on the application-level
     * transport protocol employed by the service.
     * <p>
     * For example, if this service uses HTTP, then the qualifier is a URL base
     * path, such as {@code "/base/path"}.
     *
     * @return Service qualifier.
     */
    public String uri() {
        return uri;
    }

    /**
     * @return Security schema used to authenticate and authorize service
     * users.
     */
    public SecurityDescriptor security() {
        return security;
    }

    /**
     * @return Metadata associated with this service. Their significance and
     * use depend on the service.
     */
    public Map<String, String> metadata() {
        return metadata;
    }

    /**
     * @return Service version.
     */
    public int version() {
        return version;
    }

    /**
     * @return Interface triplets supported by the described service. The
     * returned list should be unmodifiable.
     */
    public List<InterfaceDescriptor> interfaces() {
        return interfaces;
    }

    /**
     * Builder useful for creating {@link ServiceDescription} instances.
     */
    public static class Builder {
        private String name;
        private SystemDescription provider;
        private List<InterfaceDescriptor> interfaces;
        private String uri;
        private SecurityDescriptor security;
        private Map<String, String> metadata;
        private int version;

        /**
         * Service name. <b>Must be specified.</b>
         *
         * @param name Service name.
         * @return This builder.
         */
        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * Description of system providing this service. <b>Must be
         * specified.</b>
         *
         * @param provider Providing system description.
         * @return This builder.
         */
        public Builder provider(final SystemDescription provider) {
            this.provider = provider;
            return this;
        }

        /**
         * Sets service URI. <b>Must be specified.</b>
         *
         * @return This builder.
         * @see ServiceDescription#uri()
         */
        public Builder uri(final String uri) {
            this.uri = uri;
            return this;
        }

        /**
         * Sets security descriptor. <b>Must be specified.</b>
         *
         * @param security Security descriptor.
         * @return This builder.
         */
        public Builder security(final SecurityDescriptor security) {
            this.security = security;
            return this;
        }

        /**
         * Sets service metadata.
         *
         * @param metadata Service metadata.
         * @return This builder.
         */
        public Builder metadata(final Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        /**
         * Sets service version. Defaults to 0.
         *
         * @param version Service version.
         * @return This builder.
         */
        public Builder version(final int version) {
            this.version = version;
            return this;
        }

        /**
         * Sets interface triplets supported by service. <b>At least one must
         * be specified.</b>
         *
         * @param supportedInterfaces Interface triplets.
         * @return This builder.
         */
        public Builder interfaces(final List<InterfaceDescriptor> supportedInterfaces) {
            this.interfaces = supportedInterfaces;
            return this;
        }

        /**
         * @return New {@link ServiceDescription}.
         */
        public ServiceDescription build() {
            return new ServiceDescription(this);
        }
    }
}
