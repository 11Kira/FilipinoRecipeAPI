package com.kira.api.FilipinoRecipeAPI

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
@EnableMongoRepositories(basePackages = ["com.kira.api.FilipinoRecipeAPI.repository"])
class FilipinoRecipeApiApplication

fun main(args: Array<String>) {
	runApplication<FilipinoRecipeApiApplication>(*args)
}