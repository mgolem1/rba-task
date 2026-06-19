package com.rba.repositories;

import com.rba.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByIdentificationNumber(String identificationNumber);

    Optional<User> findUserByIdentificationNumber(String identificationNumber);

    User findUserById(Long id);
}
