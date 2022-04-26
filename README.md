# NovelLambdaPost
An AWS Lambda function which serves as HTTP gateway relay messages to an SQS queue in order to trigger the worker.

# Configuration
## Runtime settings
```
Runtime=Java 11 (Corretto)
Handler=com.xiangronglin.novel.lambda.post.AwsHandler::handleRequest
Architecture=x86_64
```

## Environment Variables
Set these values as environment variables for the lambda function. 
```
SQS_QUEUE_URL=<your-url>
```

## Role
The lambda needs a role with :
- Permission "sqs:SendMessage"

And optionally for logging
- Policy "AWSLambdaBasicExecutionRole"