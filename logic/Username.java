package logic;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.Locale;


public class Username{

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 32;

    private static final Pattern PATTERN = Pattern.compile("^[a-z0-9-]+$");

    private final String value;

    public Username(String username){

        Objects.requireNonNull(username, "Username cannot be NULL");
        String normalized = username.trim().toLowerCase(Locale.ROOT);

        if (MAX_LENGTH < normalized.length() || MIN_LENGTH > normalized.length()){
            throw new IllegalArgumentException(
                "Username must be between " + MIN_LENGTH + " and " + MAX_LENGTH
            );
        }

        if(!PATTERN.matcher(normalized).matches()){
            throw new IllegalArgumentException(
                "Username must only be thiS character format: a-z 0-9 and '-'"
            );
        }

        this.value = normalized;
    }

    public String getValue(){
        return value;
    }

    @Override
    public String toString(){
        return value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Username)) return false;
        return value.equals(((Username) o).value);
    }
    @Override
    public int hashCode(){
        return value.hashCode();
    }
}