package messenger.backend.repository

import kotlinx.coroutines.runBlocking
import messenger.shared.model.User
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.data.r2dbc.core.*

class UserRepository(private val client: DatabaseClient) {

	fun findAll() = client.select().from("users").asType<User>().fetch().flow()

	suspend fun findOne(login: String) =
			client.execute("SELECT * FROM users WHERE login = :login").bind("login", login).asType<User>().fetch().awaitOneOrNull()

	suspend fun create(user: User)=
			client.insert().into<User>().table("users").using(user).await()

	suspend fun delete(login: String) =
			client.execute("DELETE FROM users WHERE login = :login").bind("login", login).await()

	suspend fun deleteAll() =
			client.execute("DELETE FROM users").await()

	fun init() = runBlocking {
		client.execute("CREATE TABLE IF NOT EXISTS users (login varchar PRIMARY KEY, firstname varchar, lastname varchar);").await()
		deleteAll()
		create(User("snicoll", "Stéphane", "Nicoll"))
		create(User("sdeleuze", "Sébastien", "Deleuze"))
		create(User("bclozel", "Brian", "Clozel"))
	}

}