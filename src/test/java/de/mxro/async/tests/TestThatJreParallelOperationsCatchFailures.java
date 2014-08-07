package de.mxro.async.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import de.mxro.async.PromiseBlueprint;
import de.mxro.async.Promise;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.AsyncJre;


public class TestThatJreParallelOperationsCatchFailures {

	private final class RandomlyFailingPromise implements PromiseBlueprint<String> {

		@Override
		public void get(final ValueCallback<String> callback) {
			
			new Thread() {

				@Override
				public void run() {
					
					if (new Random().nextInt(2) == 1) {
						callback.onSuccess("Got it.");
						return;
					}
					
					callback.onFailure(new Exception("So failed!"));
					
				}
				
				
				
			}.start();
			
		}
		
		
		
	}
	
	@Test(expected = Exception.class)
	public void test_it() {
		List<Promise<String>> promises = new ArrayList<Promise<String>>();

		for (int i = 1; i <= 50; i++) {
			Promise<String> p = AsyncJre.promise(new RandomlyFailingPromise());
			promises.add(p);
		}
		
		AsyncJre.parallel(promises);
		
	}
	
}
