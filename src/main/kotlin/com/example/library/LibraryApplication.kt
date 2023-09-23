package com.example.library

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class LibraryApplication

fun main(args: Array<String>) {
	runApplication<LibraryApplication>(*args)
}
