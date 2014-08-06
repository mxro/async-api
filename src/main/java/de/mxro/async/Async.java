package de.mxro.async;

import java.util.List;

import de.mxro.async.callbacks.ListCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.flow.CallbackMap;
import de.mxro.fn.Calculation;

public class Async {

	public static <InputType, ResultType> void map(List<InputType> inputs,
			Calculation<InputType, ValueCallback<ResultType>> operation,
			ListCallback<ResultType> callback) {

		final CallbackMap<InputType, ResultType> callbackMap = new CallbackMap<InputType, ResultType>(
				inputs, callback);

	}

}
