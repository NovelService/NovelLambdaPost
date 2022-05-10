package com.xiangronglin.novel.lambda.post

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.http.crt.AwsCrtAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import java.io.InputStream
import java.io.OutputStream

class AwsHandler : RequestStreamHandler {

    companion object {
        private val gson = GsonBuilder().create()
        private val config = ConfigManager.config
        private val dynamoTable: DynamoDbAsyncTable<Novel>
        private val sqsClient: SqsAsyncClient

        init {
            val httpClient = AwsCrtAsyncHttpClient.create()
            val dynamoClient = DynamoDbAsyncClient.builder()
                .httpClient(httpClient)
                .region(Region.of(config.region))
                .build()
            val enhancedClient = DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoClient)
                .build()
            dynamoTable =
                enhancedClient.table(config.dynamoDBConfig.tableName, TableSchema.fromBean(Novel::class.java))

            sqsClient = SqsAsyncClient.builder()
                .httpClient(httpClient)
                .region(Region.of(config.region))
                .build()
        }
    }

    override fun handleRequest(input: InputStream, output: OutputStream, context: Context) {
        val reader = input.reader()
        val writer = output.writer()
        val logger = context.logger

        try {
            val event = JsonParser.parseReader(reader)
            val request = gson.fromJson(event.asJsonObject.get("body").asString, Request::class.java)
            logger.log("Handling request. id ${request.id}")

            val sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(config.sqsConfig.queueUrl)
                .messageBody(gson.toJson(request))
                .build()

            logger.log("Queuing message. id ${request.id}")
            val sqsRequestFuture = sqsClient.sendMessage(sendMessageRequest)

            logger.log("Putting item into db. id ${request.id}")
            val dynamoRequestFuture = dynamoTable.putItem(Novel(request.id.toString(), "queued"))

            logger.log("Finished handling request. id ${request.id}")
            writer.write(gson.toJson(Response(request.id)))

            sqsRequestFuture.join()
            dynamoRequestFuture.join()
            logger.log("Async requests finished")
        } catch (e: java.lang.Exception) {
            logger.log("ERROR occurred: $e")
        } finally {
            reader.close()
            writer.close()
        }
    }
}