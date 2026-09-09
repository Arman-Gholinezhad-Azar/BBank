package logic;

import java.util.Objects;
import java.util.Locale;
import java.util.regex.Pattern;


public class Username {

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 32;
    private static final Pattern PATTERN = Pattern.compile("^[a-z0-9-]+$");

    private final String value;

    public Username(String username) {
        Objects.requireNonNull(username, "Username must not be null");

        String normalized = username.trim().toLowerCase(Locale.ROOT);

        if (normalized.length() < MIN_LENGTH || normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                "Username must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters!"
            );
        }

        if (!PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                "Username must only contain a-z, 0-9 and '-'"
            );
        }

        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Username)) return false;
        return value.equals(((Username) o).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}