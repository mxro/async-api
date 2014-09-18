package de.mxro.async;

import java.util.List;

import de.mxro.async.callbacks.ListCallback;
import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.flow.CallbackMap;
import de.mxro.async.internal.PromiseImpl;
import de.mxro.async.internal.Value;
import de.mxro.fn.Success;

public final class Async {

    /**
     * <p>
     * Tries to resolve a {@link Deferred} immediately without waiting for the
     * asynchronous operation.
     * <p>
     * This is useful for operations which actually resolve in a synchronous
     * fashion (which might be added for legacy logic).
     * 
     * @param deferred
     * @return
     */
    public static final <ResultType> ResultType getDirty(final Deferred<ResultType> deferred) {

        final Value<Boolean> resolved = new Value<Boolean>(false);
        final Value<ResultType> value = new Value<ResultType>(null);
        final Value<Throwable> exception = new Value<Throwable>(null);

        deferred.get(new ValueCallback<ResultType>() {

            @Override
            public void onFailure(final Throwable t) {
                exception.set(t);
            }

            @Override
            public void onSuccess(final ResultType result) {

                value.set(result);
                resolved.set(true);
            }
        });

        if (exception.get() != null) {
            throw new RuntimeException(exception.get());
        }

        if (!resolved.get()) {
            throw new RuntimeException("Asynchronous get could not be resolved for " + deferred);
        }

        return value.get();

    }

    /**
     * <p>
     * Will apply the <b>asynchronous</b> operation operation to all inputs and
     * call the callback once all operations are completed.
     * <p>
     * Callback is also called upon the first operation which fails.
     * <p>
     * ValueCallback must be called in closure.
     * 
     * @param inputs
     * @param operation
     * @param callback
     */
    public static <InputType, ResultType> void map(final List<InputType> inputs,
            final Operation<InputType, ResultType> operation, final ListCallback<ResultType> callback) {

        final CallbackMap<InputType, ResultType> callbackMap = new CallbackMap<InputType, ResultType>(inputs, callback);

        for (final InputType input : inputs) {
            operation.apply(input, callbackMap.createCallback(input));
        }

    }

    public final static <ResultType> Promise<ResultType> promise(final Deferred<ResultType> promise) {
        return new PromiseImpl<ResultType>(promise);
    }

    public final static SimpleCallback wrap(final ValueCallback<Success> callback) {
        return new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(Success.INSTANCE);
            }
        };
    }

    public final static SimpleCallback doNothing() {
        return new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public void onSuccess() {

            }
        };
    }

}
