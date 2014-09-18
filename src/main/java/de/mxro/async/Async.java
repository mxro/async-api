package de.mxro.async;

import java.util.List;

import de.mxro.async.callbacks.ListCallback;
import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.flow.CallbackMap;
import de.mxro.async.internal.PromiseImpl;
import de.mxro.fn.Success;

public final class Async {

    public static final <ResultType> ResultType get(final Deferred<ResultType> deferred) {

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
