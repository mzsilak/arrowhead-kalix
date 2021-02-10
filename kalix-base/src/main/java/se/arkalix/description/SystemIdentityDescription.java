package se.arkalix.description;

import se.arkalix.internal.description.DefaultSystemIdentityDescription;
import se.arkalix.security.SecurityDisabled;
import se.arkalix.security.identity.SystemIdentity;

import java.net.InetSocketAddress;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Objects;
import java.util.Optional;

/**
 * Describes an Arrowhead system with a known {@link #identity() identity}, if
 * the described system is running in {@link se.arkalix.security secure mode}.
 */
public interface SystemIdentityDescription extends SystemDescription {
    /**
     * Creates new Arrowhead consumer system description.
     *
     * @param identity            System certificate chain.
     * @param remoteSocketAddress IP-address/hostname and port through which
     *                            the system can be contacted.
     * @throws NullPointerException If {@code identity} or {@code
     *                              remoteSocketAddress} is {@code null}.
     */
    static SystemIdentityDescription from(final SystemIdentity identity, final InetSocketAddress remoteSocketAddress) {
        Objects.requireNonNull(identity, "Expected identity");
        return new DefaultSystemIdentityDescription(identity.name(), identity, remoteSocketAddress);
    }

    /**
     * Creates new Arrowhead consumer system description.
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
    static SystemIdentityDescription from(final String name, final InetSocketAddress remoteSocketAddress) {
        return new DefaultSystemIdentityDescription(name, null, remoteSocketAddress);
    }

    /**
     * Tries to create new Arrowhead system description from given certificate
     * {@code chain} and {@code remoteSocketAddress}.
     *
     * @param chain               System certificate chain.
     * @param remoteSocketAddress IP-address/hostname and port through which
     *                            the system can be contacted.
     * @return System description, if all criteria are satisfied.
     */
    static Optional<SystemIdentityDescription> tryFrom(
        final Certificate[] chain,
        final InetSocketAddress remoteSocketAddress
    ) {
        if (remoteSocketAddress == null) {
            return Optional.empty();
        }
        return SystemIdentity.tryFrom(chain)
            .map(identity -> from(identity, remoteSocketAddress));
    }

    @Override
    default PublicKey publicKey() {
        return identity().publicKey();
    }

    /**
     * Gets identity of peer system, or throws if not in secure mode.
     *
     * @return System identity.
     * @throws SecurityDisabled If the system does not have an identity. This
     *                          will only be the case if the system that
     *                          retrieved this description runs in the
     *                          <i>insecure</i> {@link se.arkalix.security
     *                          security mode}.
     */
    SystemIdentity identity();
}
