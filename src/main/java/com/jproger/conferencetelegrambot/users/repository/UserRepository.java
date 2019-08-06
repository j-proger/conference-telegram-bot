package com.jproger.conferencetelegrambot.users.repository;

import com.jproger.conferencetelegrambot.users.entities.User;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserRepository {
    private static long currentId = 0;

    private Map<Long, User> users = new HashMap<>();

    public User save(@Nonnull User user) {
        if (Objects.isNull(user.getId())) {
            user.setId(nextId());
        }

        users.put(user.getId(), user);

        return users.get(user.getId());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    private Long nextId() {
        return ++currentId;
    }
}
