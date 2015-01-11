package de.mxro.async;

import de.mxro.async.callbacks.ValueCallback;

/**
 * The definition of an asynchronous operation with no parameters.
 *
 * @param <ResultType>
 */
public interface Deferred<ResultType> {

    public void get(ValueCallback<ResultType> callback);

}
