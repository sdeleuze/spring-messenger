package messenger.backend.web

import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.*
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfiguration {

	@FlowPreview
	@Bean
	fun routes(userHandler: UserHandler, messageHandler: MessageHandler, htmlHandler: HtmlHandler) = coRouter {
		GET("/", htmlHandler::index)
		"/user".nest {
			GET("/", userHandler::findAll)
			GET("/{login}", userHandler::findOne)
			DELETE("/{login}", userHandler::delete)
			POST("/", userHandler::create)
		}
		"/message".nest {
			POST("/send", messageHandler::send)
			GET("/stream", accept(TEXT_EVENT_STREAM), messageHandler::stream)
		}
	}
}

