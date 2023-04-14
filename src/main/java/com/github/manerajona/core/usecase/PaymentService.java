package com.github.manerajona.core.usecase;

import com.github.manerajona.core.domain.CardDetails;
import com.github.manerajona.core.domain.Payment;
import com.github.manerajona.core.domain.PaymentStatus;
import com.github.manerajona.core.repository.CardRepository;
import com.github.manerajona.core.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CardRepository cardRepository;

    public PaymentService(PaymentRepository paymentRepository, CardRepository cardRepository) {
        this.paymentRepository = paymentRepository;
        this.cardRepository = cardRepository;
    }

    /**
     * Registers a {@link Payment} record based on the logic below.
     * <pre>
     * IF CardDetails is present THEN
     *      create Payment with status PENDING_VALIDATION
     *      validate CardDetails
     *      update Payment status to OK or ERROR, depending on whether the card is valid
     * OR ELSE
     *      create Payment with status OK
     * </pre>
     *
     * @param amount       the payment amount instance of {@link BigDecimal}
     * @param optionalCard the {@link Optional} parameter with the {@link CardDetails}
     */
    public void registerPayment(Double amount, Optional<CardDetails> optionalCard) {
        optionalCard.ifPresentOrElse(cardDetails -> {
                    UUID id = paymentRepository.save(
                            new Payment(amount, cardDetails, PaymentStatus.PENDING_VALIDATION));

                    boolean isValidCard = cardRepository.validateCard(cardDetails);

                    paymentRepository.updateStatus(id,
                            isValidCard ? PaymentStatus.OK : PaymentStatus.ERROR);

                }, () -> paymentRepository.save(
                        new Payment(amount, PaymentStatus.OK))
        );
    }
}