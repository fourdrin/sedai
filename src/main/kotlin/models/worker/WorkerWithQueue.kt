package app.fourdrin.sedai.models.worker

import java.io.Closeable
import java.util.*

interface WorkerWithQueue<T: Work>: Closeable {
    val workerQueue: AbstractQueue<T>
    fun start()
}
