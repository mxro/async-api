package de.mxro.async.flow;

import java.util.List;

import de.mxro.async.callbacks.ValueCallback;

public final class CallbackAggregator<V> {

    final int expected;

    final ValueCallback<List<V>> callback;

}
