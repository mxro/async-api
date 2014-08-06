package de.mxro.async;

import de.mxro.async.callbacks.ValueCallback;

public interface AsyncPromise<ResultType> {

	public void get(ValueCallback<ResultType> callback);
	
}
