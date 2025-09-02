package ru.otus.order.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakeOrderRequest {
    private UUID accountId;
    private String productName;
    private Double amount;
    private String details;
}
