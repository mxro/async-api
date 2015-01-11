package de.mxro.async;

import de.mxro.async.callbacks.ValueCallback;
import de.mxro.fn.Closure2;

/**
 * A base template for asynchronous operations with one parameter.
 * 
 * @author Max Rohde
 *
 * @param <InputType>
 * @param <OutputType>
 */
public interface Operation<InputType, OutputType> extends Closure2<InputType, ValueCallback<OutputType>> {

    @Override
    public void apply(InputType input, ValueCallback<OutputType> callback);

}
