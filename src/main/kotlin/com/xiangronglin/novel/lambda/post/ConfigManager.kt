package com.xiangronglin.novel.lambda.post

class ConfigManager {

    companion object {
        private const val SQS_QUEUE_URL_KEY = "SQS_QUEUE_URL"
    }

    val config: Config

    init {
        config = Config(
            sqsConfig = SqsConfig(System.getenv(SQS_QUEUE_URL_KEY))
        )
    }

}

