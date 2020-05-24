package com.gitlab.tulliocba.application.user.domain;

import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.user.service.UserCRUDUseCase.UserView;
import com.gitlab.tulliocba.application.user.service.exception.RuleException;
import com.gitlab.tulliocba.application.util.RSAUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@EqualsAndHashCode(of = "uuid")
@ToString
@Getter
public class User {

    private Long id;
    private UserId uuid;
    private String name;
    private String email;
    private String publicKey;
    private Set<UniqueNumber> uniqueNumbers;

    public User(Long id,
                UserId uuid,
                String name,
                String email,
                String publicKey,
                Set<UniqueNumber> uniqueNumbers) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.publicKey = publicKey;
        this.uniqueNumbers = (uniqueNumbers == null ? new HashSet<>() : uniqueNumbers);
    }

    public void addUniqueNumber(UniqueNumber uniqueNumber) {
        this.uniqueNumbers.add(uniqueNumber);
    }

    public void addKey(String publicKey) {

        if (isBlank(publicKey)) throw new RuleException("Public key is required");

        if (isNotBlank(this.publicKey)) return;

        this.publicKey = publicKey;

        encryptData();
    }

    public void encryptData() {
        try {
            this.name = RSAUtils.encrypt(this.name, this.publicKey);
            this.email = RSAUtils.encrypt(this.email, this.publicKey);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Public key is required");
        }
    }

    public UserView toView() {
        return new UserView(uuid.getValue(),
                name, email, uniqueNumbers.stream().map(UniqueNumber::toView).collect(toSet()));
    }

    public boolean isEncrypted() {
        if (isNotBlank(publicKey)) return true;

        throw new RuleException("User cannot be retrieved because the data are decrypted. " +
                "Send your public key to encrypt the data");
    }

    @Value
    public static class UserId {
        private String value;

        public UserId(String value) {

            if (StringUtils.isBlank(value)) {
                value = UUID.randomUUID().toString();
            }
            this.value = value;
        }
    }
}
