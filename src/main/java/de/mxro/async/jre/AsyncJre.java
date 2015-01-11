package de.mxro.async.jre;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.mxro.async.Value;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.internal.JrePromiseImpl;
import de.mxro.async.promise.Deferred;

/**
 * Asynchronous utilities which are only available in Oracle Java, OpenJDK and
 * Android (and not for GWT).
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class AsyncJre {

    public static <ResultType> ResultType get(final Deferred<ResultType> promise) {
        return new JrePromiseImpl<ResultType>(promise).get();
    }

    /**
     * Executes the specified {@link Deferred} operation and blocks the calling
     * thread until the operation is completed.
     * 
     * @param deferred
     *            The deferred operation to be executed.
     * @return The result of the deferred operation.
     */
    public static final <T> T waitFor(final int timeout, final Deferred<T> deferred) {

        final CountDownLatch latch = new CountDownLatch(1);
        final Value<T> result = new Value<T>(null);
        final Value<Throwable> failure = new Value<Throwable>(null);

        deferred.get(new ValueCallback<T>() {

            @Override
            public void onFailure(final Throwable t) {
                failure.set(t);
                latch.countDown();
            }

            @Override
            public void onSuccess(final T value) {
                result.set(value);
                latch.countDown();
            }
        });

        try {
            latch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (latch.getCount() > 0) {
            throw new RuntimeException("Operation not completed in timeout: " + deferred);
        }

        if (failure.get() != null) {
            throw new RuntimeException("Exception while performing asynchronous operation", failure.get());
        }

        return result.get();

    }

    /**
     * Executes the specified {@link Deferred} operation and blocks the calling
     * thread until the operation is completed.
     * 
     * @param deferred
     *            The deferred operation to be executed.
     * @return The result of the deferred operation.
     */
    public static final <T> T waitFor(final Deferred<T> deferred) {
        return waitFor(30000, deferred);

    }
}
