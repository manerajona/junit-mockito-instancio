package com.github.manerajona.core.usecase;

import com.github.manerajona.core.domain.CardDetails;
import com.github.manerajona.core.domain.Payment;
import com.github.manerajona.core.domain.PaymentStatus;
import com.github.manerajona.core.repository.CardRepository;
import com.github.manerajona.core.repository.PaymentRepository;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class PaymentServiceTest {

    @InjectMocks PaymentService paymentService;
    @Mock CardRepository cardRepository;
    @Mock PaymentRepository paymentRepository;

    @ParameterizedTest
    @InstancioSource
    void registerPayment_ShouldSaveCASHPaymentWithStatusOK_Test(
            Double givenAmount, UUID givenId
    ) {
        given(paymentRepository.save(any(Payment.class))).willReturn(givenId);

        paymentService.registerPayment(givenAmount, Optional.empty());

        then(paymentRepository).should().save(any(Payment.class));
        then(cardRepository).shouldHaveNoInteractions();
    }

    @ParameterizedTest
    @InstancioSource
    void registerPayment_ShouldSaveCARDPaymentWithStatusOK_Test(
            Double givenAmount, CardDetails givenCard, UUID givenId
    ) {
        given(paymentRepository.save(any(Payment.class))).willReturn(givenId);
        given(cardRepository.validateCard(givenCard)).willReturn(true);
        given(paymentRepository.updateStatus(givenId, PaymentStatus.OK)).willReturn(true);

        paymentService.registerPayment(givenAmount, Optional.of(givenCard));

        then(paymentRepository).should().save(any(Payment.class));
        then(cardRepository).should().validateCard(eq(givenCard));
        then(paymentRepository).should().updateStatus(eq(givenId), eq(PaymentStatus.OK));
    }

    @ParameterizedTest
    @InstancioSource
    void registerPayment_ShouldSaveCARDPaymentWithStatusERROR_Test(
            Double givenAmount, CardDetails givenCard, UUID givenId
    ) {
        given(paymentRepository.save(any(Payment.class))).willReturn(givenId);
        given(cardRepository.validateCard(givenCard)).willReturn(false);
        given(paymentRepository.updateStatus(givenId, PaymentStatus.ERROR)).willReturn(true);

        paymentService.registerPayment(givenAmount, Optional.of(givenCard));

        then(paymentRepository).should().save(any(Payment.class));
        then(cardRepository).should().validateCard(eq(givenCard));
        then(paymentRepository).should().updateStatus(eq(givenId), eq(PaymentStatus.ERROR));
    }
}