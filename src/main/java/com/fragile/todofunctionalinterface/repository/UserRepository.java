package com.fragile.todofunctionalinterface.repository;

import com.fragile.todofunctionalinterface.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
