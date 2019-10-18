package messenger.backend.web

import messenger.backend.repository.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HtmlController(private val repository: UserRepository) {

	@GetMapping("/")
	fun index(model: Model): String {
		model["users"] = repository.findAll()
		return "index"
	}
}