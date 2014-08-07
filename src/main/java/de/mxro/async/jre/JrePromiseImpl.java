package de.mxro.async.jre;

import de.mxro.async.AsyncPromise;
import de.mxro.async.internal.PromiseImpl;

public class JrePromiseImpl<ResultType> extends PromiseImpl<ResultType> {

	
	private final Object monitor;
	
	@Override
	public ResultType get() {
		
		ResultType result = super.get();
		
		if (result != null) {
			return result;
		}
		
		synchronized (monitor) {
			monitor.wait();
		}
		
		return resultType;
	}

	public JrePromiseImpl(AsyncPromise<ResultType> asyncPromise) {
		super(asyncPromise);
		this.monitor = new Object();
	}

}
