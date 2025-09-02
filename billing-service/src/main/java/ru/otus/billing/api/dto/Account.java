package ru.otus.billing.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class Account {
    // readOnly in API -> server-populated
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[A-Z]{3}$", message = "currency must be ISO 4217 alpha-3 (e.g., USD)")
    private String currency;

    // readOnly in API -> server-populated
    private Double balance;

    // readOnly in API -> server-populated
    private OffsetDateTime createdAt;
}
