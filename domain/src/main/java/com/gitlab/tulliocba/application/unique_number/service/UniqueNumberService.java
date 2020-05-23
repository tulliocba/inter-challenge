package com.gitlab.tulliocba.application.unique_number.service;

import com.gitlab.tulliocba.application.cache.domain.CacheManager;
import com.gitlab.tulliocba.application.unique_number.domain.Param;
import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.user.domain.User;
import com.gitlab.tulliocba.application.user.infrastructure.UserRepository;
import com.gitlab.tulliocba.application.user.service.exception.RuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static java.util.Optional.of;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Transactional
@RequiredArgsConstructor
public class UniqueNumberService implements CalculateUniqueNumberUserCase {

    private final UserRepository userRepository;

    @Override
    public UniqueNumber calculateUniqueNumber(NewUniqueNumberCalculationCommand command) {
        Optional<User> userDB = Optional.empty();

        if (isNotBlank(command.getUserId())) {
            userDB = of(userRepository.findUserById(command.getUserId())
                    .orElseThrow(() -> new RuleException("User not found")));
        }

        final Param param = Param.of(command.getInputNumber(), command.getConcatenationQuantity());

        final UniqueNumber uniqueNumber = CacheManager.get(param.getConcatenatedNumber())
                .map(value -> UniqueNumber.calculated(param, value))
                .orElse(UniqueNumber.from(param));

        userDB.ifPresent(user -> {
            user.addUniqueNumber(uniqueNumber);
            userRepository.save(user);
        });

        return uniqueNumber;
    }
}
