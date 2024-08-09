package com.example.demo
import java.util.*
import org.springframework.data.annotation.Id
import org.springframework.data.repository.CrudRepository
import kotlin.jvm.optionals.toList

import org.springframework.data.relational.core.mapping.Table
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.jdbc.core.query
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*
import java.util.UUID

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
interface MessageRepository : CrudRepository<Message, String>

@Table("MESSAGE")
data class Message(@Id var id : String? , val text : String)

@Service
class MessageService(val db: MessageRepository) {

	fun findMessages(): List<Message> = db.findAll().toList()

	fun findMessageById(id: String): List<Message> = db.findById(id).toList()

	fun save(message: Message) {
		val id = message.id ?: UUID.randomUUID().toString()
		db.save(message)
	}

	fun <T : Any> Optional<out T>.toList() : List<T> =
		if(isPresent) listOf(get()) else emptyList()
}


@RestController
class MessageController(val service: MessageService) {
	@GetMapping("/")
	fun index(): List<Message> = service.findMessages()

	@GetMapping("/{id}")
	fun index(@PathVariable id : String): List<Message> = service.findMessageById(id)

	@PostMapping("/")
	fun post(@RequestBody message: Message) {
		service.save(message)
	}
}







//data class  Message(val id : String? , val text : String)


