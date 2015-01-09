package de.mxro.async.promise;

import de.mxro.async.callbacks.ValueCallback;

/**
 * A potential for a promise. Use Async or AsyncJre utilities to convert into full promises.
 * 
 * @author adminuser
 *
 * @param <ResultType>
 */
public interface Deferred<ResultType> {

	public void get(ValueCallback<ResultType> callback);
	
}
