package ru.otus.order.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MakePaymentRequest {
    @NotNull
    private UUID accountId;

    @NotNull
    private Double amount;

    @NotBlank
    private String paymentPurpose;
}
