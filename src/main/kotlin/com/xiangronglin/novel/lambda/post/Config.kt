package com.xiangronglin.novel.lambda.post

data class Config(
    val sqsConfig: SqsConfig,
    val dynamoDBConfig: DynamoDBConfig,
    val region: String
)

data class SqsConfig(
    val queueUrl: String
)

data class DynamoDBConfig(
    val tableName: String
)
