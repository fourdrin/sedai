package app.fourdrin.sedai.worker

import software.amazon.awssdk.services.s3.S3Client

interface FtpRunnable : Runnable {
    val s3Client: S3Client
}
