package com.example.bilingual_c.repository;

import com.example.bilingual_c.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findClientByAuthInfoEmail(String email);
}
