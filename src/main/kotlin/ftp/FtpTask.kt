package app.fourdrin.sedai.ftp

import software.amazon.awssdk.services.s3.S3Client

abstract class FtpTask constructor(private val s3Client: S3Client) : Runnable { }