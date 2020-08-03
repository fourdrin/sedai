package app.fourdrin.sedai.worker

import app.fourdrin.sedai.models.worker.Work
import java.io.Closeable
import java.util.*

interface Worker<T: Work>: Closeable {
    val queue: AbstractQueue<T>
    fun start()
}
