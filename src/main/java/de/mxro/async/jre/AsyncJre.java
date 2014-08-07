package de.mxro.async.jre;

import java.util.List;

import de.mxro.async.Async;
import de.mxro.async.AsyncPromise;
import de.mxro.async.Operation;
import de.mxro.async.Promise;
import de.mxro.async.callbacks.ListCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.internal.JrePromiseImpl;

public class AsyncJre {

	public static <ResultType> Promise<ResultType> promise(
			AsyncPromise<ResultType> promise) {
		return new JrePromiseImpl<ResultType>(promise);
	}
	
	public static List<Object> parallel(Promise... promises) {
		
		Async.map(promises, new Operation<Promise, Object>() {

			@Override
			public void apply(Promise input,
					ValueCallback<Object> callback) {
				// TODO Auto-generated method stub
				
			}
		}, new ListCallback<Object>() {

			@Override
			public void onSuccess(List<Object> value) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFailure(Throwable t) {
				// TODO Auto-generated method stub
				
			}
		});
		
		for (Promise p: promises) {
			
		}
	}
	
}
