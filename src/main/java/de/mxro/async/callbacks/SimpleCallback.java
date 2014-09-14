package de.mxro.async.callbacks;

/**
 * <p>
 * A very basic callback which allows for the caller to be notified of the
 * completion of the operation.
 * <p>
 * This callback extends {@link FailureCallback} thus failures can be reported
 * through the method {@link #onFailure(Throwable)}.
 * <p>
 * For asynchronous operations that return a value, use {@link ValueCallback}
 * instead.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public interface SimpleCallback extends FailureCallback {

    /**
     * Called when the asynchronous operation has completed successfully.
     */
    public void onSuccess();

}
