package logic;

import java.util.Objects;
import java.util.UUID;

public final class AccountID {

    private final UUID value;

    public AccountID(UUID uuid) {
        this.value = Objects.requireNonNull(uuid, "UUID cannot be null!");
    }

    /**
     * Convenience constructor to support existing code paths that take a string.
     *
     * <p>We keep the stricter {@link #fromString(String)} factory for clarity.</p>
     */
    public AccountID(String uuid) {
        this(UUID.fromString(Objects.requireNonNull(uuid, "uuid").trim()));
    }

    public static AccountID generate() {
        return new AccountID(UUID.randomUUID());
    }

    public static AccountID fromString(String s) {
        return new AccountID(UUID.fromString(s));
    }

    public UUID getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountID that = (AccountID) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "AccountID{" + value + '}';
    }
}
