package eu.arrowhead.kalix.util;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The result of an operation that can either succeed or fail, during which
 * exceptions would not end up at the appropriate caller if thrown.
 * <p>
 * A {@code Result} may either be a <i>success</i>, in which case a
 * <i>value</i> is available, or a <i>failure</i>, which makes an
 * <i>error</i> available. The {@link #isSuccess()} method is used to
 * determine which of the two situations is the case. The {@link #value()}
 * and {@link #fault()} methods are used to collect the value or error,
 * respectively.
 *
 * @param <V> Type of value provided by {@code Result} if successful.
 */
public class Result<V> {
    private final boolean isSuccess;
    private final V value;
    private final Throwable throwable;

    private Result(final boolean isSuccess, final V value, final Throwable throwable) {
        this.isSuccess = isSuccess;
        this.value = value;
        this.throwable = throwable;
    }

    /**
     * Creates new successful {@code Result}.
     *
     * @param value Value.
     * @param <V>   Type of value.
     * @return New {@code Result}.
     */
    public static <V> Result<V> success(final V value) {
        return new Result<>(true, value, null);
    }

    /**
     * Creates new failure {@code Result}.
     *
     * @param throwable Reason for failure.
     * @param <V>   Type of value that would have been provided by the
     *              created {@code Result}, if it were successful.
     * @return New {@code Result}.
     */
    public static <V> Result<V> failure(final Throwable throwable) {
        return new Result<>(false, null, throwable);
    }

    /**
     * @return {@code true} if this {@code Result} contains a value.
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * @return A {@code Throwable} if this {@code Result} is a failure.
     * {@code null} otherwise.
     */
    public Throwable fault() {
        return throwable;
    }

    /**
     * @return Some value if this {@code Result} is a success. {@code null}
     * otherwise.
     */
    public V value() {
        return value;
    }

    /**
     * Either returns {@code Result} value or throws its error, depending
     * on whether it is successful or not.
     * <p>
     * In the case of being a failure, the error is thrown as-is if it is a
     * subclass of {@link RuntimeException}. If not, it is wrapped in a
     * {@code RuntimeException} before being thrown.
     *
     * @return Result value, if the {@code Result} is successful.
     * @throws RuntimeException If the {@code Result} is a failure.
     */
    public V valueOrThrow() {
        if (isSuccess()) {
            return value();
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        throw new RuntimeException(fault());
    }

    /**
     * Calls given {@code consumer} only if this result is successful.
     *
     * @param consumer Consumer function to call, if successful.
     */
    public void ifSuccess(final Consumer<? super V> consumer) {
        if (isSuccess()) {
            consumer.accept(value());
        }
    }

    /**
     * Calls given {@code consumer} only if this result is a failure.
     *
     * @param consumer Consumer function to call, if not successful.
     */
    public void ifFailure(final Consumer<Throwable> consumer) {
        if (!isSuccess()) {
            consumer.accept(fault());
        }
    }

    /**
     * If this result is successful, applies given {@code mapper} to its value.
     * Otherwise, a new {@code Result} with the error contained in this one is
     * returned.
     *
     * @param <U>    Type of return value of {@code mapper}.
     * @param mapper Function to apply to result value, if this result is
     *               successful.
     * @return New result containing either output of mapping or an error
     * passed on from this result.
     */
    public <U> Result<U> map(final Function<? super V, ? extends U> mapper) {
        if (isSuccess()) {
            return success(mapper.apply(value()));
        }
        return failure(fault());
    }

    /**
     * If this result is successful, applies given {@code mapper} to its value,
     * and then returns the {@code Result} returned by the {@code mapper}. If
     * this result is not successful, a new {@code FutureResult} with the error
     * it contains is returned.
     *
     * @param <U>    Type of value of {@code FutureResult} returned by
     *               {@code mapper}.
     * @param mapper Function to apply to result value, if this result is
     *               successful.
     * @return New result consisting either of result of {@code mapper} or an
     * error passed on from this result.
     */
    public <U> Result<U> flatMap(final Function<? super V, ? extends Result<U>> mapper) {
        if (isSuccess()) {
            return mapper.apply(value());
        }
        return failure(fault());
    }
}
