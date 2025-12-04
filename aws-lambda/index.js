const {
    DynamoDBClient,
    GetItemCommand,
} = require("@aws-sdk/client-dynamodb");

const client = new DynamoDBClient({
    region: "us-east-1",
    endpoint: process.env.AWS_ENDPOINT_URL,
});

exports.handler = async (event) => {
    console.log("EVENT:", event);

    const paramName = event.param || "MY_PARAM";

    const cmd = new GetItemCommand({
        TableName: "Parameters",
        Key: { pk: { S: paramName } }
    });

    try {
        const result = await client.send(cmd);

        console.log("Dynamo result:", result);

        if (!result.Item) {
            return {
                statusCode: 404,
                body: JSON.stringify({ error: "Parameter not found" }),
            };
        }

        return {
            statusCode: 200,
            body: JSON.stringify({
                param: paramName,
                value: result.Item.value.S,
            }),
        };

    } catch (err) {
        console.error("DYNAMO ERROR:", err);
        return {
            statusCode: 500,
            body: JSON.stringify({
                error: "Internal error",
                details: err.message,
            }),
        };
    }
};
