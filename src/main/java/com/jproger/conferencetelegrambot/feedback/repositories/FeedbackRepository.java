package com.jproger.conferencetelegrambot.feedback.repositories;

import com.jproger.conferencetelegrambot.feedback.entities.DobbyFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<DobbyFeedback, Long> {
}
