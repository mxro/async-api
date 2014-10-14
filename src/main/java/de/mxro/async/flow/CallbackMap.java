/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.async.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mxro.async.Value;
import de.mxro.async.callbacks.ListCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.fn.collections.CollectionsUtils;
import de.mxro.fn.collections.IdentityArrayList;

/**
 * This utility class supports in writing Java in a more asynchronous style.
 * This class allows to <i>collect</i> the results from various callbacks, which
 * result from calling a method on all elements of a list. The callback provided
 * by this class will only be called if all the callbacks for the individual
 * list elements have been executed.
 * 
 * @author Max Rohde
 * 
 * @param <GInput>
 * @param <GOutput>
 */
public class CallbackMap<GInput, GOutput> {
	final Map<Integer, GOutput> responseMap;
	final List<GInput> messages;
	final int expectedSize;
	final ListCallback<GOutput> callback;

	
	public ValueCallback<GOutput> createCallback(final GInput message) {
		return new ValueCallback<GOutput>() {

			@Override
			public void onSuccess(final GOutput response) {
				synchronized (responseMap) {
					responseMap.put(messages.indexOf(message), response);

					if (CollectionsUtils.isMapComplete(responseMap, expectedSize)) {
						final List<GOutput> localResponses = CollectionsUtils
								.toOrderedList(responseMap);

						callback.onSuccess(localResponses);
					}
				}
			}

			Value<Boolean> failureReported = new Value<Boolean>(false);

			@Override
			public void onFailure(final Throwable t) {
				// failure should be reported only once.
				synchronized (failureReported) {
					if (!failureReported.get()) {
						callback.onFailure(t);
					} else {
						throw new RuntimeException(t); // TODO maybe disable ...
					}
					failureReported.set(true);
				}
			}

		};
	}

	public CallbackMap(final List<GInput> messages,
			final ListCallback<GOutput> callback) {
		super();
		this.messages = new IdentityArrayList<GInput>(messages);
		this.responseMap = new HashMap<Integer, GOutput>();
		expectedSize = messages.size();
		this.callback = callback;

		if (expectedSize == 0) {
			this.callback.onSuccess(new ArrayList<GOutput>(0));
		}
	}

}
