package com.gitlab.tulliocba.persistencejpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    @Query("select user from UserEntity user where user.uuid = :uuid and user.status = 'ACTIVE'")
    Optional<UserEntity> findUserEntityByUuid(@Param("uuid") String uuid);

    @Modifying
    @Query("update UserEntity user set user.status = 'INACTIVE' where user.uuid = :uuid")
    void deleteByUuid(@Param("uuid") String uuid);
}
