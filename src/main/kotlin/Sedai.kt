package app.fourdrin.sedai

import app.fourdrin.sedai.ftp.Worker
import io.grpc.Server
import io.grpc.ServerBuilder
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter
import java.io.Closeable

fun main() {
    // Configure the gRPC Server
    val server = ServerBuilder
        .forPort(50051)
        .build()

    // Initialize the service
    val service = Sedai.initService(server)
    service.start()
}

class Sedai constructor(private val gRPCServer: Server) : LeaderSelectorListenerAdapter(), Closeable {
    companion object {
        fun initService(gRPCServer: Server): Sedai {
            return Sedai(gRPCServer)
        }
    }

    fun start() {
        gRPCServer.start()
        Worker.start()
        gRPCServer.awaitTermination()
    }

    override fun close() {
        gRPCServer.shutdown()
    }

    override fun takeLeadership(client: CuratorFramework?) {
        Worker.start()
    }
}