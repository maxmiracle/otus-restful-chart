package ru.otus.notify.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderNotification {
    private UUID accountId;
    private String id;
    private OffsetDateTime timestamp;
    private String message;
}
