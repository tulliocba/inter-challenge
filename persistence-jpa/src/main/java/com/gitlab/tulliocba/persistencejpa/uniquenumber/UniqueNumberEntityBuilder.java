package com.gitlab.tulliocba.persistencejpa.uniquenumber;

import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.common.builder.Builder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniqueNumberEntityBuilder implements Builder<UniqueNumberEntity> {

    private UniqueNumber uniqueNumber;

    public static UniqueNumberEntityBuilder builder() {
        return new UniqueNumberEntityBuilder();
    }

    public UniqueNumberEntityBuilder given(UniqueNumber uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
        return this;
    }

    @Override
    public UniqueNumberEntity build() {
        UniqueNumberEntity uniqueNumberEntity = new UniqueNumberEntity();
        uniqueNumberEntity.setInputNumber(uniqueNumber.getParam().getInputNumber());
        uniqueNumberEntity.setConcatQuantity(uniqueNumber.getParam().getConcatenationQuantity());
        uniqueNumberEntity.setUniqueNumberResult(uniqueNumber.getValue());
        return uniqueNumberEntity;
    }
}
