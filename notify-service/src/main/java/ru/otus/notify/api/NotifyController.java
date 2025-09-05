package ru.otus.notify.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.notify.api.dto.OrderNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(path = "/notify-service")
public class NotifyController {

    private final Map<UUID, List<OrderNotification>> storage = new ConcurrentHashMap<>();

    public void addMessage(UUID accountId, OrderNotification message) {
        storage.computeIfAbsent(accountId, k -> new ArrayList<>()).add(message);
    }

    @RequestMapping("/getMessages/{accountId}")
    public List<OrderNotification> getMessages(@PathVariable UUID accountId) {
        return storage.get(accountId);
    }
}
