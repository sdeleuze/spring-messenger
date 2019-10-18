package messenger.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MessengerApplication

fun main(args: Array<String>) {
	runApplication<MessengerApplication>(*args)
}
