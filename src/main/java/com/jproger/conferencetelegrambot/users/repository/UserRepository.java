package com.jproger.conferencetelegrambot.users.repository;

import com.jproger.conferencetelegrambot.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
