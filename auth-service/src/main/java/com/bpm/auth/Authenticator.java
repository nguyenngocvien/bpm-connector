package com.bpm.auth;

import java.util.List;

public class Authenticator {

    private final List<AuthenticationProvider> providers;

    public Authenticator(List<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    public AuthResult authenticate(String authHeader) {
        for (AuthenticationProvider provider : providers) {
            if (provider.supports(authHeader)) {
                return provider.authenticate(authHeader);
            }
        }
        return AuthResult.failure("Unsupported Authorization type");
    }
}
