# NovelLambdaPost
An AWS Lambda function which serves as HTTP gateway relay messages to an SQS queue in order to trigger the worker.

# Configuration

## Environment Variables
Set these values as environment variables for the lambda function. 
```
SQS_QUEUE_URL=<your-url>
```