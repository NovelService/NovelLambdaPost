package com.xiangronglin.novel.lambda.post

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.google.gson.Gson

class Handler(
    private val logger: Logger,
    private val config: Config,
    private val sqsClient: AmazonSQS = AmazonSQSClientBuilder.defaultClient(),
    private val dynamoDB: DynamoDB = DynamoDB(AmazonDynamoDBClientBuilder.defaultClient()),
    private val gson: Gson = Gson()
) {
    fun handleRequest(request: Request): Response {
        logger.log("Handling request. id ${request.id}")

        val sendMessageRequest = SendMessageRequest()
            .withQueueUrl(config.sqsConfig.queueUrl)
            .withMessageBody(gson.toJson(request))
        logger.log("Queuing message. id ${request.id}")
        sqsClient.sendMessage(sendMessageRequest)
        logger.log("Successfully queued message. id ${request.id}")

        val table = dynamoDB.getTable(config.dynamoDBConfig.tableName)
        val item = Item()
            .withPrimaryKey("id", request.id.toString())
            .with("status", "queued")
        logger.log("Putting item into db. id ${request.id}")
        table.putItem(item)
        logger.log("Successfully put item into db. id ${request.id}")

        logger.log("Finished handling request. id ${request.id}")
        return Response(request.id)
    }
}