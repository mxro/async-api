package de.mxro.async;

import de.mxro.fn.Closure;

public interface Promise<ResultType> {

	
	public ResultType get();
	
	public void catchExceptions(Closure<Throwable> closure);
	
	public void get(Closure<ResultType> closure);
	
	
}