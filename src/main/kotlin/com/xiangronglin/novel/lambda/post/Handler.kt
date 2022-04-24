package com.xiangronglin.novel.lambda.post

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.google.gson.Gson

class Handler(
    private val logger: Logger,
    private val config: Config,
    private val sqsClient : AmazonSQS = AmazonSQSClientBuilder.defaultClient(),
    private val gson: Gson = Gson()
) {
    fun handleRequest(request: Request): Response {
        logger.log("Handling request. id ${request.id}")

        val sendMessageRequest = SendMessageRequest()
            .withQueueUrl(config.sqsConfig.queueUrl)
            .withMessageBody(gson.toJson(request))
        logger.log("Queuing message. id ${request.id}")
        sqsClient.sendMessage(sendMessageRequest)

        logger.log("Finished handling request. id ${request.id}")
        return Response(request.id)
    }
}