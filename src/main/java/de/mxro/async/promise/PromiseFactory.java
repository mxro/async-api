package de.mxro.async.promise;

public interface PromiseFactory {

    public <T> Promise<T> create(Deferred<T> deferred);

}
