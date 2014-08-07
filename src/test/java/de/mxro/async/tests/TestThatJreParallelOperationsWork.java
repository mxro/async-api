package de.mxro.async.tests;

import org.junit.Test;

import de.mxro.async.AsyncPromise;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.AsyncJre;

public class TestThatJreParallelOperationsWork {

	@Test
	public void test_it() {
		
		
		AsyncJre.promise(new AsyncPromise<String>() {

			@Override
			public void get(ValueCallback<String> callback) {
				new Thread() {

					@Override
					public void run() {
						Thread.sleep(new Random());
					}
					
					
					
				}.start();
			}
		});
		
	}
	
}
