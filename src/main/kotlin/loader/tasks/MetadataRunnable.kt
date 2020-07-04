package app.fourdrin.sedai.loader.tasks

import app.fourdrin.sedai.loader.LoaderWorkerWithQueue
import app.fourdrin.sedai.models.AssetType
import app.fourdrin.sedai.models.LoaderWork
import app.fourdrin.sedai.models.MetadataVersion
import app.fourdrin.sedai.models.Work
import software.amazon.awssdk.services.s3.S3Client

class MetadataRunnable constructor(private val s3Client: S3Client, private val metadataKey: String) : Runnable {
    override fun run() {
        // Determine which ONIX version we are working with
        val work = LoaderWork(
            id = metadataKey,
            assetType = AssetType.METADATA,
            metadataVersion = MetadataVersion.UNKNOWN
        )

        // Requeue the work
        LoaderWorkerWithQueue.workerQueue.add(work)
    }
}