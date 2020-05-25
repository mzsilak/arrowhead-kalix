package se.arkalix.security.identity;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Holds certificates associated with <i>trusted</i> Arrowhead systems,
 * operators, clouds, companies and other authorities.
 * <p>
 * Instances of this class are guaranteed to only hold
 * <a href="https://tools.ietf.org/html/rfc5280">x.509</a> certificates.
 *
 * @see <a href="https://tools.ietf.org/html/rfc5280">RFC 5280</a>
 */
@SuppressWarnings("unused")
public class TrustStore {
    private final X509Certificate[] certificates;

    /**
     * Creates new trust store from given array of
     * <a href="https://tools.ietf.org/html/rfc5280">x.509</a> certificates.
     *
     * @param certificates Trusted certificates.
     * @throws IllegalArgumentException If any out of given {@code certificates}
     *                                  is not of the x.509 type.
     * @see <a href="https://tools.ietf.org/html/rfc5280">RFC 5280</a>
     */
    public TrustStore(final Certificate... certificates) {
        final var certificates0 = new X509Certificate[certificates.length];
        for (var i = 0; i < certificates.length; ++i) {
            final var certificate = certificates[i];
            if (!(certificate instanceof X509Certificate)) {
                throw new IllegalArgumentException("Only x.509 certificates " +
                    "are permitted in TrustStore instances; the " +
                    "following certificate is of some other type:\n" +
                    certificate);
            }
            certificates0[i] = (X509Certificate) certificates[i];
        }
        this.certificates = certificates0;
    }

    /**
     * Creates new trust store from given array of
     * <a href="https://tools.ietf.org/html/rfc5280">x.509</a> certificates.
     *
     * @param certificates Trusted certificates.
     * @see <a href="https://tools.ietf.org/html/rfc5280">RFC 5280</a>
     */
    public TrustStore(final X509Certificate... certificates) {
        this.certificates = certificates.clone();
    }

    /**
     * Creates new trust store by collecting all certificates from given
     * initialized {@link KeyStore}.
     *
     * @param keyStore Key store containing trusted certificates.
     * @return New trust store.
     * @throws KeyStoreException        If {@code keyStore} has not been
     *                                  initialized.
     * @throws IllegalArgumentException If {@code keyStore} would contain any
     *                                  certificates not being of the x.509
     *                                  type.
     * @see <a href="https://tools.ietf.org/html/rfc5280">RFC 5280</a>
     */
    public static TrustStore from(final KeyStore keyStore) throws KeyStoreException {
        final var certificates = new ArrayList<Certificate>();
        for (final var alias : Collections.list(keyStore.aliases())) {
            certificates.add(keyStore.getCertificate(alias));
        }
        return new TrustStore(certificates.toArray(new Certificate[0]));
    }

    /**
     * Reads JVM-compatible key store from specified path and collects all
     * contained certificates into a created {@link TrustStore}.
     * <p>
     * As of Java 11, only the
     * <a href="https://tools.ietf.org/html/rfc7292">PKCS#12</a> key store
     * format is mandatory to support for Java implementations.
     *
     * @param path     Filesystem path to key store to load.
     * @param password Key store password, or {@code null} if not required.
     * @return New trust store.
     * @throws GeneralSecurityException If the key store contains data or
     *                                  details that cannot be interpreted
     *                                  or supported properly.
     * @throws IOException              If the key store at the specified
     *                                  {@code path} could not be read.
     * @see <a href="https://tools.ietf.org/html/rfc5280">RFC 5280</a>
     * @see <a href="https://tools.ietf.org/html/rfc7292">RFC 7292</a>
     */
    public static TrustStore read(final Path path, final char[] password)
        throws GeneralSecurityException, IOException
    {
        final var file = path.toFile();
        final var keyStore = password != null
            ? KeyStore.getInstance(file, password)
            : KeyStore.getInstance(file, (KeyStore.LoadStoreParameter) null);
        return from(keyStore);
    }

    /**
     * Reads JVM-compatible key store from specified path and collects all
     * contained certificates into a created {@link TrustStore}.
     * <p>
     * As of Java 11, only the
     * <a href="https://tools.ietf.org/html/rfc7292">PKCS#12</a> key store
     * format is mandatory to support for Java implementations.
     *
     * @param path     Filesystem path to key store to load.
     * @param password Key store password, or {@code null} if not required.
     * @return New trust store.
     * @throws GeneralSecurityException If the key store contains data or
     *                                  details that cannot be interpreted
     *                                  or supported properly.
     * @throws IOException              If the key store at the specified
     *                                  {@code path} could not be read.
     * @see <a href="https://tools.ietf.org/html/rfc5280">RFC 5280</a>
     * @see <a href="https://tools.ietf.org/html/rfc7292">RFC 7292</a>
     */
    public static TrustStore read(final String path, final char[] password)
        throws GeneralSecurityException, IOException
    {
        return read(Path.of(path), password);
    }

    /**
     * @return Clone of array of trusted x.509 certificates.
     * @see <a href="https://tools.ietf.org/html/rfc5280">RFC 5280</a>
     */
    public X509Certificate[] certificates() {
        return certificates.clone();
    }
}