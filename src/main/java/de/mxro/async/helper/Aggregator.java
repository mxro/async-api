package de.mxro.async.helper;

import de.mxro.async.callbacks.ValueCallback;

public interface Aggregator<V> {

    public ValueCallback<V> createCallback();

}
