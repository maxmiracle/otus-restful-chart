package ru.otus.billing.service;


import ru.otus.billing.api.dto.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    Optional<Account> findById(UUID id);

    Optional<Account> findByName(String name);

    Account create(Account request);

    Account sumUp(UUID accountId, double amount);

    Account makePayment(UUID accountId, double amount, String purpose);
}
