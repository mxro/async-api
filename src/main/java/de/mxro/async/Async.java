package de.mxro.async;

import java.util.List;

import de.mxro.async.callbacks.ListCallback;
import de.mxro.async.flow.CallbackMap;
import de.mxro.async.internal.PromiseImpl;
import de.mxro.fn.Closure2;

public class Async {

	/**
	 * <p>
	 * Will apply the <b>asynchronous</b> operation operation to all inputs and
	 * call the callback once all operations are completed.
	 * <p>
	 * Callback is also called upon the first operation which fails.
	 * <p>
	 * ValueCallback must be called in closure.
	 * 
	 * @param inputs
	 * @param operation
	 * @param callback
	 */
	public static <InputType, ResultType> void map(List<InputType> inputs,
			Operation<InputType, ResultType> operation,
			ListCallback<ResultType> callback) {

		final CallbackMap<InputType, ResultType> callbackMap = new CallbackMap<InputType, ResultType>(
				inputs, callback);

		for (InputType input : inputs) {
			operation.apply(input, callbackMap.createCallback(input));
		}

	}

	public static <ResultType> Promise<ResultType> promise(
			Deferred<ResultType> promise) {
		return new PromiseImpl<ResultType>(promise);
	}

	public static <ResultType1, ResultType2> void sequential(
			Deferred<ResultType1> deferred1, Deferred<ResultType2> deferred2,
			Closure2<ResultType1, ResultType2> closure) {

	}

}
