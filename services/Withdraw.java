package services;

import logic.Account;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Withdraw use-case.
 */
public final class Withdraw {

    private final AccountRepository accountRepository;

    public Withdraw(AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository");
    }

    public void withdraw(Account account, BigDecimal amount) {
        Objects.requireNonNull(account, "account");
        Objects.requireNonNull(amount, "amount");

        accountRepository.executeAtomically(() -> {
            account.withdraw(amount);
            accountRepository.save(account);
        });
    }
}
