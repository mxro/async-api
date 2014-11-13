package de.mxro.async.flow;

import java.util.List;

import de.mxro.async.Value;
import de.mxro.async.callbacks.ValueCallback;

public final class CallbackAggregator<V> {

    final int expected;

    final ValueCallback<List<V>> callback;

    Value<Boolean> exceptionReceived;
    volatile Throwable exception;

    public final ValueCallback<V> createCallback() {
        return new ValueCallback<V>() {

            @Override
            public void onFailure(final Throwable t) {
                synchronized (exceptionReceived) {
                    if (exceptionReceived.get()) {
                        throw new RuntimeException("Exception already received.", t);
                    }

                    exceptionReceived.set(true);

                }
            }

            @Override
            public void onSuccess(final V value) {

            }
        };
    }

    public CallbackAggregator(final int expected, final ValueCallback<List<V>> callback) {
        super();
        this.expected = expected;
        this.callback = callback;

        this.exceptionReceived.set(false)
        this.exception = null;
    }
}
