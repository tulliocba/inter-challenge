package com.gitlab.tulliocba.application.unique_number.service;

import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public interface CalculateUniqueNumberUserCase {

    UniqueNumber calculateUniqueNumber(NewUniqueNumberCalculationCommand command);

    @Value
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @ApiModel(description = "Unique Number calculation result presentation model")
    class UniqueNumberResultView {
        private int uniqueNumber;
    }

    @Value
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @ApiModel(description = "Model to request a new unique number calculation")
    class NewUniqueNumberCalculationCommand {
        @Min(0)
        @Max(100000)
        private int concatenationQuantity;
        @NotBlank
        private String inputNumber;
        private String userId;

        public NewUniqueNumberCalculationCommand(int concatenationQuantity, String inputNumber, String userId) {
            this.concatenationQuantity = concatenationQuantity;
            this.inputNumber = inputNumber;
            this.userId = userId;
        }

        public NewUniqueNumberCalculationCommand(int concatenationQuantity, String inputNumber) {
            this(concatenationQuantity, inputNumber, null);
        }
    }
}
