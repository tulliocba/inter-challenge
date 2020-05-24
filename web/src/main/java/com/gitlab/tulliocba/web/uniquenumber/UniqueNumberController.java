package com.gitlab.tulliocba.web.uniquenumber;

import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase.NewUniqueNumberCalculationCommand;
import com.gitlab.tulliocba.application.unique_number.service.CalculateUniqueNumberUserCase.UniqueNumberResultView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.gitlab.tulliocba.web.configuration.SwaggerConfiguration.UNIQUE_NUMBERS_TAG;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(tags = UNIQUE_NUMBERS_TAG)
public class UniqueNumberController {

    private final CalculateUniqueNumberUserCase calculateUniqueNumberUserCase;

    @PostMapping("/uniqueNumbers")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Requests a new unique number calculation")
    public UniqueNumberResultView requestUniqueNumber(@RequestBody NewUniqueNumberCalculationCommand params) {
        final UniqueNumber uniqueNumber = calculateUniqueNumberUserCase.calculateUniqueNumber(params);
        return uniqueNumber.toResultView();
    }

}
