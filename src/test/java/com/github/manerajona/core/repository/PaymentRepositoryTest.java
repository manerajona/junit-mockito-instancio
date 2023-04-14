package com.github.manerajona.core.repository;

import com.github.manerajona.core.CardDetailsMockData;
import com.github.manerajona.core.domain.CardDetails;
import com.github.manerajona.core.domain.Payment;
import com.github.manerajona.core.domain.PaymentStatus;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@ExtendWith(InstancioExtension.class)
class PaymentRepositoryTest {

    @Test
    void save_Test() {

        ConcurrentMap<UUID, Payment> payments = new ConcurrentHashMap<>();
        PaymentRepository paymentRepository = new PaymentRepository(payments);

        Payment givenPayment = Instancio.of(Payment.class)
                .generate(field("amount"), generators -> generators.doubles().range(.5, 10_000.))
                .generate(field(CardDetails.class, "cvc"), generators -> generators.ints().range(1, 999))
                .generate(field(CardDetails.class, "number"), generators -> generators.finance().creditCard().masterCard())
                .set(field(CardDetails.class, "expDate"), CardDetailsMockData.VALID_ED)
                .create();

        UUID id = paymentRepository.save(givenPayment);

        assertThat(id).isNotNull();
        assertThat(payments).containsKey(id).containsValue(givenPayment);
    }

    @ParameterizedTest
    @InstancioSource
    void updateStatus_Test(Payment givenPayment, UUID givenId,  PaymentStatus givenStatus) {

        ConcurrentMap<UUID, Payment> payments = new ConcurrentHashMap<>();
        PaymentRepository paymentRepository = new PaymentRepository(payments);

        payments.put(givenId, givenPayment);

        boolean updated = paymentRepository.updateStatus(givenId, givenStatus);

        assertThat(updated).isTrue();
        assertThat(payments.get(givenId).status()).isEqualTo(givenStatus);
    }
}