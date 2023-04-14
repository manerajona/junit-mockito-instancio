package com.github.manerajona.core.repository;

import com.github.manerajona.core.domain.CardDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.manerajona.core.CardDetailsMockData.*;
import static org.assertj.core.api.Assertions.assertThat;

class CardRepositoryTest {

    @Test
    void validateCard_ValidCard_Test() {
        CardRepository repository = new CardRepository();
        boolean valid = repository.validateCard(
                new CardDetails(VALID_CN, VALID_ED, VALID_CVC)
        );
        assertThat(valid).isTrue();
    }

    @ParameterizedTest
    @MethodSource
    void validateCard_invalidCard_ParameterizedTest(CardDetails cardDetails) {
        CardRepository repository = new CardRepository();
        boolean valid = repository.validateCard(cardDetails);
        assertThat(valid).isFalse();
    }

    static Stream<CardDetails> validateCard_invalidCard_ParameterizedTest() {
        return Stream.of(
                new CardDetails(INVALID_CN, VALID_ED, VALID_CVC),
                new CardDetails(VALID_CN, INVALID_ED, VALID_CVC),
                new CardDetails(VALID_CN, VALID_ED, INVALID_CVC)
                // Other test cases are ignored for simplicity...
        );
    }
}