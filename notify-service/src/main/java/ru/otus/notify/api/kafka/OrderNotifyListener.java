package ru.otus.notify.api.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.notify.api.NotifyController;
import ru.otus.notify.api.dto.OrderNotification;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderNotifyListener {

    private final NotifyController notifyController;

    private final ObjectMapper objectMapper;

    @KafkaListener(id = "notify-service", topics = "order.notify")
    public void listen(String message) {
        try {
            OrderNotification orderNotification = objectMapper.readValue(message, OrderNotification.class);
            notifyController.addMessage(orderNotification.getAccountId(), orderNotification);
            log.info("Order notified: {}", orderNotification);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
