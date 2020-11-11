package io.floodplain.r2dbcdemo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux


data class FilmWithActors(val id: Short,
                     val title: String,
                     val description: String,
                    val actors: List<String>
)

@Repository
class FilmRepository(private val client: DatabaseClient) {

    fun filmsAsFlux(): Flux<MutableMap<String, Any>> =
            client.execute("SELECT film_id,title,description from film")
                    .fetch()
                    .all()

    fun filmsWithActors(): Flow<FilmWithActors> =
            client.execute("SELECT film_id,title,description from film")
                    .fetch()
                    .all()
                    .asFlow()
                    .map { row->
                        val filmId = row["film_id"] as Integer
                        val l = actorsOfFilm(filmId.toShort())
                                .map { id -> nameOfActor(id)}
                                .toList()
                        FilmWithActors(filmId.toShort(),row["title"] as String, row["description"] as String,l)
                    }


    fun films(): Flow<MutableMap<String, Any>> =
            client.execute("SELECT film_id,title,description from film")
                    .fetch()
                    .all()
                    .asFlow()

    fun actorsOfFilm(id: Short): Flow<Short> =
            client.execute("SELECT actor_id from film_actor where film_id = $1")
                    .bind(0,id)
                    .fetch()
                    .all()
                    .map { it.get("actor_id") as Short }
                    .asFlow()

    suspend fun nameOfActor(id: Short): String =
            client.execute("SELECT first_name,last_name from actor where actor_id = $1")
                    .bind(0,id)
                    .fetch()
                    .first()
                    .map { "${it["first_name"]} ${it["last_name"]}"}
                    .awaitFirst()

}