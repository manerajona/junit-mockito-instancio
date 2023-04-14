package com.github.manerajona.core.repository;

import com.github.manerajona.core.domain.CardDetails;

import java.time.LocalDate;

public class CardRepository {

    protected static boolean checkCNWithLuhnAlgo(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    protected static boolean checkED(int expDate) {
        LocalDate now = LocalDate.now();

        final int expYear = 2000 + expDate % 100;
        if (expYear > now.getYear()) {
            return true;
        }

        final int expMonth = expDate / 100;
        return (expYear == now.getYear() && expMonth > now.getMonthValue());
    }

    protected static boolean checkCVC(int cvc) {
        return (cvc > 0 && cvc < 1000);
    }

    /**
     * Validates:
     * 1) Card Number using the Luhn Algorithm. {@see <a href="https://en.wikipedia.org/wiki/Luhn_algorithm">wiki</a>}
     * 2) Expiration Date checking that it is prior to the current month and year.
     * 3) CVC checking that it is between 0 and 1000. {@see <a href="https://en.wikipedia.org/wiki/Card_security_code">wiki</a>}
     *
     * @param card the card instance of {@link CardDetails}
     * @return {@code true} if the card is valid, or {@code false} otherwise.
     */
    public boolean validateCard(CardDetails card) {
        return checkCNWithLuhnAlgo(card.number()) && checkED(card.expDate()) && checkCVC(card.cvc());
    }
}