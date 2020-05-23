package com.gitlab.tulliocba.application.unique_number.domain;


import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase.UniqueNumberResultView;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.UniqueNumberView;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class UniqueNumber {
    private final Param param;
    private final int value;

    public static UniqueNumber from(Param param) {
        return new UniqueNumber(param, UniqueNumberGenerator.generateFrom(param));
    }

    public static UniqueNumber calculated(Param param, int value) {
        return new UniqueNumber(param, value);
    }

    public UniqueNumberResultView toResultView() {
        return new UniqueNumberResultView(value);
    }

    public UniqueNumberView toView() {
        return new UniqueNumberView(param.getConcatenationQuantity(), param.getInputNumber(), value);
    }
}
