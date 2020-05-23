package com.gitlab.tulliocba.application.unique_number.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

import static java.util.Arrays.asList;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class UniqueNumberGenerator {

    @Getter(AccessLevel.PRIVATE)
    private int value;

    private static Integer calcUniqueNumber(String number) {
        BigInteger value = new BigInteger(number);

        while (number.toCharArray().length > 1) {

            value = asList(number.split(""))
                    .stream()
                    .map(BigInteger::new)
                    .reduce(BigInteger.ZERO, (subTotal, element) -> subTotal.add(element));

            number = String.valueOf(value);
        }
        return (int) value.longValue();
    }

    public static int generateFrom(Param param) {
        int value = calcUniqueNumber(param.getConcatenatedNumber());
        return new UniqueNumberGenerator(value).getValue();
    }
}
