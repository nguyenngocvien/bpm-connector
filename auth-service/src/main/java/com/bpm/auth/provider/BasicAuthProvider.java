package com.bpm.auth.provider;

import com.bpm.auth.AuthResult;
import com.bpm.auth.AuthenticationProvider;
import com.bpm.auth.store.UserCredentialStore;
import org.apache.commons.codec.binary.Base64;

public class BasicAuthProvider implements AuthenticationProvider {

    private final UserCredentialStore credentialStore;

    public BasicAuthProvider(UserCredentialStore credentialStore) {
        this.credentialStore = credentialStore;
    }

    @Override
    public boolean supports(String authHeader) {
        return authHeader != null && authHeader.trim().startsWith("Basic ");
    }

    @Override
    public AuthResult authenticate(String authHeader) {
        try {
            String base64Credentials = authHeader.substring(6).trim();
            String decoded = new String(Base64.decodeBase64(base64Credentials));
            String[] parts = decoded.split(":", 2);
            if (parts.length != 2) return AuthResult.failure("Invalid Basic auth format");

            String username = parts[0];
            String password = parts[1];

            if (credentialStore.isValid(username, password)) {
                return AuthResult.success(username);
            } else {
                return AuthResult.failure("Invalid username or password");
            }
        } catch (Exception e) {
            return AuthResult.failure("Invalid Basic auth encoding");
        }
    }
}
