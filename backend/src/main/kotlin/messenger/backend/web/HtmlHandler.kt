package messenger.backend.web

import messenger.backend.repository.UserRepository
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.renderAndAwait

class HtmlHandler(private val userRepository: UserRepository) {

	suspend fun index(request: ServerRequest) =
			ServerResponse.ok().renderAndAwait("index", mapOf("users" to userRepository.findAll()))
}