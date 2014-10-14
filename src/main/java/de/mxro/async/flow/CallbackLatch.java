/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.async.flow;

import de.mxro.async.Value;

/**
 * Allows to wait for a specified number of asynchronous operations.
 * 
 * @see {@link ListCallbackJoiner}
 * @author <a href="http://www.mxro.de/">Max Erik Rohde</a>
 * 
 *         Copyright Max Erik Rohde 2011. All rights reserved.
 */
public abstract class CallbackLatch {

	final int expected;
	Value<Integer> received;
	volatile Value<Boolean> failed;

	
	/**
	 * This method is called when for all expected callbacks
	 * {@link #registerSuccess()} has been called.
	 */
	public abstract void onCompleted();

	public abstract void onFailed(Throwable t);

	/**
	 * Call this method when an expected callback has been received.
	 */
	public void registerSuccess() {
		synchronized (received) {
			synchronized (failed) {
				received.set(received.get() + 1);
				if (!failed.get() && received.get() == expected) {
					onCompleted();
				}
			}
		}
	}

	public void registerFail(final Throwable t) {
		synchronized (received) {
			synchronized (failed) {
				if (!failed.get()) {
					failed.set(true);
					onFailed(t);
				}
				failed.set(true);
			}
		}

	}

	public CallbackLatch(final int expected) {
		super();
		assert expected >= 0;

		this.expected = expected;
		this.failed = new Value<Boolean>(false);
		this.received = new Value<Integer>(0);

		if (expected == 0) {
			onCompleted();
		}
	}

}
