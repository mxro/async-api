package de.mxro.async.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mxro.async.Aggregator;
import de.mxro.async.Value;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.fn.collections.CollectionsUtils;

public final class CallbackAggregator<V> implements Aggregator<V> {

    final int expected;

    final ValueCallback<List<V>> callback;

    Value<Integer> callbacksDefined;
    Map<Integer, V> resultsMap;
    List<V> results;
    Value<Boolean> exceptionReceived;
    Throwable exception;

    @Override
    public final ValueCallback<V> createCallback() {

        synchronized (callbacksDefined) {

            final int callbackIdx = callbacksDefined.get();
            callbacksDefined.set(callbackIdx + 1);

            if (callbackIdx > expected - 1) {
                throw new IllegalStateException("Too many callbacks defined.");
            }

            return new ValueCallback<V>() {

                @Override
                public void onFailure(final Throwable t) {
                    synchronized (exceptionReceived) {
                        if (exceptionReceived.get()) {
                            throw new RuntimeException(
                                    "Another exception already received. Cannot sent exception to callback.", t);
                        }

                        exceptionReceived.set(true);

                        callback.onFailure(t);
                    }
                }

                @Override
                public void onSuccess(final V value) {

                    boolean call = false;
                    synchronized (resultsMap) {
                        resultsMap.put(callbackIdx, value);

                        if (CollectionsUtils.isMapComplete(resultsMap, expected)) {
                            call = true;
                        }
                    }
                    // so that it's out of the synchronized block.
                    if (call) {

                    }

                    synchronized (results) {
                        results.add(value);
                    }

                }
            };
        }

    }

    public CallbackAggregator(final int expected, final ValueCallback<List<V>> callback) {
        super();
        this.expected = expected;
        this.callback = callback;

        this.exceptionReceived = new Value<Boolean>(false);
        this.callbacksDefined = new Value<Integer>(0);
        this.results = new ArrayList<V>(expected);
        this.resultsMap = new HashMap<Integer, V>();
        this.exception = null;

    }
}
