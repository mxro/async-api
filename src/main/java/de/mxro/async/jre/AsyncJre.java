package de.mxro.async.jre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.mxro.async.Async;
import de.mxro.async.PartialPromise;
import de.mxro.async.Operation;
import de.mxro.async.Promise;
import de.mxro.async.callbacks.ListCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.internal.JrePromiseImpl;

public class AsyncJre {

	public static <ResultType> Promise<ResultType> promise(
			PartialPromise<ResultType> promise) {
		return new JrePromiseImpl<ResultType>(promise);
	}
	
	public static <T> List<Object> parallel(List<Promise<T>> promises) {
		return parallel(promises.toArray(new Promise[0]));
	}
	
	public static <ResultType> ResultType get(PartialPromise<ResultType> promise) {
		return new JrePromiseImpl<ResultType>(promise).get();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	public static List<Object> parallel(PartialPromise... promises) {
		ArrayList<Promise> list = new ArrayList<Promise>(promises.length);
		for (PartialPromise ap : promises) {
			list.add(promise(ap));
		}
		
		return parallel(list.toArray(new Promise[0]));
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Object> parallel(Promise... promises) {
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		Async.map(Arrays.asList(promises), new Operation<Promise, Object>() {

			@SuppressWarnings("unchecked")
			@Override
			public void apply(Promise input,
					final ValueCallback<Object> callback) {
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
		
		try {
			latch.await(120000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		if (latch.getCount() > 0) {
			throw new RuntimeException("Parallel operation was not completed in timeout.");
		}
		
		
		List<Object> res = new ArrayList<Object>(promises.length);
		
		for (Promise p: promises) {
			res.add(p.get());
		}
		
		return res;
		
		
	}
	
}
