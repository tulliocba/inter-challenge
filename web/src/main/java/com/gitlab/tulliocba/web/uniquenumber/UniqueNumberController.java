package com.gitlab.tulliocba.web.uniquenumber;

import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase.NewUniqueNumberCalculationCommand;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase.UniqueNumberResultView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UniqueNumberController {

    private final CalculateUniqueNumberUserCase calculateUniqueNumberUserCase;

    @PostMapping("/uniqueNumbers")
    @ResponseStatus(HttpStatus.CREATED)
    public UniqueNumberResultView requestUniqueNumber(@RequestBody NewUniqueNumberCalculationCommand params) {
        final UniqueNumber uniqueNumber = calculateUniqueNumberUserCase.calculateUniqueNumber(params);
        return uniqueNumber.toResultView();
    }

}
