package com.example.bilingual_c.repository;

import com.example.bilingual_c.entity.AuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthInfoRepository extends JpaRepository<AuthInfo, Long> {

    Optional<AuthInfo> findByEmail(String email);

    boolean existsAuthInfoByEmail(String email);
}
