package br.com.desafio.service.messaging;

import br.com.desafio.controller.PaymentItem;
import br.com.desafio.domain.util.JsonMapper;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PaymentEventProducer {

    @Value("$consumer.sqs.message.queue.partial.process.name")
    private String queueParcialName;

    @Value("$consumer.sqs.message.queue.total.process.name")
    private String queueTotalName;

    @Value("$consumer.sqs.message.queue.excess.process.name")
    private String queueExcessName;

    private final JsonMapper jsonMapper;

    @Autowired
    public PaymentEventProducer(final JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public void SendToParcialPaymentProcessing(PaymentItem payload) {
        AmazonSQS sqs = getDefaultClient();

        try {
            sendMessageToQueue(sqs, queueParcialName, jsonMapper.objectAsString(payload));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void SendToTotalPaymentProcessing(PaymentItem payload) {
        AmazonSQS sqs = getDefaultClient();

        try {
            sendMessageToQueue(sqs, queueTotalName, jsonMapper.objectAsString(payload));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void SendToExcessPaymentProcessing(PaymentItem payload) {
        AmazonSQS sqs = getDefaultClient();

        try {
            sendMessageToQueue(sqs, queueExcessName, jsonMapper.objectAsString(payload));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SendMessageResult sendMessageToQueue(AmazonSQS sqs, String queueUrl, String message) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);

        return sqs.sendMessage(sendMessageRequest);
    }

    private AmazonSQS getDefaultClient() {
        return AmazonSQSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("xx", "xxx")))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8081", "us-east-1"))
                .build();
    }
}
