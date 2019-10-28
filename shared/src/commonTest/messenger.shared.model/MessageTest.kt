package messenger.shared.model

import kotlin.test.Test
import kotlin.test.assertEquals

class MessageTest {

	@Test
	fun test() {
		assertEquals(
				"Message(login=snicoll, lastName=Hello)",
				Message("snicoll", "Hello").toString()
		)
	}
}