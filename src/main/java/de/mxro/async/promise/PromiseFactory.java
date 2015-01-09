package de.mxro.async.promise;

public interface PromiseFactory {

    public <T> Promise<T> promise(Deferred<T> deferred);

}
