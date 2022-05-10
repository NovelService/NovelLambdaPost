package com.xiangronglin.novel.lambda.post

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class Novel(
    @get:DynamoDbPartitionKey
    var id: String = "",
    var status: String = ""
)