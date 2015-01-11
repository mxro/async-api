package de.mxro.async;

import de.mxro.async.callbacks.ValueCallback;
import de.mxro.fn.Closure2;

/**
 * A base template for an asynchronous function.
 * 
 * @author Max Rohde
 *
 * @param <ParameterType>
 * @param <ResultType>
 */
public interface AsyncFunction<ParameterType, ResultType> extends Closure2<ParameterType, ValueCallback<ResultType>> {

    @Override
    public void apply(ParameterType input, ValueCallback<ResultType> callback);

}
