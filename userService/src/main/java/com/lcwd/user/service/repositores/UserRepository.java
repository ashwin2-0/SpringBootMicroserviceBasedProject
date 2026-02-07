package com.lcwd.user.service.repositores;

import com.lcwd.user.service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
