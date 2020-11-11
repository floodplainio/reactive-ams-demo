package io.floodplain.r2dbcdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class R2dbcdemoApplication

fun main(args: Array<String>) {
    runApplication<R2dbcdemoApplication>(*args)
}
