package de.mxro.async.jre;

import de.mxro.async.AsyncPromise;
import de.mxro.async.internal.PromiseImpl;

public class JrePromiseImpl<ResultType> extends PromiseImpl<ResultType> {

	
	
	
	@Override
	public ResultType get() {
		
		ResultType result = super.get();
		
		if (result != null) {
			return result;
		}
		
		
		
		return resultType;
	}

	public JrePromiseImpl(AsyncPromise<ResultType> asyncPromise) {
		super(asyncPromise);
	}

}
