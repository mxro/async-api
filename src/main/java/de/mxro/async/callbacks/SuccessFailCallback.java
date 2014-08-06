/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.async.callbacks;

public interface SuccessFailCallback<GError> {
	public void onSuccess();
	public void onFailure(GError message);
}
