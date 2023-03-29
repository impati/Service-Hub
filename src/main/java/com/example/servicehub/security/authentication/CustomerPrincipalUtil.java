package com.example.servicehub.security.authentication;

import java.util.Optional;

public abstract class CustomerPrincipalUtil {

    private CustomerPrincipalUtil() {
    }

    public static Optional<Long> getCustomerIdFrom(CustomerPrincipal customerPrincipal) {
        if (customerPrincipal == null) return Optional.empty();
        return Optional.ofNullable(customerPrincipal.getId());
    }

}
