package ru.otus.billing.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.otus.billing.api.dto.Account;
import ru.otus.billing.api.dto.MakePaymentRequest;
import ru.otus.billing.api.dto.SumUpRequest;
import ru.otus.billing.exception.InsufficientFundsException;
import ru.otus.billing.service.AccountService;

import java.util.UUID;

@RestController
@RequestMapping
public class BillingController {

    private final AccountService service;

    public BillingController(AccountService service) {
        this.service = service;
    }

    // POST /accounts (IF1 - Create new account)
    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account request) {
        if (service.findByName(request.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Account created = service.create(request);
        // Spec returns 201 with no body defined; include Location header
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/accounts/" + created.getId())
                .build();
    }

    // POST /sumup (IF2 - Sum up account balance)
    @PostMapping("/sumup")
    public ResponseEntity<Void> sumUp(@Valid @RequestBody SumUpRequest request) {
        try {
            service.sumUp(request.getAccountId(), request.getAmount());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /getBalance/{accountId} (IF3 - Get account balance)
    @GetMapping("/getBalance/{accountId}")
    public ResponseEntity<Double> getBalance(@PathVariable("accountId") UUID accountId) {
        return service.findById(accountId)
                .map(a -> ResponseEntity.ok(a.getBalance() == null ? 0.0 : a.getBalance()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /makePayment (IF5 - Make payment)
    @PostMapping("/makePayment")
    public ResponseEntity<Void> makePayment(@Valid @RequestBody MakePaymentRequest request) {
            service.makePayment(request.getAccountId(), request.getAmount(), request.getPaymentPurpose());
            return ResponseEntity.ok().build();
       }


}
