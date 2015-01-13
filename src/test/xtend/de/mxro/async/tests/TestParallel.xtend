package de.mxro.async.tests

import de.mxro.async.AsyncCommon
import de.mxro.async.Operation
import de.mxro.async.callbacks.ValueCallback
import de.mxro.async.jre.Async
import de.oehme.xtend.junit.JUnit
import java.util.ArrayList
import org.junit.Test

@JUnit
class TestParallel {

	@Test
	def void test_with_list() {

		val ops = new ArrayList<Operation<String>>

		ops.add(
			[ cb |
				cb.onSuccess("123")
			])

		ops.add(
			[ cb |
				cb.onSuccess("456")
			])

		val res = Async.waitFor(
			[ cb |
				AsyncCommon.parallel(ops, cb)
			])

		(res.size == 2) => true

	}

	@Test
	def void test_with_array() {

		val ops = #[
			[ ValueCallback<String> cb |
				cb.onSuccess("123")
			],
			[ ValueCallback<String> cb |
				cb.onSuccess("456")
			]
		]

		val res = Async.waitFor(
			[ cb |
				AsyncCommon.parallel(ops, cb)
			])

		(res.size == 2) => true

	}

}
