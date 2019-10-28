package messenger.backend

import kotlinx.coroutines.runBlocking
import messenger.backend.repository.UserRepository
import messenger.shared.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.*
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserIntegrationTests @Autowired constructor(
		val client: WebTestClient,
		val repository: UserRepository) {

	@Test
	fun `Find all users`()  {
		runBlocking {
			client.get().uri("/user/").exchange().expectBodyList<User>().contains(
					User("snicoll", "Stéphane", "Nicoll"),
					User("sdeleuze", "Sébastien", "Deleuze"),
					User("bclozel", "Brian", "Clozel")
			)
		}
	}

	@Test
	fun `Find one user`()  {
		runBlocking {
			client.get().uri("/user/bclozel").exchange().expectBody<User>().isEqualTo(User("bclozel", "Brian", "Clozel"))
		}
	}

	@Test
	fun `Find one user with wrong login`()  {
		runBlocking {
			client.get().uri("/user/foo").exchange().expectStatus().isNotFound()
		}
	}

	@Test
	fun `Create user`()  {
		runBlocking {
			client.post().uri("/user/").bodyValue(User("sbasle", "Simon", "Baslé")).exchange().expectStatus().isCreated()
			assertThat(repository.findOne("sbasle")).isNotNull()
			repository.delete("sbasle")
		}
	}

	@Test
	fun `Create existing user`()  {
		runBlocking {
			client.post().uri("/user/").bodyValue(User("bclozel", "Brian", "Clozel")).exchange().expectStatus().is5xxServerError()
		}
	}

	@Test
	fun `Delete user`()  {
		runBlocking {
			repository.create(User("sbasle", "Simon", "Baslé"))
			client.delete().uri("/user/sbasle").exchange().expectStatus().isOk()
			assertThat(repository.findOne("sbasle")).isNull()
		}
	}
}