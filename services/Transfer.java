package services;

import logic.Account;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Transfer use-case.
 *
 * <p>The repository defines the transaction boundary. The in-memory adapter
 * serializes this operation; a persistent adapter should use a database
 * transaction.</p>
 */
public final class Transfer {

    private final AccountRepository accountRepository;

    public Transfer(AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository, "accountRepository");
    }

    public void transfer(Account sender, Account receiver, BigDecimal amount) {
        Objects.requireNonNull(sender, "sender");
        Objects.requireNonNull(receiver, "receiver");
        Objects.requireNonNull(amount, "amount");

        if (sender.getAccountId().equals(receiver.getAccountId())) {
            throw new IllegalArgumentException("Sender and receiver must be different accounts");
        }

        accountRepository.executeAtomically(() -> {
            sender.withdraw(amount);
            receiver.deposit(amount);
            accountRepository.save(sender);
            accountRepository.save(receiver);
        });
    }
}
