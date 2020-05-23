package com.gitlab.tulliocba.application.unique_number.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode(of = "concatenatedNumber")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Param {

    private static final int CONCAT_MAX_NUMBER = 100000;

    private final String inputNumber;
    private final int concatenationQuantity;
    private final String concatenatedNumber;

    public static Param of(final String inputNumber, final int concatenationQuantity) {
        isValidConcatNumber(concatenationQuantity);

        isValidUniqueNumber(inputNumber);

        String concatenatedNumber = concatNumber(inputNumber, concatenationQuantity);

        return new Param(inputNumber, concatenationQuantity, concatenatedNumber);
    }

    private static String concatNumber(final String inputNumber, final int concatenationQuantity) {

        StringBuilder builder = new StringBuilder(inputNumber);

        for (int i = 1; i < concatenationQuantity; i++) builder.append(inputNumber);

        return builder.toString();
    }

    private static void isValidUniqueNumber(String inputNumber) {
        if (new BigDecimal(inputNumber).signum() == 1) return;

        throw new IllegalArgumentException("Invalid unique inputNumber");
    }

    private static void isValidConcatNumber(int concatenationQuantity) {
        if (concatenationQuantity <= CONCAT_MAX_NUMBER && concatenationQuantity > 0) return;

        throw new IllegalArgumentException("Invalid concat number");
    }
}
