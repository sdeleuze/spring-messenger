package messenger.backend.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfiguration {

	@Bean
	fun routes(userHandler: UserHandler, htmlHandler: HtmlHandler) = coRouter {
		GET("/", htmlHandler::index)
		"/user".nest {
			GET("/", userHandler::findAll)
			GET("/{login}", userHandler::findOne)
			DELETE("/{login}", userHandler::delete)
			POST("/", userHandler::create)
		}
	}
}

