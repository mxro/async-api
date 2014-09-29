package de.mxro.async.callbacks;

public interface BooleanCallback extends FailureCallback {

    /**
     * Asynchronous operation successfully completed.
     * 
     * @param value
     */
    public void onSuccess(boolean value);

}
