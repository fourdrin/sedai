package app.fourdrin.sedai

import app.fourdrin.sedai.ftp.FTPWorker
import io.grpc.ServerBuilder
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.framework.recipes.leader.CancelLeadershipException
import org.apache.curator.framework.recipes.leader.LeaderSelector
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.curator.utils.CloseableUtils

const val SEDAI_GRPC_SERVER_PORT = 50051
const val SEDAI_ZK_CONNECTION_STRING = "localhost:2181"
const val SEDAI_ZK_LEADERSHIP_GROUP = "/fourdrin/sedai"

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
        .build()

    private val client: CuratorFramework = CuratorFrameworkFactory.newClient(
        SEDAI_ZK_CONNECTION_STRING,
        ExponentialBackoffRetry(1000, 3)
    )

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
        grpcServer.start()
        grpcServer.awaitTermination()
    }

    fun shutdown() {
        println("Shutting down Sedai...")
        grpcServer.shutdown()
        CloseableUtils.closeQuietly(client)
        CloseableUtils.closeQuietly(leaderSelector)
    }

    override fun takeLeadership(client: CuratorFramework?) {
        try {
            println("This instance of Sedai is currently the leader. I will be managing the FTP server.")
            FTPWorker.start()
        } catch (e: CancelLeadershipException) {
            println("This instance of Sedai is no longer the leader")
            FTPWorker.close()
        }
    }
}