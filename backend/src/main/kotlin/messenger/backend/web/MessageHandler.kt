package messenger.backend.web

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asFlow
import messenger.shared.model.Message
import org.springframework.http.MediaType.*
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.connectTcpAndAwait
import org.springframework.messaging.rsocket.dataWithType
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.ReplayProcessor

class MessageHandler(private val builder: RSocketRequester.Builder) {

	private val processor = ReplayProcessor.create<Message>(0)

	private val sink = processor.sink()

	suspend fun send(request: ServerRequest): ServerResponse {
		val message = request.awaitBody<Message>()
		sink.next(message)
		return ok().buildAndAwait()
	}

	@FlowPreview
	suspend fun stream(request: ServerRequest): ServerResponse {
		val requester = builder.dataMimeType(APPLICATION_CBOR).connectTcpAndAwait("localhost", 9898)
		val replies = requester.route("bot.messages").dataWithType(processor).retrieveFlow<Message>()
		val broadcast = requester.route("bot.broadcast").retrieveFlow<Message>()
		val messages = flowOf(replies, processor.asFlow(), broadcast).flattenMerge()
		return ok().sse().bodyAndAwait(messages)
	}
}