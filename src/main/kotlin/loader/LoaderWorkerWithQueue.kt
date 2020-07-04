package app.fourdrin.sedai.loader

import app.fourdrin.sedai.models.LoaderWork
import app.fourdrin.sedai.models.WorkerWithQueue
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object LoaderWorkerWithQueue : WorkerWithQueue<LoaderWork> {
    override val workerQueue = ConcurrentLinkedQueue<LoaderWork>()
    private val loaderExecutor = Executors.newSingleThreadScheduledExecutor()

    override fun start() {
        loaderExecutor.scheduleAtFixedRate({
            println(workerQueue)
        } , 0, 5, TimeUnit.SECONDS)
    }

    override fun close() {
        loaderExecutor.shutdown()
    }
}