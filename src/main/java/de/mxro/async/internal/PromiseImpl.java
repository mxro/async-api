package de.mxro.async.internal;

import java.util.ArrayList;
import java.util.List;

import de.mxro.async.AsyncPromise;
import de.mxro.async.Promise;
import de.mxro.async.callbacks.ValueCallback;

public class PromiseImpl<ResultType> implements Promise<ResultType> {

	private final AsyncPromise<ResultType> asyncPromise;

	private final List<ValueCallback<ResultType>> deferredCalls;

	private ResultType resultCache;
	private Boolean isRequesting;
	private Throwable failureCache;

	private final void requestResult(final ValueCallback<ResultType> callback) {

		final boolean triggerOnFailure;
		final boolean triggerOnSuccess;
		synchronized (failureCache) {
			triggerOnFailure = failureCache != null;

			if (!triggerOnFailure) {
				
				synchronized (resultCache) {
					triggerOnSuccess = resultCache != null;

					if (!triggerOnSuccess) {
						synchronized (isRequesting) {

							if (isRequesting) {
								synchronized (deferredCalls) {
									deferredCalls.add(callback);
								}
								return;
							} else {
								isRequesting = true;
							}

						}
					}

				}
			} else {
				triggerOnSuccess = false;
			}
		}

		if (triggerOnFailure) {
			callback.onFailure(failureCache);
			return;
		}

		if (triggerOnSuccess) {
			callback.onSuccess(resultCache);
			return;
		}

		asyncPromise.get(new ValueCallback<ResultType>() {

			@Override
			public void onFailure(Throwable t) {
				synchronized (failureCache) {
					failureCache = t;
				}

				synchronized (deferredCalls) {
					cachedCalls = new ArrayList<ValueCallback<ResultType>>(
							deferredCalls);
				}

				for (ValueCallback<ResultType> deferredCb : deferredCalls) {
					deferredCb.onSuccess(resultCache);
				}

				callback.onFailure(t);
			}

			@Override
			public void onSuccess(ResultType value) {

			}
		});

	}

}
