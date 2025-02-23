package com.gitlab.tulliocba.web.user;

import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.user.service.SendPublicKeyUseCase;
import com.gitlab.tulliocba.application.user.service.SendPublicKeyUseCase.NewPublicKeyCommand;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.NewUserCommand;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.UniqueNumberView;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.UserView;
import com.gitlab.tulliocba.web.configuration.SwaggerConfiguration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(tags = SwaggerConfiguration.USERS_TAG)
public class UserController {

    private final UserCRUDUseCase crudUseCase;
    private final SendPublicKeyUseCase sendPublicKeyUseCase;

    @PostMapping("/users")
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Creates a new User")
    public UserView create(@Valid @RequestBody NewUserCommand user) {
        return crudUseCase.createUser(user).toView();
    }

    @PutMapping("/users/{id}")
    @ApiOperation(value = "Updates User attributes")
    public ResponseEntity<UserView> update(@PathVariable String id,
                                           @Valid @RequestBody NewUserCommand user) {
        return crudUseCase.updateUser(id, user)
                .map(opUser -> ResponseEntity.ok(opUser.toView()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{id}")
    @ApiOperation(value = "Retrieves a User by ID")
    public ResponseEntity<UserView> find(@PathVariable String id) {
        return crudUseCase.getUserById(id)
                .map(user -> ResponseEntity.ok(user.toView()))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Deletes a User by ID")
    public void delete(@PathVariable String id) {
        crudUseCase.deleteUser(id);
    }


    @PostMapping("/users/{id}/keys")
    @ApiOperation(value = "Sends a public key by User ID")
    public ResponseEntity<UserView> sendPublicKey(@PathVariable String id,
                                                  @Valid @RequestBody NewPublicKeyCommand publicKey) {
        return sendPublicKeyUseCase.sendPublicKey(id, publicKey.getPublicKey())
                .map(user -> ResponseEntity.status(CREATED).body(user.toView()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{id}/uniqueNumbers")
    @ApiOperation(value = "Retrieves Unique Numbers by User ID")
    public Set<UniqueNumberView> retrieveUniqueNumbers(@PathVariable String id) {
        return crudUseCase.findAllUniqueNumbers(id).stream().map(UniqueNumber::toView)
                .collect(Collectors.toSet());
    }
}
