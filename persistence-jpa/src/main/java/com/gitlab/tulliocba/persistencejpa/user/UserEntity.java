package com.gitlab.tulliocba.persistencejpa.user;

import com.gitlab.tulliocba.application.unique_number.domain.Param;
import com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber;
import com.gitlab.tulliocba.application.user.domain.User;
import com.gitlab.tulliocba.application.user.domain.UserBuilder;
import com.gitlab.tulliocba.common.constants.Status;
import com.gitlab.tulliocba.persistencejpa.uniquenumber.UniqueNumberEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gitlab.tulliocba.application.unique_number.domain.UniqueNumber.calculated;
import static javax.persistence.CascadeType.ALL;

@Data
@Entity
@Table(name = "user")
@EqualsAndHashCode(of = "uuid")
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String uuid;
    @Lob
    @Column(length = 1000)
    private String name;
    @Lob
    @Column(length = 1000)
    private String email;
    @Lob
    @Column(length = 1000)
    private String publicKey;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<UniqueNumberEntity> uniqueNumbers = new HashSet<>();

    public User toDomain() {
        return UserBuilder.builder().given(id, uuid, name, email, publicKey, mapUniqueNumberEntityToDomain()).build();
    }

    private Set<UniqueNumber> mapUniqueNumberEntityToDomain() {
        return uniqueNumbers.stream().map(uniqueNumber ->
                calculated(Param.of(uniqueNumber.getInputNumber(),
                        uniqueNumber.getConcatQuantity()), uniqueNumber.getUniqueNumberResult()))
                .collect(Collectors.toSet());
    }
}
