package messenger.backend

import messenger.backend.repository.UserRepository
import messenger.backend.web.HtmlHandler
import messenger.backend.web.MessageHandler
import messenger.backend.web.UserHandler
import messenger.backend.web.routes
import org.springframework.boot.WebApplicationType
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.configuration
import org.springframework.fu.kofu.r2dbc.r2dbcH2
import org.springframework.fu.kofu.webflux.mustache
import org.springframework.fu.kofu.webflux.webFlux
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.http.codec.cbor.Jackson2CborEncoder
import org.springframework.http.codec.cbor.Jackson2CborDecoder
import com.fasterxml.jackson.dataformat.cbor.CBORFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder


val dataConfig = configuration {
	beans {
		bean<UserRepository>()
	}
	listener<ApplicationReadyEvent> {
		ref<UserRepository>().init()
	}
	r2dbcH2()
}

val webConfig = configuration {
	beans {
		bean(::routes)
		bean<HtmlHandler>()
		bean<UserHandler>()
		bean<MessageHandler>()
	}
	webFlux {
		codecs {
			resource()
			jackson {
				indentOutput = true
			}
		}
		mustache()
	}
}

val rsocketConfig = configuration {
	beans {
		bean { RSocketRequester.builder().rsocketStrategies {
			val objectMapper: ObjectMapper = ref<Jackson2ObjectMapperBuilder>().createXmlMapper(false).factory(CBORFactory()).build()
			it.decoder(Jackson2CborDecoder(objectMapper, MediaType.APPLICATION_CBOR))
			it.encoder(Jackson2CborEncoder(objectMapper, MediaType.APPLICATION_CBOR))
		} }
	}
}

val app = application(WebApplicationType.REACTIVE) {
	enable(dataConfig)
	enable(webConfig)
	enable(rsocketConfig)
}

fun main(args: Array<String>) {
	app.run(args)
}
