package de.mxro.async;

import de.mxro.async.callbacks.ValueCallback;

/**
 * A potential for a promise. Use Async or AsyncJre utilities to convert into full promises.
 * 
 * @author adminuser
 *
 * @param <ResultType>
 */
public interface PotentialPromise<ResultType> {

	public void get(ValueCallback<ResultType> callback);
	
}
