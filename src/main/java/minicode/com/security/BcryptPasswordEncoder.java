package minicode.com.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BcryptPasswordEncoder implements PasswordEncoder {
    private static final int LOG_ROUNDS = 12;

    @Override
    public String encode(CharSequence rawPassword) {
        return BCrypt.hashpw(
                rawPassword.toString(),
                BCrypt.gensalt(LOG_ROUNDS)
        );
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean isPasswordNull = rawPassword == null || encodedPassword == null;
        if (isPasswordNull) {
            return false;
        }

        try {
            return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
