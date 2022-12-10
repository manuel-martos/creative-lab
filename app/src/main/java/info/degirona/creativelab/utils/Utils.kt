package info.degirona.creativelab.utils

import io.reactivex.functions.Consumer

fun <T> ((T) -> Unit).asConsumer(): Consumer<T> = Consumer { t -> this@asConsumer.invoke(t) }
