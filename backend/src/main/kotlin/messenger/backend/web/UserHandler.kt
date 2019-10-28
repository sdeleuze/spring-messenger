package messenger.backend.web

import messenger.backend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*

class UserHandler(private val repository: UserRepository) {

	suspend fun findAll(request: ServerRequest) =
			ok().bodyAndAwait(repository.findAll())

	suspend fun findOne(request: ServerRequest) =
			repository.findOne(request.pathVariable("login"))
					?.let { ok().bodyValueAndAwait(it) }
					?: notFound().buildAndAwait()

	suspend fun delete(request: ServerRequest) =
			repository.delete(request.pathVariable("login")).let { ok().buildAndAwait() }

	suspend fun create(request: ServerRequest) =
			repository.create(request.awaitBody()).let { status(HttpStatus.CREATED).buildAndAwait() }
}
