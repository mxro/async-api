package de.mxro.async.flow;

import java.util.List;

import de.mxro.async.callbacks.ValueCallback;

public final class CallbackAggregator<V> {

    final int expected;

    final ValueCallback<List<V>> callback;

    public final ValueCallback<V> createCallback() {

    }

    public CallbackAggregator(final int expected, final ValueCallback<List<V>> callback) {
        super();
        this.expected = expected;
        this.callback = callback;
    }

}
