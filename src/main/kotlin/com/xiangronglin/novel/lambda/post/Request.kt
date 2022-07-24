package com.xiangronglin.novel.lambda.post

import java.util.UUID

data class Request(
    val id: UUID,
    val urls: List<String>,
    val options: Options
)

data class Options(
    val fileType: String = "epub",
    val output: String
)
