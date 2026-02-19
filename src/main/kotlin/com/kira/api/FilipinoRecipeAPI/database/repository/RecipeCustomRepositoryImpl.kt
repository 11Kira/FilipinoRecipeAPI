package com.kira.api.FilipinoRecipeAPI.database.repository

import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class RecipeCustomRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : RecipeCustomRepository {

    override fun searchRecipes(
        query: String?,
        categoryList: List<String>?,
        proteinList: List<String>?, // accept list
        difficultyList: List<String>?,
        maxCookingTime: Int?,
        pageable: Pageable
    ): Page<Recipe> {

        val criteriaList = mutableListOf<Criteria>()

        query?.takeIf { it.isNotBlank() }?.let { q ->
            val regex = Regex.escape(q)
            criteriaList.add(
                Criteria().orOperator(
                    Criteria.where("title").regex(regex, "i"),
                    Criteria.where("description").regex(regex, "i")
                )
            )
        }

        categoryList?.takeIf { it.isNotEmpty() }?.let { list ->
            criteriaList.add(
                Criteria.where("category").`in`(list)
            )
        }

        proteinList?.takeIf { it.isNotEmpty() }?.let { list ->
            criteriaList.add(
                Criteria.where("protein").`in`(list)
            )
        }

        difficultyList?.takeIf { it.isNotEmpty() }?.let { list ->
            criteriaList.add(
                Criteria.where("difficulty").`in`(list)
            )
        }

        maxCookingTime?.let {
            criteriaList.add(Criteria.where("cookingTime").lte(it))
        }

        val criteria = if (criteriaList.isNotEmpty())
            Criteria().andOperator(*criteriaList.toTypedArray())
        else
            Criteria()

        val queryObj = Query(criteria).with(pageable)

        val recipes = mongoTemplate.find(queryObj, Recipe::class.java)
        val total = mongoTemplate.count(
            Query(criteria),
            Recipe::class.java
        )

        return PageImpl(recipes, pageable, total)
    }
}