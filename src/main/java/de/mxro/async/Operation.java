package de.mxro.async;

import de.mxro.async.callbacks.ValueCallback;
import de.mxro.fn.Closure2;

public interface Operation<InputType, OutputType> extends Closure2<InputType, ValueCallback<OutputType>> {

	public void apply(InputType input, ValueCallback<OutputType> callback);
	
}
