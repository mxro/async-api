package de.mxro.async;

import de.mxro.async.callbacks.ValueCallback;
import de.mxro.fn.Closure;

/**
 * The definition of an asynchronous operation with no parameters.
 *
 * @param <ResultType>
 */
public interface Deferred<ResultType> extends Closure<ValueCallback<ResultType>> {

    @Override
    public void apply(ValueCallback<ResultType> callback);

}
