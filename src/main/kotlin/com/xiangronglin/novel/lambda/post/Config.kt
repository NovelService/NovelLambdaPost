package com.xiangronglin.novel.lambda.post

data class Config(
    val sqsConfig: SqsConfig
)

data class SqsConfig(
    val queueUrl: String
)
