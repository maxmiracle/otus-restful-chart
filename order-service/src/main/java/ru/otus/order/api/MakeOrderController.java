package ru.otus.order.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.otus.order.api.dto.MakeOrderRequest;
import ru.otus.order.api.dto.MakePaymentRequest;
import ru.otus.order.api.dto.OrderNotification;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MakeOrderController implements MakeOrderApi {

    @Value("${make-payment-url}")
    private String makePaymentUrl;

    private final RestClient defaultClient = RestClient.create();

    private final ObjectMapper objectMapper;


    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public ResponseEntity<Void> makeOrder(@Valid MakeOrderRequest makeOrderRequest) {
        MakePaymentRequest request = new MakePaymentRequest();
        request.setAmount(makeOrderRequest.getAmount());
        request.setAccountId(makeOrderRequest.getAccountId());
        request.setPaymentPurpose(makeOrderRequest.getProductName());
        var result = defaultClient.post()
                .uri(makePaymentUrl)
                .body(request)
                .retrieve()
                .toBodilessEntity();
        if (result.getStatusCode().is2xxSuccessful()) {
            OrderNotification notification = new OrderNotification(
                    makeOrderRequest.getAccountId(),
                    UUID.randomUUID().toString(),
                    OffsetDateTime.now(),
                    String.format("Order created. Product: %s, Amount: %s", makeOrderRequest.getProductName(), makeOrderRequest.getAmount()));
            sendMessage(notification);
        } else {
            OrderNotification notification = new OrderNotification(
                    makeOrderRequest.getAccountId(),
                    UUID.randomUUID().toString(),
                    OffsetDateTime.now(),
                    String.format("Error order payment. Product: %s, Amount: %s. ErrCode: %s", makeOrderRequest.getProductName(), makeOrderRequest.getAmount(), result.getStatusCode()));
            sendMessage(notification);
        }
        return ResponseEntity.ok().build();
    }



    private void sendMessage(OrderNotification notification) {
        try {
            String message = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send("order.notify", message);
         } catch (Exception e) {
            log.error("Error sending message", e);
        }
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler( HttpClientErrorException.UnprocessableEntity.class )
    public String handleException(HttpClientErrorException.UnprocessableEntity ex) {
        return ex.getMessage();
    }
}
