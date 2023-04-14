package com.github.manerajona.core.repository;

import com.github.manerajona.core.domain.Payment;
import com.github.manerajona.core.domain.PaymentStatus;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

public class PaymentRepository {

    private final ConcurrentMap<UUID, Payment> payments;

    public PaymentRepository(ConcurrentMap<UUID, Payment> payments) {
        this.payments = payments;
    }

    /**
     * Stores a payment record.
     *
     * @param payment the payment instance of {@link Payment}
     * @return the payment id instance of {@link UUID}
     */
    public UUID save(Payment payment) {
        UUID id = UUID.randomUUID();
        payments.put(id, payment);
        return id;
    }

    /**
     * Updates payment status.
     *
     * @param id     the payment id instance of {@link UUID}
     * @param status the payment status instance of {@link PaymentStatus}
     * @return {@code true} if the payment was updated, or {@code false} otherwise.
     */
    public boolean updateStatus(UUID id, PaymentStatus status) {
        return Objects.nonNull(
                payments.computeIfPresent(id, update(status))
        );
    }

    protected static BiFunction<UUID, Payment, Payment> update(PaymentStatus status) {
        return (id, payment) -> new Payment(payment.amount(), payment.method(), status, payment.card());
    }
}