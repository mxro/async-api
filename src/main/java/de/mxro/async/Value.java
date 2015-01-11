package de.mxro.async;

public class Value<T> {

    private T value;

    public synchronized T get() {
        return value;
    }

    public synchronized Value<T> set(final T value) {
        this.value = value;
        return this;
    }

    public Value(final T value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "[(" + value + ") wrapped in (" + super.toString() + ")]";
    }

}
