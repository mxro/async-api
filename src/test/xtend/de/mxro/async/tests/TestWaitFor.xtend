package de.mxro.async.tests

import de.mxro.fn.Success
import de.oehme.xtend.junit.JUnit
import org.junit.Test
import de.mxro.async.jre.Async

@JUnit
class TestWaitFor {
	
	@Test
	def void test() {
		
		Async.waitFor([cb | cb.onSuccess(Success.INSTANCE)])
		
	}
	
	
}