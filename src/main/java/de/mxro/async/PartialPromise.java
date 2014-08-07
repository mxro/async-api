package de.mxro.async;

import de.mxro.async.callbacks.ValueCallback;

public interface PartialPromise<ResultType> {

	public void get(ValueCallback<ResultType> callback);
	
}
