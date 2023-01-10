package bot.nektome.nektobot.util

import reactor.core.publisher.Flux
import kotlin.streams.toList

fun <T> Flux<T>.toTypedArray(): ArrayList<T> {
    return ArrayList(this.toStream().toList())
}