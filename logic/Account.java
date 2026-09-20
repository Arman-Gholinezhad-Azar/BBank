package logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;

/**
 * Core domain entity representing a bank account.
 *
 * <p>This project currently uses an in-memory persistence model, therefore this
 * entity is mutable in a controlled way (e.g., {@link #deposit(BigDecimal)}).
 * In a production system, you would typically enforce invariants via domain
 * services and persist changes in a transactional datastore.</p>
 */
public final class Account {

    private final AccountID accountId;
    private Username username;
    private Password password;
    private CardNumber cardNumber;

    /**
     * Monetary balance is stored as a 2-decimal BigDecimal.
     *
     * <p>Using BigDecimal prevents common floating point rounding issues.</p>
     */
    private BigDecimal balance;

    private Instant createdAt;
    private Instant updatedAt;

    public Account(AccountID accountId, Username username, Password password, CardNumber cardNumber) {
        this.accountId = Objects.requireNonNull(accountId, "accountId");
        this.username = Objects.requireNonNull(username, "username");
        this.password = Objects.requireNonNull(password, "password");
        this.cardNumber = Objects.requireNonNull(cardNumber, "cardNumber");
        this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    /** Rehydrates an account from a trusted persistence adapter. */
    public static Account restore(AccountID accountId, Username username, Password password,
                                  CardNumber cardNumber, BigDecimal balance,
                                  Instant createdAt, Instant updatedAt) {
        Account account = new Account(accountId, username, password, cardNumber);
        account.balance = Money.normalize(balance);
        if (account.balance.signum() < 0) {
            throw new IllegalArgumentException("balance cannot be negative");
        }
        account.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        account.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
        return account;
    }

    public AccountID getAccountId() {
        return accountId;
    }

    public Username getUsername() {
        return username;
    }

    public CardNumber getCardNumber() {
        return cardNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /** Immutable data needed by a persistence adapter. Never expose this in API responses. */
    public Snapshot snapshot() {
        return new Snapshot(accountId, username, password, cardNumber, balance, createdAt, updatedAt);
    }

    public record Snapshot(AccountID accountId, Username username, Password password,
                           CardNumber cardNumber, BigDecimal balance,
                           Instant createdAt, Instant updatedAt) { }

    public boolean passwordMatches(Password candidate) {
        return this.password.equals(Objects.requireNonNull(candidate, "candidate"));
    }

    public void changeUsername(Username newUsername) {
        this.username = Objects.requireNonNull(newUsername, "newUsername");
        touch();
    }

    public void changePassword(Password newPassword) {
        this.password = Objects.requireNonNull(newPassword, "newPassword");
        touch();
    }

    public void changeCardNumber(CardNumber newCardNumber) {
        this.cardNumber = Objects.requireNonNull(newCardNumber, "newCardNumber");
        touch();
    }

    public void deposit(BigDecimal amount) {
        Money.validatePositive(amount, "amount");
        this.balance = this.balance.add(Money.normalize(amount));
        touch();
    }

    public void withdraw(BigDecimal amount) {
        Money.validatePositive(amount, "amount");
        BigDecimal normalized = Money.normalize(amount);

        if (this.balance.compareTo(normalized) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        this.balance = this.balance.subtract(normalized);
        touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }
}
