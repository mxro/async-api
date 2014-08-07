package de.mxro.async.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.mxro.async.Deferred;
import de.mxro.async.Promise;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.fn.Closure;

public class PromiseImpl<ResultType> implements Promise<ResultType> {

	private final Deferred<ResultType> asyncPromise;

	private final List<ValueCallback<ResultType>> deferredCalls;

	private Value<ResultType> resultCache;
	private Boolean isRequesting;
	private Value<Throwable> failureCache;
	private List<Closure<Throwable>> exceptionCatchers;

	
	
	@Override
	public void get(ValueCallback<ResultType> callback) {
		requestResult(callback);
	}

	private final void requestResult(final ValueCallback<ResultType> callback) {

		final boolean triggerOnFailure;
		final boolean triggerOnSuccess;
		synchronized (failureCache) {
			triggerOnFailure = failureCache.get() != null;

			if (!triggerOnFailure) {

				synchronized (resultCache) {
					triggerOnSuccess = resultCache.get() != null;

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
			callback.onFailure(failureCache.get());
			return;
		}

		if (triggerOnSuccess) {
			callback.onSuccess(resultCache.get());
			return;
		}

		asyncPromise.get(new ValueCallback<ResultType>() {

			@Override
			public void onFailure(Throwable t) {
				final List<ValueCallback<ResultType>> cachedCalls;
				synchronized (failureCache) {
					failureCache.set(t);

					synchronized (deferredCalls) {
						cachedCalls = new ArrayList<ValueCallback<ResultType>>(
								deferredCalls);
					}
				}

				for (ValueCallback<ResultType> deferredCb : cachedCalls) {
					deferredCb.onFailure(t);
				}

				callback.onFailure(t);
			}

			@Override
			public void onSuccess(ResultType value) {
				final List<ValueCallback<ResultType>> cachedCalls;
				synchronized (failureCache) {

					assert failureCache.get() != null;
					synchronized (resultCache) {
						resultCache.set(value);

						synchronized (deferredCalls) {
							cachedCalls = new ArrayList<ValueCallback<ResultType>>(
									deferredCalls);
						}
					}

				}

				for (ValueCallback<ResultType> deferredCb : cachedCalls) {
					deferredCb.onSuccess(value);
				}

				callback.onSuccess(value);
			}
		});

	}

	@Override
	public ResultType get() {
		
		get(new Closure<ResultType>() {

			@Override
			public void apply(ResultType o) {

			}
		});
		
		synchronized (this.failureCache) {
			if (this.failureCache.get() != null) {
				throw new RuntimeException(this.failureCache.get());
			}
		}

	    ResultType resultCache2;
		synchronized (this.resultCache) {
			resultCache2 = this.resultCache.get();
			
		}
		return resultCache2;
	}

	@Override
	public void catchExceptions(Closure<Throwable> closure) {
		assert this.resultCache.get() == null && this.failureCache.get() == null;
		
		synchronized (exceptionCatchers) {
			exceptionCatchers.add(closure);
		}
	}

	@Override
	public void get(final Closure<ResultType> closure) {
		requestResult(new ValueCallback<ResultType>() {

			@Override
			public void onFailure(Throwable t) {
				final ArrayList<Closure<Throwable>> catchers;
				synchronized (exceptionCatchers) {
					catchers = new ArrayList<Closure<Throwable>>(exceptionCatchers);
				}
				for (Closure<Throwable> exceptionCatcher: catchers) {
					exceptionCatcher.apply(t);
				}
			}

			@Override
			public void onSuccess(ResultType value) {
				closure.apply(value);
			}
		});
	}

	public PromiseImpl(Deferred<ResultType> asyncPromise) {
		super();
		this.asyncPromise = asyncPromise;
		this.deferredCalls = new LinkedList<ValueCallback<ResultType>>();
		this.resultCache = new Value<ResultType>(null);
		this.failureCache = new Value<Throwable>(null);
		this.exceptionCatchers = new LinkedList<Closure<Throwable>>();
		this.isRequesting = false;

	}

}
