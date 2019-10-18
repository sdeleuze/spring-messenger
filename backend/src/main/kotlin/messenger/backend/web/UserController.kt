package messenger.backend.web

import messenger.backend.repository.UserRepository
import messenger.backend.model.User
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController @RequestMapping("/user")
class UserController(private val repository: UserRepository) {

	@GetMapping("/")
	fun findAll() = repository.findAll()

	@GetMapping("/{login}")
	suspend fun findOne(@PathVariable login: String) = repository.findOne(login) ?: throw ResponseStatusException(NOT_FOUND, "Wrong user login provided")

	@DeleteMapping("/{login}")
	suspend fun delete(@PathVariable login: String) = repository.delete(login)

	@PostMapping("/")
	@ResponseStatus(CREATED)
	suspend fun create(@RequestBody user: User) = repository.create(user)

}