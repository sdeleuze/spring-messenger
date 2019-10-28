package messenger.bot

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import messenger.shared.model.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class BotController {

	@FlowPreview
	@MessageMapping("bot.messages")
	fun messages(messages: Flow<Message>) = messages.flatMapMerge {
		flow {
			when (it.content.toLowerCase()) {
				"hello" -> emit(Message("Lionel", "Is it me you're looking for?"))
			}
		}
	}

	@MessageMapping("bot.broadcast")
	fun broadcast() = flow {
		while (true) {
			emit(Message("Bot", "Service message"))
			delay(5000)
		}
	}

}