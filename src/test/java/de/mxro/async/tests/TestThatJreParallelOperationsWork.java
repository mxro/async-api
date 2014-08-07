package de.mxro.async.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import de.mxro.async.PotentialPromise;
import de.mxro.async.Promise;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.AsyncJre;

public class TestThatJreParallelOperationsWork {

	private final class RandomlyDelayedPromise implements PotentialPromise<String> {
		@Override
		public void get(final ValueCallback<String> callback) {
			new Thread() {

				@Override
				public void run() {
					int delay = new Random().nextInt(10) + 1;
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}

					callback.onSuccess("Completed after delay: " + delay);
				}

			}.start();
		}
	}

	@Test
	public void test_it() {

		List<Promise<String>> promises = new ArrayList<Promise<String>>();

		for (int i = 1; i <= 50; i++) {
			Promise<String> p = AsyncJre.promise(new RandomlyDelayedPromise());
			promises.add(p);
		}
		
		AsyncJre.parallel(promises);

	}
}
