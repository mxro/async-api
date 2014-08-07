package de.mxro.async.jre;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		Async.map(Arrays.asList(promises), new Operation<Promise, Object>() {

			@Override
			public void apply(Promise input,
					ValueCallback<Object> callback) {
				input.get(new ValueCallback<Object>() {

					@Override
					public void onFailure(Throwable t) {
						callback.onFailure(t);
					}

					@Override
					public void onSuccess(Object value) {
						callback.onSuccess(value);
					}
				});
			}
		}, new ListCallback<Object>() {

			@Override
			public void onSuccess(List<Object> value) {
				latch.countDown();
			}

			@Override
			public void onFailure(Throwable t) {
				latch.countDown();
			}
		});
		
		
		
		
	}
	
}
