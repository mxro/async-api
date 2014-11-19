package de.mxro.async.jre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.mxro.async.Async;
import de.mxro.async.Deferred;
import de.mxro.async.Operation;
import de.mxro.async.Promise;
import de.mxro.async.Value;
import de.mxro.async.callbacks.ListCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.internal.JrePromiseImpl;

/**
 * Asynchronous utilities which are only available in Oracle Java, OpenJDK and
 * Android (and not for GWT).
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class AsyncJre {

    public static <ResultType> Promise<ResultType> promise(final Deferred<ResultType> promise) {
        return new JrePromiseImpl<ResultType>(promise);
    }

    public static <T> List<Object> parallel(final List<Promise<T>> promises) {
        return parallel(promises.toArray(new Promise[0]));
    }

    public static <ResultType> ResultType get(final Deferred<ResultType> promise) {
        return new JrePromiseImpl<ResultType>(promise).get();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<Object> parallel(final Deferred... promises) {
        final ArrayList<Promise> list = new ArrayList<Promise>(promises.length);
        for (final Deferred ap : promises) {
            list.add(promise(ap));
        }

        return parallel(list.toArray(new Promise[0]));
    }

    @SuppressWarnings("rawtypes")
    public static List<Object> parallel(final Promise... promises) {

        final CountDownLatch latch = new CountDownLatch(1);

        Async.map(Arrays.asList(promises), new Operation<Promise, Object>() {

            @SuppressWarnings("unchecked")
            @Override
            public void apply(final Promise input, final ValueCallback<Object> callback) {
                input.get(new ValueCallback<Object>() {

                    @Override
                    public void onFailure(final Throwable t) {
                        callback.onFailure(t);
                    }

                    @Override
                    public void onSuccess(final Object value) {
                        callback.onSuccess(value);
                    }
                });
            }
        }, new ListCallback<Object>() {

            @Override
            public void onSuccess(final List<Object> value) {
                latch.countDown();
            }

            @Override
            public void onFailure(final Throwable t) {
                latch.countDown();
            }
        });

        try {
            latch.await(120000, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (latch.getCount() > 0) {
            throw new RuntimeException("Parallel operation was not completed in timeout.");
        }

        final List<Object> res = new ArrayList<Object>(promises.length);

        for (final Promise p : promises) {
            res.add(p.get());
        }

        return res;

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
        waitFor(30000, deferred)

    }
}
