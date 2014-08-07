/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.async.callbacks;

public abstract class SimpleCallback implements FailureCallback, ValueCallback<Void> {
	public abstract void onSuccess();

	@Override
	public void onSuccess(Void value) {
		onSuccess();
	}

	
	

}
