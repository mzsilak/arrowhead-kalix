package se.arkalix.description;

import se.arkalix.security.NotSecureException;

import java.net.InetSocketAddress;
import java.security.PublicKey;
import java.util.Objects;

/**
 * Describes an Arrowhead system that can be contacted in order to consume the
 * services it provides.
 */
public class ProviderDescription {
    private final String name;
    private final InetSocketAddress remoteSocketAddress;
    private final PublicKey publicKey;

    /**
     * Creates new Arrowhead provider system description.
     *
     * @param name                System name.
     * @param remoteSocketAddress IP-address/hostname and port through which
     *                            the system can be contacted.
     * @param publicKey           System public key.
     * @throws NullPointerException If {@code name} or {@code
     *                              remoteSocketAddress} is {@code null}.
     */
    public ProviderDescription(
        final String name,
        final InetSocketAddress remoteSocketAddress,
        final PublicKey publicKey)
    {
        this.name = Objects.requireNonNull(name, "Expected name");
        this.remoteSocketAddress = Objects.requireNonNull(remoteSocketAddress, "Expected remoteSocketAddress");
        this.publicKey = publicKey;
    }

    /**
     * Creates new Arrowhead provider system description.
     * <p>
     * This constructor is meant to be used only if the system invoking it is
     * running in insecure mode.
     *
     * @param name                System name.
     * @param remoteSocketAddress IP-address/hostname and port through which
     *                            the system can be contacted.
     * @throws NullPointerException If {@code name} or {@code
     *                              remoteSocketAddress} is {@code null}.
     */
    public ProviderDescription(final String name, final InetSocketAddress remoteSocketAddress) {
        this(name, remoteSocketAddress, null);
    }

    /**
     * @return System name.
     */
    public String name() {
        return name;
    }

    /**
     * @return The hostname/port or IP-address/port of the described system.
     */
    public InetSocketAddress socketAddress() {
        return remoteSocketAddress;
    }

    /**
     * @return {@code true} only if the described service is known to be
     * running in secure mode.
     */
    public boolean isSecure() {
        return publicKey != null;
    }

    /**
     * @return System public key.
     * @throws NotSecureException If the system does not have an public key.
     *                            This will only be the case if the described
     *                            system runs in the <i>insecure</i> {@link
     *                            se.arkalix.security security mode}, which it
     *                            only can do if the system that requested it
     *                            also runs in insecure mode.
     */
    public PublicKey publicKey() {
        if (publicKey == null) {
            throw new NotSecureException("Not in secure mode");
        }
        return publicKey;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) { return true; }
        if (other == null || getClass() != other.getClass()) { return false; }
        final ProviderDescription that = (ProviderDescription) other;
        return name.equals(that.name) &&
            remoteSocketAddress.equals(that.remoteSocketAddress) &&
            Objects.equals(publicKey, that.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, remoteSocketAddress, publicKey);
    }

    @Override
    public String toString() {
        return "ProviderDescription{" +
            "name='" + name + '\'' +
            ", remoteSocketAddress=" + remoteSocketAddress +
            ", publicKey=" + publicKey +
            '}';
    }
}
