package messenger.bot

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.core.ParameterizedTypeReference
import org.springframework.messaging.rsocket.RSocketRequester

suspend inline fun <reified T : Any> RSocketRequester.RequestSpec.retrieveAndAwaitOrNull(): T? =
		retrieveMono(object : ParameterizedTypeReference<T>() {}).awaitFirstOrNull()

