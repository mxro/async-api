package de.mxro.async.promise;

import de.mxro.async.callbacks.ValueCallback;

/**
 * An operation which can be deferred for asynchronous processing.
 *
 * @param <ResultType>
 */
public interface Deferred<ResultType> {

    public void get(ValueCallback<ResultType> callback);

}
