package de.mxro.async.tests

import de.mxro.async.jre.AsyncJre
import de.mxro.fn.Success
import de.oehme.xtend.junit.JUnit
import org.junit.Test

@JUnit
class TestWaitFor {
	
	@Test
	def void test() {
		
		AsyncJre.waitFor([cb | cb.onSuccess(Success.INSTANCE)])
		
	}
	
	
}