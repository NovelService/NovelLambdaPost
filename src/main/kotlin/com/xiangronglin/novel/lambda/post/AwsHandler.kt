package com.xiangronglin.novel.lambda.post

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import java.io.InputStream
import java.io.OutputStream

class AwsHandler : RequestStreamHandler {
    private val gson = GsonBuilder().create()
    private val config = ConfigManager().config

    override fun handleRequest(input: InputStream, output: OutputStream, context: Context) {
        val reader = input.reader()
        val writer = output.writer()
        val logger = Logger(context.logger)

        try {
            val event = JsonParser.parseReader(reader)
            logger.log("Post function called")
            logger.log("ENV_VARS: ${gson.toJson(System.getenv())}")
            logger.log("CONTEXT: ${gson.toJson(context)}")
            logger.log("EVENT: $event")

            val request = gson.fromJson(event.asJsonObject.get("body").asString, Request::class.java)
            val response = Handler(logger, config).handleRequest(request)

            writer.write(gson.toJson(response))
        } catch (e: java.lang.Exception) {
            logger.log("ERROR occurred: $e")
        } finally {
            reader.close()
            writer.close()
        }
    }
}