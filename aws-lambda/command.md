# AWS Lambda Commands in LocalStack

- Configure Credentials for LocalStack.

```bash
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1
```

- Create a table in DynamoDB in LocalStack.

```bash
aws --endpoint-url=http://localhost:4566 \
  --region us-east-1 \
  dynamodb create-table \
  --table-name Parameters \
  --attribute-definitions AttributeName=pk,AttributeType=S \
  --key-schema AttributeName=pk,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST
```

- Put an item into the DynamoDB table in LocalStack.

```bash
aws --endpoint-url=http://localhost:4566 \
  --region us-east-1 \
  dynamodb put-item \
  --table-name Parameters \
  --item '{
    "pk": {"S": "MY_PARAM"},
    "value": {"S": "Hola desde DynamoDB!"}
  }'
``` 

- List all DynamoDB tables in LocalStack.

```bash
aws --endpoint-url=http://localhost:4566 \
  --region us-east-1 \
  dynamodb list-tables
```

- List all AWS Lambda funcions in LocalStack.

```bash
aws --endpoint-url=http://localhost:4566 \
  --region us-east-1 \
  lambda \
  list-functions
```

- Create a new AWS Lambda function in LocalStack.

```bash
aws --endpoint-url=http://localhost:4566 \
    --region us-east-1 \
    lambda \
    create-function \
    --function-name ban-xrs-get-parameter \
    --runtime nodejs22.x \
    --role arn:aws:iam::000000000000:role/lambda-role \
    --handler index.handler \
    --zip-file fileb://function.zip
```

- Invoke the AWS Lambda function in LocalStack.

```bash
aws --endpoint-url=http://localhost:4566 \
    --region us-east-1 \
    lambda \
    invoke \
    --function-name ban-xrs-get-parameter \
    --payload '{}' \
    response.json
```

- View the response from the AWS Lambda function invocation.

```bash
cat response.json
```

- View the logs of the AWS Lambda function in LocalStack.

```bash
aws --endpoint-url=http://localhost:4566 \
    --region us-east-1 \
    logs \
    describe-log-groups
```

- Update the AWS Lambda function code in LocalStack.

```bash
aws --endpoint-url=http://localhost:4566 \
    --region us-east-1 \
    lambda \
    update-function-code \
    --function-name ban-xrs-get-parameter \
    --zip-file fileb://function.zip
```

```bash
aws --endpoint-url=http://localhost:4566 \
    --region us-east-1 \
    lambda update-function-configuration \
    --function-name ban-xrs-get-parameter \
    --handler index.handler
```

- Delete the AWS Lambda function in LocalStack.

```bash
aws --endpoint-url=http://localhost:4566 \
    --region us-east-1 \
    lambda \
    delete-function \
    --function-name ban-xrs-get-parameter
```

- Delete the DynamoDB table in LocalStack.

```bash 
aws --endpoint-url=http://localhost:4566 \
  --region us-east-1 \
  dynamodb delete-table \
  --table-name Parameters
```

- Execute cURL command to invoke the AWS Lambda function in LocalStack via API Gateway.

```bash
curl -X POST "http://localhost:4566/restapis/{api-id}/{stage}/_myresource" \
  -H "Content-Type: application/json" \
  -d '{}'
```

- Execute HTTP request using Spring Java WebFlux.

```bash
curl http://localhost:8080/lambda/MY_PARAM
```

### Author
- [Raul R. Bolivar Navas](https://rasysbox.com)
- GitHub: [@raulrobinson](https://github.com/raulrobinson)

### License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
