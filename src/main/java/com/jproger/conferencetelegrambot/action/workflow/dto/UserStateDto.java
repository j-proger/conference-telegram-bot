package com.jproger.conferencetelegrambot.action.workflow.dto;

import com.jproger.conferencetelegrambot.action.workflow.entities.WorkflowState.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStateDto {
    private Long externalId;
    private Status status;
    private String selectedTopic;
}
