package messenger.backend

import kotlinx.coroutines.runBlocking
import messenger.backend.repository.UserRepository
import messenger.shared.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.*
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.expectBodyList

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserIntegrationTests {

	private val client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()

	private lateinit var context: ConfigurableApplicationContext

	@BeforeAll
	fun setup() {
		context = app.run()
	}

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
			val repository = context.getBean<UserRepository>()
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
			val repository = context.getBean<UserRepository>()
			repository.create(User("sbasle", "Simon", "Baslé"))
			client.delete().uri("/user/sbasle").exchange().expectStatus().isOk()
			assertThat(repository.findOne("sbasle")).isNull()
		}
	}

	@AfterAll
	fun tearDown() {
		context.close()
	}
}