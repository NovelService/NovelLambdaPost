package com.xiangronglin.novel.lambda.post

class ConfigManager {

    companion object {
        private const val SQS_QUEUE_URL_KEY = "SQS_QUEUE_URL"
        private const val DYNAMO_DB_TABLE_NAME_KEY = "DYNAMO_DB_TABLE_NAME"
    }

    val config: Config

    init {
        config = Config(
            sqsConfig = SqsConfig(System.getenv(SQS_QUEUE_URL_KEY)),
            dynamoDBConfig = DynamoDBConfig(System.getenv(DYNAMO_DB_TABLE_NAME_KEY))
        )
    }

}

