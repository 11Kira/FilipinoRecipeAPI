package com.kira.api.FilipinoRecipeAPI

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories(basePackages = ["com.kira.api.FilipinoRecipeAPI.database.repository"])
class FilipinoRecipeApiApplication

fun main(args: Array<String>) {
	runApplication<FilipinoRecipeApiApplication>(*args)
}