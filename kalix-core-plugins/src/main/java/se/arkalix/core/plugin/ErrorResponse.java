package se.arkalix.core.plugin;

import se.arkalix.dto.DtoEqualsHashCode;
import se.arkalix.dto.DtoReadableAs;
import se.arkalix.dto.DtoToString;
import se.arkalix.dto.DtoWritableAs;
import se.arkalix.dto.json.JsonName;

import static se.arkalix.dto.DtoEncoding.JSON;

/**
 * An error description, as provided by the core services when a request
 * cannot be fulfilled.
 */
@DtoReadableAs(JSON)
@DtoWritableAs(JSON)
@DtoEqualsHashCode
@DtoToString
public interface ErrorResponse {
    /**
     * Human-readable description of error.
     */
    @JsonName("errorMessage")
    String message();

    /**
     * Error code.
     */
    @JsonName("errorCode")
    int code();

    /**
     * Error type descriptor.
     */
    @JsonName("exceptionType")
    String type();
}
