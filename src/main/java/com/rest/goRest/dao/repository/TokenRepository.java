package com.rest.goRest.dao.repository;


import com.rest.goRest.dao.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Query("SELECT te FROM TokenEntity te WHERE te.token = :token")
    Optional<TokenEntity> findByToken(@Param("token") String token);
}
