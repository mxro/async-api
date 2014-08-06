package de.mxro.async.internal;

import java.util.List;

import de.mxro.async.AsyncPromise;
import de.mxro.async.Promise;
import de.mxro.async.callbacks.ValueCallback;

public class PromiseImpl<ResultType> implements Promise<ResultType> {

	private final AsyncPromise<ResultType> asyncPromise;
	
	private final List<ValueCallback<ResultType>> deferredCalls;
	
	private ResultType resultCache;
	
	
	private final void requestResult(ValueCallback<ResultType> callback) {
		synchronized (resultCache) {
			if (resultCache != null) {
				callback.onSuccess(resultCache);
				return;
			}
			
			
			
		}
		
		
		
		
	}
	
	

	
	
}
