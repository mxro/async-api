package de.mxro.async;

import de.mxro.async.callbacks.ValueCallback;

public interface Aggregator<V> {

    public ValueCallback<V> createCallback();

}
