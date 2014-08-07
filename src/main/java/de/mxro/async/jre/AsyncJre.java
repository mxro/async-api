package de.mxro.async.jre;

import de.mxro.async.AsyncPromise;
import de.mxro.async.Promise;
import de.mxro.async.jre.internal.JrePromiseImpl;

public class AsyncJre {
	
	
	public static <ResultType> Promise<ResultType> promise(
			AsyncPromise<ResultType> promise) {
		return new JrePromiseImpl<ResultType>(promise);
	}
	
	
	
}
