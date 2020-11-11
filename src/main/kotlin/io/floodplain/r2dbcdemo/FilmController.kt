package io.floodplain.r2dbcdemo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux


@RestController
class FilmController(val repository: FilmRepository) {
    @GetMapping("/films")
    fun films(): Flow<Map<String, Any>> {
        return repository.films()

    }

    // Stream+json mime type allows streaming out the data
    @GetMapping("/filmsWithActors", produces = ["application/stream+json"])
    fun filmsWithActors(): Flow<FilmWithActors> {
        return repository.filmsWithActors()

    }

    @GetMapping("/actor/{id}")
    suspend fun actorName(@PathVariable id: Short): String {
        return repository.nameOfActor(id)

    }

    @GetMapping("/actors/{id}")
    suspend fun actors(@PathVariable id: Short): Flow<Short> {
        return repository.actorsOfFilm(id)
    }

    @GetMapping("/actorsWithName/{id}")
    suspend fun actorsWithName(@PathVariable id: Short): Flow<String> {
        return repository.actorsOfFilm(id).map (repository::nameOfActor)
    }


    @GetMapping("/filmsFlux")
    fun filmsFlux(): Flux<MutableMap<String, Any>> {
        return repository.filmsAsFlux()
    }

}