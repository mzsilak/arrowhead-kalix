package se.arkalix.core.plugin;

/**
 *
 */
public class ArCoreIntegrationException extends Exception {
    public ArCoreIntegrationException(final String message) {
        super(message);
    }

    public ArCoreIntegrationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}