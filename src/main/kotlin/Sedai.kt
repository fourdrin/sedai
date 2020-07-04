package app.fourdrin.sedai

import app.fourdrin.sedai.ftp.FTPWorkerWithQueue
import app.fourdrin.sedai.loader.LoaderService
import app.fourdrin.sedai.loader.LoaderWorkerWithQueue
import app.fourdrin.sedai.models.WorkerWithQueue
import io.grpc.ServerBuilder
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.framework.recipes.leader.CancelLeadershipException
import org.apache.curator.framework.recipes.leader.LeaderSelector
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.curator.utils.CloseableUtils

const val SEDAI_GRPC_SERVER_HOST = "localhost"
const val SEDAI_GRPC_SERVER_PORT = 50051
const val SEDAI_ZK_CONNECTION_STRING = "localhost:2181"
const val SEDAI_ZK_LEADERSHIP_GROUP = "/fourdrin/sedai"
const val SEDAI_FTP_ROOT_DIRECTORY = "books-ftp"
const val SEDAI_PIPELINE_DIRECTORY = "books-data-pipeline"
const val SEDAI_MANIFEST_NAME = "manifest.json"

fun main() {
    // Initialize the service
    val service = Sedai.initService()

    // Start up everything
    try {
        service.start()
    } catch (e: Exception) {
        println(e)
        service.shutdown()
    }

}

class Sedai : LeaderSelectorListenerAdapter() {
    private val grpcServer = ServerBuilder
        .forPort(SEDAI_GRPC_SERVER_PORT)
        .addService(LoaderService())
        .build()

    private val client: CuratorFramework = CuratorFrameworkFactory.newClient(
        SEDAI_ZK_CONNECTION_STRING,
        ExponentialBackoffRetry(1000, 3)
    )

    private val workers = listOf<WorkerWithQueue<*>>(LoaderWorkerWithQueue)

    private val leaderSelector = LeaderSelector(client, SEDAI_ZK_LEADERSHIP_GROUP, this)

    companion object {
        // Configure curator client and the leader selector
        fun initService(): Sedai {
            return Sedai()
        }
    }

    fun start() {
        println("Starting up Sedai...determining leadership...")
        client.start()
        leaderSelector.start()

        // Enable workers
        workers.forEach { worker ->
            worker.start()
        }

        // Start the gRPC server
        grpcServer.start()
        grpcServer.awaitTermination()

    }

    fun shutdown() {
        println("Shutting down Sedai...")
        grpcServer.shutdown()
        workers.forEach { worker ->
            worker.close()
        }

        CloseableUtils.closeQuietly(client)
        CloseableUtils.closeQuietly(leaderSelector)
    }

    override fun takeLeadership(client: CuratorFramework?) {
        try {
            println("This instance of Sedai is currently the leader. I will be managing the FTP server.")
            FTPWorkerWithQueue.start()
        } catch (e: CancelLeadershipException) {
            println("This instance of Sedai is no longer the leader")
            FTPWorkerWithQueue.close()
        }
    }
}
