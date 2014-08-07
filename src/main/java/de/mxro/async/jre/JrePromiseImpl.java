package de.mxro.async.jre;

import java.util.concurrent.CountDownLatch;

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
		
		CountDownLatch latch = new CountDownLatch(1);
		
		return resultType;
	}

	public JrePromiseImpl(AsyncPromise<ResultType> asyncPromise) {
		super(asyncPromise);
		this.monitor = new Object();
	}

}
