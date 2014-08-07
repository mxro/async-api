package de.mxro.async.internal;

public class Value<T> {

	private T value;
	
	public synchronized T get() {
		return value;
	}

	public synchronized Value<T> set(T value) {
		this.value = value;
		return this;
	}
	
	public Value(T value) {
		super();
		this.value = value;
	}
	
	
	
}
