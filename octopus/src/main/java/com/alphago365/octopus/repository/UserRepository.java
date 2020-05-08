package com.alphago365.octopus.repository;

import com.alphago365.octopus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    Optional<User> findByCellphone(String cellphone);
    Boolean existsByCellphone(String cellphone);

    Optional<User> findByUsernameOrEmailOrCellphone(String username, String email, String cellphone);
}
