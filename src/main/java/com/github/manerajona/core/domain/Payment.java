package com.github.manerajona.core.domain;

public record Payment(Double amount,
                      PaymentMethod method,
                      PaymentStatus status,
                      CardDetails card) {

    public Payment(Double amount, PaymentStatus status) {
        this(amount, PaymentMethod.CASH, status, null);
    }

    public Payment(Double amount, CardDetails card, PaymentStatus status) {
        this(amount, PaymentMethod.CARD, status, card);
    }
}