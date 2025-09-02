package ru.otus.billing.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class SumUpRequest {
    @NotNull
    private UUID accountId;

    @NotNull
    private Double amount;
}
