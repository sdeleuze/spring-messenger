package messenger.bot

import kotlinx.coroutines.runBlocking
import messenger.shared.model.Message
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.rsocket.context.LocalRSocketServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.messaging.rsocket.*


@SpringBootTest(properties = ["spring.rsocket.server.port=0"])
class BotApplicationTests(
		@Autowired val requesterBuilder: RSocketRequester.Builder,
		@LocalRSocketServerPort val port: Int) {

	@Test
	fun `Message with reply`() {
		runBlocking {
			val requester = requesterBuilder
					.dataMimeType(MediaType.APPLICATION_CBOR)
					.connectTcpAndAwait("localhost", port)
			val response = requester.route("bot.messages").data(Message("BotApplicationTests", "Hello")).retrieveAndAwaitOrNull<Message>()
			Assertions.assertThat(response).isEqualTo(Message("Lionel", "Is it me you're looking for?"))
		}
	}

	@Test
	fun `Message without reply`() {
		runBlocking {
			val requester = requesterBuilder
					.dataMimeType(MediaType.APPLICATION_CBOR)
					.connectTcpAndAwait("localhost", port)
			val response = requester.route("bot.messages").data(Message("BotApplicationTests", "Goodbye")).retrieveAndAwaitOrNull<Message>()
			Assertions.assertThat(response).isNull()
		}
	}

}