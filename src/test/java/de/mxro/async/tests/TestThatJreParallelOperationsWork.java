package de.mxro.async.tests;

import java.util.Random;

import org.junit.Test;

import de.mxro.async.AsyncPromise;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.AsyncJre;

public class TestThatJreParallelOperationsWork {

	private final class AsyncPromiseImplementation implements
			AsyncPromise<String> {
		@Override
		public void get(final ValueCallback<String> callback) {
			new Thread() {

				@Override
				public void run() {
					int delay = new Random().nextInt(10)+1;
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					
					callback.onSuccess("Completed after delay: "+delay);
				}
				
				
				
			}.start();
		}
	}

	@Test
	public void test_it() {
		
		
		AsyncJre.promise(new AsyncPromiseImplementation());
		
	}
	
}
