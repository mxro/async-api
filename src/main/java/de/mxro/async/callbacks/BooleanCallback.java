package de.mxro.async.callbacks;

/**
 * <p>
 * A callback for asynchronous operations which return a boolean value.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public interface BooleanCallback extends FailureCallback {

    /**
     * Asynchronous operation successfully completed.
     * 
     * @param value
     */
    public void onSuccess(boolean value);

}
