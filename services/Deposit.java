package services;

import logic.Account;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Deposit use-case.
 */
public final class Deposit {

    private final AccountRepository accountRepository;

    public Deposit(AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository");
    }

    public void deposit(Account account, BigDecimal amount) {
        Objects.requireNonNull(account, "account");
        Objects.requireNonNull(amount, "amount");

        accountRepository.executeAtomically(() -> {
            account.deposit(amount);
            accountRepository.save(account);
        });
    }
}
