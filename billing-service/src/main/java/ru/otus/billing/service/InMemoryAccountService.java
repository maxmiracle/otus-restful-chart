package ru.otus.billing.service;

import org.springframework.stereotype.Service;
import ru.otus.billing.api.dto.Account;
import ru.otus.billing.exception.InsufficientFundsException;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryAccountService implements AccountService {

    private final Map<UUID, Account> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Account> findByName(String name) {
        return storage.values().stream().filter(a -> a.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public Account create(Account request) {
        Account acc = new Account();
        acc.setId(request.getId() == null ? UUID.randomUUID() : request.getId());
        acc.setName(request.getName());
        acc.setCurrency(request.getCurrency());
        acc.setBalance(0.0);
        acc.setCreatedAt(OffsetDateTime.now());
        storage.put(acc.getId(), acc);
        return acc;
    }

    @Override
    public Account sumUp(UUID accountId, double amount) {
        Account acc = require(accountId);
        acc.setBalance((acc.getBalance() == null ? 0.0 : acc.getBalance()) + amount);
        return acc;
    }

    @Override
    public Account makePayment(UUID accountId, double amount, String purpose) {
        Account acc = require(accountId);
        if (acc.getBalance() == null || acc.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds: " + amount);
        }
        acc.setBalance(acc.getBalance() - amount);
        return acc;
    }

    private Account require(UUID id) {
        Account acc = storage.get(id);
        if (acc == null) {
            throw new IllegalArgumentException("Account not found: " + id);
        }
        return acc;
    }
}
