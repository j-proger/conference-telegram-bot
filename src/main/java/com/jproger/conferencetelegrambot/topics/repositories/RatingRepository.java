package com.jproger.conferencetelegrambot.topics.repositories;

import com.jproger.conferencetelegrambot.topics.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
